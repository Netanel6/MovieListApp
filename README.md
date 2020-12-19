# MovieApp



First screen: SplashActivity

1. Check if you already downloaded the data previously. If you did, retrive the data from the local DataBase and proceed to stage 4 else, go through all stages below.
2. Go to http://api.androidhive.info/json/movies.json and download the JSON. 
3. Parse the JSON and save it using SQLite.
4. Go to next screen: movie list

Second screen: MovieListActivity

1. Display the list of movies by release year and from new to old. 
2. Clicking a movie should transfer the user to the Movie details screen.
3. Clicking on the add button should take to a QR scanning screen.
4. Scanning the QR code below, add the movie to DataBase (If the DB already contains the movie, display a SnackBar with message - “Current movie already exist in the Database”.
  

Third screen: MovieDetailsActivity

1. Show a movie details screen which will contain all data from the model.
2. Back press should take you back to the movie list screen.

Bonus:
* Except the SplashActivity & MainActivity try to use Fragments.

