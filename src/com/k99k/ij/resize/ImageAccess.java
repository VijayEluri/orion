package com.k99k.ij.resize;


/*      */ import ij.ImagePlus;
/*      */ import ij.gui.ImageWindow;
/*      */ import ij.process.ByteProcessor;
/*      */ import ij.process.ColorProcessor;
/*      */ import ij.process.FloatProcessor;
/*      */ import ij.process.ImageProcessor;
/*      */ import java.awt.Point;
/*      */ 
/*      */ public class ImageAccess
/*      */ {
/*      */   public static final int PATTERN_SQUARE_3x3 = 0;
/*      */   public static final int PATTERN_CROSS_3x3 = 1;
/*   31 */   private double[] pixels = null;
/*   32 */   private int nx = 0;
/*   33 */   private int ny = 0;
/*   34 */   private int size = 0;
/*      */ 
/*      */   public ImageAccess(double[][] array)
/*      */   {
/*   43 */     if (array == null) {
/*   44 */       throw new ArrayStoreException("Constructor: array == null.");
/*      */     }
/*   46 */     this.ny = array[0].length;
/*   47 */     this.nx = array.length;
/*   48 */     this.size = (this.nx * this.ny);
/*   49 */     this.pixels = new double[this.size];
/*   50 */     int k = 0;
/*   51 */     for (int j = 0; j < this.ny; ++j)
/*   52 */       for (int i = 0; i < this.nx; ++i)
/*   53 */         this.pixels[(k++)] = array[i][j];
/*      */   }
/*      */ 
/*      */   public ImageAccess(ImageProcessor ip)
/*      */   {
/*   67 */     if (ip == null) {
/*   68 */       throw new ArrayStoreException("Constructor: ImageProcessor == null.");
/*      */     }
/*   70 */     this.nx = ip.getWidth();
/*   71 */     this.ny = ip.getHeight();
/*   72 */     this.size = (this.nx * this.ny);
/*   73 */     this.pixels = new double[this.size];
/*   74 */     if (ip.getPixels() instanceof byte[]) {
/*   75 */       byte[] bsrc = (byte[])(byte[])ip.getPixels();
/*   76 */       for (int k = 0; k < this.size; ++k) {
/*   77 */         this.pixels[k] = (bsrc[k] & 0xFF);
/*      */       }
/*      */     }
/*   80 */     else if (ip.getPixels() instanceof short[]) {
/*   81 */       short[] ssrc = (short[])(short[])ip.getPixels();
/*   82 */       for (int k = 0; k < this.size; ++k)
/*   83 */         this.pixels[k] = (ssrc[k] & 0xFFFF);
/*      */     }
/*   85 */     else if (ip.getPixels() instanceof float[]) {
/*   86 */       float[] fsrc = (float[])(float[])ip.getPixels();
/*   87 */       for (int k = 0; k < this.size; ++k)
/*   88 */         this.pixels[k] = fsrc[k];
/*      */     }
/*      */     else {
/*   91 */       throw new ArrayStoreException("Constructor: Unexpected image type.");
/*      */     }
/*      */   }
/*      */ 
/*      */   public ImageAccess(ColorProcessor cp, int colorPlane)
/*      */   {
/*  109 */     if (cp == null) {
/*  110 */       throw new ArrayStoreException("Constructor: ColorProcessor == null.");
/*      */     }
/*  112 */     if (colorPlane < 0) {
/*  113 */       throw new ArrayStoreException("Constructor: colorPlane < 0.");
/*      */     }
/*  115 */     if (colorPlane > 2) {
/*  116 */       throw new ArrayStoreException("Constructor: colorPlane > 2.");
/*      */     }
/*  118 */     this.nx = cp.getWidth();
/*  119 */     this.ny = cp.getHeight();
/*  120 */     this.size = (this.nx * this.ny);
/*  121 */     this.pixels = new double[this.size];
/*  122 */     byte[] r = new byte[this.size];
/*  123 */     byte[] g = new byte[this.size];
/*  124 */     byte[] b = new byte[this.size];
/*  125 */     cp.getRGB(r, g, b);
/*  126 */     if (colorPlane == 0)
/*  127 */       for (int k = 0; k < this.size; ++k)
/*  128 */         this.pixels[k] = (r[k] & 0xFF);
/*  129 */     else if (colorPlane == 1)
/*  130 */       for (int k = 0; k < this.size; ++k)
/*  131 */         this.pixels[k] = (g[k] & 0xFF);
/*  132 */     else if (colorPlane == 2)
/*  133 */       for (int k = 0; k < this.size; ++k)
/*  134 */         this.pixels[k] = (b[k] & 0xFF);
/*      */   }
/*      */ 
/*      */   public ImageAccess(int nx, int ny)
/*      */   {
/*  147 */     if (nx < 1) {
/*  148 */       throw new ArrayStoreException("Constructor: nx < 1.");
/*      */     }
/*  150 */     if (ny < 1) {
/*  151 */       throw new ArrayStoreException("Constructor: ny < 1.");
/*      */     }
/*  153 */     this.nx = nx;
/*  154 */     this.ny = ny;
/*  155 */     this.size = (nx * ny);
/*  156 */     this.pixels = new double[this.size];
/*      */   }
/*      */ 
/*      */   public int getWidth()
/*      */   {
/*  165 */     return this.nx;
/*      */   }
/*      */ 
/*      */   public int getHeight()
/*      */   {
/*  174 */     return this.ny;
/*      */   }
/*      */ 
/*      */   public double getMaximum()
/*      */   {
/*  183 */     double maxi = this.pixels[0];
/*  184 */     for (int i = 1; i < this.size; ++i)
/*  185 */       if (this.pixels[i] > maxi)
/*  186 */         maxi = this.pixels[i];
/*  187 */     return maxi;
/*      */   }
/*      */ 
/*      */   public double getMinimum()
/*      */   {
/*  196 */     double mini = this.pixels[0];
/*  197 */     for (int i = 1; i < this.size; ++i)
/*  198 */       if (this.pixels[i] < mini)
/*  199 */         mini = this.pixels[i];
/*  200 */     return mini;
/*      */   }
/*      */ 
/*      */   public double getMean()
/*      */   {
/*  210 */     double mean = 0.0D;
/*  211 */     for (int i = 0; i < this.size; ++i)
/*  212 */       mean += this.pixels[i];
/*  213 */     mean /= this.size;
/*  214 */     return mean;
/*      */   }
/*      */ 
/*      */   public double[][] getArrayPixels()
/*      */   {
/*  224 */     double[][] array = new double[this.nx][this.ny];
/*  225 */     int k = 0;
/*  226 */     for (int j = 0; j < this.ny; ++j)
/*  227 */       for (int i = 0; i < this.nx; ++i)
/*  228 */         array[i][j] = this.pixels[(k++)];
/*  229 */     return array;
/*      */   }
/*      */ 
/*      */   public double[] getPixels()
/*      */   {
/*  238 */     return this.pixels;
/*      */   }
/*      */ 
/*      */   public FloatProcessor createFloatProcessor()
/*      */   {
/*  248 */     FloatProcessor fp = new FloatProcessor(this.nx, this.ny);
/*  249 */     float[] fsrc = new float[this.size];
/*  250 */     for (int k = 0; k < this.size; ++k)
/*  251 */       fsrc[k] = (float)this.pixels[k];
/*  252 */     fp.setPixels(fsrc);
/*  253 */     return fp;
/*      */   }
/*      */ 
/*      */   public ByteProcessor createByteProcessor()
/*      */   {
/*  263 */     ByteProcessor bp = new ByteProcessor(this.nx, this.ny);
/*  264 */     byte[] bsrc = new byte[this.size];
/*      */ 
/*  266 */     for (int k = 0; k < this.size; ++k) {
/*  267 */       double p = this.pixels[k];
/*  268 */       if (p < 0.0D)
/*  269 */         p = 0.0D;
/*  270 */       if (p > 255.0D)
/*  271 */         p = 255.0D;
/*  272 */       bsrc[k] = (byte)(int)p;
/*      */     }
/*  274 */     bp.setPixels(bsrc);
/*  275 */     return bp;
/*      */   }
/*      */ 
/*      */   public ImageAccess duplicate()
/*      */   {
/*  285 */     ImageAccess ia = new ImageAccess(this.nx, this.ny);
/*  286 */     for (int i = 0; i < this.size; ++i)
/*  287 */       ia.pixels[i] = this.pixels[i];
/*  288 */     return ia;
/*      */   }
/*      */ 
/*      */   public double getPixel(int x, int y)
/*      */   {
/*  302 */     int periodx = 2 * this.nx - 2;
/*  303 */     int periody = 2 * this.ny - 2;
/*  304 */     if (x < 0) {
/*  305 */       for (; x < 0; x += periodx);
/*  306 */       if (x >= this.nx) x = periodx - x;
/*      */     }
/*  308 */     else if (x >= this.nx) {
/*  309 */       for (; x >= this.nx; x -= periodx);
/*  310 */       if (x < 0) x = -x;
/*      */     }
/*      */ 
/*  313 */     if (y < 0) {
/*  314 */       for (; y < 0; y += periody);
/*  315 */       if (y >= this.ny) y = periody - y;
/*      */     }
/*  317 */     else if (y >= this.ny) {
/*  318 */       for (; y >= this.ny; y -= periody);
/*  319 */       if (y < 0) y = -y;
/*      */     }
/*  321 */     return this.pixels[(x + y * this.nx)];
/*      */   }
/*      */ 
/*      */   public double getInterpolatedPixel(double x, double y)
/*      */   {
/*  337 */     if (Double.isNaN(x))
/*  338 */       return 0.0D;
/*  339 */     if (Double.isNaN(y)) {
/*  340 */       return 0.0D;
/*      */     }
/*  342 */     if (x < 0.0D) {
/*  343 */       int periodx = 2 * this.nx - 2;
/*  344 */       for (; x < 0.0D; x += periodx);
/*  345 */       if (x >= this.nx) x = periodx - x;
/*      */     }
/*  347 */     else if (x >= this.nx) {
/*  348 */       int periodx = 2 * this.nx - 2;
/*  349 */       for (; x >= this.nx; x -= periodx);
/*  350 */       if (x < 0.0D) x = -x;
/*      */     }
/*      */ 
/*  353 */     if (y < 0.0D) {
/*  354 */       int periody = 2 * this.ny - 2;
/*  355 */       for (; y < 0.0D; y += periody);
/*  356 */       if (y >= this.ny) y = periody - y;
/*      */     }
/*  358 */     else if (y >= this.ny) {
/*  359 */       int periody = 2 * this.ny - 2;
/*  360 */       for (; y >= this.ny; y -= periody);
/*  361 */       if (y < 0.0D) y = -y;
/*      */     }
/*      */     int i;
/*      */    // int i;
/*  364 */     if (x >= 0.0D) {
/*  365 */       i = (int)x;
/*      */     } else {
/*  367 */       int iAdd = (int)x - 1;
/*  368 */       i = (int)(x - iAdd) + iAdd;
/*      */     }
/*      */    // int j;
/*      */     int j;
/*  371 */     if (y >= 0.0D) {
/*  372 */       j = (int)y;
/*      */     } else {
/*  374 */       int iAdd = (int)y - 1;
/*  375 */       j = (int)(y - iAdd) + iAdd;
/*      */     }
/*      */ 
/*  378 */     double dx = x - i;
/*  379 */     double dy = y - j;
/*      */     //int di;
/*      */     int di;
/*  381 */     if (i >= this.nx - 1)
/*  382 */       di = -1;
/*      */     else
/*  384 */       di = 1;
/*  385 */     int index = i + j * this.nx;
/*  386 */     double v00 = this.pixels[index];
/*  387 */     double v10 = this.pixels[(index + di)];
/*  388 */     if (j >= this.ny - 1)
/*  389 */       index -= this.nx;
/*      */     else
/*  391 */       index += this.nx;
/*  392 */     double v01 = this.pixels[index];
/*  393 */     double v11 = this.pixels[(index + di)];
/*  394 */     return dx * (v11 * dy - v10 * (dy - 1.0D)) - (dx - 1.0D) * (v01 * dy - v00 * (dy - 1.0D));
/*      */   }
/*      */ 
/*      */   public void getColumn(int x, double[] column)
/*      */   {
/*  407 */     if (x < 0)
/*  408 */       throw new IndexOutOfBoundsException("getColumn: x < 0.");
/*  409 */     if (x >= this.nx)
/*  410 */       throw new IndexOutOfBoundsException("getColumn: x >= nx.");
/*  411 */     if (column == null) {
/*  412 */       throw new ArrayStoreException("getColumn: column == null.");
/*      */     }
/*  414 */     if (column.length != this.ny) {
/*  415 */       throw new ArrayStoreException("getColumn: column.length != ny.");
/*      */     }
/*  417 */     for (int i = 0; i < this.ny; ++i) {
/*  418 */       column[i] = this.pixels[x];
/*  419 */       x += this.nx;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void getColumn(int x, int y, double[] column)
/*      */   {
/*  435 */     if (x < 0)
/*  436 */       throw new IndexOutOfBoundsException("getColumn: x < 0.");
/*  437 */     if (x >= this.nx)
/*  438 */       throw new IndexOutOfBoundsException("getColumn: x >= nx.");
/*  439 */     if (column == null)
/*  440 */       throw new ArrayStoreException("getColumn: column == null.");
/*  441 */     int by = column.length;
/*  442 */     if ((y >= 0) && 
/*  443 */       (y < this.ny - by - 1)) {
/*  444 */       int index = y * this.nx + x;
/*  445 */       for (int i = 0; i < by; ++i) {
/*  446 */         column[i] = this.pixels[index];
/*  447 */         index += this.nx;
/*      */       }
/*  449 */       return;
/*      */     }
/*      */ 
/*  452 */     int[] yt = new int[by];
/*  453 */     for (int k = 0; k < by; ++k) {
/*  454 */       int ya = y + k;
/*  455 */       int periody = 2 * this.ny - 2;
/*  456 */       for (; ya < 0; ya += periody);
/*  457 */       while (ya >= this.ny) {
/*  458 */         ya = periody - ya;
/*  459 */         if (ya >= 0) continue; ya = -ya;
/*      */       }
/*  461 */       yt[k] = ya;
/*      */     }
/*  463 */     int index = 0;
/*  464 */     for (int i = 0; i < by; ++i) {
/*  465 */       index = yt[i] * this.nx + x;
/*  466 */       column[i] = this.pixels[index];
/*      */     }
/*      */   }
/*      */ 
/*      */   public void getRow(int y, double[] row)
/*      */   {
/*  480 */     if (y < 0)
/*  481 */       throw new IndexOutOfBoundsException("getRow: y < 0.");
/*  482 */     if (y >= this.ny)
/*  483 */       throw new IndexOutOfBoundsException("getRow: y >= ny.");
/*  484 */     if (row == null) {
/*  485 */       throw new ArrayStoreException("getColumn: row == null.");
/*      */     }
/*  487 */     if (row.length != this.nx) {
/*  488 */       throw new ArrayStoreException("getColumn: row.length != nx.");
/*      */     }
/*  490 */     y *= this.nx;
/*  491 */     for (int i = 0; i < this.nx; ++i)
/*  492 */       row[i] = this.pixels[(y++)];
/*      */   }
/*      */ 
/*      */   public void getRow(int x, int y, double[] row)
/*      */   {
/*  507 */     if (y < 0)
/*  508 */       throw new IndexOutOfBoundsException("getRow: y < 0.");
/*  509 */     if (y >= this.ny)
/*  510 */       throw new IndexOutOfBoundsException("getRow: y >= ny.");
/*  511 */     if (row == null)
/*  512 */       throw new ArrayStoreException("getRow: row == null.");
/*  513 */     int bx = row.length;
/*  514 */     if ((x >= 0) && 
/*  515 */       (x < this.nx - bx - 1)) {
/*  516 */       int index = y * this.nx + x;
/*  517 */       for (int i = 0; i < bx; ++i) {
/*  518 */         row[i] = this.pixels[(index++)];
/*      */       }
/*  520 */       return;
/*      */     }
/*  522 */     int periodx = 2 * this.nx - 2;
/*  523 */     int[] xt = new int[bx];
/*  524 */     for (int k = 0; k < bx; ++k) {
/*  525 */       int xa = x + k;
/*  526 */       for (; xa < 0; xa += periodx);
/*  527 */       while (xa >= this.nx) {
/*  528 */         xa = periodx - xa;
/*  529 */         if (xa >= 0) continue; xa = -xa;
/*      */       }
/*  531 */       xt[k] = xa;
/*      */     }
/*  533 */     int somme = 0;
/*  534 */     int index = y * this.nx;
/*  535 */     for (int i = 0; i < bx; ++i) {
/*  536 */       somme = index + xt[i];
/*  537 */       row[i] = this.pixels[somme];
/*      */     }
/*      */   }
/*      */ 
/*      */   public void getNeighborhood(int x, int y, double[][] neigh)
/*      */   {
/*  572 */     int bx = neigh.length;
/*  573 */     int by = neigh[0].length;
/*  574 */     int bx2 = (bx - 1) / 2;
/*  575 */     int by2 = (by - 1) / 2;
/*  576 */     if ((x >= bx2) && 
/*  577 */       (y >= by2) && 
/*  578 */       (x < this.nx - bx2 - 1) && 
/*  579 */       (y < this.ny - by2 - 1)) {
/*  580 */       int index = (y - by2) * this.nx + (x - bx2);
/*  581 */       for (int j = 0; j < by; ++j) {
/*  582 */         for (int i = 0; i < bx; ++i) {
/*  583 */           neigh[i][j] = this.pixels[(index++)];
/*      */         }
/*  585 */         index += this.nx - bx;
/*      */       }
/*  587 */       return;
/*      */     }
/*  589 */     int[] xt = new int[bx];
/*  590 */     for (int k = 0; k < bx; ++k) {
/*  591 */       int xa = x + k - bx2;
/*  592 */       int periodx = 2 * this.nx - 2;
/*  593 */       while (xa < 0)
/*  594 */         xa += periodx;
/*  595 */       while (xa >= this.nx) {
/*  596 */         xa = periodx - xa;
/*  597 */         if (xa >= 0) continue; xa = -xa;
/*      */       }
/*  599 */       xt[k] = xa;
/*      */     }
/*  601 */     int[] yt = new int[by];
/*  602 */     for (int k = 0; k < by; ++k) {
/*  603 */       int ya = y + k - by2;
/*  604 */       int periody = 2 * this.ny - 2;
/*  605 */       for (; ya < 0; ya += periody);
/*  606 */       while (ya >= this.ny) {
/*  607 */         ya = periody - ya;
/*  608 */         if (ya >= 0) continue; ya = -ya;
/*      */       }
/*  610 */       yt[k] = ya;
/*      */     }
/*  612 */     int sum = 0;
/*  613 */     for (int j = 0; j < by; ++j) {
/*  614 */       int index = yt[j] * this.nx;
/*  615 */       for (int i = 0; i < bx; ++i) {
/*  616 */         sum = index + xt[i];
/*  617 */         neigh[i][j] = this.pixels[sum];
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void getPattern(int x, int y, double[] neigh, int pattern)
/*      */   {
/*  659 */     if (neigh == null)
/*  660 */       throw new ArrayStoreException("getPattern: neigh == null.");
/*  661 */     switch (pattern)
/*      */     {
/*      */     case 0:
/*  663 */       if (neigh.length != 9) {
/*  664 */         throw new ArrayStoreException("getPattern: neigh.length != 9.");
/*      */       }
/*  666 */       getPatternSquare3x3(x, y, neigh);
/*  667 */       break;
/*      */     case 1:
/*  669 */       if (neigh.length != 5) {
/*  670 */         throw new ArrayStoreException("getPattern: neigh.length != 5");
/*      */       }
/*  672 */       getPatternCross3x3(x, y, neigh);
/*  673 */       break;
/*      */     default:
/*  675 */       throw new ArrayStoreException("getPattern: unexpected pattern.");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void getPatternSquare3x3(int x, int y, double[] neigh)
/*      */   {
/*  687 */     if ((x >= 1) && 
/*  688 */       (y >= 1) && 
/*  689 */       (x < this.nx - 1) && 
/*  690 */       (y < this.ny - 1)) {
/*  691 */       int index = (y - 1) * this.nx + (x - 1);
/*  692 */       neigh[0] = this.pixels[(index++)];
/*  693 */       neigh[1] = this.pixels[(index++)];
/*  694 */       neigh[2] = this.pixels[index];
/*  695 */       index += this.nx - 2;
/*  696 */       neigh[3] = this.pixels[(index++)];
/*  697 */       neigh[4] = this.pixels[(index++)];
/*  698 */       neigh[5] = this.pixels[index];
/*  699 */       index += this.nx - 2;
/*  700 */       neigh[6] = this.pixels[(index++)];
/*  701 */       neigh[7] = this.pixels[(index++)];
/*  702 */       neigh[8] = this.pixels[index];
/*  703 */       return;
/*      */     }
/*  705 */     int x1 = x - 1;
/*  706 */     int x2 = x;
/*  707 */     int x3 = x + 1;
/*  708 */     int y1 = y - 1;
/*  709 */     int y2 = y;
/*  710 */     int y3 = y + 1;
/*  711 */     if (x == 0)
/*  712 */       x1 = x3;
/*  713 */     if (y == 0)
/*  714 */       y1 = y3;
/*  715 */     if (x == this.nx - 1)
/*  716 */       x3 = x1;
/*  717 */     if (y == this.ny - 1)
/*  718 */       y3 = y1;
/*  719 */     int offset = y1 * this.nx;
/*  720 */     neigh[0] = this.pixels[(offset + x1)];
/*  721 */     neigh[1] = this.pixels[(offset + x2)];
/*  722 */     neigh[2] = this.pixels[(offset + x3)];
/*  723 */     offset = y2 * this.nx;
/*  724 */     neigh[3] = this.pixels[(offset + x1)];
/*  725 */     neigh[4] = this.pixels[(offset + x2)];
/*  726 */     neigh[5] = this.pixels[(offset + x3)];
/*  727 */     offset = y3 * this.nx;
/*  728 */     neigh[6] = this.pixels[(offset + x1)];
/*  729 */     neigh[7] = this.pixels[(offset + x2)];
/*  730 */     neigh[8] = this.pixels[(offset + x3)];
/*      */   }
/*      */ 
/*      */   private void getPatternCross3x3(int x, int y, double[] neigh)
/*      */   {
/*  742 */     if ((x >= 1) && 
/*  743 */       (y >= 1) && 
/*  744 */       (x < this.nx - 1) && 
/*  745 */       (y < this.ny - 1)) {
/*  746 */       int index = (y - 1) * this.nx + x;
/*  747 */       neigh[0] = this.pixels[index];
/*  748 */       index += this.nx - 1;
/*  749 */       neigh[1] = this.pixels[(index++)];
/*  750 */       neigh[2] = this.pixels[(index++)];
/*  751 */       neigh[3] = this.pixels[index];
/*  752 */       index += this.nx - 1;
/*  753 */       neigh[4] = this.pixels[index];
/*  754 */       return;
/*      */     }
/*  756 */     int x1 = x - 1;
/*  757 */     int x2 = x;
/*  758 */     int x3 = x + 1;
/*  759 */     int y1 = y - 1;
/*  760 */     int y2 = y;
/*  761 */     int y3 = y + 1;
/*  762 */     if (x == 0)
/*  763 */       x1 = x3;
/*  764 */     if (y == 0)
/*  765 */       y1 = y3;
/*  766 */     if (x == this.nx - 1)
/*  767 */       x3 = x1;
/*  768 */     if (y == this.ny - 1)
/*  769 */       y3 = y1;
/*  770 */     int offset = y1 * this.nx;
/*  771 */     neigh[0] = this.pixels[(offset + x2)];
/*  772 */     offset = y2 * this.nx;
/*  773 */     neigh[1] = this.pixels[(offset + x1)];
/*  774 */     neigh[2] = this.pixels[(offset + x2)];
/*  775 */     neigh[3] = this.pixels[(offset + x3)];
/*  776 */     offset = y3 * this.nx;
/*  777 */     neigh[4] = this.pixels[(offset + x2)];
/*      */   }
/*      */ 
/*      */   public void getSubImage(int x, int y, ImageAccess output)
/*      */   {
/*  791 */     if (output == null)
/*  792 */       throw new ArrayStoreException("getSubImage: output == null.");
/*  793 */     if (x < 0)
/*  794 */       throw new ArrayStoreException("getSubImage: Incompatible image size");
/*  795 */     if (y < 0)
/*  796 */       throw new ArrayStoreException("getSubImage: Incompatible image size");
/*  797 */     if (x >= this.nx)
/*  798 */       throw new ArrayStoreException("getSubImage: Incompatible image size");
/*  799 */     if (y >= this.ny)
/*  800 */       throw new ArrayStoreException("getSubImage: Incompatible image size");
/*  801 */     int nxcopy = output.getWidth();
/*  802 */     int nycopy = output.getHeight();
/*  803 */     double[][] neigh = new double[nxcopy][nycopy];
/*  804 */     int nx2 = (nxcopy - 1) / 2;
/*  805 */     int ny2 = (nycopy - 1) / 2;
/*  806 */     getNeighborhood(x + nx2, y + ny2, neigh);
/*  807 */     output.putArrayPixels(neigh);
/*      */   }
/*      */ 
/*      */   public void putPixel(int x, int y, double value)
/*      */   {
/*  820 */     if (x < 0)
/*  821 */       return;
/*  822 */     if (x >= this.nx)
/*  823 */       return;
/*  824 */     if (y < 0)
/*  825 */       return;
/*  826 */     if (y >= this.ny)
/*  827 */       return;
/*  828 */     this.pixels[(x + y * this.nx)] = value;
/*      */   }
/*      */ 
/*      */   public void putColumn(int x, double[] column)
/*      */   {
/*  839 */     if (x < 0)
/*  840 */       throw new IndexOutOfBoundsException("putColumn: x < 0.");
/*  841 */     if (x >= this.nx)
/*  842 */       throw new IndexOutOfBoundsException("putColumn: x >= nx.");
/*  843 */     if (column == null)
/*  844 */       throw new ArrayStoreException("putColumn: column == null.");
/*  845 */     if (column.length != this.ny)
/*  846 */       throw new ArrayStoreException("putColumn: column.length != ny.");
/*  847 */     for (int i = 0; i < this.ny; ++i) {
/*  848 */       this.pixels[x] = column[i];
/*  849 */       x += this.nx;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void putColumn(int x, int y, double[] column)
/*      */   {
/*  863 */     if (x < 0)
/*  864 */       throw new IndexOutOfBoundsException("putColumn: x < 0.");
/*  865 */     if (x >= this.nx)
/*  866 */       throw new IndexOutOfBoundsException("putColumn: x >= nx.");
/*  867 */     if (column == null)
/*  868 */       throw new ArrayStoreException("putColumn: column == null.");
/*  869 */     int by = column.length;
/*  870 */     int index = y * this.nx + x;
/*  871 */     int top = 0;
/*  872 */     int bottom = 0;
/*  873 */     if (y >= 0) {
/*  874 */       if (y < this.ny - by)
/*  875 */         bottom = by;
/*      */       else
/*  877 */         bottom = -y + this.ny;
/*  878 */       for (int i = top; i < bottom; ++i) {
/*  879 */         this.pixels[index] = column[i];
/*  880 */         index += this.nx;
/*      */       }
/*  882 */       return;
/*      */     }
/*      */ 
/*  885 */     index = x;
/*  886 */     top = -y;
/*  887 */     if (y < this.ny - by)
/*  888 */       bottom = by;
/*      */     else
/*  890 */       bottom = -y + this.ny;
/*  891 */     for (int i = top; i < bottom; ++i) {
/*  892 */       this.pixels[index] = column[i];
/*  893 */       index += this.nx;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void putRow(int y, double[] row)
/*      */   {
/*  906 */     if (y < 0)
/*  907 */       throw new IndexOutOfBoundsException("putRow: y < 0.");
/*  908 */     if (y >= this.ny)
/*  909 */       throw new IndexOutOfBoundsException("putRow: y >= ny.");
/*  910 */     if (row == null)
/*  911 */       throw new ArrayStoreException("putRow: row == null.");
/*  912 */     if (row.length != this.nx)
/*  913 */       throw new ArrayStoreException("putRow: row.length != nx.");
/*  914 */     y *= this.nx;
/*  915 */     for (int i = 0; i < this.nx; ++i)
/*  916 */       this.pixels[(y++)] = row[i];
/*      */   }
/*      */ 
/*      */   public void putRow(int x, int y, double[] row)
/*      */   {
/*  931 */     if (y < 0)
/*  932 */       throw new IndexOutOfBoundsException("putRow: y < 0.");
/*  933 */     if (y >= this.ny)
/*  934 */       throw new IndexOutOfBoundsException("putRow: y >= ny.");
/*  935 */     if (row == null)
/*  936 */       throw new ArrayStoreException("putRow: row == null.");
/*  937 */     int bx = row.length;
/*  938 */     int index = y * this.nx + x;
/*  939 */     int left = 0;
/*  940 */     int right = 0;
/*  941 */     if (x >= 0) {
/*  942 */       if (x < this.nx - bx)
/*  943 */         right = bx;
/*      */       else {
/*  945 */         right = -x + this.nx;
/*      */       }
/*  947 */       for (int i = left; i < right; ++i) {
/*  948 */         this.pixels[(index++)] = row[i];
/*      */       }
/*  950 */       return;
/*      */     }
/*      */ 
/*  953 */     index = y * this.nx;
/*  954 */     left = -x;
/*      */ 
/*  956 */     if (x < this.nx - bx)
/*  957 */       right = bx;
/*      */     else {
/*  959 */       right = -x + this.nx;
/*      */     }
/*  961 */     for (int i = left; i < right; ++i)
/*  962 */       this.pixels[(index++)] = row[i];
/*      */   }
/*      */ 
/*      */   public void putArrayPixels(double[][] array)
/*      */   {
/*  974 */     if (array == null)
/*  975 */       throw new IndexOutOfBoundsException("putArrayPixels: array == null.");
/*  976 */     int bx = array.length;
/*  977 */     int by = array[0].length;
/*  978 */     if (bx * by != this.size)
/*  979 */       throw new IndexOutOfBoundsException("putArrayPixels: imcompatible size.");
/*  980 */     int k = 0;
/*  981 */     for (int j = 0; j < by; ++j)
/*  982 */       for (int i = 0; i < bx; ++i)
/*  983 */         this.pixels[(k++)] = array[i][j];
/*      */   }
/*      */ 
/*      */   public void putSubImage(int x, int y, ImageAccess input)
/*      */   {
/*  997 */     if (input == null)
/*  998 */       throw new ArrayStoreException("putSubImage: input == null.");
/*  999 */     if (x < 0)
/* 1000 */       throw new IndexOutOfBoundsException("putSubImage: x < 0.");
/* 1001 */     if (y < 0)
/* 1002 */       throw new IndexOutOfBoundsException("putSubImage: y < 0.");
/* 1003 */     if (x >= this.nx)
/* 1004 */       throw new IndexOutOfBoundsException("putSubImage: x >= nx.");
/* 1005 */     if (y >= this.ny)
/* 1006 */       throw new IndexOutOfBoundsException("putSubImage: y >= ny.");
/* 1007 */     int nxcopy = input.getWidth();
/* 1008 */     int nycopy = input.getHeight();
/*      */ 
/* 1010 */     if (x + nxcopy > this.nx)
/* 1011 */       nxcopy = this.nx - x;
/* 1012 */     if (y + nycopy > this.ny) {
/* 1013 */       nycopy = this.ny - y;
/*      */     }
/* 1015 */     double[] dsrc = input.getPixels();
/* 1016 */     for (int j = 0; j < nycopy; ++j)
/* 1017 */       System.arraycopy(dsrc, j * nxcopy, this.pixels, (j + y) * this.nx + x, nxcopy);
/*      */   }
/*      */ 
/*      */   public void setConstant(double constant)
/*      */   {
/* 1027 */     for (int k = 0; k < this.size; ++k)
/* 1028 */       this.pixels[k] = constant;
/*      */   }
/*      */ 
/*      */   public void normalizeContrast()
/*      */   {
/* 1036 */     double minGoal = 0.0D;
/* 1037 */     double maxGoal = 255.0D;
/*      */ 
/* 1039 */     double minImage = getMinimum();
/* 1040 */     double maxImage = getMaximum();
/*      */     double a;
/* 1043 */     if (minImage - maxImage == 0.0D) {
/* 1044 */       a = 1.0D;
/* 1045 */       minImage = (maxGoal - minGoal) / 2.0D;
/*      */     }
/*      */     else {
/* 1048 */       a = (maxGoal - minGoal) / (maxImage - minImage);
/* 1049 */     }for (int i = 0; i < this.size; ++i)
/* 1050 */       this.pixels[i] = (float)(a * (this.pixels[i] - minImage) + minGoal);
/*      */   }
/*      */ 
/*      */   public void show(String title, Point loc)
/*      */   {
/* 1061 */     FloatProcessor fp = createFloatProcessor();
/* 1062 */     fp.resetMinAndMax();
/* 1063 */     ImagePlus impResult = new ImagePlus(title, fp);
/* 1064 */     impResult.show();
/* 1065 */     ImageWindow window = impResult.getWindow();
/* 1066 */     window.setLocation(loc.x, loc.y);
/* 1067 */     impResult.show();
/*      */   }
/*      */ 
/*      */   public void show(String title)
/*      */   {
/* 1076 */     FloatProcessor fp = createFloatProcessor();
/* 1077 */     fp.resetMinAndMax();
/* 1078 */     ImagePlus impResult = new ImagePlus(title, fp);
/* 1079 */     impResult.show();
/*      */   }
/*      */ 
/*      */   public void abs()
/*      */   {
/* 1086 */     for (int k = 0; k < this.size; ++k)
/* 1087 */       this.pixels[k] = Math.abs(this.pixels[k]);
/*      */   }
/*      */ 
/*      */   public void sqrt()
/*      */   {
/* 1094 */     for (int k = 0; k < this.size; ++k)
/* 1095 */       this.pixels[k] = Math.sqrt(this.pixels[k]);
/*      */   }
/*      */ 
/*      */   public void pow(double a)
/*      */   {
/* 1105 */     for (int k = 0; k < this.size; ++k)
/* 1106 */       this.pixels[k] = Math.pow(this.pixels[k], a);
/*      */   }
/*      */ 
/*      */   public void add(double constant)
/*      */   {
/* 1117 */     for (int k = 0; k < this.size; ++k)
/* 1118 */       this.pixels[k] += constant;
/*      */   }
/*      */ 
/*      */   public void multiply(double constant)
/*      */   {
/* 1128 */     for (int k = 0; k < this.size; ++k)
/* 1129 */       this.pixels[k] *= constant;
/*      */   }
/*      */ 
/*      */   public void subtract(double constant)
/*      */   {
/* 1139 */     for (int k = 0; k < this.size; ++k)
/* 1140 */       this.pixels[k] -= constant;
/*      */   }
/*      */ 
/*      */   public void divide(double constant)
/*      */   {
/* 1150 */     if (constant == 0.0D)
/* 1151 */       throw new ArrayStoreException("divide: Divide by 0");
/* 1152 */     for (int k = 0; k < this.size; ++k)
/* 1153 */       this.pixels[k] /= constant;
/*      */   }
/*      */ 
/*      */   public void add(ImageAccess im1, ImageAccess im2)
/*      */   {
/* 1169 */     if (im1.getWidth() != this.nx)
/* 1170 */       throw new ArrayStoreException("add: incompatible size.");
/* 1171 */     if (im1.getHeight() != this.ny)
/* 1172 */       throw new ArrayStoreException("add: incompatible size.");
/* 1173 */     if (im2.getWidth() != this.nx)
/* 1174 */       throw new ArrayStoreException("add: incompatible size.");
/* 1175 */     if (im2.getHeight() != this.ny)
/* 1176 */       throw new ArrayStoreException("add: incompatible size.");
/* 1177 */     double[] doubleOperand1 = im1.getPixels();
/* 1178 */     double[] doubleOperand2 = im2.getPixels();
/* 1179 */     for (int k = 0; k < this.size; ++k)
/* 1180 */       this.pixels[k] = (doubleOperand1[k] + doubleOperand2[k]);
/*      */   }
/*      */ 
/*      */   public void multiply(ImageAccess im1, ImageAccess im2)
/*      */   {
/* 1197 */     if (im1.getWidth() != this.nx)
/* 1198 */       throw new ArrayStoreException("multiply: incompatible size.");
/* 1199 */     if (im1.getHeight() != this.ny)
/* 1200 */       throw new ArrayStoreException("multiply: incompatible size.");
/* 1201 */     if (im2.getWidth() != this.nx)
/* 1202 */       throw new ArrayStoreException("multiply: incompatible size.");
/* 1203 */     if (im2.getHeight() != this.ny)
/* 1204 */       throw new ArrayStoreException("multiply: incompatible size.");
/* 1205 */     double[] doubleOperand1 = im1.getPixels();
/* 1206 */     double[] doubleOperand2 = im2.getPixels();
/* 1207 */     for (int k = 0; k < this.size; ++k)
/* 1208 */       this.pixels[k] = (doubleOperand1[k] * doubleOperand2[k]);
/*      */   }
/*      */ 
/*      */   public void subtract(ImageAccess im1, ImageAccess im2)
/*      */   {
/* 1225 */     if (im1.getWidth() != this.nx)
/* 1226 */       throw new ArrayStoreException("subtract: incompatible size.");
/* 1227 */     if (im1.getHeight() != this.ny)
/* 1228 */       throw new ArrayStoreException("subtract: incompatible size.");
/* 1229 */     if (im2.getWidth() != this.nx)
/* 1230 */       throw new ArrayStoreException("subtract: incompatible size.");
/* 1231 */     if (im2.getHeight() != this.ny)
/* 1232 */       throw new ArrayStoreException("subtract: incompatible size.");
/* 1233 */     double[] doubleOperand1 = im1.getPixels();
/* 1234 */     double[] doubleOperand2 = im2.getPixels();
/* 1235 */     for (int k = 0; k < this.size; ++k)
/* 1236 */       this.pixels[k] = (doubleOperand1[k] - doubleOperand2[k]);
/*      */   }
/*      */ 
/*      */   public void divide(ImageAccess im1, ImageAccess im2)
/*      */   {
/* 1253 */     if (im1.getWidth() != this.nx)
/* 1254 */       throw new ArrayStoreException("divide: incompatible size.");
/* 1255 */     if (im1.getHeight() != this.ny)
/* 1256 */       throw new ArrayStoreException("divide: incompatible size.");
/* 1257 */     if (im2.getWidth() != this.nx)
/* 1258 */       throw new ArrayStoreException("divide: incompatible size.");
/* 1259 */     if (im2.getHeight() != this.ny)
/* 1260 */       throw new ArrayStoreException("divide: incompatible size.");
/* 1261 */     double[] doubleOperand1 = im1.getPixels();
/* 1262 */     double[] doubleOperand2 = im2.getPixels();
/* 1263 */     for (int k = 0; k < this.size; ++k)
/* 1264 */       this.pixels[k] = (doubleOperand1[k] / doubleOperand2[k]);
/*      */   }
/*      */ }
