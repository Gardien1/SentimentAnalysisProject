package com.sentimentanalysis.usq.sentimentanalysis;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.*;
import java.util.*;

/**
 * Contains a hashmap of words and their scores.  On creation, takes a lexicon file and
 * converts to the hashmap.
 * @author 	Ben Garden
 * @version 1.0
 * @since	2018-09-08
 */
public class Lexicon {
	private HashMap<String,Integer> wordScore = new HashMap<String,Integer>();

	public Lexicon(ArrayList<String> lexiconData) {
		for(Iterator<String> line = lexiconData.iterator(); line.hasNext();)
		{
		    readLexLine(line.next());
		}

		for(String word: wordScore.keySet())
        {

           // Log.i("WORD TEST",word);

        }
	}
	
	private void readLexLine( String line ) {
		List<String> splitLine = Arrays.asList(line.split( " " ));
		//Log.i("READ LEX LINE" , line);
		String word = (splitLine.get(2)).replace( "word1","" );
		Integer score = 0;
		try {

			if( splitLine.get(5) == "priorpolarity=positive" ) {
                if ( splitLine.get(0) == "type=weaksubj" )
                    score = 1;
                else if ( splitLine.get(0) == "type=strongsubj" )
                    score = 2;
                else
                    throw new IllegalArgumentException( "No type given." );
            }

			else if ( splitLine.get(5) == "priorpolarity=negative" ) {
                if ( splitLine.get(0) == "type=weaksubj" )
                    score = -1;
                else if ( splitLine.get(0) == "type=strongsubj" )
                    score = -2;
                //else
                    //throw new IllegalArgumentException( "No type given." );
            }

            else {
                //throw new IllegalArgumentException( "No strength given." );
            }

		}  catch ( IllegalArgumentException exception ) {
            exception.printStackTrace();
        }
        wordScore.put(word,score);
	}

    public Integer scoreTweet( String tweet ){
        List<String> words = Arrays.asList(tweet.split( " " ));
        Integer sum = 0;
        for ( String word : words ) {

            //Log.i("LEXICON SCORE" , "WORD TO BE SCANNED: " + word);
            Log.i("Word Score length", "Length: " + wordScore.keySet().size());
            if(  wordScore.containsKey(word.toLowerCase()) )
            {
                sum += wordScore.get(word.toLowerCase());
                Log.i("LEXICON SCORE" , "WORD: " + word + "SCORE: " + wordScore.get(word));
            }

        }
        return sum;
    }
}
