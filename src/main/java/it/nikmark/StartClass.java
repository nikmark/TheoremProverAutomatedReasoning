package it.nikmark;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import it.nikmark.comparators.ClauseComparator;
import it.nikmark.parser.TPTPParser;

import it.nikmark.datastructures.Clause;

class StartClass {
	
	  /**
	   * The to select queue.
	   */
	  private static Queue<Clause> to_select = new PriorityQueue<Clause>();
	
	  /**
	   * The selected list.
	   */
	  private static final ArrayList<Clause> selected = new ArrayList<Clause>();
	
	public static void main(String args[]) {
		
		if (args.length < 1) {
			System.out.println("Errore: parametri non inseriti.");
		
			System.out
	 		.println("usage:\t java TheoremProver.jar -sos [-otter | -e ] [-min | -max | -fifo] [-t <num>] <file> \t\n");
			System.exit(-1);
		}
		
		String[] arguments = new String[args.length-1];
		for(int i=0;i<args.length -1;i++){
			arguments[i]=args[i].substring(1);
		}
		String fileName= args[args.length - 1];

		StringBuilder input = new StringBuilder();
		try {
		  FileReader reader = new FileReader(fileName);
		  Scanner in = new Scanner(reader);
		  while (in.hasNextLine())
			input.append(in.nextLine()).append("\n");
		  reader.close();
		  in.close();
		} catch (FileNotFoundException e) {
		  
		  System.out.println("Oops: File Not Found!");
		  System.exit(-1);
		} catch (IOException e) {
		  e.printStackTrace();
		  System.exit(-1);
		}


		GivenClause.parseArguments(arguments);
		System.out.println("parse= "+GivenClause.rPlan);
		
		switch (GivenClause.queue) {
			case 0:
				//Use FIFO
				  to_select = new LinkedList<Clause>();
				  break;
			case 1:
				//Min priority
			case 2:
				//Max Priority
				to_select = new PriorityQueue<Clause>(10, new ClauseComparator());
				break;
		}
        
		System.out.println("stampa");

		new TPTPParser(input.toString(), to_select, selected, GivenClause.sos);

//		
//		System.out.println("to_select after pars: "+to_select.toString());
//		System.out.println("selected after pars: "+selected.toString());


		
		System.out.println("result = " + GivenClause.givenClauseAlgorithm(to_select, selected));
		
//		ExecutorService executor = Executors.newSingleThreadExecutor();
//		Callable<Boolean> task = new Callable<Boolean>() {
//			  public Boolean call() {
//				return GivenClause.givenClauseAlgorithm(to_select, selected);
//			  }
//			};
//
//			Collection<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
//			tasks.clear();
//			tasks.add(task);
//			Boolean res = false;
//			
//			List<Future<Boolean>> taskFutures = new LinkedList<Future<Boolean>>();
//			try {
//				taskFutures = executor.invokeAll(tasks, GivenClause.timeout, TimeUnit.MINUTES);
//				if(taskFutures.get(0).isDone()){
//					res = taskFutures.get(0).get();
//
//				}else{
//					System.out.println("Soluzione UNKNOWN");
//				}
//			} catch (InterruptedException e) {
//				System.out.println("Timeout Raggiunto, soluzione UNKNOWN");
//				System.exit(-1);
//
//			} catch (ExecutionException e) {
//				System.exit(-1);
//			}
//			catch (CancellationException e) {
//				System.out.println("Timeout Raggiunto, soluzione UNKNOWN");
//				System.exit(-1);
//
//			}
//			System.out.println("risultato = " + res);
//
//			System.exit(-1);

//			try{
//				System.in.read();
//			}catch (IOException e){
//				
//			}


	}
}
