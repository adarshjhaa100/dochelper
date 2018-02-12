package com.example.ayush.afinal;

import java.io.File;

abstract class AlbumStorageDirFactory{
    public abstract File getAlbumStorageDir(String albumName);
}
