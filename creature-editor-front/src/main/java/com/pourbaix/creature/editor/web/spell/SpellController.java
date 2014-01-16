package com.pourbaix.creature.editor.web.spell;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.google.common.collect.Lists;
import com.pourbaix.creature.editor.domain.Spell;
import com.pourbaix.creature.editor.repository.SpellRepository;

@Controller
public class SpellController {

	private static final Logger logger = LoggerFactory.getLogger(SpellController.class);

	@Autowired
	private SpellRepository spellRepository;

	@Autowired
	private DeferredResultService updateService;

	@Bean
	public LinkedBlockingQueue theQueue() {
		return new LinkedBlockingQueue();
	}

	@Bean
	public Match matches() {
		List<String> list = Lists
				.newArrayList(
						// "95:22 Final Score  Arsenal 1 - 0 Stoke",
						// "95:21 Full time The referee blows his whistle to end the game. ",
						// "94:06 Unfair challenge on Laurent Koscielny by Kenwyne Jones results in a free kick. Wojciech Szczesny takes the free kick. ",
						// "92:40 Michael Owen gives away a free kick for an unfair challenge on Mikel Arteta. Direct free kick taken by Mikel Arteta. ",
						// "89:58 Laurent Koscielny restarts play with the free kick. ",
						// "89:58 Substitution Theo Walcott goes off and Aaron Ramsey comes on. ",
						// "89:58 Nacho Monreal fouled by Cameron Jerome, the ref awards a free kick. ",
						// "88:10 Mikel Arteta restarts play with the free kick. ",
						// "88:10 Booking Ryan Shawcross is given a yellow card. ",
						// "88:04 Free kick awarded for a foul by Ryan Shawcross on Laurent Koscielny. ",
						// "82:39 Ryan Shawcross takes the free kick. ",
						// "82:39 Substitution (Stoke) makes a substitution, with Michael Owen coming on for Geoff Cameron. ",
						// "82:39 Substitution Jonathan Walters goes off and Cameron Jerome comes on. ",
						// "82:39 Substitution Kenwyne Jones is brought on as a substitute for Peter Crouch. ",
						// "82:39 Laurent Koscielny challenges Peter Crouch unfairly and gives away a free kick. ",
						// "80:50 Jack Wilshere takes the inswinging corner, Ryan Shawcross manages to make a clearance. ",
						// "79:48 Santi Cazorla takes a shot. Save by Asmir Begovic. Theo Walcott decides to take a short corner.",
						// "79:41 Santi Cazorla takes a shot. Blocked by Ryan Shawcross. Santi Cazorla takes a shot. Blocked by Ryan Shawcross. ",
						// "77:11 Assist on the goal came from Theo Walcott. ",
						// "77:11 Goal scored. Goal - Lukas Podolski - Arsenal 1 - 0 Stoke Lukas Podolski grabs a goal direct from the free kick from just outside the area low into the middle of the goal. Arsenal 1-0 Stoke. ",
						// "76:14 Booking Andy Wilkinson is given a yellow card.",
						// "75:57 Unfair challenge on Theo Walcott by Andy Wilkinson results in a free kick. ",
						// "74:12 The assistant referee signals for offside against Peter Crouch. Laurent Koscielny restarts play with the free kick. ",
						// "73:27 Shot from long distance by Nacho Monreal misses to the right of the goal. ",
						// "71:38 Headed effort from deep inside the area by Olivier Giroud misses to the right of the goal. ",
						// "69:42 Direct free kick taken by Jack Wilshere. ",
						// "69:42 Booking Glenn Whelan booked for unsporting behaviour. ",
						// "69:32 Santi Cazorla fouled by Glenn Whelan, the ref awards a free kick. ",
						// "68:46 Bacary Sagna challenges Matthew Etherington unfairly and gives away a free kick. Free kick crossed left-footed by Matthew Etherington from left wing, clearance made by Per Mertesacker. ",
						// "67:22 Substitution Santi Cazorla replaces Vassiriki Diaby. ",
						// "67:22 Substitution Lukas Podolski comes on in place of Alex Oxlade-Chamberlain. ",
						// "63:48 Ryan Shawcross has an effort at goal from just outside the area which goes wide of the left-hand post. ",
						// "61:09 Geoff Cameron is caught offside. Laurent Koscielny takes the indirect free kick. ",
						// "55:18 Effort on goal by Olivier Giroud from deep inside the area goes harmlessly over the bar. ",
						// "51:04 Ryan Shotton concedes a free kick for a foul on Alex Oxlade-Chamberlain. Jack Wilshere takes the free kick.",
						// "50:20 Free kick awarded for an unfair challenge on Jack Wilshere by Robert Huth. Mikel Arteta takes the direct free kick.",
						// "48:59 Inswinging corner taken by Theo Walcott, Asmir Begovic makes a save. ",
						// "46:45 Unfair challenge on Mikel Arteta by Peter Crouch results in a free kick. Mikel Arteta takes the free kick. 45:01 The referee gets the second half underway. ",
						// "48:23 Half time",
						// "40:01 Alex Oxlade-Chamberlain takes a shot. Save made by Asmir Begovic. Corner from the right by-line taken by Jack Wilshere, clearance made by Steven Nzonzi. Inswinging corner taken right-footed by Theo Walcott from the left by-line, clearance by Peter Crouch. ",
						// "38:25 Olivier Giroud is caught offside. Asmir Begovic restarts play with the free kick. ",
						// "34:31 Laurent Koscielny takes a shot. Asmir Begovic makes a save. ",
						// "33:58 Inswinging corner taken by Theo Walcott from the left by-line, Peter Crouch manages to make a clearance. ",
						// "30:39 Corner taken by Jack Wilshere from the right by-line, Alex Oxlade-Chamberlain takes a shot. Save by Asmir Begovic. Inswinging corner taken by Theo Walcott, Peter Crouch makes a clearance. ",
						// "29:36 Unfair challenge on Olivier Giroud by Ryan Shawcross results in a free kick. Free kick taken by Mikel Arteta. ",
						// "29:04 Effort on goal by Ryan Shawcross from just outside the area goes harmlessly over the target. ",
						// "25:37 Theo Walcott takes a shot. Blocked by Andy Wilkinson. Foul by Mikel Arteta on Geoff Cameron, free kick awarded. Free kick taken by Asmir Begovic. ",
						// "22:35 Corner from the right by-line taken by Jack Wilshere, save made by Asmir Begovic. ",
						// "21:50 Shot from just outside the box by Glenn Whelan goes over the bar. ",
						// "20:51 Alex Oxlade-Chamberlain takes a shot. Ryan Shawcross gets a block in. Olivier Giroud is caught offside. Asmir Begovic takes the free kick. ",
						// "17:04 Headed effort from the edge of the area by Ryan Shawcross goes wide of the right-hand post. Free kick awarded for an unfair challenge on Theo Walcott by Andy Wilkinson. Jack Wilshere crosses the ball from the free kick left-footed from right wing, Robert Huth manages to make a clearance. ",
						// "16:33 Peter Crouch takes a shot. Laurent Koscielny gets a block in. Inswinging corner taken by Matthew Etherington. ",
						// "14:38 Olivier Giroud produces a headed effort from deep inside the six-yard box which goes wide of the right-hand upright. ",
						// "13:49 Jonathan Walters takes a shot. Wojciech Szczesny makes a save. ",
						// "12:29 Free kick awarded for an unfair challenge on Theo Walcott by Matthew Etherington. Bacary Sagna takes the free kick. ",
						"6:15 Corner from the right by-line taken by Jack Wilshere, Geoff Cameron manages to make a clearance. Inswinging corner taken left-footed by Jack Wilshere played to the near post, Glenn Whelan manages to make a clearance. ",
						"4:57 Jack Wilshere takes a shot from inside the box clearing the bar. ",
						"1:57 Effort from the edge of the area by Alex Oxlade-Chamberlain goes wide of the left-hand upright. ",
						"1:06 Free kick awarded for an unfair challenge on Jack Wilshere by Geoff Cameron. Mikel Arteta restarts play with the free kick. ",
						"0:00 The match has kicked off.");
		Match matches = new Match("Arsenal vs Stoke City", list);
		return matches;
	}

