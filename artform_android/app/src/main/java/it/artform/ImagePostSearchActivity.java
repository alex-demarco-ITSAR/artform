package it.artform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import it.artform.feed.PostGridAdapter;
import it.artform.pojos.Post;
import it.artform.pojos.Topic;
import it.artform.web.ArtformApiEndpointInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagePostSearchActivity extends Activity {
    SearchView contentSearchView = null;
    Button searchArtworksButton = null;
    Button searchVideosButton = null;
    Button searchArtistsButton = null;
    Spinner topicSpinner = null;
    GridView artworksGridView = null;
    TextView noResultTextView = null;

    ArtformApiEndpointInterface apiService = null;

    Topic selectedTopic = null;
    Post[] searchedPosts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post_search);

        //widget setup
        contentSearchView = findViewById(R.id.contentSearchView); //search while typing
        searchArtworksButton = findViewById(R.id.searchArtworksButton); //no listener
        searchVideosButton = findViewById(R.id.searchVideosButton); //activity
        searchArtistsButton = findViewById(R.id.searchArtistsButton); //activity
        topicSpinner = findViewById(R.id.topicSpinner); //fetch topics
        artworksGridView = findViewById(R.id.artworksGridView); //load posts
        noResultTextView = findViewById(R.id.noResultTextView); //show only when no posts

        //web services setup
        AFGlobal app = (AFGlobal) getApplication();
        apiService = app.retrofit.create(ArtformApiEndpointInterface.class);

        //GET dei Topic
        fetchTopics();

        //imposta Spinner e SearchView da parametri
        Bundle searchParams = getIntent().getExtras();
        if(searchParams != null) {
            contentSearchView.setQuery(searchParams.getCharSequence("keywords"), true);
            Topic topicParam = (Topic) searchParams.get("topic");
            if(topicParam != null) {
                selectedTopic = topicParam;
                ArrayAdapter topicsAdapter = (ArrayAdapter) topicSpinner.getAdapter();
                topicSpinner.setSelection(topicsAdapter.getPosition(selectedTopic));
            }
        }

        //listener barra di ricerca
        contentSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                String selectedTopicName = (selectedTopic == null ? "" : selectedTopic.getName());
                fetchPosts(selectedTopicName, String.valueOf(contentSearchView.getQuery()));
                return true;
            }
        });

        //listener pulsante Video
        searchVideosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoSearchIntent = new Intent(ImagePostSearchActivity.this, VideoPostSearchActivity.class);
                if(selectedTopic != null)
                    videoSearchIntent.putExtra("topic", selectedTopic);
                videoSearchIntent.putExtra("keywords", contentSearchView.getQuery());
                startActivity(videoSearchIntent);
            }
        });

        //listener pulsante Artisti
        searchArtistsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userSearchIntent = new Intent(ImagePostSearchActivity.this, UserSearchActivity.class);
                if(selectedTopic != null)
                    userSearchIntent.putExtra("topic", selectedTopic);
                userSearchIntent.putExtra("keywords", contentSearchView.getQuery());
                startActivity(userSearchIntent);
            }
        });

    }

    private void fetchTopics() {
        Call<List<Topic>> getTopicsCall = apiService.getAllTopics();
        getTopicsCall.enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                if(response.isSuccessful()) {
                    Topic[] topics = new Topic[response.body().size()];
                    for(int i=0; i<topics.length; i++)
                        topics[i] = response.body().get(i);
                    topicSpinner.setAdapter(new ArrayAdapter<Topic>(ImagePostSearchActivity.this, android.R.layout.simple_spinner_dropdown_item, topics));
                    topicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedTopic = (Topic) adapterView.getItemAtPosition(i);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
                else
                    Toast.makeText(ImagePostSearchActivity.this, "Error while fetching topics", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {
                Toast.makeText(ImagePostSearchActivity.this, "Error while fetching topics: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchPosts(String topic, String keywords) {
        Call<List<Post>> getPostsByFiltersCall = apiService.getPostsByFilters(topic, keywords, "img");
        getPostsByFiltersCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()) {
                    searchedPosts = new Post[response.body().size()];
                    for(int i=0; i<searchedPosts.length; i++)
                        searchedPosts[i] = response.body().get(i);
                    //Caricamento post nella GridView
                    if(searchedPosts.length > 0) {
                        noResultTextView.setVisibility(View.INVISIBLE);
                        artworksGridView.setAdapter(new PostGridAdapter(ImagePostSearchActivity.this, searchedPosts));
                        artworksGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                openPostDetails(position);
                            }
                        });
                    }
                    else
                        noResultTextView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                noResultTextView.setVisibility(View.VISIBLE);
                Toast.makeText(ImagePostSearchActivity.this, "Error while searching posts: " + t.toString(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private void openPostDetails(int pos) {
        Intent postListIntent = new Intent(ImagePostSearchActivity.this, PostListActivity.class);
        postListIntent.putExtra("postList", searchedPosts);
        postListIntent.putExtra("postIndex", pos);
        startActivity(postListIntent);
    }
}