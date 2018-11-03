package angles.com.home.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class PlaceResults{
  @SerializedName("predictions")
  @Expose
  private List<Predictions> predictions;
  @SerializedName("status")
  @Expose
  private String status;
  public PlaceResults(){
  }
  public PlaceResults(List<Predictions> predictions,String status){
   this.predictions=predictions;
   this.status=status;
  }
  public void setPredictions(List<Predictions> predictions){
   this.predictions=predictions;
  }
  public List<Predictions> getPredictions(){
   return predictions;
  }
  public void setStatus(String status){
   this.status=status;
  }
  public String getStatus(){
   return status;
  }
}