package com.k99k.ij.resize;

/*      */ public class Resize
/*      */ {
/*      */   private int interpDegree;
/*      */   private int analyDegree;
/*      */   private int syntheDegree;
/*      */   private double zoomY;
/*      */   private double zoomX;
/*      */   private boolean inversable;
/*      */   private int analyEven;
/*      */   private int corrDegree;
/*      */   private double halfSupport;
/*      */   private double[] splineArrayHeight;
/*      */   private double[] splineArrayWidth;
/*      */   private int[] indexMinHeight;
/*      */   private int[] indexMaxHeight;
/*      */   private int[] indexMinWidth;
/*      */   private int[] indexMaxWidth;
/*      */   private final double tolerance = 1.E-009D;
/*      */ 
/*      */   public Resize()
/*      */   {
/*   25 */     this.analyEven = 0;
/*      */ 
/*   35 */     //this.tolerance = 1.E-009D;
/*      */   }
/*      */ 
/*      */   public void computeZoom(ImageAccess input, ImageAccess output, int analyDegree, int syntheDegree, int interpDegree, double zoomY, double zoomX, double shiftY, double shiftX, boolean inversable)
/*      */   {
/*   54 */     this.interpDegree = interpDegree;
/*   55 */     this.analyDegree = analyDegree;
/*   56 */     this.syntheDegree = syntheDegree;
/*   57 */     this.zoomY = zoomY;
/*   58 */     this.zoomX = zoomX;
/*   59 */     this.inversable = inversable;
/*      */ 
/*   61 */     int nx = input.getWidth();
/*   62 */     int ny = input.getHeight();
/*      */ 
/*   67 */     int[] size = new int[4];
/*      */ 
/*   72 */     int totalDegree = interpDegree + analyDegree + 1;
/*      */ 
/*   74 */     size = calculatefinalsize(inversable, ny, nx, zoomY, zoomX);
/*      */ 
/*   76 */     int workingSizeX = size[1];
/*   77 */     int workingSizeY = size[0];
/*   78 */     int finalSizeX = size[3];
/*   79 */     int finalSizeY = size[2];
/*      */ 
/*   81 */     if ((analyDegree + 1) / 2 * 2 == analyDegree + 1) {
/*   82 */       this.analyEven = 1;
/*      */     }
/*   84 */     double cociente = (analyDegree + 1) / 2.0D;
/*   85 */     double go = analyDegree + 1;
/*   86 */     this.corrDegree = (analyDegree + syntheDegree + 1);
/*   87 */     this.halfSupport = ((totalDegree + 1.0D) / 2.0D);
/*      */ 
/*   89 */     int addBorderHeight = border(finalSizeY, this.corrDegree);
/*   90 */     if (addBorderHeight < totalDegree) {
/*   91 */       addBorderHeight += totalDegree;
/*      */     }
/*      */ 
/*   94 */     int finalTotalHeight = finalSizeY + addBorderHeight;
/*   95 */     int lengthTotalHeight = workingSizeY + (int)Math.ceil(addBorderHeight / zoomY);
/*      */ 
/*   97 */     this.indexMinHeight = new int[finalTotalHeight];
/*   98 */     this.indexMaxHeight = new int[finalTotalHeight];
/*      */ 
/*  100 */     int lengthArraySplnHeight = finalTotalHeight * (2 + totalDegree);
/*  101 */     int i = 0;
/*      */ 
/*  103 */     double factHeight = Math.pow(zoomY, analyDegree + 1);
/*      */ 
/*  105 */     shiftY += ((analyDegree + 1.0D) / 2.0D - Math.floor((analyDegree + 1.0D) / 2.0D)) * (1.0D / zoomY - 1.0D);
/*  106 */     this.splineArrayHeight = new double[lengthArraySplnHeight];
/*      */ 
/*  108 */     for (int l = 0; l < finalTotalHeight; ++l) {
/*  109 */       double affineIndex = l / zoomY + shiftY;
/*  110 */       this.indexMinHeight[l] = (int)Math.ceil(affineIndex - this.halfSupport);
/*  111 */       this.indexMaxHeight[l] = (int)Math.floor(affineIndex + this.halfSupport);
/*  112 */       for (int k = this.indexMinHeight[l]; k <= this.indexMaxHeight[l]; ++k) {
/*  113 */         this.splineArrayHeight[i] = (factHeight * beta(affineIndex - k, totalDegree));
/*  114 */         ++i;
/*      */       }
/*      */     }
/*      */ 
/*  118 */     int addBorderWidth = border(finalSizeX, this.corrDegree);
/*  119 */     if (addBorderWidth < totalDegree) {
/*  120 */       addBorderWidth += totalDegree;
/*      */     }
/*      */ 
/*  123 */     int finalTotalWidth = finalSizeX + addBorderWidth;
/*  124 */     int lengthTotalWidth = workingSizeX + (int)Math.ceil(addBorderWidth / zoomX);
/*      */ 
/*  126 */     this.indexMinWidth = new int[finalTotalWidth];
/*  127 */     this.indexMaxWidth = new int[finalTotalWidth];
/*      */ 
/*  129 */     int lengthArraySplnWidth = finalTotalWidth * (2 + totalDegree);
/*  130 */     i = 0;
/*  131 */     double factWidth = Math.pow(zoomX, analyDegree + 1);
/*      */ 
/*  134 */     shiftX += ((analyDegree + 1.0D) / 2.0D - Math.floor((analyDegree + 1.0D) / 2.0D)) * (1.0D / zoomX - 1.0D);
/*  135 */     this.splineArrayWidth = new double[lengthArraySplnWidth];
/*      */ 
/*  137 */     for (int l = 0; l < finalTotalWidth; ++l) {
/*  138 */       double affineIndex = l / zoomX + shiftX;
/*  139 */       this.indexMinWidth[l] = (int)Math.ceil(affineIndex - this.halfSupport);
/*  140 */       this.indexMaxWidth[l] = (int)Math.floor(affineIndex + this.halfSupport);
/*  141 */       for (int k = this.indexMinWidth[l]; k <= this.indexMaxWidth[l]; ++k) {
/*  142 */         this.splineArrayWidth[i] = (factWidth * beta(affineIndex - k, totalDegree));
/*  143 */         ++i;
/*      */       }
/*      */     }
/*  146 */     double[] outputColumn = new double[finalSizeY];
/*  147 */     double[] outputRow = new double[finalSizeX];
/*  148 */     double[] workingRow = new double[workingSizeX];
/*  149 */     double[] workingColumn = new double[workingSizeY];
/*      */ 
/*  151 */     double[] addVectorHeight = new double[lengthTotalHeight];
/*  152 */     double[] addOutputVectorHeight = new double[finalTotalHeight];
/*  153 */     double[] addVectorWidth = new double[lengthTotalWidth];
/*  154 */     double[] addOutputVectorWidth = new double[finalTotalWidth];
/*      */ 
/*  156 */     int periodColumnSym = 2 * workingSizeY - 2;
/*  157 */     int periodRowSym = 2 * workingSizeX - 2;
/*  158 */     int periodColumnAsym = 2 * workingSizeY - 3;
/*  159 */     int periodRowAsym = 2 * workingSizeX - 3;
/*      */ 
/*  161 */     ImageAccess image = new ImageAccess(finalSizeX, workingSizeY);
/*      */ 
/*  164 */     if (inversable == true)
/*      */     {
/*  166 */       ImageAccess inverImage = new ImageAccess(workingSizeX, workingSizeY);
/*      */ 
/*  168 */       for (int x = 0; x < nx; ++x) {
/*  169 */         for (int y = 0; y < ny; ++y) {
/*  170 */           inverImage.putPixel(x, y, input.getPixel(x, y));
/*      */         }
/*      */       }
/*      */ 
/*  174 */       if (workingSizeX > nx) {
/*  175 */         inverImage.getColumn(nx - 1, workingColumn);
/*  176 */         for (int y = nx; y < workingSizeX; ++y) {
/*  177 */           inverImage.putColumn(y, workingColumn);
/*      */         }
/*      */       }
/*      */ 
/*  181 */       if (workingSizeY > ny) {
/*  182 */         inverImage.getRow(ny - 1, workingRow);
/*  183 */         for (int y = ny; y < workingSizeY; ++y) {
/*  184 */           inverImage.putRow(y, workingRow);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  189 */       for (int y = 0; y < workingSizeY; ++y) {
/*  190 */         inverImage.getRow(y, workingRow);
/*  191 */         getInterpolationCoefficients(workingRow, interpDegree);
/*  192 */         resamplingRow(workingRow, outputRow, addVectorWidth, addOutputVectorWidth, periodRowSym, periodRowAsym);
/*      */ 
/*  194 */         image.putRow(y, outputRow);
/*      */       }
/*      */ 
/*  198 */       for (int y = 0; y < finalSizeX; ++y) {
/*  199 */         image.getColumn(y, workingColumn);
/*  200 */         getInterpolationCoefficients(workingColumn, interpDegree);
/*  201 */         resamplingColumn(workingColumn, outputColumn, addVectorHeight, addOutputVectorHeight, periodColumnSym, periodColumnAsym);
/*      */ 
/*  203 */         output.putColumn(y, outputColumn);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  210 */       for (int y = 0; y < workingSizeY; ++y) {
/*  211 */         input.getRow(y, workingRow);
/*  212 */         getInterpolationCoefficients(workingRow, interpDegree);
/*  213 */         resamplingRow(workingRow, outputRow, addVectorWidth, addOutputVectorWidth, periodRowSym, periodRowAsym);
/*      */ 
/*  215 */         image.putRow(y, outputRow);
/*      */       }
/*      */ 
/*  219 */       for (int y = 0; y < finalSizeX; ++y) {
/*  220 */         image.getColumn(y, workingColumn);
/*  221 */         getInterpolationCoefficients(workingColumn, interpDegree);
/*  222 */         resamplingColumn(workingColumn, outputColumn, addVectorHeight, addOutputVectorHeight, periodColumnSym, periodColumnAsym);
/*      */ 
/*  224 */         output.putColumn(y, outputColumn);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void resamplingRow(double[] inputVector, double[] outputVector, double[] addVector, double[] addOutputVector, int maxSymBoundary, int maxAsymBoundary)
/*      */   {
/*  243 */     int lengthInput = inputVector.length;
/*  244 */     int lengthOutput = outputVector.length;
/*  245 */     int lengthtotal = addVector.length;
/*  246 */     int lengthOutputtotal = addOutputVector.length;
/*      */ 
/*  248 */     double average = 0.0D;
/*      */ 
/*  255 */     if (this.analyDegree != -1) {
/*  256 */       average = doInteg(inputVector, this.analyDegree + 1);
/*      */     }
/*      */ 
/*  259 */     System.arraycopy(inputVector, 0, addVector, 0, lengthInput);
/*      */ 
/*  261 */     for (int l = lengthInput; l < lengthtotal; ++l) {
/*  262 */       if (this.analyEven == 1) {
/*  263 */         int l2 = l;
/*  264 */         if (l >= maxSymBoundary)
/*  265 */           l2 = (int)Math.abs(Math.IEEEremainder(l, maxSymBoundary));
/*  266 */         if (l2 >= lengthInput)
/*  267 */           l2 = maxSymBoundary - l2;
/*  268 */         addVector[l] = inputVector[l2];
/*      */       }
/*      */       else {
/*  271 */         int l2 = l;
/*  272 */         if (l >= maxAsymBoundary)
/*  273 */           l2 = (int)Math.abs(Math.IEEEremainder(l, maxAsymBoundary));
/*  274 */         if (l2 >= lengthInput)
/*  275 */           l2 = maxAsymBoundary - l2;
/*  276 */         addVector[l] = (-inputVector[l2]);
/*      */       }
/*      */     }
/*      */ 
/*  280 */     int i = 0;
/*      */ 
/*  282 */     for (int l = 0; l < lengthOutputtotal; ++l) {
/*  283 */       addOutputVector[l] = 0.0D;
/*  284 */       for (int k = this.indexMinWidth[l]; k <= this.indexMaxWidth[l]; ++k) {
/*  285 */         int index = k;
/*  286 */         double sign = 1.0D;
/*  287 */         if (k < 0) {
/*  288 */           index = -k;
/*  289 */           if (this.analyEven == 0) {
/*  290 */             --index;
/*  291 */             sign = -1.0D;
/*      */           }
/*      */         }
/*  294 */         if (k >= lengthtotal) {
/*  295 */           index = lengthtotal - 1;
/*      */         }
/*  297 */         addOutputVector[l] += sign * addVector[index] * this.splineArrayWidth[i];
/*  298 */         ++i;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  303 */     if (this.analyDegree != -1)
/*      */     {
/*  305 */       doDiff(addOutputVector, this.analyDegree + 1);
/*  306 */       for (i = 0; i < lengthOutputtotal; ++i) {
/*  307 */         addOutputVector[i] += average;
/*      */       }
/*  309 */       getInterpolationCoefficients(addOutputVector, this.corrDegree);
/*      */ 
/*  311 */       getSamples(addOutputVector, this.syntheDegree);
/*      */     }
/*      */ 
/*  314 */     System.arraycopy(addOutputVector, 0, outputVector, 0, lengthOutput);
/*      */   }
/*      */ 
/*      */   private void resamplingColumn(double[] inputVector, double[] outputVector, double[] addVector, double[] addOutputVector, int maxSymBoundary, int maxAsymBoundary)
/*      */   {
/*  333 */     int lengthInput = inputVector.length;
/*  334 */     int lengthOutput = outputVector.length;
/*  335 */     int lengthtotal = addVector.length;
/*  336 */     int lengthOutputtotal = addOutputVector.length;
/*      */ 
/*  338 */     double average = 0.0D;
/*      */ 
/*  345 */     if (this.analyDegree != -1) {
/*  346 */       average = doInteg(inputVector, this.analyDegree + 1);
/*      */     }
/*      */ 
/*  349 */     System.arraycopy(inputVector, 0, addVector, 0, lengthInput);
/*      */ 
/*  351 */     for (int l = lengthInput; l < lengthtotal; ++l) {
/*  352 */       if (this.analyEven == 1) {
/*  353 */         int l2 = l;
/*  354 */         if (l >= maxSymBoundary)
/*  355 */           l2 = (int)Math.abs(Math.IEEEremainder(l, maxSymBoundary));
/*  356 */         if (l2 >= lengthInput)
/*  357 */           l2 = maxSymBoundary - l2;
/*  358 */         addVector[l] = inputVector[l2];
/*      */       }
/*      */       else {
/*  361 */         int l2 = l;
/*  362 */         if (l >= maxAsymBoundary)
/*  363 */           l2 = (int)Math.abs(Math.IEEEremainder(l, maxAsymBoundary));
/*  364 */         if (l2 >= lengthInput)
/*  365 */           l2 = maxAsymBoundary - l2;
/*  366 */         addVector[l] = (-inputVector[l2]);
/*      */       }
/*      */     }
/*      */ 
/*  370 */     int i = 0;
/*      */ 
/*  372 */     for (int l = 0; l < lengthOutputtotal; ++l) {
/*  373 */       addOutputVector[l] = 0.0D;
/*  374 */       for (int k = this.indexMinHeight[l]; k <= this.indexMaxHeight[l]; ++k) {
/*  375 */         int index = k;
/*  376 */         double sign = 1.0D;
/*  377 */         if (k < 0) {
/*  378 */           index = -k;
/*  379 */           if (this.analyEven == 0) {
/*  380 */             --index;
/*  381 */             sign = -1.0D;
/*      */           }
/*      */         }
/*  384 */         if (k >= lengthtotal) {
/*  385 */           index = lengthtotal - 1;
/*      */         }
/*  387 */         addOutputVector[l] += sign * addVector[index] * this.splineArrayHeight[i];
/*  388 */         ++i;
/*      */       }
/*      */     }
/*      */ 
/*  392 */     if (this.analyDegree != -1)
/*      */     {
/*  395 */       doDiff(addOutputVector, this.analyDegree + 1);
/*  396 */       for (i = 0; i < lengthOutputtotal; ++i) {
/*  397 */         addOutputVector[i] += average;
/*      */       }
/*  399 */       getInterpolationCoefficients(addOutputVector, this.corrDegree);
/*      */ 
/*  401 */       getSamples(addOutputVector, this.syntheDegree);
/*      */     }
/*      */ 
/*  404 */     System.arraycopy(addOutputVector, 0, outputVector, 0, lengthOutput);
/*      */   }
/*      */ 
/*      */   private double beta(double x, int degree)
/*      */   {
/*  415 */     double betan = 0.0D;
/*      */     double a;
/*  418 */     switch (degree)
/*      */     {
/*      */     case 0:
/*  420 */       if (Math.abs(x) < 0.5D) {
/*  421 */         betan = 1.0D; return betan;
/*      */       }
/*      */ 
/*  424 */       if (x != -0.5D) return betan;
/*  425 */       betan = 1.0D; break;
/*      */     case 1:
/*  430 */       x = Math.abs(x);
/*  431 */       if (x >= 1.0D) return betan;
/*  432 */       betan = 1.0D - x; break;
/*      */     case 2:
/*  436 */       x = Math.abs(x);
/*  437 */       if (x < 0.5D) {
/*  438 */         betan = 0.75D - x * x; return betan;
/*      */       }
/*      */ 
/*  441 */       if (x >= 1.5D) return betan;
/*  442 */       x -= 1.5D;
/*  443 */       betan = x * x * 0.5D; break;
/*      */     case 3:
/*  448 */       x = Math.abs(x);
/*  449 */       if (x < 1.0D) {
/*  450 */         betan = x * x * (x - 2.0D) * 0.5D + 0.6666666666666666D; return betan;
/*      */       }
/*  452 */       if (x >= 2.0D) return betan;
/*  453 */       x -= 2.0D;
/*  454 */       betan = x * x * x * -0.1666666666666667D; break;
/*      */     case 4:
/*  458 */       x = Math.abs(x);
/*  459 */       if (x < 0.5D) {
/*  460 */         x *= x;
/*  461 */         betan = x * (x * 0.25D - 0.625D) + 0.5989583333333334D; return betan;
/*      */       }
/*  463 */       if (x < 1.5D) {
/*  464 */         betan = x * (x * (x * (0.8333333333333334D - x * 0.1666666666666667D) - 1.25D) + 0.2083333333333333D) + 0.5729166666666666D; return betan;
/*      */       }
/*      */ 
/*  467 */       if (x >= 2.5D) return betan;
/*  468 */       x -= 2.5D;
/*  469 */       x *= x;
/*  470 */       betan = x * x * 0.04166666666666666D; break;
/*      */     case 5:
/*  474 */       x = Math.abs(x);
/*  475 */       if (x < 1.0D) {
/*  476 */         a = x * x;
/*  477 */         betan = a * (a * (0.25D - x * 0.08333333333333333D) - 0.5D) + 0.55D; return betan;
/*      */       }
/*      */ 
/*  480 */       if (x < 2.0D) {
/*  481 */         betan = x * (x * (x * (x * (x * 0.04166666666666666D - 0.375D) + 1.25D) - 1.75D) + 0.625D) + 0.425D; return betan;
/*      */       }
/*      */ 
/*  484 */       if (x >= 3.0D) return betan;
/*  485 */       a = 3.0D - x;
/*  486 */       x = a * a;
/*  487 */       betan = a * x * x * 0.008333333333333333D; break;
/*      */     case 6:
/*  491 */       x = Math.abs(x);
/*  492 */       if (x < 0.5D) {
/*  493 */         x *= x;
/*  494 */         betan = x * (x * (0.1458333333333333D - x * 0.02777777777777778D) - 0.4010416666666667D) + 0.5110243055555556D; return betan;
/*      */       }
/*      */ 
/*  497 */       if (x < 1.5D) {
/*  498 */         betan = x * (x * (x * (x * (x * (x * 0.02083333333333333D - 0.1458333333333333D) + 0.328125D) - 0.1215277777777778D) - 0.35546875D) - 0.009114583333333334D) + 0.5117838541666667D; return betan;
/*      */       }
/*      */ 
/*  502 */       if (x < 2.5D) {
/*  503 */         betan = x * (x * (x * (x * (x * (0.1166666666666667D - x * 0.008333333333333333D) - 0.65625D) + 1.847222222222222D) - 2.5703125D) + 1.319791666666667D) + 0.1795572916666667D; return betan;
/*      */       }
/*      */ 
/*  507 */       if (x >= 3.5D) return betan;
/*  508 */       x -= 3.5D;
/*  509 */       x *= x * x;
/*  510 */       betan = x * x * 0.001388888888888889D; break;
/*      */     case 7:
/*  514 */       x = Math.abs(x);
/*  515 */       if (x < 1.0D) {
/*  516 */         a = x * x;
/*  517 */         betan = a * (a * (a * (x * 0.006944444444444444D - 0.02777777777777778D) + 0.111111111111111D) - 0.3333333333333333D) + 0.4793650793650794D; return betan;
/*      */       }
/*      */ 
/*  520 */       if (x < 2.0D) {
/*  521 */         betan = x * (x * (x * (x * (x * (x * (0.05D - x * 0.004166666666666667D) - 0.2333333333333333D) + 0.5D) - 0.388888888888889D) - 0.1D) - 0.07777777777777778D) + 0.4904761904761905D; return betan;
/*      */       }
/*      */ 
/*  525 */       if (x < 3.0D) {
/*  526 */         betan = x * (x * (x * (x * (x * (x * (x * 0.001388888888888889D - 0.02777777777777778D) + 0.2333333333333333D) - 1.055555555555556D) + 2.722222222222222D) - 3.833333333333334D) + 2.411111111111111D) - 0.2206349206349206D; return betan;
/*      */       }
/*      */ 
/*  530 */       if (x >= 4.0D) return betan;
/*  531 */       a = 4.0D - x;
/*  532 */       x = a * a * a;
/*  533 */       betan = x * x * a * 0.0001984126984126984D;

/*      */     }
/*      */ 
/*  538 */     return betan;
/*      */   }
/*      */ 
/*      */   private double doInteg(double[] c, int nb)
/*      */   {
/*  548 */     int size = c.length;
/*  549 */     double m = 0.0D; double average = 0.0D;
/*      */ 
/*  551 */     switch (nb)
/*      */     {
/*      */     case 1:
/*  553 */       for (int f = 0; f < size; ++f)
/*  554 */         average += c[f];
/*  555 */       average = (2.0D * average - c[(size - 1)] - c[0]) / (2 * size - 2);
/*  556 */       integSA(c, average);
/*  557 */       break;
/*      */     case 2:
/*  559 */       for (int f = 0; f < size; ++f)
/*  560 */         average += c[f];
/*  561 */       average = (2.0D * average - c[(size - 1)] - c[0]) / (2 * size - 2);
/*  562 */       integSA(c, average);
/*  563 */       integAS(c, c);
/*  564 */       break;
/*      */     case 3:
/*  566 */       for (int f = 0; f < size; ++f)
/*  567 */         average += c[f];
/*  568 */       average = (2.0D * average - c[(size - 1)] - c[0]) / (2 * size - 2);
/*  569 */       integSA(c, average);
/*  570 */       integAS(c, c);
/*  571 */       for (int f = 0; f < size; ++f)
/*  572 */         m += c[f];
/*  573 */       m = (2.0D * m - c[(size - 1)] - c[0]) / (2 * size - 2);
/*  574 */       integSA(c, m);
/*  575 */       break;
/*      */     case 4:
/*  577 */       for (int f = 0; f < size; ++f)
/*  578 */         average += c[f];
/*  579 */       average = (2.0D * average - c[(size - 1)] - c[0]) / (2 * size - 2);
/*  580 */       integSA(c, average);
/*  581 */       integAS(c, c);
/*  582 */       for (int f = 0; f < size; ++f)
/*  583 */         m += c[f];
/*  584 */       m = (2.0D * m - c[(size - 1)] - c[0]) / (2 * size - 2);
/*  585 */       integSA(c, m);
/*  586 */       integAS(c, c);
/*      */     }
/*      */ 
/*  589 */     return average;
/*      */   }
/*      */ 
/*      */   private void integSA(double[] c, double m)
/*      */   {
/*  600 */     int size = c.length;
/*  601 */     c[0] = ((c[0] - m) * 0.5D);
/*  602 */     for (int i = 1; i < size; ++i)
/*  603 */       c[i] = (c[i] - m + c[(i - 1)]);
/*      */   }
/*      */ 
/*      */   private void integAS(double[] c, double[] y)
/*      */   {
/*  615 */     int size = c.length;
/*  616 */     double[] z = new double[size];
/*  617 */     System.arraycopy(c, 0, z, 0, size);
/*  618 */     y[0] = z[0];
/*  619 */     y[1] = 0.0D;
/*  620 */     for (int i = 2; i < size; ++i)
/*  621 */       y[i] = (y[(i - 1)] - z[(i - 1)]);
/*      */   }
/*      */ 
/*      */   private void doDiff(double[] c, int nb)
/*      */   {
/*  632 */     int size = c.length;
/*  633 */     switch (nb)
/*      */     {
/*      */     case 1:
/*  635 */       diffAS(c);
/*  636 */       break;
/*      */     case 2:
/*  638 */       diffSA(c);
/*  639 */       diffAS(c);
/*  640 */       break;
/*      */     case 3:
/*  642 */       diffAS(c);
/*  643 */       diffSA(c);
/*  644 */       diffAS(c);
/*  645 */       break;
/*      */     case 4:
/*  647 */       diffSA(c);
/*  648 */       diffAS(c);
/*  649 */       diffSA(c);
/*  650 */       diffAS(c);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void diffSA(double[] c)
/*      */   {
/*  663 */     int size = c.length;
/*  664 */     double old = c[(size - 2)];
/*  665 */     for (int i = 0; i <= size - 2; ++i)
/*  666 */       c[i] -= c[(i + 1)];
/*  667 */     c[(size - 1)] -= old;
/*      */   }
/*      */ 
/*      */   private void diffAS(double[] c)
/*      */   {
/*  678 */     int size = c.length;
/*  679 */     for (int i = size - 1; i > 0; --i)
/*  680 */       c[i] -= c[(i - 1)];
/*  681 */     c[0] = (2.0D * c[0]);
/*      */   }
/*      */ 
/*      */   private int border(int size, int degree)
/*      */   {
/*  694 */     int horizon = size;
/*      */     double z;
/*  696 */     switch (degree)
/*      */     {
/*      */     case 0:
/*      */     case 1:
/*  699 */       return 0;
/*      */     case 2:
/*  701 */       z = Math.sqrt(8.0D) - 3.0D;
/*  702 */       break;
/*      */     case 3:
/*  704 */       z = Math.sqrt(3.0D) - 2.0D;
/*  705 */       break;
/*      */     case 4:
/*  707 */       z = Math.sqrt(664.0D - Math.sqrt(438976.0D)) + Math.sqrt(304.0D) - 19.0D;
/*  708 */       break;
/*      */     case 5:
/*  710 */       z = Math.sqrt(67.5D - Math.sqrt(4436.25D)) + Math.sqrt(26.25D) - 6.5D;
/*      */ 
/*  712 */       break;
/*      */     case 6:
/*  714 */       z = -0.4882945893030448D;
/*  715 */       break;
/*      */     case 7:
/*  717 */       z = -0.5352804307964382D;
/*  718 */       break;
/*      */     default:
/*  720 */       throw new IllegalArgumentException("Invalid interpDegree degree (should be [0..7])");
/*      */     }
/*      */ 
/*  723 */     horizon = 2 + (int)(Math.log(1.E-009D) / Math.log(Math.abs(z)));
/*  724 */     horizon = (horizon < size) ? horizon : size;
/*  725 */     return horizon;
/*      */   }
/*      */ 
/*      */   public static int[] calculatefinalsize(boolean inversable, int height, int width, double zoomY, double zoomX)
/*      */   {
/*  741 */     int[] size = new int[4];
/*      */ 
/*  746 */     size[0] = height;
/*  747 */     size[1] = width;
/*      */ 
/*  749 */     if (inversable == true) {
/*  750 */       int w2 = (int)Math.round(Math.round((size[0] - 1) * zoomY) / zoomY);
/*  751 */       while (size[0] - 1 - w2 != 0) {
/*  752 */         size[0] += 1;
/*  753 */         w2 = (int)Math.round(Math.round((size[0] - 1) * zoomY) / zoomY);
/*      */       }
/*      */ 
/*  756 */       int h2 = (int)Math.round(Math.round((size[1] - 1) * zoomX) / zoomX);
/*  757 */       while (size[1] - 1 - h2 != 0) {
/*  758 */         size[1] += 1;
/*  759 */         h2 = (int)Math.round(Math.round((size[1] - 1) * zoomX) / zoomX);
/*      */       }
/*  761 */       size[2] = ((int)Math.round((size[0] - 1) * zoomY) + 1);
/*  762 */       size[3] = ((int)Math.round((size[1] - 1) * zoomX) + 1);
/*      */     }
/*      */     else {
/*  765 */       size[2] = (int)Math.round(size[0] * zoomY);
/*  766 */       size[3] = (int)Math.round(size[1] * zoomX);
/*      */     }
/*  768 */     return size;
/*      */   }
/*      */ 
/*      */   private void getInterpolationCoefficients(double[] c, int degree)
/*      */   {
/*  774 */     double[] z = new double[0];
/*  775 */     double lambda = 1.0D;
/*      */ 
/*  777 */     switch (degree)
/*      */     {
/*      */     case 0:
/*      */     case 1:
/*  780 */       return;
/*      */     case 2:
/*  782 */       z = new double[1];
/*  783 */       z[0] = (Math.sqrt(8.0D) - 3.0D);
/*  784 */       break;
/*      */     case 3:
/*  786 */       z = new double[1];
/*  787 */       z[0] = (Math.sqrt(3.0D) - 2.0D);
/*  788 */       break;
/*      */     case 4:
/*  790 */       z = new double[2];
/*  791 */       z[0] = (Math.sqrt(664.0D - Math.sqrt(438976.0D)) + Math.sqrt(304.0D) - 19.0D);
/*  792 */       z[1] = (Math.sqrt(664.0D + Math.sqrt(438976.0D)) - Math.sqrt(304.0D) - 19.0D);
/*  793 */       break;
/*      */     case 5:
/*  795 */       z = new double[2];
/*  796 */       z[0] = (Math.sqrt(67.5D - Math.sqrt(4436.25D)) + Math.sqrt(26.25D) - 6.5D);
/*      */ 
/*  798 */       z[1] = (Math.sqrt(67.5D + Math.sqrt(4436.25D)) - Math.sqrt(26.25D) - 6.5D);
/*      */ 
/*  800 */       break;
/*      */     case 6:
/*  802 */       z = new double[3];
/*  803 */       z[0] = -0.4882945893030448D;
/*  804 */       z[1] = -0.08167927107623751D;
/*  805 */       z[2] = -0.001414151808325818D;
/*  806 */       break;
/*      */     case 7:
/*  808 */       z = new double[3];
/*  809 */       z[0] = -0.5352804307964382D;
/*  810 */       z[1] = -0.1225546151923267D;
/*  811 */       z[2] = -0.009148694809608277D;
/*  812 */       break;
/*      */     default:
/*  814 */       throw new IllegalArgumentException("Invalid spline degree (should be [0..7])");
/*      */     }
/*      */ 
/*  817 */     if (c.length == 1) {
/*  818 */       return;
/*      */     }
/*      */ 
/*  821 */     for (int k = 0; k < z.length; ++k) {
/*  822 */       lambda = lambda * (1.0D - z[k]) * (1.0D - 1.0D / z[k]);
/*      */     }
/*      */ 
/*  825 */     for (int n = 0; n < c.length; ++n) {
/*  826 */       c[n] *= lambda;
/*      */     }
/*      */ 
/*  829 */     for (int k = 0; k < z.length; ++k) {
/*  830 */       c[0] = getInitialCausalCoefficient(c, z[k], 1.E-009D);
/*  831 */       for (int n = 1; n < c.length; ++n) {
/*  832 */         c[n] += z[k] * c[(n - 1)];
/*      */       }
/*  834 */       c[(c.length - 1)] = getInitialAntiCausalCoefficient(c, z[k], 1.E-009D);
/*  835 */       for (int n = c.length - 2; 0 <= n; --n)
/*  836 */         c[n] = (z[k] * (c[(n + 1)] - c[n]));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void getSamples(double[] c, int degree)
/*      */   {
/*  844 */     double[] h = new double[0];
/*  845 */     double[] s = new double[c.length];
/*      */ 
/*  847 */     switch (degree)
/*      */     {
/*      */     case 0:
/*      */     case 1:
/*  850 */       return;
/*      */     case 2:
/*  852 */       h = new double[2];
/*  853 */       h[0] = 0.75D;
/*  854 */       h[1] = 0.125D;
/*  855 */       break;
/*      */     case 3:
/*  857 */       h = new double[2];
/*  858 */       h[0] = 0.6666666666666666D;
/*  859 */       h[1] = 0.1666666666666667D;
/*  860 */       break;
/*      */     case 4:
/*  862 */       h = new double[3];
/*  863 */       h[0] = 0.5989583333333334D;
/*  864 */       h[1] = 0.1979166666666667D;
/*  865 */       h[2] = 0.002604166666666667D;
/*  866 */       break;
/*      */     case 5:
/*  868 */       h = new double[3];
/*  869 */       h[0] = 0.55D;
/*  870 */       h[1] = 0.2166666666666667D;
/*  871 */       h[2] = 0.008333333333333333D;
/*  872 */       break;
/*      */     case 6:
/*  874 */       h = new double[4];
/*  875 */       h[0] = 0.5110243055555556D;
/*  876 */       h[1] = 0.2287977430555556D;
/*  877 */       h[2] = 0.01566840277777778D;
/*  878 */       h[3] = 2.170138888888889E-005D;
/*  879 */       break;
/*      */     case 7:
/*  881 */       h = new double[4];
/*  882 */       h[0] = 0.4793650793650794D;
/*  883 */       h[1] = 0.236309523809524D;
/*  884 */       h[2] = 0.02380952380952381D;
/*  885 */       h[3] = 0.0001984126984126984D;
/*  886 */       break;
/*      */     default:
/*  888 */       throw new IllegalArgumentException("Invalid spline degree (should be [0..7])");
/*      */     }
/*      */ 
/*  891 */     symmetricFir(h, c, s);
/*  892 */     System.arraycopy(s, 0, c, 0, s.length);
/*      */   }
/*      */ 
/*      */   private double getInitialAntiCausalCoefficient(double[] c, double z, double tolerance)
/*      */   {
/*  900 */     return (z * c[(c.length - 2)] + c[(c.length - 1)]) * z / (z * z - 1.0D);
/*      */   }
/*      */ 
/*      */   private double getInitialCausalCoefficient(double[] c, double z, double tolerance)
/*      */   {
/*  907 */     double z1 = z; double zn = Math.pow(z, c.length - 1);
/*  908 */     double sum = c[0] + zn * c[(c.length - 1)];
/*  909 */     int horizon = c.length;
/*      */ 
/*  911 */     if (tolerance > 0.0D) {
/*  912 */       horizon = 2 + (int)(Math.log(tolerance) / Math.log(Math.abs(z)));
/*  913 */       horizon = (horizon < c.length) ? horizon : c.length;
/*      */     }
/*  915 */     zn *= zn;
/*  916 */     for (int n = 1; n < horizon - 1; ++n) {
/*  917 */       zn /= z;
/*  918 */       sum += (z1 + zn) * c[n];
/*  919 */       z1 *= z;
/*      */     }
/*  921 */     return sum / (1.0D - Math.pow(z, 2 * c.length - 2));
/*      */   }
/*      */ 
/*      */   private void symmetricFir(double[] h, double[] c, double[] s)
/*      */   {
/*  928 */     if (c.length != s.length) {
/*  929 */       throw new IndexOutOfBoundsException("Incompatible size");
/*      */     }
/*  931 */     switch (h.length)
/*      */     {
/*      */     case 2:
/*  933 */       if (2 <= c.length) {
/*  934 */         s[0] = (h[0] * c[0] + 2.0D * h[1] * c[1]);
/*  935 */         for (int i = 1; i < c.length - 1; ++i) {
/*  936 */           s[i] = (h[0] * c[i] + h[1] * (c[(i - 1)] + c[(i + 1)]));
/*      */         }
/*  938 */         s[(s.length - 1)] = (h[0] * c[(c.length - 1)] + 2.0D * h[1] * c[(c.length - 2)]);
/*      */       }
/*      */       else
/*      */       {
/*  942 */         switch (c.length)
/*      */         {
/*      */         case 1:
/*  944 */           s[0] = ((h[0] + 2.0D * h[1]) * c[0]);
/*  945 */           break;
/*      */         default:
/*  947 */           throw new NegativeArraySizeException("Invalid length of data");
/*      */         }
/*      */       }
				//TODO add break
					break;
/*      */     case 3:
/*  952 */       if (4 <= c.length) {
/*  953 */         s[0] = (h[0] * c[0] + 2.0D * h[1] * c[1] + 2.0D * h[2] * c[2]);
/*  954 */         s[1] = (h[0] * c[1] + h[1] * (c[0] + c[2]) + h[2] * (c[1] + c[3]));
/*  955 */         for (int i = 2; i < c.length - 2; ++i) {
/*  956 */           s[i] = (h[0] * c[i] + h[1] * (c[(i - 1)] + c[(i + 1)]) + h[2] * (c[(i - 2)] + c[(i + 2)]));
/*      */         }
/*      */ 
/*  959 */         s[(s.length - 2)] = (h[0] * c[(c.length - 2)] + h[1] * (c[(c.length - 3)] + c[(c.length - 1)]) + h[2] * (c[(c.length - 4)] + c[(c.length - 2)]));
/*      */ 
/*  962 */         s[(s.length - 1)] = (h[0] * c[(c.length - 1)] + 2.0D * h[1] * c[(c.length - 2)] + 2.0D * h[2] * c[(c.length - 3)]);
/*      */       }
/*      */       else
/*      */       {
/*  966 */         switch (c.length)
/*      */         {
/*      */         case 3:
/*  968 */           s[0] = (h[0] * c[0] + 2.0D * h[1] * c[1] + 2.0D * h[2] * c[2]);
/*  969 */           s[1] = (h[0] * c[1] + h[1] * (c[0] + c[2]) + 2.0D * h[2] * c[1]);
/*  970 */           s[2] = (h[0] * c[2] + 2.0D * h[1] * c[1] + 2.0D * h[2] * c[0]);
/*  971 */           break;
/*      */         case 2:
/*  973 */           s[0] = ((h[0] + 2.0D * h[2]) * c[0] + 2.0D * h[1] * c[1]);
/*  974 */           s[1] = ((h[0] + 2.0D * h[2]) * c[1] + 2.0D * h[1] * c[0]);
/*  975 */           break;
/*      */         case 1:
/*  977 */           s[0] = ((h[0] + 2.0D * (h[1] + h[2])) * c[0]);
/*  978 */           break;
/*      */         default:
/*  980 */           throw new NegativeArraySizeException("Invalid length of data");
/*      */         }
/*      */       }
//TODO add break
break;
/*      */     case 4:
/*  985 */       if (6 <= c.length) {
/*  986 */         s[0] = (h[0] * c[0] + 2.0D * h[1] * c[1] + 2.0D * h[2] * c[2] + 2.0D * h[3] * c[3]);
/*      */ 
/*  988 */         s[1] = (h[0] * c[1] + h[1] * (c[0] + c[2]) + h[2] * (c[1] + c[3]) + h[3] * (c[2] + c[4]));
/*      */ 
/*  990 */         s[2] = (h[0] * c[2] + h[1] * (c[1] + c[3]) + h[2] * (c[0] + c[4]) + h[3] * (c[1] + c[5]));
/*      */ 
/*  992 */         for (int i = 3; i < c.length - 3; ++i) {
/*  993 */           s[i] = (h[0] * c[i] + h[1] * (c[(i - 1)] + c[(i + 1)]) + h[2] * (c[(i - 2)] + c[(i + 2)]) + h[3] * (c[(i - 3)] + c[(i + 3)]));
/*      */         }
/*      */ 
/*  996 */         s[(s.length - 3)] = (h[0] * c[(c.length - 3)] + h[1] * (c[(c.length - 4)] + c[(c.length - 2)]) + h[2] * (c[(c.length - 5)] + c[(c.length - 1)]) + h[3] * (c[(c.length - 6)] + c[(c.length - 2)]));
/*      */ 
/* 1000 */         s[(s.length - 2)] = (h[0] * c[(c.length - 2)] + h[1] * (c[(c.length - 3)] + c[(c.length - 1)]) + h[2] * (c[(c.length - 4)] + c[(c.length - 2)]) + h[3] * (c[(c.length - 5)] + c[(c.length - 3)]));
/*      */ 
/* 1004 */         s[(s.length - 1)] = (h[0] * c[(c.length - 1)] + 2.0D * h[1] * c[(c.length - 2)] + 2.0D * h[2] * c[(c.length - 3)] + 2.0D * h[3] * c[(c.length - 4)]);
/*      */       }
/*      */       else
/*      */       {
/* 1008 */         switch (c.length)
/*      */         {
/*      */         case 5:
/* 1010 */           s[0] = (h[0] * c[0] + 2.0D * h[1] * c[1] + 2.0D * h[2] * c[2] + 2.0D * h[3] * c[3]);
/*      */ 
/* 1012 */           s[1] = (h[0] * c[1] + h[1] * (c[0] + c[2]) + h[2] * (c[1] + c[3]) + h[3] * (c[2] + c[4]));
/*      */ 
/* 1014 */           s[2] = (h[0] * c[2] + (h[1] + h[3]) * (c[1] + c[3]) + h[2] * (c[0] + c[4]));
/*      */ 
/* 1016 */           s[3] = (h[0] * c[3] + h[1] * (c[2] + c[4]) + h[2] * (c[1] + c[3]) + h[3] * (c[0] + c[2]));
/*      */ 
/* 1018 */           s[4] = (h[0] * c[4] + 2.0D * h[1] * c[3] + 2.0D * h[2] * c[2] + 2.0D * h[3] * c[1]);
/*      */ 
/* 1020 */           break;
/*      */         case 4:
/* 1022 */           s[0] = (h[0] * c[0] + 2.0D * h[1] * c[1] + 2.0D * h[2] * c[2] + 2.0D * h[3] * c[3]);
/*      */ 
/* 1024 */           s[1] = (h[0] * c[1] + h[1] * (c[0] + c[2]) + h[2] * (c[1] + c[3]) + 2.0D * h[3] * c[2]);
/*      */ 
/* 1026 */           s[2] = (h[0] * c[2] + h[1] * (c[1] + c[3]) + h[2] * (c[0] + c[2]) + 2.0D * h[3] * c[1]);
/*      */ 
/* 1028 */           s[3] = (h[0] * c[3] + 2.0D * h[1] * c[2] + 2.0D * h[2] * c[1] + 2.0D * h[3] * c[0]);
/*      */ 
/* 1030 */           break;
/*      */         case 3:
/* 1032 */           s[0] = (h[0] * c[0] + 2.0D * (h[1] + h[3]) * c[1] + 2.0D * h[2] * c[2]);
/* 1033 */           s[1] = (h[0] * c[1] + (h[1] + h[3]) * (c[0] + c[2]) + 2.0D * h[2] * c[1]);
/* 1034 */           s[2] = (h[0] * c[2] + 2.0D * (h[1] + h[3]) * c[1] + 2.0D * h[2] * c[0]);
/* 1035 */           break;
/*      */         case 2:
/* 1037 */           s[0] = ((h[0] + 2.0D * h[2]) * c[0] + 2.0D * (h[1] + h[3]) * c[1]);
/* 1038 */           s[1] = ((h[0] + 2.0D * h[2]) * c[1] + 2.0D * (h[1] + h[3]) * c[0]);
/* 1039 */           break;
/*      */         case 1:
/* 1041 */           s[0] = ((h[0] + 2.0D * (h[1] + h[2] + h[3])) * c[0]);
/* 1042 */           break;
/*      */         default:
/* 1044 */           throw new NegativeArraySizeException("Invalid length of data");
/*      */         }
/*      */       }
//TODO add break
break;
				default:
					throw new IllegalArgumentException("Invalid filter half-length (should be [2..4])");
/*      */     }
/*      */ 		
/*      */   }
/*      */ }

