package com.pourbaix.script.creature.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pourbaix.script.creature.context.GlobalContext;
import com.pourbaix.script.creature.dao.TriggerDao;
import com.pourbaix.script.creature.keyword.KeywordService;
import com.pourbaix.script.creature.model.Trigger;
import com.pourbaix.script.creature.utils.Constant;

@Service
@Transactional(readOnly = true)
public class GeneratorService {

	private static final Logger logger = LoggerFactory.getLogger(GeneratorService.class);

	@Resource
	private GlobalContext globalContext;
	@Resource
	private KeywordService keywordService;
	@Resource
	private BlockService blockService;
	@Autowired
	private TriggerDao triggerDao;

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
		List<Trigger> triggers = triggerDao.loadAll();
		try {
			loadFile(file);
			logger.debug("done!");
		} catch (GeneratorException e) {
			logger.error(e.getMessage());
		}
	}

	private FileContent loadFile(final String input) throws GeneratorException {
		try {
			File inputFile = new File(input);
			FileInputStream fis = new FileInputStream(inputFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			Line line = null;
			while ((line = getLine(br)) != null) {
			}
			fis.close();
		} catch (FileNotFoundException e) {
			throw new GeneratorException(e);
		} catch (IOException e) {
			throw new GeneratorException(e);
		}
		return null;
	}

	// private void ProcessFile(final String input) throws Exception {
	// try {
	// File inputFile = new File(input);
	// FileInputStream fis = new FileInputStream(inputFile);
	// InputStreamReader isr = new InputStreamReader(fis);
	// BufferedReader br = new BufferedReader(isr);
	//
	// Line line = null;
	// while ((line = getLine(br)) != null) {
	// // System.out.println(line.getText());
	// List<Keyword> keywords = keywordService.getKeywords(line.getText());
	// if (!keywords.isEmpty()) {
	// Block block = blockService.getBlock(keywords);
	// out.write(blockService.generateOutput(block));
	// } else {
	// out.write(line.getRawText() + Constant.CR);
	// }
	// }
	// fis.close();
	// } catch (FileNotFoundException e) {
	// System.err.println(e);
	// throw new Exception(e);
	// } catch (IOException e) {
	// System.err.println(e);
	// }
	// }

	private Line getLine(final BufferedReader br) throws GeneratorException, IOException {
		Line line = new Line();
		StringBuffer sb = new StringBuffer();
		String textLine;
		int numLine = 0;
		while ((textLine = br.readLine()) != null) {
			numLine++;
			String trimTextLine = textLine.trim();
			sb.append(trimTextLine);
			if (numLine == 1) {
				if (!trimTextLine.startsWith("[") || (trimTextLine.startsWith("[") && trimTextLine.endsWith("]"))) {
					// simple line
					line.setRawText(textLine);
					break;
				} else if (!trimTextLine.startsWith("[")) {
					throw new GeneratorException("Unknown block delimiters: " + textLine);
				}
			} else {
				if (trimTextLine.startsWith("[")) {
					throw new GeneratorException("Unexpected character '[' found in: " + sb.toString());
				} else if (trimTextLine.endsWith("]")) {
					// multi-lines end
					break;
				}
			}
		}
		line.setText(sb.toString());
		return numLine == 0 ? null : line;
	}

	public void writeOutput(final String input, final String output) throws GeneratorException {
		// System.out.println("Building " + output + "...");
		try {
			outputFile = new File(output);
			fos = new FileOutputStream(outputFile);
			out = new OutputStreamWriter(fos, "UTF-8");
			// ProcessFile(input);
			out.flush();
			fos.flush();
			out.close();
			fos.close();
		} catch (FileNotFoundException e) {
			throw new GeneratorException(e);
		} catch (IOException e) {
			throw new GeneratorException(e);
		}
		// System.out.println("Done!");
	}

}
