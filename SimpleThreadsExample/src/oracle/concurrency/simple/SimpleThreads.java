package oracle.concurrency.simple;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * @author Chris Wong
 * 
 * <p>
 *  <a href="http://docs.oracle.com/javase/tutorial/essential/concurrency/simple.html">SimpleThreads</a>
 * </p>
 */
public class SimpleThreads {
	
	// Display a message, preceded by the name of the current thread
	private static void threadMessage(String message){
		String threadName = Thread.currentThread().getName();
		out.format("%s: %s%n", threadName, message);				// %n, line separator(platform independent)
	}
	
	/**
	 * @author Chris Wong
	 * <p>
	 *  MessageLoop
	 * </p>
	 */
	private static class MessageLoop implements Runnable {
		/**
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run(){
			String importantInfo[] = {
					"Mares eat oats",
					"Does eat oats",
					"Little lambs eat ivy",
					"A kid will eat ivy too"
			};
			
			try {
				for(int i = 0; i < importantInfo.length; i++){
					// Pause for 4 seconds
					Thread.sleep(4000);
					
					// Print a message
					threadMessage(importantInfo[i]);
				}
			}  catch (InterruptedException exception){
				threadMessage("I wasn't done!");
			}
		}
	}
	
	/**
	 * Main program
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String args[]) 
			throws InterruptedException {
		
		// Delay, in milliseconds before we interrupt MessageLoop thread (default one hour).
		long patience = 1000 * 60 * 60;
		
		// If command line argument present, gives patience in seconds.
		if(args.length > 0){
			try {
				patience = Long.parseLong(args[0]) * 1000;
			} catch (NumberFormatException exception){
				err.println("Argument must be an integer.");
				System.exit(1);
			}
		}
		
		threadMessage("Starting MessageLoop thread");
		long startTime = System.currentTimeMillis();
		Thread thread = new Thread(new MessageLoop());
		thread.start();
		
		threadMessage("Waiting for MessageLoop thread to finish");
		// Loop until MessageLoop thread exits
		while(thread.isAlive()){
			threadMessage("Still waiting...");
			// Wait maximum of 1 second for MessageLoop thread to finish.
			thread.join(1000);
			if(((System.currentTimeMillis() - startTime) > patience) && thread.isAlive()){
				threadMessage("Tired of waiting!");
				thread.interrupt();
				
				// Shouldn't be long now -- wait indefinitely
				thread.join();
			}
		}
		threadMessage("Finally!");
	}
	
}
