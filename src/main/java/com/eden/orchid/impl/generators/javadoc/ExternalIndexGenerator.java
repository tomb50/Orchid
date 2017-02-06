package com.eden.orchid.impl.generators.javadoc;

import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.generators.Generator;
import com.eden.orchid.impl.resources.JsonResource;
import com.eden.orchid.resources.OrchidPage;
import com.eden.orchid.resources.OrchidReference;
import com.eden.orchid.resources.OrchidResource;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.javadoc.RootDoc;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AutoRegister
public class ExternalIndexGenerator implements Generator {

    private static OkHttpClient client = new OkHttpClient();
    public JSONArray externalClasses;
    private List<OrchidPage> meta;
    private JSONObject siteAssets;

    @Override
    public String getName() {
        return "externalClasses";
    }

    @Override
    public int priority() {
        return 900;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public JSONElement startIndexing() {
        RootDoc root = Orchid.getRootDoc();

        if(root == null) {
            return null;
        }

        externalClasses = new JSONArray();
        meta = new ArrayList<>();
        siteAssets = new JSONObject();

        loadExternalClasses();

        OrchidResource resource = new JsonResource(new JSONElement(externalClasses), new OrchidReference("meta/externalClasses.json"));
        OrchidPage entry = new OrchidPage(resource);
        JSONObject index = new JSONObject();
        index.put("name", entry.getReference().getTitle());
        index.put("url", entry.getReference().toString());

        meta.add(entry);

        OrchidUtils.buildTaxonomy(resource, siteAssets, index);

        return new JSONElement(index);
    }

    @Override
    public void startGeneration() {
        RootDoc root = Orchid.getRootDoc();

        if(root == null) {
            return;
        }

        for (OrchidPage metaPage : meta) {
            metaPage.renderRawContent();
        }
    }

    private void loadExternalClasses() {
        Object additionalClasses = Orchid.query("options.data.additionalClasses").getElement();

        if(additionalClasses instanceof JSONArray) {
            for(int i = 0; i < ((JSONArray) additionalClasses).length(); i++) {
                loadAdditionalFile(((JSONArray) additionalClasses).getString(i));
            }
        }
    }

    private void loadAdditionalFile(String url) {
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();

            if(response.isSuccessful()) {
                JSONArray responseArray = new JSONArray(response.body().string());
                for(int i = 0; i < responseArray.length(); i++) {
                    externalClasses.put(responseArray.getJSONObject(i));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}