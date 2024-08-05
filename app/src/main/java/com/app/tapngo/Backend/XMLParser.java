package com.app.tapngo.Backend;

import android.util.Xml;

import com.app.tapngo.models.StationDataModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

    // XML tags
    private static final String NS = null;
    private static final String TAG_ARRAY = "ArrayOfObjStationData";
    private static final String TAG_OBJ = "objStationData";


    // XML tags
    private static final String TAG_SERVERTIME = "Servertime";
    private static final String TAG_TRAIN_CODE = "Traincode";
    private static final String TAG_STATION_FULL_NAME = "Stationfullname";
    private static final String TAG_STATION_CODE = "Stationcode";
    private static final String TAG_QUERY_TIME = "Querytime";
    private static final String TAG_TRAIN_DATE = "Traindate";
    private static final String TAG_ORIGIN = "Origin";
    private static final String TAG_DESTINATION = "Destination";
    private static final String TAG_ORIGIN_TIME = "Origintime";
    private static final String TAG_DESTINATION_TIME = "Destinationtime";
    private static final String TAG_STATUS = "Status";
    private static final String TAG_LAST_LOCATION = "Lastlocation";
    private static final String TAG_DUE_IN = "Duein";
    private static final String TAG_LATE = "Late";
    private static final String TAG_EXP_ARRIVAL = "Exparrival";
    private static final String TAG_EXP_DEPART = "Expdepart";
    private static final String TAG_SCH_ARRIVAL = "Scharrival";
    private static final String TAG_SCH_DEPART = "Schdepart";
    private static final String TAG_DIRECTION = "Direction";
    private static final String TAG_TRAIN_TYPE = "Traintype";
    private static final String TAG_LOCATION_TYPE = "Locationtype";

    // Add other tags as needed

    public List<StationDataModel> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readArrayOfObjStationData(parser);
        } finally {
            inputStream.close();
        }
    }

    private List<StationDataModel> readArrayOfObjStationData(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<StationDataModel> stationDataList = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, NS, TAG_ARRAY);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(TAG_OBJ)) {
                stationDataList.add(readStationData(parser));
            } else {
                skip(parser);
            }
        }
        return stationDataList;
    }

    private StationDataModel readStationData(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NS, TAG_OBJ);
        StationDataModel stationData = new StationDataModel();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case TAG_SERVERTIME:
                    stationData.setServertime(readText(parser));
                    break;
                case TAG_TRAIN_CODE:
                    stationData.setTraincode(readText(parser));
                    break;
                case TAG_STATION_FULL_NAME:
                    stationData.setStationfullname(readText(parser));
                    break;
                case TAG_STATION_CODE:
                    stationData.setStationcode(readText(parser));
                    break;
                case TAG_QUERY_TIME:
                    stationData.setQuerytime(readText(parser));
                    break;
                case TAG_TRAIN_DATE:
                    stationData.setTraindate(readText(parser));
                    break;
                case TAG_ORIGIN:
                    stationData.setOrigin(readText(parser));
                    break;
                case TAG_DESTINATION:
                    stationData.setDestination(readText(parser));
                    break;
                case TAG_ORIGIN_TIME:
                    stationData.setOrigintime(readText(parser));
                    break;
                case TAG_DESTINATION_TIME:
                    stationData.setDestinationtime(readText(parser));
                    break;
                case TAG_STATUS:
                    stationData.setStatus(readText(parser));
                    break;
                case TAG_LAST_LOCATION:
                    stationData.setLastlocation(readText(parser));
                    break;
                case TAG_DUE_IN:
                    stationData.setDuein(readText(parser));
                    break;
                case TAG_LATE:
                    stationData.setLate(readText(parser));
                    break;
                case TAG_EXP_ARRIVAL:
                    stationData.setExparrival(readText(parser));
                    break;
                case TAG_EXP_DEPART:
                    stationData.setExpdepart(readText(parser));
                    break;
                case TAG_SCH_ARRIVAL:
                    stationData.setScharrival(readText(parser));
                    break;
                case TAG_SCH_DEPART:
                    stationData.setSchdepart(readText(parser));
                    break;
                case TAG_DIRECTION:
                    stationData.setDirection(readText(parser));
                    break;
                case TAG_TRAIN_TYPE:
                    stationData.setTraintype(readText(parser));
                    break;
                case TAG_LOCATION_TYPE:
                    stationData.setLocationtype(readText(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return stationData;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
