package com.hjw.cet4.entities;

import java.io.Serializable;
import java.util.List;

import com.hjw.cet4.utils.Const;



public class Problem implements Serializable{
	
	public static final int WRITING = 1;
	public static final int SHORT_CONVERSATIONS = 2;
	public static final int LONG_CONVERSATIONS = 3;
	public static final int SHORT_PASSAGES = 4;
	public static final int PASSAGE_DICTATION = 5;
	public static final int WORDS_COMPREHENSION = 6;
	public static final int LONG_TO_READ = 7;
	public static final int CAREFUL_READING = 8;
	public static final int TRANSLATE = 9;
	
	public static final int LISTENING = 50;
	public static final int READING = 60;
	public static final int PRACTICE = 70;
	
	public static final int PART = 100;
	public static final int COMMIT = 200;
	
	public static final int PART_I = 401;
	public static final int PART_II = 402;
	public static final int PART_III = 403;
	public static final int PART_IV = 404;
	
	public int id;
	public String subject;
	public String options;
	public String answer;
	public String analyze;
	public int piece_id;
	public int type;
	
	//new
	public int num;

	public String result;     //不在数据库里
	
	public Problem(int id, String subject, String options, String answer, String analyze, int piece_id, int type) {
		super();
		this.id = id;
		this.subject = subject;
		this.options = options;
		this.answer = answer;
		this.analyze = analyze;
		this.piece_id = piece_id;
		this.type = type;
	}
	
