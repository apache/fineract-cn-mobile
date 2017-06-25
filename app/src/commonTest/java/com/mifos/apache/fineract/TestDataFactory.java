package com.mifos.apache.fineract;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * TestDataFactory is reading the input stream of the local resources
 * and parsing the json file data into java object.
 * Created by Rajan Maurya on 25/6/17.
 */
public class TestDataFactory {


    /**
     * Note : This Generic Method DeSerialize Only Json Object in POJO
     * <p/>
     * Note : Do Not use Array [] in POJO classes for of any element initialization,
     * Use Instead ArrayList.
     *
     * @param model    Class of the Model of the Pojo
     * @param jsonName Name of Json file in test/resource
     * @param <T>      Return type
     * @return Return the Object Type model by Deserialize the Json of resources
     * @Example Of Deserialize Object Type Json
     * <p/>
     * Object object = mTestDataFactory.getListTypePojo(
     * new TypeToken<Object>(){}, "Object.json")
     */
    public <T> T getObjectTypePojo(Class<T> model, String jsonName) {
        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonName);
        JsonReader reader = new JsonReader(new InputStreamReader(in));
        return new Gson().fromJson(reader, model);
    }


    /**
     * Note : This Generic Method DeSerialize Both Object and List Type Json in POJO
     * <p/>
     * Note : Do Not use Array [] in POJO classes for of any element initialization,
     * Use Instead ArrayList.
     *
     * @param listModel Class of the List Model
     * @param jsonName  Name of the Json in resources
     * @param <T>       return type
     * @return Return the List of the listModel by Deserialize the Json of resources
     * @Example of Deserialize List Type Json
     * <p/>
     * TestDataFactory mTestDataFactory = new TestDataFactory();
     * <p/>
     * List<Object> listObject = mTestDataFactory.getListTypePojo(
     * new TypeToken<List<Object>>(){}, "ListObject.json")
     * @Example Of Deserialize Object Type Json
     * <p/>
     * Object object = mTestDataFactory.getListTypePojo(
     * new TypeToken<Object>(){}, "Object.json")
     */
    public <T> T getListTypePojo(TypeToken<T> listModel, String jsonName) {
        InputStream in = getClass().getClassLoader().getResourceAsStream(jsonName);
        JsonReader reader = new JsonReader(new InputStreamReader(in));
        return new Gson().fromJson(reader, listModel.getType());
    }


}
