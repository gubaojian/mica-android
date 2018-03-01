/*
 * Copyright 2013 MicaByte Systems
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.micabytes.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.micabytes.gfx.SurfaceRenderer;
import com.micabytes.util.GameLog;

/**
 * TileMap superclass
 */
@SuppressWarnings({"unused", "AbstractClassNeverImplemented"})
public abstract class TileMap {
    protected static int mapWidth;
    protected static int mapHeight;
    private TileMapZone[][] zones;
    protected static Rect tileRect = new Rect();
    protected float scaleFactor;
    protected final Point viewPortOrigin = new Point();
    protected final Point viewPortSize = new Point();
    protected final Rect destRect = new Rect();
    protected int windowLeft;
    protected int windowTop;
    protected int windowRight;
    protected int windowBottom;
    // Draw
    protected final Canvas canvas = new Canvas();

    @SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
    public void setTileMap(TileMapZone[][] map) {
        zones = new TileMapZone[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(map[i], 0, zones[i], 0, map[i].length);
        }
        mapHeight = map[0].length;
        mapWidth = map.length;
        tileRect = new Rect(0, 0, map[0][0].getWidth(), map[0][0].getHeight());
    }

    public int getRenderHeight() {
        return mapHeight * tileRect.height();
    }

    public int getRenderWidth() {
        return mapWidth * tileRect.width();
    }

    public int getTileHeight() {
        return tileRect.height();
    }

    public int getTileWidth() {
        return tileRect.width();
    }

    @SuppressWarnings({"MethodWithMultipleLoops", "NumericCastThatLosesPrecision"})
    public void drawBase(Context context, SurfaceRenderer.ViewPort p) {
        if (p.getBitmap() == null) {
            GameLog.e("TileMap", "Viewport bitmap is null in TileMap");
            return;
        }
        canvas.setBitmap(p.getBitmap());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        float scaleFactor = p.getZoom();
        int tileSize = tileRect.width();
        p.getOrigin(viewPortOrigin);
        p.getSize(viewPortSize);
        int windowLeft = viewPortOrigin.x;
        int windowTop = viewPortOrigin.y;
        int windowRight = viewPortOrigin.x + viewPortSize.x;
        int windowBottom = viewPortOrigin.y + viewPortSize.y;
        // Clip tiles not in view
        int iMn = windowLeft / tileSize;
        if (iMn < 0) iMn = 0;
        int jMn = windowTop / tileSize;
        if (jMn < 0) jMn = 0;
        int iMx = (windowRight / tileSize) + 1;
        if (iMx >= mapWidth) iMx = mapWidth;
        int jMx = (windowBottom / tileSize) + 1;
        if (jMx >= mapHeight) jMx = mapHeight;
        // Draw Tiles
        for (int i = iMn; i < iMx; i++) {
            for (int j = jMn; j < jMx; j++) {
                if (zones[i][j] != null) {
                    destRect.left = (int) (((i * tileSize) - windowLeft) / scaleFactor);
                    destRect.top = (int) (((j * tileSize) - windowTop) / scaleFactor);
                    destRect.right = (int) ((((i * tileSize) + tileSize) - windowLeft) / scaleFactor);
                    destRect.bottom = (int) ((((j * tileSize) + tileSize) - windowTop) / scaleFactor);
                    zones[i][j].drawBase(canvas, tileRect, destRect, paint);
                }
            }
        }
    }

    public abstract void drawLayer(Context context, SurfaceRenderer.ViewPort p);

    public abstract void drawFinal(Context context, SurfaceRenderer.ViewPort p);

    public abstract Point getViewPortOrigin(int x, int y, SurfaceRenderer.ViewPort p);

    public static int getMapWidth() {
        return mapWidth;
    }

    public static int getMapHeight() {
        return mapHeight;
    }

}
