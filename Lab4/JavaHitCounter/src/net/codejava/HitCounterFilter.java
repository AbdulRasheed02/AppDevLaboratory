/**
 * Copyright by www.codejava.net
 */
package net.codejava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class HitCounterFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ServletContext context = request.getServletContext(); 
		String realWebAppPath = context.getRealPath("");
		String hitFilePath = realWebAppPath.concat("hit.txt");
		File hitFile = new File(hitFilePath);
		
		long currentHit = readHitCounterFromFile(hitFile);

		updateHitCounterFile(++currentHit, hitFile);
		
		CharResponseWrapper wrapper = new CharResponseWrapper((HttpServletResponse) response);	
		
		chain.doFilter(request, wrapper);
		
		
		displayHitCounter(wrapper, response, currentHit);
	}

	private long readHitCounterFromFile(File hitFile) throws NumberFormatException, IOException {
		if (!hitFile.exists()) {
			return 0;
		}
		
		try (BufferedReader reader = new BufferedReader(new FileReader(hitFile));) {
			
			long hit = Integer.parseInt(reader.readLine());
			
			return hit;
		}
		
	}
	
	private void updateHitCounterFile(long hit, File hitFile) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(hitFile));) {
			writer.write(String.valueOf(hit));
		}
	}
	
	private void displayHitCounter(CharResponseWrapper wrapper, ServletResponse response, 
			long currentHit) throws IOException {
		PrintWriter writer = response.getWriter();
		
		if (wrapper.getContentType().contains("text/html")) {
			CharArrayWriter caw = new CharArrayWriter();
			String originalContent = wrapper.toString();
			int indexOfCloseBodyTag = originalContent.indexOf("</body>") - 1;
			
			caw.write(originalContent.substring(0, indexOfCloseBodyTag));
			
			String hitCounterContent = 
				"<p> - Pageviews: " + currentHit + "</p>";
			caw.write(hitCounterContent);
			caw.write("\n</body></html>");
			
			String alteredContent = caw.toString();
			response.setContentLength(alteredContent.length());
			writer.write(alteredContent);
			
		} else {
			writer.write(wrapper.toString());
		}
		
		writer.close();		
	}
}
