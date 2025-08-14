package com.sinse.stockapp.model.member;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

@Slf4j
@Component
public class WaterHandler extends DefaultHandler {
    @Getter
    private List<Water> waterList;

    Water water;

    private boolean isResultCode;
    private boolean isResultMsg;
    private boolean isPageIndex;
    private boolean isPageSize;
    private boolean isStartPage;
    private boolean isTotalCount;
    private boolean isAround;
    private boolean isBcode;
    private boolean isGroupCode;
    private boolean isIngredient;
    private boolean isLocation;
    private boolean isPosx;
    private boolean isPosy;
    private boolean isTitle;
    private boolean isWaterBase;
    private boolean isWaterType;
    
    //문서 시작
    @Override
    public void startDocument() throws SAXException{
        waterList = new java.util.ArrayList<>();
    }

    // 시작 태그
    public void startElement(String uri, String localName, String tag, Attributes attributes){
        if(tag.equals("water")){
            water = new Water();
        }
    }

}
