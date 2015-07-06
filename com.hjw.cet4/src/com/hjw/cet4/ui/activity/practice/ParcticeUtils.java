package com.hjw.cet4.ui.activity.practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hjw.cet4.App;
import com.hjw.cet4.entities.Paper;
import com.hjw.cet4.entities.Piece;
import com.hjw.cet4.entities.Problem;


public class ParcticeUtils {
	
//	private static ParcticeUtils mParcticeUtils;
//
//	public static final ParcticeUtils getInstance() {
//		if (mParcticeUtils == null) {
//			mParcticeUtils = new ParcticeUtils();
//		}
//		return mParcticeUtils;
//	}
//	
//	private ParcticeUtils(){
//		
//	}
	
	public static HashMap<Integer, Piece> getPiecesMapByPaper(Paper paper){
		List<Piece> pieces;
		HashMap<Integer, Piece> pieceMap = new HashMap<Integer, Piece>();
		pieces = App.getInstance().getExamDBHelper().getPiecesByPaperId(paper.id);
		for(Piece piece : pieces){
			pieceMap.put(piece.id, piece);
		}
		return pieceMap;
	}
	
	public static List<Problem> getProblemsByPaper(Paper paper, boolean withResult){
		List<Problem> results = new ArrayList<Problem>();
		List<Problem> problems;
		problems = App.getInstance().getExamDBHelper().getProblemsByPaperId(paper.id);
		
		//短文听写处理成一个题
    	boolean isHasDictation = false;
    	for(Problem problem : problems){
    		if(problem.type == Problem.PASSAGE_DICTATION){
    			if(isHasDictation){
    				continue;
    			}
    			isHasDictation = true;
    		}
    		results.add(problem);
    	}
    	
    	if(withResult){
    		App.getInstance().getDBHelper().getResultByProblems(App.getInstance().getUserDB(), results);
    	}
    	return results;
	}

}
