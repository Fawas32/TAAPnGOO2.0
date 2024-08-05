package com.app.tapngo.Backend;

import android.util.Log;

import com.app.tapngo.models.StationDataModel;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// This is an server connection class. This java file is used to fetch data from server or send/update data to/on server..
public class HttpRequest {

    //This function is used to fetch data from server according to provided url and user credentials as arguments//
    public static List<StationDataModel> executeGet() {
        URL url;
        HttpURLConnection server_connection = null;
        try {
            url = new URL(Utils.API_URL);
            server_connection = (HttpURLConnection) url.openConnection();
            server_connection.setRequestMethod("GET");

            // Set request headers
            server_connection.setRequestProperty("Cache-Control", "private, max-age=0");
            server_connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

            InputStream is;
            int status = server_connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK){
                is = server_connection.getErrorStream();
            }else{
                is = server_connection.getInputStream();
            }

            Log.d("server_response",server_connection.getResponseMessage()+"");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            bufferedReader.close();
            List<StationDataModel> stationDataList = parseResponse(response.toString());
            return stationDataList;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("serverFetchError",e.getMessage());
            return null;
        } finally {
            if (server_connection != null) {
                server_connection.disconnect();
            }
        }
    }

    private static List<StationDataModel> parseResponse(String xmlResponse) {
        InputStream inputStream = new ByteArrayInputStream(xmlResponse.getBytes());
        XMLParser xmlParser = new XMLParser();
        List<StationDataModel> stationDataList = new ArrayList<>();
        try {
            stationDataList = xmlParser.parse(inputStream);
            Log.d("dataSize",stationDataList.size()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stationDataList;
    }
}
