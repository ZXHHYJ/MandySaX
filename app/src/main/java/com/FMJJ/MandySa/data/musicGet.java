package com.FMJJ.MandySa.data;
import mandysax.core.annotation.*;
import org.json.*;

@GET("ARRAY")
public class musicGet
{
	@STRING("name")
	public String name;
	
	@INT("id")
	public int id;
	 
	@ARRAY("artists")
	public JSONArray artists;
}
