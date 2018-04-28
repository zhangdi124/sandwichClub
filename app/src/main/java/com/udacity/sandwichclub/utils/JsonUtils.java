package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.serialization.JObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class JsonUtils {
    public static Sandwich parseSandwichJson(String json) {
        if(json == null)
            return null;

        json = json.trim();
        if(json.length() == 0)
            return null;

        final StringReader reader = new StringReader(json);
        final StreamTokenizer tokenizer = new StreamTokenizer(reader);
        tokenizer.quoteChar((int)'"');

        try {
            JObject root = new JObject();
            Stack<JObject> jsonStack = new Stack<>();
            String lastProp = null;

            List<String> workingArray = null;

            while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
                switch(tokenizer.ttype){
                    //Open { bracket
                    case 123:
                        if(jsonStack.isEmpty())
                            jsonStack.push(root);
                        else{
                            jsonStack.push(new JObject(lastProp));
                            lastProp = null;
                        }
                        break;
                    //Close } bracket
                    case 125:
                        JObject jObject = jsonStack.pop();
                        if(!jsonStack.isEmpty()){
                            jsonStack.peek().setProperty(jObject.getName(), jObject);
                        }
                        break;
                    //Open [ Array
                    case 91:
                        workingArray = new ArrayList<>();
                        break;
                    //Close ] Array
                    case 93:
                        if(workingArray != null && !jsonStack.isEmpty()){
                            jsonStack.peek().setProperty(lastProp, workingArray);
                            lastProp = null;
                            workingArray = null;
                        }
                        break;

                    //Quoted string - either a value or a property
                    case 34:
                        if(lastProp == null){
                            lastProp = tokenizer.sval;
                        }else{
                            if(workingArray != null){
                                workingArray.add(tokenizer.sval);
                            }else if(!jsonStack.isEmpty()){
                                jsonStack.peek().setProperty(lastProp, tokenizer.sval);
                                lastProp = null;
                            }
                        }
                        break;

                    default:
                        break;
                }
            }

            //JObject is assembled at this point and we can create a sandwich
            //String mainName, List<String> alsoKnownAs, String placeOfOrigin, String description, String image, List<String> ingredients
            final String mainName = root.getJObject("name").getString("mainName");

            final List<String> alsoKnownAs = (List<String>)root.getJObject("name").getList("alsoKnownAs");
            final String placeOfOrigin = root.getString("placeOfOrigin");
            final String description = root.getString("description");
            final String image = root.getString("image");
            final List<String> ingredients = (List<String>)root.getList("ingredients");

            return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
        }catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }
}
