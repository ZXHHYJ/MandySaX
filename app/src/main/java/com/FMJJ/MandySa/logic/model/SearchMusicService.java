package com.FMJJ.MandySa.logic.model;
import mandysax.core.annotation.ARRAY;
import mandysax.core.annotation.GET;
import mandysax.core.annotation.INT;
import mandysax.core.annotation.STRING;
import org.json.JSONArray;

@GET("ARRAY")
public class SearchMusicService
{
	@STRING("name")
	public String name;
	
	@INT("id")
	public int id;
	 
	@ARRAY("artists")
	public JSONArray artists;
}
