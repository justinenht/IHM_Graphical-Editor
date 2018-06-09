package sample.controller;

import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Controller {
	@FXML
	private Pane pane;
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private RadioButton selectBtn;
	
	@FXML
	private RadioButton ellipseBtn;
	
	@FXML
	private RadioButton rectBtn;
	
	@FXML
	private RadioButton lineBtn;
	
	@FXML
	private ColorPicker colorPick;
	
	@FXML
	private Button deleteBtn;
	
	@FXML
	private Button cloneBtn;
		
	/**
	 * attributs supplémentaires pour le traitement
	 */
	final ToggleGroup group = new ToggleGroup(); //permet de ne sélectionner qu'un bouton radio à la fois
	private String choix = new String(); //stocke le type de figure choisie par l'utilisateur
	private int cptClick = 0; //permet de compter 2 click pour créer une ligne (début et fin)
	private double coordX1 = 0.0; //coordonnées de l'abscisse du début de la ligne
	private double coordY1 = 0.0; //coordonnées de l'ordonnée du début de la ligne
	private SubScene subs = null; //subscene pour traiter le clonage ou la délétion
	private double x; //nouvelle abscisse du drag 
    private double y; //nouvelle ordonnée du drag
    private boolean enableDrag = false; //booléen permettant d'activer ou non le drag d'une figure
    
	/**
	 * Constructeur
	 */
	public Controller(){
		
    }	
	
	/**
	 * Contrôle du Pane où se trouvent les figures
	 */
	@FXML
	private void paneControl(){
		this.pane.setOnMouseClicked((event)-> {
			if((this.choix != null ) && (this.colorPick.getValue() != null)){
				if(this.choix.equals("Ellipse") || this.choix.equals("Rectangle"))
					paneDessin(this.choix, this.colorPick.getValue(), event.getX(), event.getY());
				else if(this.choix.equals("Line")){
					if(this.cptClick < 2){
						if(this.cptClick == 0){
							this.coordX1 = event.getX();
							this.coordY1 = event.getY();
						}							
						this.cptClick++;
					}
					if(this.cptClick == 2){
						paneDessin(this.choix, this.colorPick.getValue(), event.getX(), event.getY());
						this.cptClick = 0;
					}
				}			
			}
         });
	}
	
	/**
	 * Dessiner une figure dans le Pane
	 * @param forme
	 * @param col
	 * @param coordX
	 * @param coordY
	 */
	public void paneDessin(String forme, Color col, double coordX, double coordY){

		if(forme.equals("Rectangle")){
			Group root3D = new Group();
			Rectangle rect1 = new Rectangle(coordX-25, coordY-25, 50, 30); //-25 pour que le clic soit le centre
			rect1.setFill(col);
			root3D.getChildren().add(rect1);
		    this.makeDraggable(rect1);
			SubScene subscene = new SubScene(root3D, 700, 600, true, SceneAntialiasing.BALANCED);
			subscene.setOnMouseClicked(e ->{
				subs = subscene;
			});
			this.pane.getChildren().addAll(subscene);	
		}
		else if(forme.equals("Ellipse")){
			Group root3D = new Group();
			Ellipse ell = new Ellipse(coordX, coordY, 25, 25);
			ell.setFill(col);
			root3D.getChildren().add(ell);
		    this.makeDraggable(ell);
		    SubScene subscene = new SubScene(root3D, 600, 600, true, SceneAntialiasing.BALANCED);
		    subscene.setOnMouseClicked(e ->{
				subs = subscene;
			});
			this.pane.getChildren().addAll(subscene);	
		}
		else if(forme.equals("Line")){
			Group root3D = new Group();
			Line line = new Line();
			line.setStartX(this.coordX1);
			line.setStartY(this.coordY1);
			line.setEndX(coordX);
			line.setEndY(coordY);
			line.setStroke(col);
			line.setStrokeWidth(9.0f);
			root3D.getChildren().add(line);
			this.makeDraggable(line);
			SubScene subscene = new SubScene(root3D, 600, 600, true, SceneAntialiasing.BALANCED);
			subscene.setOnMouseClicked(e ->{
				subs = subscene;
			});
			this.pane.getChildren().addAll(subscene);	
		}
	}
	
	/**
	 * changement de la couleur de la forme sur laquelle onn clique si on le souhaite
	 */
	@FXML
	public void colorPickControl(){
		this.colorPick.setOnAction(e ->{
			if(subs!=null){
				Group root1 = (Group) subs.getRoot();
				if(root1.getChildren().get(0) instanceof Rectangle)
					((Rectangle) root1.getChildren().get(0)).setFill(this.colorPick.getValue());
				else if(root1.getChildren().get(0) instanceof Ellipse)
					((Ellipse) root1.getChildren().get(0)).setFill(this.colorPick.getValue());
				else if(root1.getChildren().get(0) instanceof Line)
					((Line) root1.getChildren().get(0)).setStroke(this.colorPick.getValue());
			}
		});
	}

	/**
	 * Initialisaton des boutons et boutons radio
	 */
	@FXML
	private void initButtons(){
		//boutons radio
		selectBtn.setToggleGroup(group);
		ellipseBtn.setToggleGroup(group);
		rectBtn.setToggleGroup(group);
		lineBtn.setToggleGroup(group);
		
		cloneBtn.setDisable(true);
		deleteBtn.setDisable(true);
		
		selectBtn.setOnAction((event)-> {
			cloneBtn.setDisable(false);
			deleteBtn.setDisable(false);
			choix = null;
			enableDrag = true;
         });
		
		ellipseBtn.setOnAction((event)-> {
			cloneBtn.setDisable(true);
			deleteBtn.setDisable(true);
			choix = "Ellipse";
			enableDrag = false;
         });
		
		rectBtn.setOnAction((event)-> {
			cloneBtn.setDisable(true);
			deleteBtn.setDisable(true);
			choix = "Rectangle";
			enableDrag = false;
         });
		
		lineBtn.setOnAction((event)-> {
			cloneBtn.setDisable(true);
			deleteBtn.setDisable(true);
			choix = "Line";
			this.cptClick = 0;
			enableDrag = false;
         });
		
		//boutons
		deleteBtn.setOnAction((event)-> {
			if(subs!=null)
				this.pane.getChildren().remove(subs);
			subs = null;
         });
		
		cloneBtn.setOnAction((event)-> {
			if(subs!=null){		
				Group root1 = (Group) subs.getRoot();
				Group root = new Group();
				if(root1.getChildren().get(0) instanceof Rectangle){
					Rectangle re = new Rectangle(((Rectangle) root1.getChildren().get(0)).getX()+10,((Rectangle) root1.getChildren().get(0)).getY()+10,50,30) ;				
					re.setFill(((Rectangle) root1.getChildren().get(0)).getFill());
					 this.makeDraggable(re);
					root.getChildren().add(re);
				}
				else if(root1.getChildren().get(0) instanceof Ellipse){
					Ellipse el = new Ellipse(((Ellipse) root1.getChildren().get(0)).getCenterX()+10,((Ellipse) root1.getChildren().get(0)).getCenterY()+10,25,25) ;				
					el.setFill(((Ellipse) root1.getChildren().get(0)).getFill());
					this.makeDraggable(el);
					root.getChildren().add(el);
				}
				else if(root1.getChildren().get(0) instanceof Line){
					Line li = new Line();				
					li.setStartX(((Line) root1.getChildren().get(0)).getStartX()+10);
					li.setStartY(((Line) root1.getChildren().get(0)).getStartY()+10);
					li.setEndX(((Line) root1.getChildren().get(0)).getEndX()+10);
					li.setEndY(((Line) root1.getChildren().get(0)).getEndY()+10);
					li.setStroke(((Line) root1.getChildren().get(0)).getStroke());
					li.setStrokeWidth(9.0f);
					this.makeDraggable(li);
					root.getChildren().add(li);
				}
				SubScene sub = new SubScene(root, 700,600, true, SceneAntialiasing.BALANCED);
				sub.setOnMouseClicked(e ->{
					subs = sub;
				});
				this.pane.getChildren().add(sub);
			}
			subs = null;
        });
		
	} 
	
	/**
	 * Permettre le drag d'une figure
	 * @param node
	 */
	public void makeDraggable(Node node) {
	     node.setOnMousePressed(onMousePressedEventHandler);
	     node.setOnMouseDragged(onMouseDraggedEventHandler);
	}

	EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
		if(event.getSource() instanceof Rectangle && enableDrag) {
			Rectangle rect = (Rectangle)(event.getSource());
	        this.x = rect.getX() - event.getSceneX();
	        this.y = rect.getY() - event.getSceneY();
	    }
		else if(event.getSource() instanceof Ellipse && enableDrag) {
			Ellipse ell = (Ellipse)(event.getSource());
	        this.x = ell.getCenterX() - event.getSceneX();
	        this.y = ell.getCenterY() - event.getSceneY();
	    }
		else if(event.getSource() instanceof Line && enableDrag) {
			Line line = (Line) (event.getSource());
	        this.x = line.getTranslateX() - event.getSceneX();
	        this.y = line.getTranslateY() - event.getSceneY();
	    }
	  };

	EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
		if(event.getSource() instanceof Rectangle && enableDrag) {
			Rectangle rect = (Rectangle)(event.getSource());
	        rect.setX(this.x + event.getSceneX());
	        rect.setY(this.y + event.getSceneY());
	    }
		else if(event.getSource() instanceof Ellipse && enableDrag) {
			Ellipse elli = (Ellipse)(event.getSource());
			elli.setCenterX(this.x + event.getSceneX());
			elli.setCenterY(this.y + event.getSceneY());
	    } 
		else if(event.getSource() instanceof Line && enableDrag) {
			Line line = (Line)(event.getSource());
			line.setTranslateX(this.x + event.getSceneX());
			line.setTranslateY(this.y + event.getSceneY());
	    } 	
	 };	

	/**
	 * Initialisation
	 */
	@FXML
	public void initialize() {
		initButtons();
		paneControl();
		colorPickControl();
		colorPick.setValue(Color.BLACK); //choix arbitraire de mettre en noir au début
	}
	
}
