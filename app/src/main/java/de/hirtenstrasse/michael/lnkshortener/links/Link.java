package de.hirtenstrasse.michael.lnkshortener.links;

// Copyright (C) 2017 Michael Achmann

//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

public class Link {
    private long id, added, category, lastUpdated;
    private String originalLink, shortLink, linkTitle;
    private boolean isStarred, isSecret;
    private int clicks;
    private byte[] linkImage;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public long getAdded(){
        return added;
    }

    public void setAdded(long added){
        this.added = added;
    }

    public String getLongLink(){
        return originalLink;
    }

    public void setLongLink(String originalLink){
        this.originalLink = originalLink;
    }

    public String getShortLink(){
        return shortLink;
    }

    public void setShortLink(String shortLink){
        this.shortLink = shortLink;
    }

    public void setCategory(long category){
        this.category = category;
    }

    public long getCategory(){
        return category;
    }

    public void setLastUpdated(long lastUpdated){
        this.lastUpdated = lastUpdated;
    }

    public long getLastUpdated(){
        return lastUpdated;
    }

    public void setLinkTitle(String linkTitle){
        this.linkTitle = linkTitle;
    }

    public String getLinkTitle(){
        return linkTitle;
    }

    public void setIsStarred(boolean isStarred){
        this.isStarred = isStarred;
    }

    public boolean getIsStarred(){
        return isStarred;
    }

    public void setIsSecret(boolean isSecret){
        this.isSecret = isSecret;
    }

    public boolean getIsSecret(){
        return isSecret;
    }

    public void setClicks(int clicks){
        this.clicks = clicks;
    }

    public int getClicks(int clicks){
        return clicks;
    }

    public void setLinkImage(byte[] linkImage){
        this.linkImage = linkImage;
    }

    public byte[] getLinkImage(){
        return linkImage;
    }


}
