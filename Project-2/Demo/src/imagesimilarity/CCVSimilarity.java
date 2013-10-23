package imagesimilarity;

import imagesimilarity.ColorCoherence.Result;

// TODO (David) normalize the score

public class CCVSimilarity {
	private static double WEIGHT_COHERENT = 0.7;
	private static double WEIGHT_INCOHERENT = 0.3;
	
	public static double getScore(ProcessedImage img1, ProcessedImage img2) {
		Result[] results1 = img1.getCCV();
		Result[] results2 = img2.getCCV();
		
		double score = 0;
		double scoreCoherent = 0;
		double scoreIncoherent = 0;
		
		// For all colors red, green, blue
		// Assume results1 and results2 have same dimensions
		for (int i = 0; i < results1.length; i++) {
			for (int j = 0; j < results1[i].coherent.length; j++) {
				
				double coherent1 = results1[i].coherent[j];
				double coherent2 = results2[i].coherent[j];
				
				double diff = Math.abs(coherent1 - coherent2);
				double max = Math.max(coherent1, coherent2);
				
				if (max <= 0) {
					// both the sizes are 0, ignore this
					continue; 
				}
				
				scoreCoherent += coherent1 * (1 - (diff / max));
			}
		}
		
		// Do the same thing for incoherent
		for (int i = 0; i < results1.length; i++) {
			for (int j = 0; j < results1[i].incoherent.length; j++) {
				
				double incoherent1 = results1[i].incoherent[j];
				double incoherent2 = results2[i].incoherent[j];
				
				double diff = Math.abs(incoherent1 - incoherent2);
				double max = Math.max(incoherent1, incoherent2);
				
				if (max <= 0) {
					continue; 
				}
				
				scoreIncoherent += incoherent1 * (1 - (diff / max));
			}
		}
		
		score = WEIGHT_COHERENT * scoreCoherent + WEIGHT_INCOHERENT * scoreIncoherent;
		return score;
	}
}
