package hash;

//
// SEQREADER.JAVA
// Read a sequence from a file.  The file is assumed to contain a single
// sequence, possibly split across multiple lines.  Case is not preserved.
//

import java.io.*;
import java.net.*;

import javax.net.ssl.HttpsURLConnection;

public class SeqReader {
    public static String readSeq(String fileName)
    {
	StringWriter buffer = new StringWriter();
	BufferedReader r = null;
	
	parsing: {
	    
	    //
	    // Create a reader for the file
	    //
		URL url;
		HttpsURLConnection connection;
		try {
			url = new URL(fileName);
			connection = (HttpsURLConnection)url.openConnection();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			break parsing;
		} catch (Exception e) {
			e.printStackTrace();
			break parsing;
		}
		InputStream is;
		try {
			is = connection.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
			break parsing;
		}
		r = new BufferedReader(new InputStreamReader(is));
	    
	    // Accumulate each line of the file (minus surrounding
	    // whitespace) sequentially in a string buffer.  Convert
	    // to lower case as we read.
	    //
	    try {
		boolean stop = false;
		
		while (!stop)
		    {
			String nextline = r.readLine();
			if (nextline == null) // end of file
			    stop = true;
			else
			    {
				String seq = nextline.trim();
				buffer.write(seq.toLowerCase());
			    }
		    }
	    }
	    catch (IOException e) {
		System.out.println("IOException while reading sequence from " +
				   fileName + "\n" + e);
		break parsing;
	    }
	}
	
	//
	// final cleanup
	//
	
	if (r != null)
	    {
		try {
		    r.close();
		} catch (IOException e) {
		    // error in closing stream
		}
	    }
	
	return buffer.toString();
    }
}
