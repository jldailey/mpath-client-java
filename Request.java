
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Request implements Future<String> {
	private HttpURLConnection connection = null;
	public Request(String uri) throws IOException, ProtocolException {
		this("GET", uri, null, null);
	}
	public Request(String method, String uri, Map<String, String> headers, Map<String, String> formData)
		throws IOException, ProtocolException
	{
		method = method.toUpperCase();
		URL url = new URL(uri);
		String proto = url.getProtocol();
		connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod(method);
		if( headers != null ) {
			for( String key : headers.keySet() ) {
				connection.setRequestProperty( key, headers.get(key) );
			}
		}
		if( formData != null ) {
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			for( String key : formData.keySet() ) {
				out.write(key);
				out.write('=');
				out.write(formData.get(key));
			}
		}
	}

	public String get(long timeout, TimeUnit unit) {
		connection.setReadTimeout((int)unit.toMillis(timeout));
		return get();
	}
	public String get() {
		try {
			connection.connect();
			return new Scanner(connection.getInputStream()).useDelimiter("\\A").next();
		} catch( NoSuchElementException e ) {
			return "";
		} catch( IOException e ) {
			return "";
		}
	}

	private boolean cancelled = false;
	private boolean done = false;
	public boolean cancel(boolean mayInterrupt) {
		connection.disconnect();
		done = cancelled = true;
		return true;
	}
	public boolean isCancelled() {
		return cancelled;
	}
	public boolean isDone() {
		return done;
	}
}
