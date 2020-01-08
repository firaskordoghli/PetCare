package kordoghli.firas.petcare.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageResponse {

    @SerializedName("filename")
    @Expose
    private String filename;

    public ImageResponse(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
