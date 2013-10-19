import twitter4j.GeoLocation;


public abstract class DistanceBetweenTwoGPS
{
	 private static double deg2rad(double deg)
	    {
	        return (deg * Math.PI / 180.0);
	    }

	    private static double rad2deg(double rad)
	    {
	        return (rad / Math.PI * 180.0);
	    }
	public static Double distanceBetweenTwoGPS(GeoLocation loc1,GeoLocation loc2)
	{
		//https://github.com/grumlimited/geocalc
		Coordinate lat = new DegreeCoordinate(loc1.getLatitude());
		Coordinate lng = new DegreeCoordinate(loc1.getLongitude());
		Point p1 = new Point(lat, lng);

		lat = new DegreeCoordinate(loc2.getLatitude());
		lng = new DegreeCoordinate(loc2.getLongitude());
		Point p2 = new Point(lat, lng);
		System.out.println("POS1:"+p2.latitude+","+p2.longitude);
		System.out.println("POS2:"+p1.latitude+","+p1.longitude);
		double distance = EarthCalc.getDistance(p2, p1); //in meters
		System.out.println("distance in METERS"+distance);
		if(distance<0)
			distance=Math.abs(distance)*2;
		return distance/1000; //km olarak
		
		
	

		    
	}
}
