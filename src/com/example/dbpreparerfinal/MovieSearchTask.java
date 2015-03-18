package com.example.dbpreparerfinal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.example.dbpreparerfinal.MovieContract.MovieEntry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class MovieSearchTask extends AsyncTask<String, Void, ArrayList<ArrayList<String>>> {

	MovieDbHelper dbHelper = new MovieDbHelper(GameApp.getAppInstance());

	@Override
	protected ArrayList<ArrayList<String>> doInBackground(String... strings) {

		ArrayList<ArrayList<String>> completeMovieList = new ArrayList<ArrayList<String>>();
		ArrayList<String> nonjumbledMoviesList = new ArrayList<String>();
		ArrayList<String> jumbledMoviesList = new ArrayList<String>();


		SQLiteDatabase database = dbHelper.getWritableDatabase(); 
		String selectQuery = "SELECT  * FROM " + MovieEntry.TABLE_NAME;
		Cursor c = database.rawQuery(selectQuery, null);

//		System.out.println("ANKUR "+c.getCount());
		if (c.moveToFirst()){ 

			//			for(int i = 0 ; i < 500 ; i++){
			do{
				String movieName;
				String[] movieWords = null;
				String jumbledMovieName = "";
				//fetch names from db and store in list1
				movieName = c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_TITLE)) ;

//				if (movieName.length() < 11){
//					System.out.println("movie name fetched from DB " + movieName);
					nonjumbledMoviesList.add(movieName);

					movieWords = movieName.split("\\s+");

					for(int j = 0 ; j < movieWords.length ; j++){

						jumbledMovieName += shuffle(movieWords[j]);

						if( j < (movieWords.length - 1) )
							jumbledMovieName += " ";

					}
//					System.out.println("JUMBLED name " + jumbledMovieName);
					//jumble the alphabets and store in list2
					jumbledMoviesList.add(jumbledMovieName );
//				}
				c.moveToNext();
			}while(c.moveToNext());
			//			}
			long seed = System.nanoTime();
			Collections.shuffle(nonjumbledMoviesList, new Random(seed));
			Collections.shuffle(jumbledMoviesList, new Random(seed));

			completeMovieList.add(0, nonjumbledMoviesList);
			completeMovieList.add(1, jumbledMoviesList);



		}

		return completeMovieList;
	}

	@Override
	protected void onPostExecute(final ArrayList<ArrayList<String>> movieList) {

//		responseListener.onReceiveResult(movieList);

		//		for(String name: movieList.get(0))
		//			System.out.println("original Names = " + name);
		//
		//		for(String name: movieList.get(1))
		//			System.out.println("jumbled Names = " + name);
	}

	private String shuffle(String input){
		List<Character> characters = new ArrayList<Character>();
		for(char c:input.toCharArray()){
			characters.add(c);
		}
		StringBuilder output = new StringBuilder(input.length());
		while(characters.size()!=0){
			int randPicker = (int)(Math.random()*characters.size());
			output.append(characters.remove(randPicker));
		}
		return output.toString();
	}
}
