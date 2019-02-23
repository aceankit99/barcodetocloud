/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kenexx.view;

import com.kenexx.model.UserFiles;
import com.kenexx.web.WebSession;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author user
 */
@Named
@ViewScoped
public class Barcode implements Serializable {
   
    @Inject
    private WebSession webSession;        
    UserFiles uf=new UserFiles();
    private String barCode;
    private int rowNum;
    private String columnName;
    private String sheetName;
    
    @PostConstruct
    public void init(){
    uf=webSession.getFiles();
    }

    public UserFiles getUf() {
        return uf;
    }

    public void setUf(UserFiles uf) {
        this.uf = uf;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
