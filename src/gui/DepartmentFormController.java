package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DateChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	private DepartmentService service;
	
	private List <DateChangeListener> dateChangeListeners=new ArrayList<>();
	private Department entity;	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label lbError;
	@FXML
	private Button btnSalve;
	@FXML
	private Button btnCancel;
	
	public void setDepartment(Department entity) {
		this.entity=entity;
	}
	
	public void seDepartmentService(DepartmentService service) {
		this.service=service;
	}
	
	public void subscribeDateChangeListener(DateChangeListener listener) {
		dateChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtnSalveAction(ActionEvent event) {
		if(entity==null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service==null) {
			throw new IllegalStateException("service was null");
		}
 try {
		entity=getFormData();
		service.saveOrUpdate(entity);
		notifyDateChangeListener();
		Utils.currentStage(event).close();
     }catch(DbException e) {
    	 Alerts.showAlert("Error save object", null, e.getMessage(), AlertType.ERROR);
     }
	}
	
	private void notifyDateChangeListener() {
		for(DateChangeListener listener: dateChangeListeners) {
			listener.onDateChanger();
		}
	}

	private Department getFormData() {
		Department obj= new Department();
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		return obj;
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateFormDate() {
		if(entity==null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}

}
