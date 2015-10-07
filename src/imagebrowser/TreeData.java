package imagebrowser;

import javax.swing.*;
import javax.swing.tree.*;
import imagebrowser.ImageManipulation.LoadedImage;
import java.io.*;

public class TreeData implements Serializable {

    /**
     *
     */
    public class ImageNode extends DefaultMutableTreeNode implements Serializable {

        String name, filePath;

        public ImageNode(String name, String filePath) {
            this.name = name;
            this.filePath = filePath;
        }

        @Override
        public String toString() {
            return name;
        }

    }
    
    /**
     * 
     * @return newModel
     */
    public DefaultTreeModel loadTreeModel() {
        DefaultTreeModel newModel = null;
        try {
            FileInputStream fis = new FileInputStream("TreeModelData");
            ObjectInputStream ois = new ObjectInputStream(fis);
            newModel = (DefaultTreeModel) ois.readObject();
            System.out.println("Loaded");
        } catch (Exception e) {
            System.out.println("Not loaded");
            System.out.println(e);
        }
        return newModel;
    }
    
    /**
     * 
     * @param model 
     */
    public void saveTreeModel(DefaultTreeModel model) {

        try {
            FileOutputStream fos = new FileOutputStream("TreeModelData");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(model);
            System.out.println("Saved");
            String dir = System.getProperty("user.dir");
            fos = new FileOutputStream(dir+"/TreeModelBackup/TreeModelData");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(model);
            System.out.println("Saved again");
        } catch (Exception e) {
            System.out.println("Not saved");
            System.out.println(e);
        }

    }