	public Problem(int id, String subject, int num, String options, String answer, String analyze, int piece_id, int type) {
		super();
		this.id = id;
		this.subject = subject;
		this.options = options;
		this.answer = answer;
		this.analyze = analyze;
		this.piece_id = piece_id;
		this.type = type;
		
		this.num = num;
	}

	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}
	
	public boolean checkResult(){
		return answer.equals(result) || !mustHasResult();  //写作和翻译用后者
	}
	
	public boolean mustHasResult(){
		boolean result = true;
		switch (type) {
		case Problem.WRITING:
		case Problem.PASSAGE_DICTATION:
			result = false;
			break;
		case Problem.SHORT_CONVERSATIONS:
		case Problem.LONG_CONVERSATIONS:
		case Problem.SHORT_PASSAGES:
		case Problem.WORDS_COMPREHENSION:
		case Problem.LONG_TO_READ:
		case Problem.CAREFUL_READING:
			break;
		case Problem.TRANSLATE:
			result = false;
			break;
		case Problem.PART:
			result = false;
			break;
		default:
			break;
		}
		return result;
	}
	
	public boolean showPlayerControl(){
		boolean result = false;
		switch (type) {
		case Problem.WRITING:
			break;
		case Problem.SHORT_CONVERSATIONS:
		case Problem.LONG_CONVERSATIONS:
		case Problem.SHORT_PASSAGES:
		case Problem.PASSAGE_DICTATION:
			result = true;
			break;
		case Problem.WORDS_COMPREHENSION:
		case Problem.LONG_TO_READ:
		case Problem.CAREFUL_READING:
		case Problem.TRANSLATE:
		case Problem.PART:
			break;
		default:
			break;
		}
		return result;
	}
	
	public boolean needOriginal(boolean is_review){
		boolean result = false;
		switch (type) {
		case Problem.WRITING:
		case Problem.PASSAGE_DICTATION:
			break;
		case Problem.SHORT_CONVERSATIONS:
		case Problem.LONG_CONVERSATIONS:
		case Problem.SHORT_PASSAGES:
			result = is_review;
			break;
		case Problem.WORDS_COMPREHENSION:
		case Problem.LONG_TO_READ:
		case Problem.CAREFUL_READING:
			result = true;
			break;
		case Problem.TRANSLATE:
		case Problem.PART:
			break;
		default:
			break;
		}
		return result;
	}
	
	public boolean useSharedOptions(){
		boolean result = false;
		switch (type) {
		case Problem.SHORT_CONVERSATIONS:
		case Problem.WORDS_COMPREHENSION:
		case Problem.LONG_TO_READ:
			result = true;
			break;
		default:
			break;
		}
		return result;
	}
	
	public static String getTypeCurrentString(int type){
		String result = null;
		switch (type) {
		case Problem.WRITING:
			result = Const.WRITING_CURRENT;
			break;
		case Problem.SHORT_CONVERSATIONS:
			result = Const.SHORT_CONVERSATIONS_CURRENT;
			break;
		case Problem.LONG_CONVERSATIONS:
			result = Const.LONG_CONVERSATIONS_CURRENT;
			break;
		case Problem.SHORT_PASSAGES:
			result = Const.SHORT_PASSAGES_CURRENT;
			break;
		case Problem.PASSAGE_DICTATION:
			result = Const.PASSAGE_DICTATION_CURRENT;
			break;
		case Problem.WORDS_COMPREHENSION:
			result = Const.WORDS_COMPREHENSION_CURRENT;
			break;
		case Problem.LONG_TO_READ:
			result = Const.LONG_TO_READ_CURRENT;
			break;
		case Problem.CAREFUL_READING:
			result = Const.CAREFUL_READING_CURRENT;
			break;
		case Problem.TRANSLATE:
			result = Const.TRANSLATE_CURRENT;
			break;
		case Problem.PART:
			break;
		default:
			break;
		}
		return result;
	}
	
	public int getPart(){
		int result = 0;
		switch (type) {
		case Problem.WRITING:
			result = PART_I;
			break;
		case Problem.SHORT_CONVERSATIONS:
		case Problem.LONG_CONVERSATIONS:
		case Problem.SHORT_PASSAGES:
		case Problem.PASSAGE_DICTATION:
			result = PART_II;
			break;
		case Problem.WORDS_COMPREHENSION:
		case Problem.LONG_TO_READ:
		case Problem.CAREFUL_READING:
			result = PART_III;
			break;
		case Problem.TRANSLATE:
			result = PART_IV;
			break;
		case Problem.PART:
			break;
		default:
			break;
		}
		return result;
	}
	
	public static String getPartDirections(int type){
		String result = null;
		switch (type) {
		case Problem.WRITING:
			result = "Directions: For this part, you are allowed 30 minutes to write an essay.   You should write at least 120words but no more than 180word.";
			break;
		case Problem.SHORT_CONVERSATIONS:
			result = "Directions: In this section, you will hear short conversations. At the end of each shortt conversation, a questions will be asked about what was said. Both the conversation and the questions will be spoken only once. After eachquestion there will be a pause. During the pause, you must read the four choices markedA), B), C) and D), and decide which is the best answer. Then mark the corresponding letter on Answer Sheet 1with a single line through the centre.";
			break;
		case Problem.LONG_CONVERSATIONS:
			result = "Directions: In this section, you will hear long conversations. At the end of each long conversation, more than one question will be asked about what was said. Both the conversation and the questions will be spoken only once. After eachquestion there will be a pause. During the pause, you must read the four choices markedA), B), C) and D), and decide which is the best answer. Then mark the corresponding letter on Answer Sheet 1with a single line through the centre.";
			break;
		case Problem.SHORT_PASSAGES:
			result = "Directions: In this section, you will hear short passages. At the end of each passage, you will hear some questions. Both the passage and the questions will be spoken only once. After you hear a question, you must choose the best answer from the four choices markedA), B), C) and D). Then mark the corresponding letter on Answer Sheet 1 with a single line through the centre.";
			break;
		case Problem.PASSAGE_DICTATION:
			result = "Directions: In this section, you will hear a passage three times. When the passage is read for the first time, you should listen carefully for its general idea. When the passage is read for the second time, you are required to fill in the blanks with the exact words you have just heard. Finally, when the passage is read for the third time, you should check what you have written.";
			break;
		case Problem.WORDS_COMPREHENSION:
			result = "Directions: In this section, there is a passage with ten blanks. You are required to select one word for each blank from a list of choices given in a word bank following the passage. Read the passagethrough carefully before making your choices. Each choice in the bankis identified by a letter. Please mark the corresponding letter for each item on Answer Sheet 2 with a single line through the centre. You may not use any of the words in the bank more than once.";
			break;
		case Problem.LONG_TO_READ:
			result = "Directions: In this section, you are going to read a passage with ten statements attached to it. Each statement contains information given in one of the paragraphs. Identify the paragraph from which the information is derived. You may choose a paragraph more than once. Each paragraph is marked with a letter. Answer the questions by marking the corresponding letter on Answer Sheet 2.";
			break;
		case Problem.CAREFUL_READING:
			result = "There are 2 passages in this section. Each passageis followed by some questions or unfinished statements. For each of them there are four choices marked A), B), C) and D). You should decide on the best choice and mark the corresponding letter on Answer Sheet 2 with a single line through the centre.";
			break;
		case Problem.TRANSLATE:
			result = "Directions: For this part, you are allowed 30 minutes to translate a passage or a sentence from Chinese into English. You should write your answer on Answer Sheet 2.";
			break;
		default:
			break;
		}
		return result;
	}
	
	public static int getCorrect(List<Problem> problems){
		int result = 0;
		for(Problem problem : problems){
			if(problem.checkResult()){
				result++;
			}
		}
		return result;
	}
	
//	public static final int WRITING = 1;
//	public static final int SHORT_CONVERSATIONS = 2;
//	public static final int LONG_CONVERSATIONS = 3;
//	public static final int SHORT_PASSAGES = 4;
//	public static final int PASSAGE_DICTATION = 5;
//	public static final int WORDS_COMPREHENSION = 6;
//	public static final int LONG_TO_READ = 7;
//	public static final int CAREFUL_READING = 8;
//	public static final int TRANSLATE = 9;
	
	public static String getTypeName(int type){
		String result = "";
		switch (type) {
		case Problem.WRITING:
			result = "写作";
			break;
		case Problem.SHORT_CONVERSATIONS:
			result = "短对话";
			break;
		case Problem.LONG_CONVERSATIONS:
			result = "长对话";
			break;
		case Problem.SHORT_PASSAGES:
			result = "短文理解";
			break;
		case Problem.PASSAGE_DICTATION:
			result = "短文听写";
			break;
		case Problem.WORDS_COMPREHENSION:
			result = "词汇理解";
			break;
		case Problem.LONG_TO_READ:
			result = "长篇阅读";
			break;
		case Problem.CAREFUL_READING:
			result = "仔细阅读";
			break;
		case Problem.TRANSLATE:
			result = "翻译";
			break;
		default:
			break;
		}
		return result;
	}
	
}
