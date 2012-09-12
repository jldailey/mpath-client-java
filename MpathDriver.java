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
		String MPATH_URL = Config.get("MPATH_URL", "http://localhost:5000");
		System.out.println(MPATH_URL);
		MpathDriver driver = new MpathDriver("acme");
		Gson gson = new Gson();
		System.out.println(gson.toJson(driver.decide("agent-1", new String[] { "one", "two" })));
		System.out.println(gson.toJson(driver.decideSimple("agent-1", new String[] { "one", "two" })));
		System.out.println(gson.toJson(driver.decide("agent-1", "two")));
	}

	public String ownerCode;
	public MpathDriver(String ownerCode) { this.ownerCode = ownerCode; }

	public SimpleDecision decideSimple(String agentCode, String[] options) {
		return gson.fromJson("{'session':'pXDnoWjPMLRcWzNU','decision':'b'}", SimpleDecision.class);
	}
	public Decision decide(String agentCode, String[] options) {
		return gson.fromJson("{'session':'pXDnoWjPMLRcWzNU','policy':'sticky','decisions':{'decision-1':{'code':'b','name':'b'}},'point':'point-1','segment':'(none)','features':{'(none)':1,'jesse':1}}", Decision.class);
	}
	public Decision decide(String agentCode, String pointCode) {
		return gson.fromJson("{'session':'pXDnoWjPMLRcWzNU','policy':'sticky','decisions':{'decision-1':{'code':'b','name':'b'}},'point':'point-1','segment':'(none)','features':{'(none)':1,'jesse':1}}", Decision.class);
	}

}

