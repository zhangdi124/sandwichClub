package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private int position = DEFAULT_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(savedInstanceState != null && savedInstanceState.containsKey(EXTRA_POSITION)){
            position = savedInstanceState.getInt(EXTRA_POSITION);
        }else{
            Intent intent = getIntent();
            if (intent == null) {
                closeOnError();
                return;
            }

            position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        }

        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        getSandwichDetails();
    }

    /**
     * Created by DI Ioffe on 4/28/2018.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(EXTRA_POSITION, position);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void getSandwichDetails(){
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);

        final ImageView ingredientsIv = findViewById(R.id.image_iv);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }
    /**
     * Created by DI Ioffe on 4/28/2018.
     */
    private void populateUI(Sandwich sandwich) {
        final TextView ingredients = (TextView) findViewById(R.id.ingredients_tv);
        final TextView aka = (TextView) findViewById(R.id.aka_tv);
        final TextView description = (TextView)findViewById(R.id.description_tv);
        final TextView origin = (TextView)findViewById(R.id.origin_tv);

        updateBullets(ingredients, sandwich.getIngredients());
        updateBullets(aka, sandwich.getAlsoKnownAs());
        description.setText(sandwich.getDescription());
        origin.setText(sandwich.getPlaceOfOrigin());
    }

    private void updateBullets(TextView tv, List<String> strings){
        StringBuilder sb = new StringBuilder();
        for(String s : strings){
            sb.append("• ").append(s).append('\n');
        }

        tv.setText(sb.toString());
    }
}
