package com.example.dovchin.horseapplication1;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import java.util.List;
/**
 * Created by amour on 2/15/2017.
 */
public class SearchableActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }
    private void doMySearch(String query){
        /*List<ItemObject> dictionaryObject = databaseObject.searchDictionaryWords(query);
        SearchAdapter mSearchAdapter = new SearchAdapter(SearchableActivity.this, dictionaryObject);
        listView.setAdapter(mSearchAdapter);*/
    }
}