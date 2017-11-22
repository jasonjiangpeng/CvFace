package com.minhaskamal.genderRecognizer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.minhaskamal.genderRecognizer.weightedPixel.WeightedStandardPixelTrainer;


public class Predict {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("===");
		Mat matImage = Highgui.imread("src/res/sample/e1.png");
		//	Imgproc.resize(matImage, matImage, new Size(90,90));
		gender(matImage);
		
	}
	public static void oldData() {
		WeightedStandardPixelTrainer  weightedStandardPixelTrainer = new WeightedStandardPixelTrainer();
		//sample file
		String imageFilePath = "src/res/sample/1_female.jpg";
		Mat[] faces = new FaceDetector().snipFace(imageFilePath, new Size(90, 90));
				//experience file
		weightedStandardPixelTrainer.load("src/res/knowledge/knowledge.log");
		System.out.println(faces.length);
		int faceNo=1;
		for(Mat face: faces){
					int prediction = weightedStandardPixelTrainer.predict(face);
			System.out.println(prediction);
			if(prediction==-1){
				System.out.println("I think " + faceNo + " is not a face.");
				Highgui.imwrite("src/res/sample/" + faceNo + "_noface.jpg", face);
			}else if(prediction==0){
				System.out.println("I think " + faceNo + " is a female.");
				Highgui.imwrite("src/res/sample/" + faceNo + "_female.jpg", face);
			}else{
				System.out.println("I think " + faceNo + " is a male.");
				Highgui.imwrite("src/res/sample/" + faceNo + "_male.jpg", face);
			}
			
			faceNo++;
		}
		
		System.out.println("Operation Successful!!!");
	}
	public static void gender(Mat face) {
		WeightedStandardPixelTrainer weightedStandardPixelTrainer = new WeightedStandardPixelTrainer();
		weightedStandardPixelTrainer.load("src/res/knowledge/knowledge.log");
		int prediction = weightedStandardPixelTrainer.predict(face);
		System.out.println(prediction);
		if(prediction==-1){
			System.out.println("I think " +"is not a face.");
		//	Highgui.imwrite("src/res/sample/" + faceNo + "_noface.jpg", face);
		}else if(prediction==0){
			System.out.println("I think " +  " is a female.");
			//Highgui.imwrite("src/res/sample/" + faceNo + "_female.jpg", face);
		}else{
			System.out.println("I think " +  " is a male.");
		//	Highgui.imwrite("src/res/sample/" + faceNo + "_male.jpg", face);
		}
		
	}
}
