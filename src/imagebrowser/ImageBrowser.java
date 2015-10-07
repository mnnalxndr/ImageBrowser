package imagebrowser;

import imagebrowser.TreeData.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 *
 * @author alexmann
 */
public class ImageBrowser extends JFrame implements ActionListener, TreeSelectionListener {

    public ImageBrowser() {

        super("Image Browser");
        setLayout(new BorderLayout());
        setSize(800, 500);
        this.setMinimumSize(new Dimension(800, 450));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //Setting up the menu bar
        String dir = System.getProperty("user.dir");
        menuBar = new JMenuBar();
            setJMenuBar(menuBar);
        menuBar.add(menuFile = new JMenu("File"));
            menuFile.add(menuOpen = new JMenuItem("Open"));
                menuOpen.setToolTipText("Open an image");
                menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
                menuOpen.addActionListener(this);
            menuFile.add(menuSave = new JMenuItem("Save"));
                menuSave.setToolTipText("Save current image");
                menuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
                menuSave.addActionListener(this);
            menuFile.add(menuQuit = new JMenuItem("Quit"));
                menuQuit.setToolTipText("Quit the application");
                menuQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
                menuQuit.addActionListener(this);
                
        menuBar.add(menuEdit = new JMenu("Edit"));
            menuEdit.add(menuUndo = new JMenuItem("Undo all changes"));
                menuUndo.setToolTipText("Undo all changes made to the loaded image");
                menuUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.ALT_MASK));
                menuUndo.addActionListener(this);
            menuEdit.add(new JSeparator());
            menuEdit.add(menuRotate = new JMenu("Rotate"));
                menuRotate.add(menuRotateClock = new JMenuItem("Rotate Clockwise"));
                    menuRotateClock.setToolTipText("Rotate current image 90° clockwise");
                    menuRotateClock.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, ActionEvent.ALT_MASK));
                    menuRotateClock.addActionListener(this);
                menuRotate.add(menuRotateAnti = new JMenuItem("Rotate Anti-Clockwise"));
                    menuRotateAnti.setToolTipText("Rotate current image 90° anti-clockwise");
                    menuRotateAnti.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, ActionEvent.ALT_MASK));
                    menuRotateAnti.addActionListener(this);
            menuEdit.add(menuFlip = new JMenu("Flip"));
                menuFlip.add(menuFlipHoriz = new JMenuItem("Flip Horizontal"));
                    menuFlipHoriz.setToolTipText("Flip current image horizontally");
                    menuFlipHoriz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
                    menuFlipHoriz.addActionListener(this);
                menuFlip.add(menuFlipVert = new JMenuItem("Flip Vertical"));
                    menuFlipVert.setToolTipText("Flip current image horizontally");
                    menuFlipVert.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
                    menuFlipVert.addActionListener(this);
            menuEdit.add(menuPixelate = new JMenuItem("Pixelate!"));
                    menuPixelate.setToolTipText("Pixelate the image!");
                    menuPixelate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
                    menuPixelate.addActionListener(this);

        menuBar.add(menuView = new JMenu("View"));
            menuView.add(menuZoomIn = new JMenuItem("Zoom In"));
                menuZoomIn.setToolTipText("Zoom In on current image");
                menuZoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.ALT_MASK));
                menuZoomIn.addActionListener(this);
            menuView.add(menuZoomOut = new JMenuItem("Zoom Out"));
                menuZoomOut.setToolTipText("Zoom Out from current image");
                menuZoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.ALT_MASK));
                menuZoomOut.addActionListener(this);

        menuBar.add(menuSorting = new JMenu("Tag Tree"));
            menuSorting.add(menuFolderImageAdd = new JMenuItem("Add Image"));
        menuFolderImageAdd.setToolTipText("Add current image to selected tag folder");
        menuFolderImageAdd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuFolderImageAdd.addActionListener(this);
            menuSorting.add(menuFolderImageRename = new JMenuItem("Rename Image"));
                menuFolderImageRename.setToolTipText("Rename image selected in the Tag Folder");
                menuFolderImageRename.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
                menuFolderImageRename.addActionListener(this);
            menuSorting.add(menuFolderImageRemove = new JMenuItem("Remove Image"));
                menuFolderImageRemove.setToolTipText("Remove selected image from its containing tag folder");
                menuFolderImageRemove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
                menuFolderImageRemove.addActionListener(this);
            menuSorting.add(menuFolderSeparator = new JSeparator());
            menuSorting.add(menuFolderNew = new JMenuItem("Create New Folder"));
                menuFolderNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.ALT_MASK));
                menuFolderNew.setToolTipText("Create a new Tag Folder");
                menuFolderNew.addActionListener(this);
            menuSorting.add(menuFolderRename = new JMenuItem("Rename Selected Folder"));
                menuFolderRename.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.ALT_MASK));
                menuFolderRename.setToolTipText("Rename selected Tag Folder");
                menuFolderRename.addActionListener(this);
            menuSorting.add(menuFolderDelete = new JMenuItem("Delete Selected Folder"));
                menuFolderDelete.setToolTipText("Delete selected Tag Folder and its contents");
                menuFolderDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, ActionEvent.ALT_MASK));
                menuFolderDelete.addActionListener(this);
            menuSorting.add(menuFolderSeparator);
            menuSorting.add(menuFolderSort = new JMenuItem("Sort Tree"));
                menuFolderSort.setToolTipText("Sort Tree");
                menuFolderSort.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
                menuFolderSort.addActionListener(this);
            menuSorting.add(menuFolderSearch = new JMenuItem("Search Tree"));
                menuFolderSearch.setToolTipText("Search Tree");
                menuFolderSearch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
                menuFolderSearch.addActionListener(this);
        
        // Setting up the tool bar
        toolBar = new JToolBar();
        toolBar.setLayout(new GridLayout());
        toolBar.setFloatable(false);
        toolBar.add(toolBarOpen = new JButton(new ImageIcon(dir+"/Icons/toolbar/open.png")));
            toolBarOpen.setToolTipText("Open an image   (Alt + O)");
            toolBarOpen.addActionListener(this);
        toolBar.add(toolBarSave = new JButton(new ImageIcon(dir+"/Icons/toolbar/save.png")));
            toolBarSave.setToolTipText("Save the current image   (Alt + S)");
            toolBarSave.addActionListener(this);
        
        // Empty JPanels used in the tool bar to separate buttons into related
        // functional groups.
        toolBar.add(new JPanel());

        toolBar.add(toolBarRotateClock = new JButton(new ImageIcon(dir+"/Icons/toolbar/clock.png")));
            toolBarRotateClock.setToolTipText("Rotate current image clockwise   (Alt + ])");
            toolBarRotateClock.addActionListener(this);
        toolBar.add(toolBarRotateAnti = new JButton(new ImageIcon(dir+"/Icons/toolbar/anticlock.png")));
            toolBarRotateAnti.setToolTipText("Rotate current image anti-clockwise   (Alt + [)");
            toolBarRotateAnti.addActionListener(this);
        toolBar.add(toolBarFlipHoriz = new JButton(new ImageIcon(dir+"/Icons/toolbar/fliphoriz.png")));
            toolBarFlipHoriz.setToolTipText("Flip current image horizontally   (Alt + H)");
            toolBarFlipHoriz.addActionListener(this);
        toolBar.add(toolBarFlipVert = new JButton(new ImageIcon(dir+"/Icons/toolbar/flipvert.png")));
            toolBarFlipVert.setToolTipText("Flip current image vertically   (Alt + V)");
            toolBarFlipVert.addActionListener(this);
        toolBar.add(toolBarPixelate = new JButton(new ImageIcon(dir+"/Icons/toolbar/pixelate.png")));
            toolBarPixelate.setToolTipText("Pixelate!   (Alt + P)");
            toolBarPixelate.addActionListener(this);
        
        toolBar.add(new JPanel());
        
        toolBar.add(toolBarUndo = new JButton(new ImageIcon(dir+"/Icons/toolbar/undo.png")));
            toolBarUndo.setToolTipText("Undo all changes made to the loaded image   (Alt + Z)");
            toolBarUndo.addActionListener(this);
            
        toolBar.add(new JPanel());
        
        toolBar.add(toolBarZoomIn = new JButton(new ImageIcon(dir+"/Icons/toolbar/zoomin.png")));
            toolBarZoomIn.setToolTipText("Zoom in on current image   (Alt + =)");
            toolBarZoomIn.addActionListener(this);
        toolBar.add(toolBarZoomOut = new JButton(new ImageIcon(dir+"/Icons/toolbar/zoomout.png")));
            toolBarZoomOut.setToolTipText("Zoom out from current image   (Alt + -)");
            toolBarZoomOut.addActionListener(this);
            
        toolBar.add(new JPanel());
        
        toolBar.add(toolBarFolderImageAdd = new JButton(new ImageIcon(dir+"/Icons/toolbar/addtagimage.png")));
            toolBarFolderImageAdd.setToolTipText("Add current image to selected Tag Folder   (Alt + 1)");
            toolBarFolderImageAdd.addActionListener(this);
        toolBar.add(toolBarFolderImageRename = new JButton(new ImageIcon(dir+"/Icons/toolbar/renametagimage.png")));
            toolBarFolderImageRename.setToolTipText("Rename currently selected image  (Alt + 2)");
            toolBarFolderImageRename.addActionListener(this);
        toolBar.add(toolBarFolderImageRemove = new JButton(new ImageIcon(dir+"/Icons/toolbar/removetagimage.png")));
            toolBarFolderImageRemove.setToolTipText("Remove selected image from its Tag Folder   (Alt + 3)");
            toolBarFolderImageRemove.addActionListener(this);
        toolBar.add(toolBarFolderNew = new JButton(new ImageIcon(dir+"/Icons/toolbar/addtagfolder.png")));
            toolBarFolderNew.setToolTipText("Create a new Tag Folder within currently selected folder   (Alt + 4)");
            toolBarFolderNew.addActionListener(this);
        toolBar.add(toolBarFolderRename = new JButton(new ImageIcon(dir+"/Icons/toolbar/renametagfolder.png")));
            toolBarFolderRename.setToolTipText("Rename selected Tag Folder   (Alt + 5)");
            toolBarFolderRename.addActionListener(this);
        toolBar.add(toolBarFolderDel = new JButton(new ImageIcon(dir+"/Icons/toolbar/deletetagfolder.png")));
            toolBarFolderDel.setToolTipText("Delete selected Tag Folder   (Alt + 6)");
            toolBarFolderDel.addActionListener(this);

        add(toolBar, BorderLayout.NORTH);
        
        // Lines - are present in order to initialise the tree
        boolean debug = false;

        if (debug) {
            // Run with this code ONCE to initialise the tree
            DefaultMutableTreeNode top = new DefaultMutableTreeNode("Sorted Images");
            DefaultTreeModel model = new DefaultTreeModel(top);
            TreeData td = new TreeData();
            tree = new JTree(model);
            td.saveTreeModel(model);
        } else {
            // Run with this code afterwards
            TreeData td = new TreeData();
            model = td.loadTreeModel();
            tree = new JTree(model);
            tree.addTreeSelectionListener(this);
            tree.setExpandsSelectedPaths(true);
            tree.setCellRenderer(new ImageTreeRenderer());
        }

        // Setting up the main GUI
        treeTopPanel = new JPanel(new BorderLayout());
        treePanel = new JPanel(new BorderLayout());
        treeScrollPane = new JScrollPane(tree);
        TitledBorder treePaneTitle = new TitledBorder("Tags and Images");
            treePaneTitle.setTitleJustification(TitledBorder.LEFT);
        treePanel.setBorder(treePaneTitle);
        treePanel.add(treeScrollPane);
        
        treeTopPanel.add(treePanel, BorderLayout.NORTH);
        treeFunctionPanel = new JPanel(new BorderLayout());
        treeSearchPanel = new JPanel(new BorderLayout());
        treeSortButton = new JButton(new ImageIcon(dir+"/Icons/toolbar/sort2.png"));
        treeSortButton.setToolTipText("Sort the Tags Folders and images alphabetically   (Alt + G)");
        treeSortButton.addActionListener(this);
        treeSeparator = new JSeparator();
        treeSeparator.setOrientation(JSeparator.VERTICAL);
        treeSearchEntry = new JTextField();
        treeSearchEntry.setToolTipText("Enter image filename to search for");
        treeSearchButton = new JButton("Search");
        treeSearchButton.setToolTipText("Search the Tag Folders for a specific image   (Alt + F)");
        treeSearchButton.addActionListener(this);
        treeFunctionPanel.add(treeSortButton, BorderLayout.WEST);
        treeSearchPanel.add(treeSeparator, BorderLayout.WEST);
        treeSearchPanel.add(treeSearchEntry, BorderLayout.CENTER);
        treeSearchPanel.add(treeSearchButton, BorderLayout.EAST);
        treeFunctionPanel.add(treeSearchPanel, BorderLayout.CENTER);
        treePanel.add(treeFunctionPanel, BorderLayout.SOUTH);

        treeButtonPanel = new JPanel(new BorderLayout());
        treeButtonTopPanel = new JPanel(new BorderLayout());
        TitledBorder treeButtonTopTitle = new TitledBorder("Edit Tag Folders");
        treeButtonTopTitle.setTitleJustification(TitledBorder.LEFT);
        treeButtonTopPanel.setBorder(treeButtonTopTitle);

        treeButtonBottomPanel = new JPanel(new BorderLayout());
        TitledBorder treeButtonBottomTitle = new TitledBorder("Add/Rename/Remove Image");
        treeButtonBottomTitle.setTitleJustification(TitledBorder.LEFT);
        treeButtonBottomPanel.setBorder(treeButtonBottomTitle);

        treeButtonPanel.add(treeButtonTopPanel, BorderLayout.NORTH);
        treeButtonPanel.add(treeButtonBottomPanel, BorderLayout.SOUTH);

        addToTree = new JButton("Add Image");
        addToTree.setToolTipText("Add current image to currently selected Tag Folder   (Alt + 1)");
        addToTree.addActionListener(this);
        renameImageNode = new JButton("Rename Image");
        renameImageNode.setToolTipText("Remove Image selected in Tag Folders   (Alt + 2)");
        renameImageNode.addActionListener(this);
        removeFromTree = new JButton("Remove Image");
        removeFromTree.setToolTipText("Remove selected image from containing Tag Folder   (Alt + 3)");
        removeFromTree.addActionListener(this);

        addTreeNode = new JButton("Add Tag Folder");
        addTreeNode.setToolTipText("Add new Tag Folder within currently selected folder   (Alt + 4)");
        addTreeNode.addActionListener(this);
        renameTreeNode = new JButton("Rename Tag Folder");
        renameTreeNode.setToolTipText("Rename the selected Tag Folder   (Alt + 5)");
        renameTreeNode.addActionListener(this);
        deleteTreeNode = new JButton("Delete Tag Folder");
        deleteTreeNode.setToolTipText("Delete the selected Tag Folder   (Alt + 6)");
        deleteTreeNode.addActionListener(this);
        treeButtonTopPanel.add(addTreeNode, BorderLayout.NORTH);
        treeButtonTopPanel.add(renameTreeNode, BorderLayout.CENTER);
        treeButtonTopPanel.add(deleteTreeNode, BorderLayout.SOUTH);
        treeButtonBottomPanel.add(addToTree, BorderLayout.NORTH);
        treeButtonBottomPanel.add(renameImageNode, BorderLayout.CENTER);
        treeButtonBottomPanel.add(removeFromTree, BorderLayout.SOUTH);

        treeSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, treePanel, treeButtonPanel);
        treeSplitPane.setResizeWeight(1.0);
        treeSplitPane.setEnabled(false);

        pictureScrollPane = new JScrollPane();
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                treeSplitPane, pictureScrollPane);
        mainSplitPane.setResizeWeight(0.2);
        add(mainSplitPane, BorderLayout.CENTER);

    }
    
    /**
     * All action listeners call this function. Source of the ActionEvent determines the action taken.
     * 
     * @param event 
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        ImageManipulation im = new ImageManipulation();
        TreeData td = new TreeData();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        if (source == menuOpen || source == toolBarOpen) {
            try {
                System.out.println(this.getSize());
                loadedImage = im.loadAndDisplayImage(pictureScrollPane, picturePane, true, null);
                currentImage = loadedImage.image;
                currentFilePath = loadedImage.filePath;
                edited = false;

            } catch (NullPointerException npe) {
                System.out.println(npe);
            }
        } else if (source == menuSave || source == toolBarSave) {
            try {
                System.out.println("SAVE");
                loadedImage = im.saveImage(currentImage);
                edited = false;
            } catch (NullPointerException npe) {
                System.out.println(npe);
            }
        } else if (source == menuQuit) {
            System.exit(0);

        } else if (source == menuFlipHoriz || source == toolBarFlipHoriz) {
            try {
                currentImage = im.flipImage(pictureScrollPane, picturePane, currentImage, false);
                im.displayImage(pictureScrollPane, picturePane, currentImage);
                edited = true;
            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuFlipVert || source == toolBarFlipVert) {
            try {
                currentImage = im.flipImage(pictureScrollPane, picturePane, currentImage, true);
                im.displayImage(pictureScrollPane, picturePane, currentImage);
                edited = true;
            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuRotateClock || source == toolBarRotateClock) {
            try {
                currentImage = im.rotateImage(currentImage, 90);
                im.displayImage(pictureScrollPane, picturePane, currentImage);
                edited = true;
            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuRotateAnti || source == toolBarRotateAnti) {
            try {
                currentImage = im.rotateImage(currentImage, 270);
                im.displayImage(pictureScrollPane, picturePane, currentImage);
                edited = true;

            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuZoomIn || source == toolBarZoomIn) {
            try {

                currentImage = im.zoomImage(currentImage, currentImage.getWidth() * 2, currentImage.getHeight() * 2);
                System.out.println("ZOOMIN");
                im.displayImage(pictureScrollPane, picturePane, currentImage);
                edited = true;

            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuZoomOut || source == toolBarZoomOut) {
            try {

                currentImage = im.zoomImage(currentImage, currentImage.getWidth() / 2, currentImage.getHeight() / 2);
                im.displayImage(pictureScrollPane, picturePane, currentImage);
                edited = true;

            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuPixelate || source == toolBarPixelate) {
            try {

                currentImage = im.zoomImage(currentImage, currentImage.getWidth() / 5, currentImage.getHeight() / 5);
                currentImage = im.zoomImage(currentImage, currentImage.getWidth() * 5, currentImage.getHeight() * 5);
                im.displayImage(pictureScrollPane, picturePane, currentImage);
                edited = true;

            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuUndo || source == toolBarUndo) {
            try {
                System.out.println("a".compareToIgnoreCase("z"));
                System.out.println("3".compareToIgnoreCase("III"));
                currentImage = im.loadImage(currentFilePath);
                im.displayImage(pictureScrollPane, picturePane, currentImage);
                edited = false;

            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuFolderImageAdd || source == toolBarFolderImageAdd || source == addToTree) {
            try {
                if (edited) {
                    int save = JOptionPane.showConfirmDialog(null, "Image has been edited since it was saved."
                            + "\nTo add this image to a tag folder, it must be saved."
                            + "\nSave image?", "Save File?", JOptionPane.YES_NO_OPTION);
                    if (save == JOptionPane.NO_OPTION) {
                        return;
                    } else {
                        loadedImage = im.saveImage(currentImage);
                        edited = false;
                    }
                }
                DefaultMutableTreeNode parentNode = td.getSelectedNode(tree);
                while (true) {
                    TreePath path = td.searchTree(root, loadedImage.fileName);
                    if (path != null) {
                        String newName = JOptionPane.showInputDialog(tree, "Image already exists in The Tag Folder tree with name '" + loadedImage.fileName + "'\n\nPlease choose a new name for this image: ", "Please rename the image", JOptionPane.WARNING_MESSAGE);
                        if (newName.equals("")) {
                            continue;
                        }
                        loadedImage.fileName = newName;
                        
                    } else {
                        td.addImage(tree, model, loadedImage, parentNode);
                        td.saveTreeModel(model);
                        break;
                    }
                }

            } catch (NullPointerException npe) {
                System.out.println(npe);
                System.out.println("No Image Loaded");
                return;
            }
        } else if (source == menuFolderImageRename || source == toolBarFolderImageRename || source == renameImageNode) {
            try {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                td.renameImage(tree, model, node, currentFilePath);
                model.reload(node);
                model.removeNodeFromParent(node);
                td.saveTreeModel(model);

            } catch (ClassCastException cce) {
                System.out.println(cce);
                JOptionPane.showMessageDialog(tree, "To rename a tag folder, select the 'Rename Tag Folder' option.", "Not an image", JOptionPane.ERROR_MESSAGE);
            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuFolderImageRemove || source == toolBarFolderImageRemove || source == removeFromTree) {
            try {

                DefaultMutableTreeNode selectedNode = td.getSelectedNode(tree);
                td.removeImage(tree, model, selectedNode);
                td.saveTreeModel(model);
            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuFolderNew || source == toolBarFolderNew || source == addTreeNode) {
            try {
                DefaultMutableTreeNode parentNode = td.getSelectedNode(tree);
                td.addTag(tree, model, parentNode);
                td.saveTreeModel(model);
            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuFolderRename || source == toolBarFolderRename || source == renameTreeNode) {
            try {
                DefaultMutableTreeNode selectedNode = td.getSelectedNode(tree);
                td.renameTag(tree, model, selectedNode);
                model.reload(selectedNode);
                td.saveTreeModel(model);
            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuFolderDelete || source == toolBarFolderDel || source == deleteTreeNode) {
            try {
                DefaultMutableTreeNode selectedNode = td.getSelectedNode(tree);
                td.removeTag(tree, model, selectedNode);
                td.saveTreeModel(model);
            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuFolderSort || source == treeSortButton) {
            try {
                DefaultMutableTreeNode sortedRoot = td.sortTree(root);
                model.setRoot(sortedRoot);
                td.saveTreeModel(model);
            } catch (NullPointerException npe) {
                System.out.println(npe);
                return;
            }
        } else if (source == menuFolderSearch || source == treeSearchButton) {
            try {
                System.out.println(tree.getRowCount());
                String target = treeSearchEntry.getText();
                if (target.equals("")) {
                    JOptionPane.showMessageDialog(treeSearchButton, "Please enter an image name to search for.");
                    throw new NullPointerException();
                }
                System.out.println(target);
                TreePath path = td.searchTree(root, target);
                if (path == null) {
                    JOptionPane.showMessageDialog(treeSearchButton, "No image '" + target + "' found.");
                    throw new NullPointerException();
                }
                System.out.println(path);
                tree.setSelectionPath(path);
                tree.scrollPathToVisible(path);

            } catch (NullPointerException npe) {
                System.out.println("Exit Search");
                System.out.println(npe);
                return;
            }
        }
    }

    @Override
    /**
     * 
     */
    public void valueChanged(TreeSelectionEvent event) {

        ImageManipulation im = new ImageManipulation();

        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            if (node.getUserObject() instanceof ImageNode) {
                ImageNode selectedNode = (ImageNode) node.getUserObject();

                System.out.println("A:  " + selectedNode.name);
                loadedImage = im.loadAndDisplayImage(pictureScrollPane, picturePane, false, selectedNode.filePath);
                currentFilePath = loadedImage.filePath;
                currentFileName = loadedImage.fileName;
                currentImage = loadedImage.image;

            }
        } catch (NullPointerException npe) {
            System.out.println(npe);
        }

    }

    /**
     * 
     */
    public class ImageTreeRenderer extends JLabel implements TreeCellRenderer {

        final private Icon closedFolder;
        final private Icon openFolder;
        final private Icon picture;
        private Icon chosenIcon;
        private boolean isFolder;
        private boolean expanded;
        private boolean selected;

        public ImageTreeRenderer() {
            String dir = System.getProperty("user.dir");
            closedFolder = new ImageIcon(dir+"/Icons/tree/GenericFolderIcon.png");
            openFolder = new ImageIcon(dir+"/Icons/tree/OpenFolderIcon.png");
            picture = new ImageIcon(dir+"/Icons/tree/ColorSyncProfileIcon.png");
            chosenIcon = null;
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                boolean leaf, int row, boolean bHasFocus) {
            // Find out which node we are rendering and get its text
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

            String labelText = node.toString();
            this.isFolder = node.getAllowsChildren();
            this.expanded = expanded;
            this.selected = selected;

            if (!selected) {
                setForeground(Color.black);
            } else {
                setForeground(Color.white);
            }

            if (!isFolder) {
                chosenIcon = picture;
            } else if (expanded) {
                chosenIcon = openFolder;
            } else {
                chosenIcon = closedFolder;
            }

            setIcon(chosenIcon);
            setText(labelText);

            return this;
        }

        public void paint(Graphics g) {
            Color bColor;
            bColor = selected ? SystemColor.textHighlight : Color.white;
            g.setColor(bColor);
            g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

            super.paint(g);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        ImageBrowser browser = new ImageBrowser();
        browser.setVisible(true);
    }

    /**
     * Initialisation of variables.
     */
    
    //Set up menu bar objects
    final private JMenuBar menuBar;
    final private JMenu menuFile;
    final private JMenuItem menuOpen;
    final private JMenuItem menuSave;
    final private JMenuItem menuQuit;
    
    final private JMenu menuEdit;
    final private JMenu menuRotate;
    final private JMenuItem menuRotateClock;
    final private JMenuItem menuRotateAnti;
    final private JMenu menuFlip;
    final private JMenuItem menuFlipHoriz;
    final private JMenuItem menuFlipVert;
    final private JMenuItem menuUndo;
    final private JMenuItem menuPixelate;
    
    final private JMenu menuView;
    final private JMenuItem menuZoomIn;
    final private JMenuItem menuZoomOut;
    
    final private JMenu menuSorting;
    final private JMenuItem menuFolderImageAdd;
    final private JMenuItem menuFolderImageRename;
    final private JMenuItem menuFolderImageRemove;
    final private JSeparator menuFolderSeparator;
    final private JMenuItem menuFolderNew;
    final private JMenuItem menuFolderRename;
    final private JMenuItem menuFolderDelete;
    final private JMenuItem menuFolderSort;
    final private JMenuItem menuFolderSearch;

    // Setting up toolbar objects
    final private JToolBar toolBar;
    final private JButton toolBarOpen;
    final private JButton toolBarSave;

    final private JButton toolBarRotateClock;
    final private JButton toolBarRotateAnti;
    final private JButton toolBarFlipHoriz;
    final private JButton toolBarFlipVert;

    final private JButton toolBarPixelate;
    final private JButton toolBarUndo;

    final private JButton toolBarZoomIn;
    final private JButton toolBarZoomOut;

    final private JButton toolBarFolderImageAdd;
    final private JButton toolBarFolderImageRename;
    final private JButton toolBarFolderImageRemove;
    final private JButton toolBarFolderNew;
    final private JButton toolBarFolderRename;
    final private JButton toolBarFolderDel;

    // Setting up main GUI components
    final private JSplitPane mainSplitPane;
    final private JSplitPane treeSplitPane;
    final private JPanel treeTopPanel;
    final private JPanel treePanel;
    final private JScrollPane treeScrollPane;
    final private JPanel treeButtonPanel;
    final private JPanel treeButtonTopPanel;
    final private JPanel treeButtonBottomPanel;
    final private JButton addToTree;
    final private JButton renameImageNode;
    final private JButton removeFromTree;
    final private JScrollPane pictureScrollPane;

    final private JButton deleteTreeNode;
    final private JButton renameTreeNode;
    final private JButton addTreeNode;

    public JTree tree;
    public DefaultTreeModel model;

    final private JPanel treeFunctionPanel;
    final private JPanel treeSearchPanel;
    final private JButton treeSortButton;
    final private JSeparator treeSeparator;
    final private JTextField treeSearchEntry;
    final private JButton treeSearchButton;

    public ImageManipulation.JImagePanel picturePane;

    public ImageManipulation.LoadedImage loadedImage;
    public BufferedImage currentImage;
    public String currentFilePath;
    public String currentFileName;

    boolean edited;

}