    public DefaultMutableTreeNode getSelectedNode(JTree tree) {
        return (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

    }
    
    /**
     * 
     * @param tree
     * @param model
     * @param parent 
     */
    public void addTag(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode parent) {

        if (parent == null) {
            parent = (DefaultMutableTreeNode) model.getRoot();
            System.out.println("Nothing Selected. New Folder.");
            while (true) {
                String newTagName = JOptionPane.showInputDialog(tree, "New tag:");
                if (newTagName.equals("")) {
                    JOptionPane.showMessageDialog(tree, "Please enter a name for the Tag.", "Enter Tag", JOptionPane.ERROR_MESSAGE);
                } else {
                    model.insertNodeInto(new DefaultMutableTreeNode(newTagName, true),
                            parent, parent.getChildCount());
                    break;
                }
            }
        } else if (parent.getAllowsChildren()) {
            System.out.println("New Folder");
            while (true) {
                String newTagName = JOptionPane.showInputDialog(tree, "New tag:");
                if (newTagName.equals("")) {
                    JOptionPane.showMessageDialog(tree, "Please enter a name for the Tag.", "Enter Tag", JOptionPane.ERROR_MESSAGE);
                } else {
                    model.insertNodeInto(new DefaultMutableTreeNode(newTagName, true),
                            parent, parent.getChildCount());
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(tree, "Cannot add a new folder to an image!", "Image Selected", JOptionPane.ERROR_MESSAGE);
        }

    }
    
    /**
     * 
     * @param tree
     * @param model
     * @param selectedNode 
     */
    public void renameTag(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode selectedNode) {

        if (selectedNode == null) {
            JOptionPane.showMessageDialog(null, "Please select a tag folder to rename", "No tag folder selected", JOptionPane.ERROR_MESSAGE);
        } else if (selectedNode.getAllowsChildren() == false) {
            JOptionPane.showMessageDialog(tree, "To rename an image, select the 'Rename Image' option.", "Not a Tag Folder", JOptionPane.ERROR_MESSAGE);
        } else {
            String newTagName = JOptionPane.showInputDialog(tree, "New tag:");
            if (newTagName.equals("")) {
                return;
            } else {
                selectedNode.setUserObject(newTagName);
            }
        }
    }

    /**
     * 
     * @param tree
     * @param model
     * @param selectedNode 
     */
    public void removeTag(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode selectedNode) {

        try {
            if (selectedNode == null) {
                JOptionPane.showMessageDialog(null, "Please select a tag folder to remove", "No tag folder selected", JOptionPane.ERROR_MESSAGE);
            } else if (selectedNode.getAllowsChildren() == false) {
                String[] options = new String[]{"Remove Image", "Delete Tag Folder"};
                int response = JOptionPane.showOptionDialog(null, "You have selected an image, not a tag folder."
                        + "\nWould you like to remove the image from the tag folder, or"
                        + "delete the tag folder itself?", "Image Selected",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, options, options[1]);
                if (response == 0) {
                    removeImage(tree, model, selectedNode);
                } else {
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                    removeTag(tree, model, parentNode);
                }
            } else {
                if (selectedNode.getChildCount() > 0) {
                    int response = JOptionPane.showConfirmDialog(null, "Tag Folder '" + selectedNode.toString() + "' is not empty. Are you sure you want to remove this tag folder?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == 0) {
                        model.removeNodeFromParent(selectedNode);
                    }
                } else {
                    model.removeNodeFromParent(selectedNode);
                }
            }
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Please do not delete the Sorted Images folder!", "Cannot delete root folder", JOptionPane.ERROR_MESSAGE);
            System.out.println(iae);
        }

    }
    
    /**
     * 
     * @param tree
     * @param model
     * @param img
     * @param parent 
     */
    public void addImage(JTree tree, DefaultTreeModel model, LoadedImage img, DefaultMutableTreeNode parent) {

        if (parent == null) {
            JOptionPane.showMessageDialog(tree, "Please select a tag folder.", "No Folder Selected.", JOptionPane.ERROR_MESSAGE);;
        } else if (parent.getAllowsChildren()) {
            System.out.println("New Image");
            String newName = img.fileName;
            model.insertNodeInto(new DefaultMutableTreeNode(new ImageNode(newName, img.filePath), false),
                    parent, parent.getChildCount());
        } else {
            JOptionPane.showMessageDialog(tree, "Cannot add a new folder to an image!.", "Nope!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 
     * @param tree
     * @param model
     * @param selectedNode
     * @param filePath 
     */
    public void renameImage(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode selectedNode, String filePath) {

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        ImageNode selectedimgNode = (ImageNode) selectedNode.getUserObject();

        if (selectedNode == null) {
            JOptionPane.showMessageDialog(null, "Please select an image to rename.", "No tag folder selected", JOptionPane.ERROR_MESSAGE);
        } else if (selectedNode.getAllowsChildren()) {
            JOptionPane.showMessageDialog(tree, "To rename a tag folder, select the 'Rename Tag Folder' option.", "Not an image", JOptionPane.ERROR_MESSAGE);
        } else {
            String newImageName = JOptionPane.showInputDialog(tree, "New image name:");
            while (true) {
                TreePath path = searchTree(root, newImageName);
                if (path != null) {
                    String newName = JOptionPane.showInputDialog(tree, "Image already exists in The Tag Folder tree with name '" + newImageName + "'\n\nPlease choose a new name for this image: ", "Please rename the image", JOptionPane.WARNING_MESSAGE);
                    if (newName == "") {
                        JOptionPane.showMessageDialog(tree, "Please enter a new name for the image.", "Enter image name", JOptionPane.ERROR_MESSAGE);
                        continue;
                    } else if (newName == null) {
                        newImageName = selectedNode.toString();
                        break;
                    } else {
                        newImageName = newName;
                        continue;
                    }
                } else {
                    break;
                }
            }
            int index = selectedNode.getParent().getIndex(selectedNode);
            MutableTreeNode parent = (MutableTreeNode) selectedNode.getParent();
            if (newImageName == null) {
                newImageName = selectedNode.toString();
            }

            model.insertNodeInto(new DefaultMutableTreeNode(new ImageNode(newImageName, filePath), false), parent, index);

        }

    }

    /**
     * 
     * @param tree
     * @param model
     * @param selectedNode 
     */
    public void removeImage(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode selectedNode) {
        if (selectedNode == null) {
            JOptionPane.showMessageDialog(null, "Please select an image to remove", "No image selected", JOptionPane.ERROR_MESSAGE);
        } else if (selectedNode.getAllowsChildren()) {
            JOptionPane.showMessageDialog(null, "You have selected an entire tag folder. "
                    + "\nPlease select the specific image that you wish to remove", "No image specified", JOptionPane.ERROR_MESSAGE);

        } else {
            model.removeNodeFromParent(selectedNode);
        }
    }
    
    /**
     * 
     * @param root
     * @return 
     */
    public DefaultMutableTreeNode sortTree(DefaultMutableTreeNode root) {
        int childNo = root.getChildCount();
        DefaultMutableTreeNode node = null, nextNode;

        while (true) {
            boolean swapped = false;
            for (int i = 0; i < childNo - 1; i++) {

                node = (DefaultMutableTreeNode) root.getChildAt(i);
                String nodeName = node.getUserObject().toString();

                nextNode = (DefaultMutableTreeNode) root.getChildAt(i + 1);
                String prevNodeName = nextNode.getUserObject().toString();
                System.out.println(nodeName + " - " + prevNodeName);
                if (nodeName.compareToIgnoreCase(prevNodeName) > 0) {
                    System.out.println("SWAP");
                    root.insert(node, i + 1);
                    root.insert(nextNode, i);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }

        }
        for (int i = 0; i < childNo; i++) {
            node = (DefaultMutableTreeNode) root.getChildAt(i);
            if (node.getAllowsChildren()) {
                if (node.getChildCount() > 0) {
                    node = sortTree(node);
                }
            }
        }
        return root;
    }

    /**
     * 
     * @param root
     * @param target
     * @return 
     */
    public TreePath searchTree(DefaultMutableTreeNode root, String target) {

        TreePath path = null;

        for (int i = 0; i < root.getChildCount(); i++) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
            if (node.getAllowsChildren()) {
                if (node.getChildCount() > 0) {
                    path = searchTree(node, target);
                }
                if (path != null) {
                    return path;
                }
            } else {
                String nodeName = node.getUserObject().toString();
                if (nodeName.equalsIgnoreCase(target)) {
                    path = new TreePath(node.getPath());
                    System.out.println("FOUND IT");
                    return path;
                }
            }
        }
        return path;
    }

}
