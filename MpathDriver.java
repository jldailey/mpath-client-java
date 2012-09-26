import java.util.*;
import java.net.*;
import com.google.gson.*;

public class MpathDriver {

	static class Config { // Configuration belongs in the environment
		private static Map<String, String> env = System.getenv();
		public static String get(String name) {
			return Config.get(name, "");
		}
		public static String get(String name, String dflt ) {
			if( env.containsKey(name) )
				return env.get(name);
			return dflt;
		}
	}

	public enum DecisionPolicy {
		explore, exploit, sticky, control
	}
	public class Choice {
		public String code;
		public String name;
		public Map<String, String> meta;
	}
	public class Decision {
		public String session;
		public DecisionPolicy policy;
		public String pointCode;
		public Map<String, Choice> decisions;
		public String segment;
		public Map<String, Double> features;
	}
	public class SimpleDecision {
		public String session;
		public String decision;
	}

	Gson gson = new Gson();

	public final static void main(String[] args) throws Exception {
		String MPATH_URL = Config.get("MPATH_URL", "https://api.conductrics.com");
		System.out.println(MPATH_URL);
		MpathDriver driver = new MpathDriver(MPATH_URL, "acme", "api_1234");
		Gson gson = new Gson();
		String session = "1234";
		System.out.println(gson.toJson(driver.decideSimple(session, "agent-1", new String[] { "one", "two" })));
		System.out.println(gson.toJson(driver.decide(session, "agent-1", new String[] { "one", "two" })));
		System.out.println(gson.toJson(driver.decide(session, "agent-1", "point-1")));
	}

	public String ownerCode;
	public String baseUrl;
	public String apiKey;
	public MpathDriver(String baseUrl, String ownerCode, String apiKey) {
		this.ownerCode = ownerCode;
		this.baseUrl = baseUrl;
		this.apiKey = apiKey;
	}

	private String join(String[] ar, String sep) {
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < ar.length; i++) {
			sb.append(ar[i]);
			if( i < ar.length - 1 )
				sb.append(sep);
		}
		return sb.toString();
	}

	public SimpleDecision decideSimple(String sessionCode, String agentCode, String[] options) throws java.io.IOException {
		String url = this.baseUrl
			+ "/" + this.ownerCode
			+ "/" + agentCode
			+ "/decision/"
			+ join(options, ",")
			+ "?session=" + sessionCode
			+ "&apikey=" + apiKey;
		return gson.fromJson(new Request(url).get(), SimpleDecision.class);
	}
	public Decision decide(String sessionCode, String agentCode, String[] options) throws java.io.IOException {
		String url = this.baseUrl
			+ "/" + this.ownerCode
			+ "/" + agentCode
			+ "/decisions/"
			+ join(options, ",")
			+ "?session=" + sessionCode
			+ "&apikey=" + apiKey;
		return gson.fromJson(new Request(url).get(), Decision.class);
	}
	public Decision decide(String sessionCode, String agentCode, String pointCode) throws java.io.IOException {
		String url = this.baseUrl
			+ "/" + this.ownerCode
			+ "/" + agentCode
			+ "/decisions"
			+ "?point=" + pointCode
			+ "&session=" + sessionCode
			+ "&apikey=" + apiKey;
		return gson.fromJson(new Request(url).get(), Decision.class);
	}
	public void reward(String sessionCode, String agentCode, String goalCode) throws java.io.IOException {
		String url = this.baseUrl
			+ "/" + this.ownerCode
			+ "/" + agentCode
			+ "/goal/" + goalCode
			+ "?session=" + sessionCode
			+ "&apikey=" + apiKey;
		new Request(url); // dont wait for an answer
	}
	public void expire(String agentCode, String sessionCode) throws java.io.IOException {
		String url = this.baseUrl
			+ "/" + this.ownerCode
			+ "/" + agentCode
			+ "/expire"
			+ "?session=" + sessionCode
			+ "&apikey=" + apiKey;
		new Request(url);
	}

}

