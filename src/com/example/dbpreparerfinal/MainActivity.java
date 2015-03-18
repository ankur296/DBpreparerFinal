package com.example.dbpreparerfinal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import com.example.dbpreparerfinal.MovieContract.MovieEntry;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new Thread(new Runnable() {

			@Override
			public void run() {

				ArrayList<Movie> MovieList;
				try { 
					//					MovieList = retrieveMoviesList(""); 
					MovieDbAssetHelper dbAssetHelper = new MovieDbAssetHelper(GameApp.getAppInstance());
										MovieDbHelper dbHelper = new MovieDbHelper(MainActivity.this);
					SQLiteDatabase assetdb = dbAssetHelper.getWritableDatabase();  
										SQLiteDatabase db = dbHelper.getWritableDatabase(); 
					ContentValues values = new ContentValues(); 
 
					//open the existing db and fetch items
					String selectQuery = "SELECT  * FROM " + MovieEntry.TABLE_NAME;
					Cursor c = assetdb.rawQuery(selectQuery, null);

					if (c.moveToFirst()){ 
						do{

							//fetch the title first
							String movieName;
							movieName = c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_TITLE)) ;

							//fetch id
							int id;
							id = c.getInt(c.getColumnIndexOrThrow(MovieEntry.COLUMN_ENTRY_ID)) ;

							ArrayList<ArrayList<String>> completeCastList = retrieveCastList(id);
							ArrayList<String> castList = completeCastList.get(0);
							ArrayList<String> characterList = completeCastList.get(1);
							
							JSONObject jsonCast = new JSONObject();
							jsonCast.put("starcast", new JSONArray(castList));
							String castListString = jsonCast.toString();
							
							JSONObject jsonChar = new JSONObject();
							jsonChar.put("character", new JSONArray(characterList));
							String charListString = jsonChar.toString();
							
//							To Read, Read the string from db as String,
//
//							 JSONObject json = new JSONObject(stringreadfromsqlite);
//							  ArrayList items = json.optJSONArray("uniqueArrays");
							
							System.out.println("dbprep " + id +  " Title = " + movieName);
							
//							for(int i = 0 ; i < completeCastList.get(0).size() ; i++){
//								System.out.println("dbprep Cast = " + completeCastList.get(0).get(i) + " Char = " + completeCastList.get(1).get(i) );
//							}
							
							//fetch votecount
							int votecount;
							votecount = c.getInt(c.getColumnIndexOrThrow(MovieEntry.COLUMN_VOTE_COUNT)) ;
 
							
							//Write the fetched data into a new DB
							values.put("entryid", id); 
							values.put("title", movieName);
							values.put("votecount", votecount);
							values.put("starcast", castListString);
							values.put("character", charListString);
							
							
							db.insert("entry", null, values);
							
							c.moveToNext();
						}while(c.moveToNext());
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}



			}
		}).start();

	}


	static int pageNo = 0;
	public static ArrayList<Movie> parseMovieResponse(String jsonStr) {

		try{

			JSONObject jsonObject = new JSONObject(jsonStr);
			JSONArray results = jsonObject.getJSONArray("results");

			ArrayList<Movie> completeMovieList = new ArrayList<Movie>();

			for(int i = 0; i < results.length(); i++){

				Movie movie = new Movie();

				JSONObject result = results.getJSONObject(i);

				//Handle title
				movie.title = result.getString("title");

				String[] movieWords = null;
				String jumbledMovieName = "";  

				if ( 
						( movie.title .length() < 11 ) 
						&& ( movie.title .matches("[a-zA-Z0-9.? ]*") ) 
						&& (!movie.title .toLowerCase().contains("sex"))  
						&& (!movie.title .toLowerCase().contains("dick")) 
						&& (!movie.title .toLowerCase().contains("ass")) 
						){

					System.out.println("ankur MOVIE NAME : "+ movie.title );

					//Handle votecount
					movie.votecount = result.getInt("vote_count");

					//Handle entryId
					movie.entryId = result.getInt("id");


					completeMovieList.add(movie); 

					/*movieWords = movie.title.split("\\s+");

					for(int j = 0 ; j < movieWords.length ; j++){

						jumbledMovieName += shuffle(movieWords[j]);

						if( j < (movieWords.length - 1) )
						jumbledMovieName += " ";

					}
					jumbledMoviesList.add(jumbledMovieName );*/
				}
			}

			long seed = System.nanoTime();
			//			Collections.shuffle(nonjumbledMoviesList, new Random(seed));
			//			Collections.shuffle(jumbledMoviesList, new Random(seed));

			//			completeMovieList.add(0, nonjumbledMoviesList);
			//			completeMovieList.add(1, jumbledMoviesList);

			return completeMovieList;

		}
		catch(JSONException e){
			System.out.println("ERROR: Response field changed !!");
			e.printStackTrace();
		}
		return null;
	}

	ArrayList<ArrayList<String>> retrieveCastList(int id) throws JSONException{


		HttpRetriever httpRetriever = new HttpRetriever();
		String url = constructSearchUrl(id);
		String response = httpRetriever.retrieve(url);

		System.out.println("Ankur response fetched !!");
		JSONObject jsonObject = new JSONObject(response);
		JSONArray castArray = jsonObject.getJSONArray("cast");

		ArrayList<ArrayList<String>> completeCastList = new ArrayList<ArrayList<String>>();
		ArrayList<String> castList = new ArrayList<String>();
		ArrayList<String>  characterList = new ArrayList<String>();

		for(int i = 0 ; i < castArray.length() ; i++){
			castList.add(i, castArray.getJSONObject(i).getString("name"));
			characterList.add(i, castArray.getJSONObject(i).getString("character"));
		}

		completeCastList.add(0, castList);
		completeCastList.add(1, characterList);
		return completeCastList;
	}

	//	http://api.themoviedb.org/3/movie/id/credits
	// http://api.themoviedb.org/3/discover/movie?api_key=196527b28198a82e77196ba38b0d32fb&sort_by=vote_count.asc&language=en&vote_count.gte=50
	protected String constructSearchUrl(int id) {

		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http")
		.authority("api.themoviedb.org")
		.appendPath("3")
		.appendPath("movie")
		.appendPath(Integer.toString(id) )
		.appendPath("credits")
		.appendQueryParameter("api_key", "196527b28198a82e77196ba38b0d32fb");

		return builder.build().toString();
	}
}
