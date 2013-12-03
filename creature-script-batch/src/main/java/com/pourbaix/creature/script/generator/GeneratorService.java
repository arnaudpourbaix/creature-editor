package com.pourbaix.creature.script.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pourbaix.creature.editor.repository.ActionRepository;
import com.pourbaix.creature.editor.repository.KeywordRepository;
import com.pourbaix.creature.editor.repository.TriggerRepository;
import com.pourbaix.creature.script.context.GlobalContext;
import com.pourbaix.creature.script.instruction.CommentInstruction;
import com.pourbaix.creature.script.instruction.GeneratedInstruction;
import com.pourbaix.creature.script.instruction.Instruction;
import com.pourbaix.creature.script.instruction.InstructionException;
import com.pourbaix.creature.script.instruction.InstructionService;
import com.pourbaix.creature.script.instruction.InstructionTypeEnum;
import com.pourbaix.creature.script.instruction.NativeInstruction;
import com.pourbaix.creature.script.utils.Constant;

@Service
public class GeneratorService {

	private static final Logger logger = LoggerFactory.getLogger(GeneratorService.class);

	@Resource
	private GlobalContext globalContext;
	@Resource
	private InstructionService instructionService;
	@Resource
	private BlockService blockService;
	@Autowired
	private KeywordRepository keywordRepository;
	@Autowired
	private TriggerRepository triggerRepository;
	@Autowired
	private ActionRepository actionRepository;

	private File outputFile;
	private FileOutputStream fos;
	private OutputStreamWriter out;

	public void generate(final String[] args) {
		if (args.length == 0) {
			logger.error("you must provide a file or directory parameter !");
			return;
		}
		File f = new File(args[0]);
		if (f.isDirectory()) {
			String path = args[0];
			final FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(final File dir, final String name) {
					return name.toUpperCase().endsWith(Constant.SCRIPT_EXT);
				}
			};
			final String[] chld = f.list(filter);
			for (final String c : chld) {
				final String filename = path + "\\" + c;
				generateFile(filename);
			}
		} else if (f.isFile()) {
			generateFile(args[0]);
		} else {
			logger.error("bad path: " + args[0]);
		}
	}

	public void generateFile(final String file) {
		logger.debug("generating " + file);
		try {
			loadFile(file);
			logger.debug("done!");
		} catch (GeneratorException e) {
			logger.error(e.getMessage());
		}
	}

	private List<Instruction> loadFile(final String input) throws GeneratorException {
		try {
			List<Instruction> instructions = new ArrayList<>();
			TextFileReader textFileReader = new TextFileReader(input);
			Instruction instruction = null;
			while ((instruction = getInstruction(textFileReader)) != null) {
				instructionService.parseInstruction(instruction);
				instructions.add(instruction);
			}
			textFileReader.close();
		} catch (TextFileReaderException e) {
			String message = e.getMessage();
			if (e.getLineNumber() != null) {
				message = "Line " + e.getLineNumber() + ": " + message;
			}
			throw new GeneratorException(message, e);
		} catch (FileNotFoundException e) {
			throw new GeneratorException(e);
		} catch (InstructionException e) {
			throw new GeneratorException(e);
		}
		return null;
	}

	private Instruction getInstruction(TextFileReader textFileReader) throws TextFileReaderException {
		Instruction instruction = null;
		StringBuffer sbRawText = new StringBuffer();
		String line = null;
		while ((line = textFileReader.readLine()) != null) {
			line = line.trim();
			if (line.isEmpty()) { // blank lines are useless
				continue;
			}
			sbRawText.append(line).append(Constant.CARRIAGE_RETURN);
			// Comments
			if (instruction instanceof CommentInstruction && instruction.getType() == InstructionTypeEnum.MultiLineComment
					&& !StringUtils.endsWith(line, Constant.Comment.MULTI_LINE_END)) {
				// multi-lines comment has priority over all intructions
				continue;
			} else if (instruction == null
					&& (StringUtils.startsWith(line, Constant.Comment.SINGLE_LINE) || (StringUtils.startsWith(line, Constant.Comment.MULTI_LINE_START) && StringUtils
							.endsWith(line, Constant.Comment.MULTI_LINE_END)))) {
				// single line comment or mutli-lines comment on a single line
				instruction = new CommentInstruction(InstructionTypeEnum.SingleLineComment);
				break;
			} else if (instruction == null && StringUtils.startsWith(line, Constant.Comment.MULTI_LINE_START)) {
				instruction = new CommentInstruction(InstructionTypeEnum.MultiLineComment);
				continue;
			} else if (instruction instanceof CommentInstruction && StringUtils.endsWith(line, Constant.Comment.MULTI_LINE_END)) {
				break;
			}
			// Native block (IF-THEN-END)
			if (StringUtils.startsWithIgnoreCase(line, Constant.BlockKeyword.IF)) {
				if (instruction != null) {
					throw new TextFileReaderException("Found IF inside another instruction", textFileReader.getLineNumber());
				}
				instruction = new NativeInstruction(InstructionTypeEnum.NativeBlock);
			} else if (StringUtils.endsWithIgnoreCase(line, Constant.BlockKeyword.END)) {
				if (!(instruction instanceof NativeInstruction)) {
					throw new TextFileReaderException("Found END outside a block", textFileReader.getLineNumber());
				}
				break;
			} else if (instruction instanceof NativeInstruction) {
				continue;
			}
			// Generated block
			if (instruction == null) {
				instruction = new GeneratedInstruction(InstructionTypeEnum.GeneratedBlock);
			}
			if (line.endsWith(Constant.GENERATED_INSTRUCTION_END_CHAR)) {
				if (sbRawText.toString().split(";").length > 2) {
					throw new TextFileReaderException("Can't have more than one instruction per line", textFileReader.getLineNumber());
				}
				if (sbRawText.toString().contains(Constant.Comment.SINGLE_LINE)) {
					throw new TextFileReaderException("Can't have a single-line comment within a generated instruction", textFileReader.getLineNumber());
				}
				break;
			}
			// Script config
			// if (StringUtils.startsWithIgnoreCase(line, Constant.ConfigKeyword.CONFIG)) {
			// instruction = new ConfigurationInstruction(InstructionTypeEnum.Config);
			// break;
			// }
		}
		if (instruction != null) {
			instruction.setRawText(sbRawText.toString());
			// if (!(instruction instanceof CommentInstruction)) {
			// logger.debug("line " + textFileReader.getLineNumber() + " - " + instruction.toString());
			// }
		}
		return instruction;
	}
}