	@Bean
	public MatchReporter BillSkyes() {
		MatchReporter match = new MatchReporter(matches(), theQueue());
		return match;
	}

	@RequestMapping(value = "/spell", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<Spell> list() {
		List<Spell> spells = spellRepository.findAllFetchMod();
		return spells;
	}

	@RequestMapping(value = "/spell/import", params = "modId", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	// public DeferredResult<String> importSpells(Integer modId) {
	public String start() {
		updateService.subscribe();
		// final DeferredResult<String> deferredResult = new DeferredResult<>();
		// runInOtherThread(deferredResult);
		return "OK";
	}

	/**
	 * Get hold of the latest match report - when it arrives But in the process hold on to server resources
	 */
	@RequestMapping(value = "/spell/deferred", method = RequestMethod.GET)
	@ResponseBody
	// public DeferredResult<Message> getUpdate() {
	public String getUpdate() {
		final DeferredResult<Message> result = new DeferredResult<Message>();
		updateService.getUpdate(result);
		return "TEST";
	}

	private final Queue<DeferredResult<String>> eventQueue = new ConcurrentLinkedQueue<>();

	@RequestMapping("/spell/deferred2")
	public @ResponseBody
	DeferredResult<String> deferredCall() {
		DeferredResult<String> result = new DeferredResult<>();
		this.eventQueue.add(result);
		return result;
	}

	@Scheduled(fixedRate = 5000)
	public void simulateExternalThread() throws InterruptedException {
		Thread.sleep(5000);
		for (DeferredResult<String> result : this.eventQueue) {
			result.setResult("Hello");
			this.eventQueue.remove(result);
		}
	}

	private void runInOtherThread(DeferredResult<String> deferredResult) {
		// seconds later in other thread...
		deferredResult.setResult("HTTP response is: 42");
	}

	@ExceptionHandler
	@ResponseBody
	public String handleException(Exception ex) {
		logger.debug(ex.getMessage());
		return "Handled exception: " + ex.getMessage();
	}

	@RequestMapping(value = "/matchupdate/begin" + "", method = RequestMethod.GET)
	@ResponseBody
	public String start2() {
		updateService.subscribe();
		return "OK";
	}

	@RequestMapping("/matchupdate/deferred")
	@ResponseBody
	public DeferredResult<Message> getUpdate2() {
		final DeferredResult<Message> result = new DeferredResult<Message>();
		updateService.getUpdate(result);
		return result;
	}

}
