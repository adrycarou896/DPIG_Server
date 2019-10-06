package com.util.reconocimiento;

import static org.opencv.objdetect.Objdetect.CASCADE_SCALE_IMAGE;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.math3.util.Pair;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.IPCamera;
import com.util.entrenamiento.Entrenar;
import com.util.patterns.PatternsManager;

@Service
public class ReconocimientoFacial {
	 
    private String RutaDelCascade = "C:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt2.xml";
    private CascadeClassifier Cascade;
    
    private MatOfRect rostros;//Guarda los rostros que va capturando
    
    private Entrenar entrenamiento;
    private Map<Long, Long> personsTimes;
    
    @Autowired
    private PatternsManager patternsManager;
    
    public void setConf(Entrenar entrenamiento){
    	this.Cascade = new CascadeClassifier(RutaDelCascade);
    	this.rostros = new MatOfRect();
    	
    	this.entrenamiento = entrenamiento;
    	this.personsTimes = new HashMap<Long, Long>();
    	//this.server = new Server();
    }
    
    public void reconocer(IPCamera device, Mat frame, Mat frame_gray) throws Exception{
		
		Imgproc.cvtColor(frame, frame_gray, Imgproc.COLOR_BGR2GRAY);//Colvierte la imagene a color a blanco y negro
        Imgproc.equalizeHist(frame_gray, frame_gray);//Valanzeamos los tonos grises
        double w = frame.width();
        double h = frame.height();
        
        Cascade.detectMultiScale(frame_gray, rostros, 1.1, 2, 0|CASCADE_SCALE_IMAGE, new Size(30, 30), new Size(w, h));
        Rect[] rostrosLista = rostros.toArray();
        
        Rect rectCrop = new Rect();

        for (Rect rostro : rostrosLista) {
    		String rutaImagen = "img/persona.jpg";
    	    
    		//Se recorta la imagen
    		rectCrop = new Rect(rostro.x, rostro.y, rostro.width, rostro.height); 
    		Mat frameRecortado = new Mat(frame,rectCrop);
    		
    		//Se guarda la imagen
    		Imgcodecs.imwrite(rutaImagen, frameRecortado);
    		
    		InputStream input = new FileInputStream(rutaImagen);
    		String srcSalida="img/test.jpg";
			OutputStream output = new FileOutputStream(srcSalida);
			resize(input, output, 607, 607);
			
			/*input = new FileInputStream(srcSalida);
			output = new FileOutputStream("img/usuario0/img"+cont+".jpg");
			resize(input, output, 607, 607);
			cont++;*/
			
    		Pair<Integer, Double> personPair = this.entrenamiento.test(srcSalida);
    		if(personPair!=null){
    			long personId = (long) personPair.getFirst();//La id es la label
    			long momentoActual = System.currentTimeMillis();
    			if(!this.personsTimes.containsKey(personId)) {
    				this.personsTimes.put(personId, momentoActual);
    				this.patternsManager.find(device, personId, new Date());
    				//this.server.sendMatch(this.cameraId, personId, new Date());
    			}
    			else {
    				long momentoUltimoMatch = this.personsTimes.get(personId);
    				long tiempoTranscurrido = momentoActual - momentoUltimoMatch;
    				if(tiempoTranscurrido>=5000) {
    					this.personsTimes.replace(personId, momentoActual);
    					this.patternsManager.find(device, personId, new Date());
    					//this.server.sendMatch(this.cameraId, personId, new Date());
    				}
    			}
    			
    		}
    		
        } 
        
    }
    
    public void reconocerRostroYGuardar(int numImagen, Mat frame, Mat frame_gray) throws Exception{
    	Imgproc.cvtColor(frame, frame_gray, Imgproc.COLOR_BGR2GRAY);//Colvierte la imagene a color a blanco y negro
        Imgproc.equalizeHist(frame_gray, frame_gray);//Valanzeamos los tonos grises
        double w = frame.width();
        double h = frame.height();
        
        Cascade.detectMultiScale(frame_gray, rostros, 1.1, 2, 0|CASCADE_SCALE_IMAGE, new Size(30, 30), new Size(w, h));
        Rect[] rostrosLista = rostros.toArray();
        
        Rect rectCrop = new Rect();
        
        int caras=0;
        for (Rect rostro : rostrosLista) {
        	caras++;
    		//Se recorta la imagen
    		rectCrop = new Rect(rostro.x, rostro.y, rostro.width, rostro.height); 
    		Mat frameRecortado = new Mat(frame,rectCrop);
    		
    		String srcSalida="img/usuarioAdrian/faces/img"+numImagen+"cara"+caras+".jpg";
    		//Se guarda la imagen
    		Imgcodecs.imwrite(srcSalida, frameRecortado);
    		
    		//InputStream input = new FileInputStream(rutaImagen);
    		//System.out.println("INPUT: "+input);
    		BufferedImage input=ImageIO.read(new File(srcSalida));
			OutputStream output = new FileOutputStream(srcSalida);
			resizePrueba(input, output, 607, 607);
			
			/*input = new FileInputStream(srcSalida);
			output = new FileOutputStream("img/usuario0/img"+cont+".jpg");
			resize(input, output, 607, 607);
			cont++;*/
        } 
    }
    
    public static void resizePrueba(BufferedImage src, OutputStream output, int width, int height) throws Exception {
    	//BufferedImage src = GraphicsUtilities.createThumbnail(ImageIO.read(file), 300);
	    BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = dest.createGraphics();
	    AffineTransform at = AffineTransform.getScaleInstance
	    		((double)width / src.getWidth(), 
	    				(double)height / src.getHeight());
	    g.drawRenderedImage(src, at);
	    ImageIO.write(dest, "JPG", output);
	    output.close();
	}
    
	public static void resize(InputStream input, OutputStream output, int width, int height) throws Exception {
	    BufferedImage src = ImageIO.read(input);
	    BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = dest.createGraphics();
	    AffineTransform at = AffineTransform.getScaleInstance
	    		((double)width / src.getWidth(), 
	    				(double)height / src.getHeight());
	    g.drawRenderedImage(src, at);
	    ImageIO.write(dest, "JPG", output);
	    output.close();
	}
	 
}
