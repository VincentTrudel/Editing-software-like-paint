
//Optimiser et corriger la methode draw() de la classe Triangle (pour qu'elle ressemble + aux deux autres)
//Coder effacer
//Couleur de fond
//Ajouer une mire pour la pipette
//Optimiser les variables
//Ajouter la forme tracee au vecteur Traits
//Remettre les ovals/rectangles/triangles persistents
//Ajouter une méthode draw à la classe Trait
import java.awt.BasicStroke;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;

import java.awt.Graphics2D;

import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;

import javax.swing.*;

public class FramePaint extends JFrame {

	private JPanel contentPane;
	JPanel panelBoutonCouleur1, panelBoutonCouleur2;
	JPanel panelSurface;
	JPanel panelCouleurs;
	JLabel grilleCouleurs[][];
	JLabel texteLargeurTrait;
	
	private ImageIcon iconePaint = new ImageIcon("icones/paint.png");
	
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int largeurSurface = (int)screenSize.getWidth(), hauteurSurface = (int)screenSize.getHeight();
	
	private Forme formeTemp, formeTemp2;
	private int tailleDuTrait = 1;
	private JTextField fieldTailleTrait;
	private Color couleurActive1, couleurActive2, couleurUtilisee;
	private Color couleur1 = new Color(255, 175, 175), couleur2 = new Color(255, 255, 0), couleur3 = new Color(255, 0, 255),
			couleur4 = new Color(0, 0, 255), couleur5 = new Color(255, 0, 0), couleur6 = new Color(255, 200, 0),
			couleur7 = new Color(0, 255, 0), couleur8 = new Color(0, 255, 255), couleur9 = new Color(0, 0, 0),
			couleur10 = new Color(255, 255, 255), tempColor, couleurBG = couleur10;//le bg est initialement blanc
	private Ecouteur ec;
	private DocChangeListener ec2;
	private ImageIcon dessin;
	private JLabel labelDessin;
	private SurfDessin surfaceDessin;
	private Cursor penCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			new ImageIcon("icones/crayonTourneTransp.gif").getImage(),
			new java.awt.Point(0,0), "curseurCrayon");
	private Cursor erasorCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			new ImageIcon("icones/effaceTourneeTransp.gif").getImage(),
			new java.awt.Point(0,0), "curseurEfface");
	private Cursor wandCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			new ImageIcon("icones/baguette.png").getImage(),
			new java.awt.Point(0,0), "curseurBaguette");
	private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private Cursor crosshairCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);

	private JToggleButton boutonCrayon, boutonEffacer, boutonPipette, boutonRemplissage,boutonBaguette, boutonRectangle, boutonOvale,
			boutonTriangle, boutonCouleur1, boutonCouleur2;
	private JCheckBox formePersistenteCB;
	private JButton boutonEnregistrer, boutonNouveau;
	private ButtonGroup groupeOutils = new ButtonGroup();
	private ButtonGroup groupeCouleurs = new ButtonGroup();
	private Graphics2D g2DSurface;
	private BufferedImage imageSurface, dessinAvantForme;
	private String username = System.getProperty("user.name");
	private String pathMesImages = "C:\\Users\\"+username+"\\Pictures\\";
	private JFileChooser fileChooser = new JFileChooser(new File( pathMesImages));
	private FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
	private Stack<Point> leStack = new Stack<Point>();
	private String[] optionsNouveau = new String[] {"Enregistrer", "Ne pas enregistrer", "Annuler"};
	private String[] optionsEnregistrer = new String[] {"Ok", "Retour", "Ne pas enregistrer"};
	private RenderedImage renderedImage;
    
	private int size, x1, x2, y1, y2, evX, evY;
	private Vector<Object> lesTraits;
	private Vector<Point> pointsDuTrait, pointsDuTraitEfface;
	private Point avantDernierPoint, dernierPoint, point1Forme, point2Forme;
	private Font fontTexte = new Font("Times New Roman",Font.PLAIN,16);
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.setProperty("swing.aatext", "true");
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FramePaint frame = new FramePaint();
					frame.setVisible(true);
					frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public FramePaint() throws IOException {
		setTitle("Paint");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize( largeurSurface+10, (int)(hauteurSurface*1.15));
		setIconImage(iconePaint.getImage());
		setLocationRelativeTo(null);
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		ec = new Ecouteur();
		ec2 = new DocChangeListener();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(214, 217, 223));
		grilleCouleurs = new JLabel[2][5];

		couleurActive1 = couleur9; // Au début le noir est sélectionné comme Couleur1
		couleurActive2 = couleur10;// Et le blanc comme couleur 2

		surfaceDessin = new SurfDessin();
		surfaceDessin.setBounds(5, 80, largeurSurface, hauteurSurface);

		imageSurface = new BufferedImage(largeurSurface, hauteurSurface, BufferedImage.TYPE_INT_RGB);
		g2DSurface = (Graphics2D) imageSurface.getGraphics();
		//g2DSurface.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // rend imparfait le floodfill
		g2DSurface.setColor(couleurBG);
		g2DSurface.fillRect(0, 0, largeurSurface, hauteurSurface);
		
		surfaceDessin.addMouseListener(ec);
		surfaceDessin.addMouseMotionListener(ec);
		contentPane.add(surfaceDessin);
		dessin = new ImageIcon(imageSurface);
		
		labelDessin = new JLabel(dessin);
		labelDessin.setBorder(new LineBorder(Color.gray, 1, true));
		surfaceDessin.add(labelDessin);

		
		boutonNouveau = new JButton();
		boutonNouveau.setBounds(5, 10, 35, 30);
		boutonNouveau.setContentAreaFilled(true);
		boutonNouveau.setOpaque(true);
		boutonNouveau.setIcon((Icon) new ImageIcon("icones/nouveau.png"));
		boutonNouveau.addMouseListener(ec);
		contentPane.add(boutonNouveau);
		
		boutonCrayon = new JToggleButton();
		boutonCrayon.setBounds(40, 10, 35, 30);
		boutonCrayon.setContentAreaFilled(true);
		boutonCrayon.setOpaque(true);
		boutonCrayon.setIcon((Icon) new ImageIcon("icones/crayon.gif"));
		
		contentPane.add(boutonCrayon);

		boutonEffacer = new JToggleButton();
		boutonEffacer.setBounds(75, 10, 35, 30);
		boutonEffacer.setContentAreaFilled(true);
		boutonEffacer.setOpaque(true);
		boutonEffacer.setIcon((Icon) new ImageIcon("icones/efface.gif"));
		contentPane.add(boutonEffacer);

		boutonPipette = new JToggleButton();
		boutonPipette.setBounds(110, 10, 35, 30);
		boutonPipette.setContentAreaFilled(true);
		boutonPipette.setOpaque(true);
		boutonPipette.setIcon((Icon) new ImageIcon("icones/pipette.gif"));
		contentPane.add(boutonPipette);

		boutonRemplissage = new JToggleButton();
		boutonRemplissage.setBounds(145, 10, 35, 30);
		boutonRemplissage.setContentAreaFilled(true);
		boutonRemplissage.setOpaque(true);
		boutonRemplissage.setIcon((Icon) new ImageIcon("icones/remplissage.gif"));
		contentPane.add(boutonRemplissage);

		boutonBaguette = new JToggleButton();
		boutonBaguette.setBounds(180, 10, 35, 30);
		boutonBaguette.setContentAreaFilled(true);
		boutonBaguette.setOpaque(true);
		boutonBaguette.setIcon((Icon) new ImageIcon("icones/baguette.png"));
		contentPane.add(boutonBaguette);
		
		texteLargeurTrait = new JLabel("Largeur du trait: ");
		texteLargeurTrait.setBounds(10, 45, 100, 30);
		texteLargeurTrait.setFont(fontTexte); 
		contentPane.add(texteLargeurTrait);

		fieldTailleTrait = new JTextField(String.valueOf(tailleDuTrait));
		fieldTailleTrait.setBounds(110, 45, 50, 30);
		fieldTailleTrait.setHorizontalAlignment(JTextField.CENTER);
		fieldTailleTrait.setFont(fontTexte); 
		contentPane.add(fieldTailleTrait);
		fieldTailleTrait.getDocument().addDocumentListener( new DocChangeListener() );
	
		groupeOutils.add(boutonCrayon);
		groupeOutils.add(boutonEffacer);
		groupeOutils.add(boutonPipette);
		groupeOutils.add(boutonRemplissage);
		groupeOutils.add(boutonBaguette);

		boutonEnregistrer = new JButton();
		boutonEnregistrer.setBounds(220, 10, 40, 30);
		boutonEnregistrer.setContentAreaFilled(true);
		boutonEnregistrer.setOpaque(true);
		boutonEnregistrer.setIcon((Icon) new ImageIcon("icones/save.gif"));
		boutonEnregistrer.addMouseListener(ec);
		contentPane.add(boutonEnregistrer);

		boutonRectangle = new JToggleButton();
		boutonRectangle.setBounds(270, 40, 40, 30);
		boutonRectangle.setContentAreaFilled(true);
		boutonRectangle.setOpaque(true);
		boutonRectangle.setIcon((Icon) new ImageIcon("icones/rectangle.gif"));
		contentPane.add(boutonRectangle);

		boutonOvale = new JToggleButton();
		boutonOvale.setBounds(320, 40, 40, 30);
		boutonOvale.setContentAreaFilled(true);
		boutonOvale.setOpaque(true);
		boutonOvale.setIcon((Icon) new ImageIcon("icones/cercle.gif"));
		contentPane.add(boutonOvale);

		boutonTriangle = new JToggleButton();
		boutonTriangle.setBounds(370, 40, 40, 30);
		boutonTriangle.setContentAreaFilled(true);
		boutonTriangle.setOpaque(true);
		boutonTriangle.setIcon((Icon) new ImageIcon("icones/triangle.gif"));
		contentPane.add(boutonTriangle);
		
		formePersistenteCB = new JCheckBox("forme persistente");
		formePersistenteCB.setBounds(275, 20, 130, 20);
		formePersistenteCB.setOpaque(false);
		formePersistenteCB.setFont(fontTexte);
		contentPane.add(formePersistenteCB);
		
		groupeOutils.add(boutonRectangle);
		groupeOutils.add(boutonOvale);
		groupeOutils.add(boutonTriangle);
		boutonCrayon.setSelected(true);
		boutonCrayon.grabFocus();
		
		boutonCouleur1 = new JToggleButton(); // ,icon?
		boutonCouleur1.setHorizontalTextPosition(SwingConstants.RIGHT);
		boutonCouleur1.setBounds(420, 10, 60, 60);
		boutonCouleur1.setContentAreaFilled(true);
		boutonCouleur1.setOpaque(true);
		boutonCouleur1.setBorder(new LineBorder(Color.black, 1, true));
		boutonCouleur1.setLayout(new FlowLayout());

		
		contentPane.add(boutonCouleur1);
		
		panelBoutonCouleur1 = new JPanel();
		panelBoutonCouleur1.setPreferredSize(new Dimension(50,30));
		panelBoutonCouleur1.setBackground(couleurActive1);
		panelBoutonCouleur1.setBorder(new LineBorder(Color.black, 1, true));
		boutonCouleur1.add(panelBoutonCouleur1);
		boutonCouleur1.add(new JLabel("Gauche"));
		boutonCouleur1.setHorizontalTextPosition(SwingConstants.CENTER);
		
		boutonCouleur2 = new JToggleButton(); // ,icon?
		boutonCouleur2.setHorizontalTextPosition(SwingConstants.RIGHT);
		boutonCouleur2.setBounds(490, 10, 60, 60);
		boutonCouleur2.setContentAreaFilled(true);
		boutonCouleur2.setOpaque(true);
		boutonCouleur2.setBorder(new LineBorder(Color.black, 1, true));
		boutonCouleur2.setLayout(new FlowLayout());
		
		contentPane.add(boutonCouleur2);
		
		panelBoutonCouleur2= new JPanel();
		panelBoutonCouleur2.setPreferredSize(new Dimension(50,30));
		panelBoutonCouleur2.setBackground(couleurActive2);
		panelBoutonCouleur2.setBorder(new LineBorder(Color.black, 1, true));
		boutonCouleur2.add(panelBoutonCouleur2);
		boutonCouleur2.add(new JLabel("Droit"));
		boutonCouleur1.setHorizontalTextPosition(SwingConstants.CENTER);
		contentPane.add(boutonCouleur2);
		//labelBoutonCouleur2
		groupeCouleurs.add(boutonCouleur1);
		groupeCouleurs.add(boutonCouleur2);
		groupeCouleurs.setSelected(boutonCouleur1.getModel(), true);
		boutonCouleur1.grabFocus();
		panelCouleurs = new JPanel();
		// panelCouleurs.setBorder(new LineBorder(Color.red, 1, true));
		panelCouleurs.setBounds(570, 10, 200, 62);
		panelCouleurs.setLayout(new GridLayout(2, 5, 0, 0));
		panelCouleurs.setOpaque(false);
		contentPane.add(panelCouleurs);
		
	    fileChooser.addChoosableFileFilter(imageFilter); //Ajout du filtre défini plus haut
	    fileChooser.setFileFilter(imageFilter);
	    fileChooser.setSelectedFile(new File(pathMesImages+"sans nom.jpg")); //Pour suggérer un nom de fichier

		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 5; j++) {

				grilleCouleurs[i][j] = new JLabel();
				grilleCouleurs[i][j].setBorder(new LineBorder(Color.black, 1, true));
				switch (i * 5 + j + 1) {
				case 1: tempColor = couleur1; break;
				case 2: tempColor = couleur2; break;
				case 3: tempColor = couleur3; break;
				case 4: tempColor = couleur4; break;
				case 5: tempColor = couleur5; break;
				case 6: tempColor = couleur6; break;
				case 7: tempColor = couleur7; break;
				case 8: tempColor = couleur8; break;
				case 9: tempColor = couleur9; break;
				case 10:tempColor = couleur10;break;
				}
				grilleCouleurs[i][j].addMouseListener(ec);
				grilleCouleurs[i][j].setOpaque(true);
				grilleCouleurs[i][j].setBackground(tempColor);
				panelCouleurs.add(grilleCouleurs[i][j]);
			}
		

		lesTraits = new Vector<Object>();
		pointsDuTrait = new Vector<Point>();
		pointsDuTraitEfface = new Vector<Point>();
		

	}; // Fin du constructeur FramePaint

	private class SurfDessin extends JPanel {

		protected void paintComponent(BufferedImage image, Graphics2D g2) {
			g2.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
		}

	}

	Action action = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == fieldTailleTrait) {
				int tailleEntree = Integer.valueOf(fieldTailleTrait.getText());
				if (tailleEntree > 255) {
					tailleDuTrait = Integer.valueOf(255);
					fieldTailleTrait.setText("255");
				} else {
					tailleDuTrait = tailleEntree;
				}
			}
		}
	};

	static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	// Listen for changes in the text

	private class DocChangeListener implements DocumentListener
	{
		@Override
		public void insertUpdate(DocumentEvent e) {	 
			this.assistTailleTrait();
		}
        private void assistTailleTrait() {
            Runnable doAssist = new Runnable() {
                @Override
                public void run() {
                    String tailleEntree = fieldTailleTrait.getText();
                    if (tailleEntree.matches(".*[\\D]+.*")) {
                    	fieldTailleTrait.setText(String.valueOf(tailleDuTrait));
                    }
                    else if ( tailleEntree.matches("^[1-9]\\d*$") )
                    {
                    	int tailleEntreeInt = Integer.valueOf(tailleEntree);
                    	if (tailleEntreeInt<=99)
                    	{
                    		tailleDuTrait = tailleEntreeInt;
                    	}
                    	else
                    		fieldTailleTrait.setText(String.valueOf(tailleDuTrait));
                    }
                    else if ( tailleEntree.matches("[0]+[\\d]+") )
                    {
                    	
                    	int tailleEntreeInt = Integer.valueOf(tailleEntree);
                       	if (tailleEntreeInt<=99)
                    	{
                    		tailleDuTrait = tailleEntreeInt;
                    		fieldTailleTrait.setText(String.valueOf(tailleEntreeInt));
                    	}
                    	else
                    		fieldTailleTrait.setText(String.valueOf(tailleDuTrait));
                    }
                    else if ( tailleEntree.matches("[0]+$") )
                    {
                    	fieldTailleTrait.setText(String.valueOf(1));
                    	tailleDuTrait = 1;
                    }
                    else if (tailleEntree == "")
                    {
                    	tailleDuTrait = 1;
                    }
                    g2DSurface.setStroke(new BasicStroke(tailleDuTrait));
                }
            };
            SwingUtilities.invokeLater(doAssist);
        }
		@Override
		public void removeUpdate(DocumentEvent e) {
			this.assistTailleTrait();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			this.assistTailleTrait();
		}
		
	}

	public void remplirInterieur(int couleurDepartRGB, int couleurArriveeRGB)//a mettre ds une classe remplissageInterieur
	{
		int x, y;
		Point lePoint;
		int couleurActuelleRGB;
		boolean lePointNEstPasDejaDeLaCouleurDArrivee, lePointEstDeLaCouleurDeDepart, lePointEstDansLaSurface ;
		   while(leStack.size() != 0) 
		   {
			   	lePoint = leStack.pop();
				x = lePoint.getX();
				y = lePoint.getY();
				lePointEstDansLaSurface = (x>=0 && x<largeurSurface && y >= 0 && y < hauteurSurface);	
			    if (lePointEstDansLaSurface)
			    {
			    	couleurActuelleRGB = imageSurface.getRGB(x, y);
			    	lePointNEstPasDejaDeLaCouleurDArrivee = (couleurActuelleRGB != couleurArriveeRGB);
			    	lePointEstDeLaCouleurDeDepart = (couleurActuelleRGB == couleurDepartRGB);
			    	if (lePointNEstPasDejaDeLaCouleurDArrivee && lePointEstDeLaCouleurDeDepart)
			    	{
			    	  imageSurface.setRGB(x, y, couleurArriveeRGB);
			    	//leStack.push( new Point(x-1, y - 1) );
			    	  leStack.push( new Point(x, y - 1) );
			    	//leStack.push( new Point(x+ 1, y-1) );	    	  
			    	  leStack.push( new Point(x - 1, y) );
			    	  leStack.push( new Point(x + 1, y) );		    	  
			    	//leStack.push( new Point(x - 1, y+1) );
			    	  leStack.push( new Point(x, y + 1) );
			    	//leStack.push( new Point(x+1, y + 1) );
			    	}
			    }
		   }
		   surfaceDessin.repaint();
		   return;
	}
	
	public void enregistrer()
	{
        int reponse1 = fileChooser.showSaveDialog(fileChooser);
        int reponse2;

        String nomSuggere;
        int i =1;

        if(reponse1 == JFileChooser.APPROVE_OPTION)//Si l'utilisateur appuie sur enregistrer
        {
            String nomFichier = fileChooser.getCurrentDirectory()+"\\" + fileChooser.getSelectedFile().getName();
            if (nomFichier.contains(".jpg") == false)
            {
            	nomFichier +=".jpg" ;
            }
            fileChooser.setSelectedFile(new File(nomFichier));

			if(fileChooser.getSelectedFile().exists()) 
			{	
		       	if (nomFichier.matches(".*[(][\\d]+[)]\\.jpg") == false)//ajouter un regex
		       	{
		       		nomSuggere = nomFichier.replace(".jpg", "("+String.valueOf(i)+").jpg");

		       	}
		       	else
		       	{
		       		System.out.println("Match!");
		       		nomSuggere = nomFichier;
		       	}
				while((new File(nomSuggere)).exists())
				{
					nomSuggere = nomSuggere.replace("("+String.valueOf(i)+")", "("+String.valueOf(i+1)+")");
					i++;
				}
				
				
				reponse2 = JOptionPane.showOptionDialog(null, 
			    		"Un fichier du même nom existe déjà. Voulez-vous le renommer pour \""+ nomSuggere+"\"", "Paint",
				        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				        null, optionsEnregistrer, optionsEnregistrer[0]);
	
				  
				  if(reponse2 == 0 ) 
				  {
			          renderedImage = (RenderedImage)imageSurface;//A mettre dans une fonction
						try {
							ImageIO.write(renderedImage, "jpg", new File(nomSuggere));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
				  }
				  else if (reponse2 == 1)
				  {
					  fileChooser.cancelSelection();
					  enregistrer();
				  }
				  else if (reponse2 == 2)
				  {
					  return;
				  }
			} //Fin du if nom choisi existe deja

	        renderedImage = (RenderedImage)imageSurface;
			try 
			{
				ImageIO.write(renderedImage, "jpg", new File(nomFichier));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	    } 
		  i=0;
	}
	
	
	private class Ecouteur extends MouseAdapter {

				
		public void selectionnerCouleurUtilisee( MouseEvent e )
		{
			if (SwingUtilities.isLeftMouseButton(e))
				couleurUtilisee = couleurActive1;
			else if (SwingUtilities.isRightMouseButton(e))
				couleurUtilisee = couleurActive2;
		};
		
		public void modifCouleurActiveSelonClic( MouseEvent e, Color couleur )
		{
			if (SwingUtilities.isLeftMouseButton(e))
			{
				couleurActive1 = couleur;
				panelBoutonCouleur1.setBackground(couleur);
			}
			else if (SwingUtilities.isRightMouseButton(e))
			{
				couleurActive2 = couleur;
				panelBoutonCouleur2.setBackground(couleur);
			}
		};

		@Override 
		public void mouseEntered(MouseEvent e) {
			if (e.getSource() == surfaceDessin ) {
				if ( boutonCrayon.isSelected() ){
					surfaceDessin.setCursor(penCursor);
				}
				else if (boutonEffacer.isSelected())
				{
					surfaceDessin.setCursor(erasorCursor);
				}
				else if ( boutonPipette.isSelected() )
					surfaceDessin.setCursor(crosshairCursor);
				else if (boutonRemplissage.isSelected())
				{
					surfaceDessin.setCursor(crosshairCursor);
				}
				else if (boutonBaguette.isSelected())
				{
					surfaceDessin.setCursor(wandCursor);
				}
				else if (boutonRectangle.isSelected() || boutonOvale.isSelected() || boutonTriangle.isSelected())
				{
					surfaceDessin.setCursor(crosshairCursor);
				}
			}
		}
		
		public void mouseExited(MouseEvent e) {
			if (e.getSource() == surfaceDessin) {
				surfaceDessin.setCursor(defaultCursor);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			if (e.getSource() == surfaceDessin) 
			{
				this.selectionnerCouleurUtilisee( e );
				g2DSurface.setColor(couleurUtilisee);
				if (boutonCrayon.isSelected()) {
					evX = e.getX();
					evY = e.getY();
					pointsDuTrait.add(new Point(evX, evY));
					g2DSurface.drawLine(evX, evY, evX,evY);
					surfaceDessin.repaint();
				}
				else if ( boutonEffacer.isSelected() ){
					g2DSurface.setColor(couleurBG); //UTILISER LA COULEUR COURANTE DU BACKGROUND	
					evX = e.getX();
					evY = e.getY();
					pointsDuTraitEfface.add(new Point(evX, evY));
					g2DSurface.drawLine(evX, evY, evX,evY);
					surfaceDessin.repaint();
				}
				else if (boutonRectangle.isSelected() || boutonOvale.isSelected() || boutonTriangle.isSelected())
				{
					selectionnerCouleurUtilisee( e );
				}
				else if (boutonPipette.isSelected())
				{
					int pixel = imageSurface.getRGB(e.getX(), e.getY()-6); //Y est ajusté pour que le pixel choisi soit au centre du crosshair
					this.modifCouleurActiveSelonClic( e, new Color(pixel) );
					groupeOutils.clearSelection();
					groupeOutils.setSelected(boutonCrayon.getModel(), true);
					boutonCrayon.grabFocus();
					surfaceDessin.setCursor(penCursor);
					this.selectionnerCouleurUtilisee( e );
					g2DSurface.setColor(couleurUtilisee);
				}
				else if ( boutonRemplissage.isSelected() )
				{
					leStack.push(new Point(e.getX(), e.getY()-6));
					remplirInterieur(imageSurface.getRGB(e.getX(), e.getY()-6), couleurUtilisee.getRGB());//Y est ajusté pour que le pixel choisi soit au centre du crosshair
					leStack = new Stack<Point>();
					//Rajouter au Vecteur trait pour quand on change le background avec la baguette?
				}
				else if (boutonBaguette.isSelected())
				{
					g2DSurface.setColor(couleurUtilisee);
					couleurBG = couleurUtilisee;
					g2DSurface.fillRect(0, 0, largeurSurface, hauteurSurface);
					Trait unTrait;
					int tailleTraitObjetCourant;
					Forme uneForme;
					Color couleurObjetCourant = null;
					Object objetCourant;
					for(int i=0; i< lesTraits.size(); i++)
					{
						objetCourant = lesTraits.get(i);
						if (objetCourant instanceof TraitEfface)
						{
							unTrait = (TraitEfface)objetCourant;
							tailleTraitObjetCourant = unTrait.getTailleTrait();
							g2DSurface.setColor(couleurBG);
							g2DSurface.setStroke(new BasicStroke(tailleTraitObjetCourant));
							unTrait.draw(g2DSurface);
						}
						else if (objetCourant instanceof Trait)//Changer 
						{
							unTrait = (Trait)objetCourant;
							couleurObjetCourant = unTrait.getCouleur();
							tailleTraitObjetCourant = unTrait.getTailleTrait();
							g2DSurface.setColor(couleurObjetCourant);
							g2DSurface.setStroke(new BasicStroke(tailleTraitObjetCourant));
							unTrait.draw(g2DSurface);
						}
						else if (objetCourant instanceof Forme)
						{
							uneForme = (Forme)objetCourant;
							couleurObjetCourant = uneForme.getCouleur();
							tailleTraitObjetCourant = uneForme.getTailleTrait();
							g2DSurface.setColor(couleurObjetCourant);
							g2DSurface.setStroke(new BasicStroke(tailleTraitObjetCourant));
							uneForme.draw(g2DSurface);
						}
						
					}
					
					surfaceDessin.repaint();
				}
			} // fin de SOURCE : SURFACE
			else if (e.getSource() == boutonEnregistrer)
			{
				enregistrer();
			}
			else if (e.getSource() == boutonNouveau)
			{
			    int reponse = JOptionPane.showOptionDialog(null, "Voulez-vous enregistrer les modifications apportées à votre dessin?", "Paint",
				        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				        null, optionsNouveau, optionsNouveau[0]);

				    // Ou reponse == 0 pour Recommencer, 1 pour Enregistrer, 2 et -1 pour Escape/Cancel.
			        if (reponse == 0) 
			        {
			        	enregistrer();	
			        }
			        else if (reponse == 1)
			        {
			        	lesTraits = new Vector<Object>();
			        	couleurBG = couleur10;
			    		g2DSurface.setColor(couleurBG);
			    		g2DSurface.fillRect(0, 0, imageSurface.getWidth(), imageSurface.getWidth());
			    		surfaceDessin.repaint();
			    		boutonCrayon.setSelected(true);
			    		boutonCrayon.grabFocus();
			        }
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			this.selectionnerCouleurUtilisee( e );
			if (e.getSource() == surfaceDessin) {
				if (boutonCrayon.isSelected()) {
					pointsDuTrait.add(new Point(e.getX(), e.getY()));
					size = pointsDuTrait.size();
					if (size > 1) {
						avantDernierPoint = pointsDuTrait.get(size - 2);
						dernierPoint = pointsDuTrait.get(size - 1);
						x1 = avantDernierPoint.getX();
						y1 = avantDernierPoint.getY();
						x2 = dernierPoint.getX();
						y2 = dernierPoint.getY();
						g2DSurface.drawLine(x1, y1, x2, y2);
						
					} // fin if size > 1
					surfaceDessin.repaint();
				} // fin if boutonCrayon.isSelected()
				else if ( boutonEffacer.isSelected() ){
					pointsDuTraitEfface.add(new Point(e.getX(), e.getY()));
					size = pointsDuTraitEfface.size();
					if (size > 1) {
						avantDernierPoint = pointsDuTraitEfface.get(size - 2);
						dernierPoint = pointsDuTraitEfface.get(size - 1);
						x1 = avantDernierPoint.getX();
						y1 = avantDernierPoint.getY();
						x2 = dernierPoint.getX();
						y2 = dernierPoint.getY();
						g2DSurface.drawLine(x1, y1, x2, y2);
						
					} // fin if size > 1
					surfaceDessin.repaint();
				}
				else if (boutonRectangle.isSelected() || boutonOvale.isSelected() || boutonTriangle.isSelected()) {
					if (point1Forme == null) {
						point1Forme = new Point(e.getX(), e.getY());
						dessinAvantForme = deepCopy(imageSurface);
					} else {
						point2Forme = new Point(e.getX(), e.getY());

						if (!formePersistenteCB.isSelected())
						{
							if (formeTemp!=null) {
								formeTemp2 = formeTemp;
								surfaceDessin.paintComponent(dessinAvantForme, g2DSurface);
								formeTemp.draw(g2DSurface);
								surfaceDessin.repaint();	
								formeTemp = null;
							} else {
								if ( boutonRectangle.isSelected() )
									formeTemp = new Rectangle(point1Forme, point2Forme, couleurUtilisee, tailleDuTrait);
								else if ( boutonOvale.isSelected() )
									formeTemp = new Ovale(point1Forme, point2Forme, couleurUtilisee, tailleDuTrait);
								else if ( boutonTriangle.isSelected() )
									formeTemp = new Triangle(point1Forme, point2Forme, couleurUtilisee, tailleDuTrait);
							}
						} else {
							if ( boutonRectangle.isSelected() )
								formeTemp = new Rectangle(point1Forme, point2Forme, couleurUtilisee, tailleDuTrait);
							else if ( boutonOvale.isSelected() )
								formeTemp = new Ovale(point1Forme, point2Forme, couleurUtilisee, tailleDuTrait);
							else if ( boutonTriangle.isSelected() )
								formeTemp = new Triangle(point1Forme, point2Forme, couleurUtilisee, tailleDuTrait);
							formeTemp.draw(g2DSurface);
							lesTraits.add(formeTemp);
							surfaceDessin.repaint();
						}
						//else
						//copier le bloc juste en haut
						
					}
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getSource() == surfaceDessin) {
				if (boutonCrayon.isSelected()) {
					lesTraits.add(new Trait(pointsDuTrait, couleurUtilisee, tailleDuTrait));
					pointsDuTrait = new Vector<Point>();
				} else if (boutonRectangle.isSelected() || boutonOvale.isSelected() || boutonTriangle.isSelected()) {
					point1Forme = null;
					point2Forme = null;
					//Ajouter la forme au vecteur Traits ICI!!!
					if (formeTemp2!=null)
						lesTraits.add(formeTemp2);
					else
						lesTraits.add(formeTemp);//VÉRIFIER SI PERTINENT!
					formeTemp = null;
				}
				else if (boutonEffacer.isSelected())
				{
					lesTraits.add(new TraitEfface(pointsDuTraitEfface, couleurBG, tailleDuTrait));
					pointsDuTraitEfface = new Vector<Point>();
				}
			} // fin de la méthode mouseReleased
			else if ( ((JLabel)e.getSource()).getParent() == panelCouleurs) { //Si on a cliqué sur une couleur dans le panelCouleurs
				for (int i = 0; i < 2; i++)
					for (int j = 0; j < 5; j++) {
						if (e.getSource() == grilleCouleurs[i][j]) {
	
							tempColor = grilleCouleurs[i][j].getBackground();
							if (boutonCouleur1.isSelected()) {
								couleurActive1 = tempColor;
								panelBoutonCouleur1.setBackground(couleurActive1);
							} else if (boutonCouleur2.isSelected()) {
								couleurActive2 = tempColor;
								panelBoutonCouleur2.setBackground(couleurActive2);
							}
							return;
						}
					}
			}
		} 
	} // fin de la classe Ecouteur
}// Fin de la classe FramePaint
