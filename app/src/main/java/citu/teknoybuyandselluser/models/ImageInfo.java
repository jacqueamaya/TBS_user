package citu.teknoybuyandselluser.models;

import org.json.JSONException;
import org.json.JSONObject;

public class ImageInfo {
    private String fileType;
    private String link;
    private int width;
    private int height;
    private double size;

    public String getFileType() {
        return fileType;
    }

    public String getLink() {
        return link;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getSize() {
        return size;
    }

    public static ImageInfo getImageInfo(JSONObject jsonObject){
        ImageInfo imginfo = new ImageInfo();

        try {
            if(!jsonObject.isNull("data")){
                JSONObject data = jsonObject.getJSONObject("data");
                imginfo.fileType = data.getString("type");
                imginfo.link = data.getString("link");
                imginfo.width = data.getInt("width");
                imginfo.height = data.getInt("height");
                imginfo.size = data.getDouble("size");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return imginfo;
    }
}
