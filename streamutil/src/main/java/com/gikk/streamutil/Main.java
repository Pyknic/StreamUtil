package com.gikk.streamutil;

import java.net.URISyntaxException;

import com.gikk.streamutil.misc.GikkPreferences;
import com.gikk.streamutil.task.Scheduler;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Starting point for the application. 
 *
 * @author Simon
 */
public class Main extends Application{
	
	@FXML TextField txt_field;
	@FXML TextField txt_field2;
	
	public void start(Stage primaryStage) throws URISyntaxException {
		ClassLoader cl = this.getClass().getClassLoader();	        
		Scene primaryScene = null;

		boolean firstLaunch = GikkPreferences.GET().isFirstLaunch();
		if( firstLaunch ){
			primaryScene = firstLaunch( primaryStage, primaryScene, cl);
		} else {
			primaryScene = normalLaunch(primaryStage, primaryScene, cl);
		}
		
		primaryStage.setScene(primaryScene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {		
		launch(args);
	}
	
	/**If this is the first time the application is launched, we want to run the Initialization process
	 */
	private Scene firstLaunch(Stage primaryStage, Scene primaryScene, ClassLoader cl){
		try {
			primaryScene = new Scene(FXMLLoader.load( cl.getResource("init/InitWindow.fxml") ) );			
			primaryStage.setOnCloseRequest( (e) -> {
				Scheduler.GET().onProgramExit();
			} );	
			primaryStage.setResizable(false);
			primaryStage.setTitle("GikkBot initialization process");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return primaryScene;
	}
	
	/**If this isn't the first time we launch the application, we just display the regular interface
	 */
	private Scene normalLaunch(Stage primaryStage, Scene primaryScene, ClassLoader cl){
		try {
			primaryScene = new Scene(FXMLLoader.load( cl.getResource("MainWindow.fxml") ) );
			primaryScene.getStylesheets().add( cl.getResource("application.css").toExternalForm() );			
			primaryStage.setOnCloseRequest( (e) -> {
				GikkBot.GET().onProgramExit();
				Scheduler.GET().onProgramExit();
				GikkPreferences.GET().clearProperties(); //TODO: REMOVE
			} );	
			primaryStage.setTitle("GikkBot dashboard");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return primaryScene;
	}
}