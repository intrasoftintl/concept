var mindplot={};
mindplot.util={};
mindplot.commands={};
mindplot.layout={};
mindplot.layout.boards={};
mindplot.layout.boards.original={};
mindplot.widget={};
mindplot.model={};
mindplot.collaboration={};
mindplot.collaboration.framework={};
mindplot.persistence={};
mindplot.layout={};
Class.Mutators.Static=function(b){this.extend(b)
};mindplot.Events=new Class({$events:{},_removeOn:function(b){return b.replace(/^on([A-Z])/,function(a,d){return d.toLowerCase()
})
},addEvent:function(f,d,e){f=this._removeOn(f);
this.$events[f]=(this.$events[f]||[]).include(d);
if(e){d.internal=true
}return this
},fireEvent:function(g,e,f){g=this._removeOn(g);
var h=this.$events[g];
if(!h){return this
}e=Array.from(e);
_.each(h,function(a){if(f){a.delay(f,this,e)
}else{a.apply(this,e)
}},this);
return this
},removeEvent:function(g,h){g=this._removeOn(g);
var e=this.$events[g];
if(e&&!h.internal){var f=e.indexOf(h);
if(f!=-1){e.splice(f,1)
}}return this
}});Options=new Class({setOptions:function(){var d=this.options=Object.merge.apply(null,[{},this.options].append(arguments));
if(this.addEvent){for(var c in d){if(typeOf(d[c])!="function"||!(/^on[A-Z]/).test(c)){continue
}this.addEvent(c,d[c]);
delete d[c]
}}return this
}});var web2d={};
web2d.peer={svg:{}};
web2d.peer.utils={};web2d.peer.utils.EventUtils={broadcastChangeEvent:function(elementPeer,type){var listeners=elementPeer.getChangeEventListeners(type);
if($defined(listeners)){for(var i=0;
i<listeners.length;
i++){var listener=listeners[i];
listener.call(elementPeer,null)
}}var children=elementPeer.getChildren();
for(var j=0;
j<children.length;
j++){var child=children[j];
web2d.peer.utils.EventUtils.broadcastChangeEvent(child,type)
}}};web2d.peer.utils.TransformUtil={workoutScale:function(elementPeer){var current=elementPeer.getParent();
var width=1;
var height=1;
while(current){var coordSize=current.getCoordSize();
var size=current.getSize();
width=width*(parseInt(size.width)/coordSize.width);
height=height*(parseInt(size.height)/coordSize.height);
current=current.getParent()
}return{width:width,height:height}
}};web2d.peer.svg.ElementPeer=new Class({initialize:function(svgElement){this._native=svgElement;
if(!this._native.addEvent){for(var key in Element){this._native[key]=Element.prototype[key]
}}this._size={width:1,height:1};
this._changeListeners={}
},setChildren:function(children){this._children=children
},getChildren:function(){var result=this._children;
if(!$defined(result)){result=[];
this._children=result
}return result
},getParent:function(){return this._parent
},setParent:function(parent){this._parent=parent
},append:function(elementPeer){elementPeer.setParent(this);
var children=this.getChildren();
children.include(elementPeer);
this._native.appendChild(elementPeer._native);
web2d.peer.utils.EventUtils.broadcastChangeEvent(this,"strokeStyle")
},removeChild:function(elementPeer){elementPeer.setParent(null);
var children=this.getChildren();
var oldLength=children.length;
children.erase(elementPeer);
$assert(children.length<oldLength,"element could not be removed:"+elementPeer);
this._native.removeChild(elementPeer._native)
},addEvent:function(type,listener){$(this._native).bind(type,listener)
},trigger:function(type,event){$(this._native).trigger(type,event)
},cloneEvents:function(from){this._native.cloneEvents(from)
},removeEvent:function(type,listener){$(this._native).unbind(type,listener)
},setSize:function(width,height){if($defined(width)&&this._size.width!=parseInt(width)){this._size.width=parseInt(width);
this._native.setAttribute("width",parseInt(width))
}if($defined(height)&&this._size.height!=parseInt(height)){this._size.height=parseInt(height);
this._native.setAttribute("height",parseInt(height))
}web2d.peer.utils.EventUtils.broadcastChangeEvent(this,"strokeStyle")
},getSize:function(){return{width:this._size.width,height:this._size.height}
},setFill:function(color,opacity){if($defined(color)){this._native.setAttribute("fill",color)
}if($defined(opacity)){this._native.setAttribute("fill-opacity",opacity)
}},getFill:function(){var color=this._native.getAttribute("fill");
var opacity=this._native.getAttribute("fill-opacity");
return{color:color,opacity:Number(opacity)}
},getStroke:function(){var vmlStroke=this._native;
var color=vmlStroke.getAttribute("stroke");
var dashstyle=this._stokeStyle;
var opacity=vmlStroke.getAttribute("stroke-opacity");
var width=vmlStroke.getAttribute("stroke-width");
return{color:color,style:dashstyle,opacity:opacity,width:width}
},setStroke:function(width,style,color,opacity){if($defined(width)){this._native.setAttribute("stroke-width",width+"px")
}if($defined(color)){this._native.setAttribute("stroke",color)
}if($defined(style)){var dashArrayPoints=this.__stokeStyleToStrokDasharray[style];
var scale=1/web2d.peer.utils.TransformUtil.workoutScale(this).width;
var strokeWidth=this._native.getAttribute("stroke-width");
strokeWidth=parseFloat(strokeWidth);
var scaledPoints=[];
for(var i=0;
i<dashArrayPoints.length;
i++){scaledPoints[i]=dashArrayPoints[i]*strokeWidth;
scaledPoints[i]=(scaledPoints[i]*scale)+"px"
}this._stokeStyle=style
}if($defined(opacity)){this._native.setAttribute("stroke-opacity",opacity)
}},setVisibility:function(isVisible){this._native.setAttribute("visibility",(isVisible)?"visible":"hidden")
},isVisible:function(){var visibility=this._native.getAttribute("visibility");
return !(visibility=="hidden")
},updateStrokeStyle:function(){var strokeStyle=this._stokeStyle;
if(this.getParent()){if(strokeStyle&&strokeStyle!="solid"){this.setStroke(null,strokeStyle)
}}},attachChangeEventListener:function(type,listener){var listeners=this.getChangeEventListeners(type);
if(!$defined(listener)){throw"Listener can not be null"
}listeners.push(listener)
},getChangeEventListeners:function(type){var listeners=this._changeListeners[type];
if(!$defined(listeners)){listeners=[];
this._changeListeners[type]=listeners
}return listeners
},moveToFront:function(){this._native.parentNode.appendChild(this._native)
},moveToBack:function(){this._native.parentNode.insertBefore(this._native,this._native.parentNode.firstChild)
},setCursor:function(type){this._native.style.cursor=type
}});
web2d.peer.svg.ElementPeer.prototype.svgNamespace="http://www.w3.org/2000/svg";
web2d.peer.svg.ElementPeer.prototype.linkNamespace="http://www.w3.org/1999/xlink";
web2d.peer.svg.ElementPeer.prototype.__stokeStyleToStrokDasharray={solid:[],dot:[1,3],dash:[4,3],longdash:[10,2],dashdot:[5,3,1,3]};web2d.peer.svg.ElipsePeer=new Class({Extends:web2d.peer.svg.ElementPeer,initialize:function(){var svgElement=window.document.createElementNS(this.svgNamespace,"ellipse");
this.parent(svgElement);
this.attachChangeEventListener("strokeStyle",web2d.peer.svg.ElementPeer.prototype.updateStrokeStyle);
this._position={x:0,y:0}
},setSize:function(width,height){this.parent(width,height);
if($defined(width)){this._native.setAttribute("rx",width/2)
}if($defined(height)){this._native.setAttribute("ry",height/2)
}var pos=this.getPosition();
this.setPosition(pos.x,pos.y)
},setPosition:function(cx,cy){var size=this.getSize();
cx=cx+size.width/2;
cy=cy+size.height/2;
if($defined(cx)){this._native.setAttribute("cx",cx)
}if($defined(cy)){this._native.setAttribute("cy",cy)
}},getPosition:function(){return this._position
}});web2d.peer.svg.Font=new Class({initialize:function(){this._size=10;
this._style="normal";
this._weight="normal"
},init:function(args){if($defined(args.size)){this._size=parseInt(args.size)
}if($defined(args.style)){this._style=args.style
}if($defined(args.weight)){this._weight=args.weight
}},getHtmlSize:function(scale){var result=0;
if(this._size==6){result=this._size*scale.height*43/32
}if(this._size==8){result=this._size*scale.height*42/32
}else{if(this._size==10){result=this._size*scale.height*42/32
}else{if(this._size==15){result=this._size*scale.height*42/32
}}}return result
},getGraphSize:function(){return this._size*43/32
},getSize:function(){return parseInt(this._size)
},getStyle:function(){return this._style
},getWeight:function(){return this._weight
},setSize:function(size){this._size=size
},setStyle:function(style){this._style=style
},setWeight:function(weight){this._weight=weight
},getWidthMargin:function(){var result=0;
if(this._size==10||this._size==6){result=4
}return result
}});web2d.peer.svg.ArialFont=new Class({Extends:web2d.peer.svg.Font,initialize:function(){this.parent();
this._fontFamily="Arial"
},getFontFamily:function(){return this._fontFamily
},getFont:function(){return web2d.Font.ARIAL
}});web2d.peer.svg.PolyLinePeer=new Class({Extends:web2d.peer.svg.ElementPeer,initialize:function(){var svgElement=window.document.createElementNS(this.svgNamespace,"polyline");
this.parent(svgElement);
this.setFill("none");
this.breakDistance=10
},setFrom:function(x1,y1){this._x1=x1;
this._y1=y1;
this._updatePath()
},setTo:function(x2,y2){this._x2=x2;
this._y2=y2;
this._updatePath()
},setStrokeWidth:function(width){this._native.setAttribute("stroke-width",width)
},setColor:function(color){this._native.setAttribute("stroke",color)
},setStyle:function(style){this._style=style;
this._updatePath()
},getStyle:function(){return this._style
},_updatePath:function(){if(this._style=="Curved"){this._updateMiddleCurvePath()
}else{if(this._style=="Straight"){this._updateStraightPath()
}else{this._updateCurvePath()
}}},_updateStraightPath:function(){if($defined(this._x1)&&$defined(this._x2)&&$defined(this._y1)&&$defined(this._y2)){var path=web2d.PolyLine.buildStraightPath(this.breakDistance,this._x1,this._y1,this._x2,this._y2);
this._native.setAttribute("points",path)
}},_updateMiddleCurvePath:function(){var x1=this._x1;
var y1=this._y1;
var x2=this._x2;
var y2=this._y2;
if($defined(x1)&&$defined(x2)&&$defined(y1)&&$defined(y2)){var diff=x2-x1;
var middlex=(diff/2)+x1;
var signx=1;
var signy=1;
if(diff<0){signx=-1
}if(y2<y1){signy=-1
}var path=x1+", "+y1+" "+(middlex-10*signx)+", "+y1+" "+middlex+", "+(y1+10*signy)+" "+middlex+", "+(y2-10*signy)+" "+(middlex+10*signx)+", "+y2+" "+x2+", "+y2;
this._native.setAttribute("points",path)
}},_updateCurvePath:function(){if($defined(this._x1)&&$defined(this._x2)&&$defined(this._y1)&&$defined(this._y2)){var path=web2d.PolyLine.buildCurvedPath(this.breakDistance,this._x1,this._y1,this._x2,this._y2);
this._native.setAttribute("points",path)
}}});web2d.peer.svg.CurvedLinePeer=new Class({Extends:web2d.peer.svg.ElementPeer,initialize:function(){var svgElement=window.document.createElementNS(this.svgNamespace,"path");
this.parent(svgElement);
this._style={fill:"#495879"};
this._updateStyle();
this._customControlPoint_1=false;
this._customControlPoint_2=false;
this._control1=new core.Point();
this._control2=new core.Point();
this._lineStyle=true
},setSrcControlPoint:function(control){this._customControlPoint_1=true;
var change=this._control1.x!=control.x||this._control1.y!=control.y;
if($defined(control.x)){this._control1=control;
this._control1.x=parseInt(this._control1.x);
this._control1.y=parseInt(this._control1.y)
}if(change){this._updatePath()
}},setDestControlPoint:function(control){this._customControlPoint_2=true;
var change=this._control2.x!=control.x||this._control2.y!=control.y;
if($defined(control.x)){this._control2=control;
this._control2.x=parseInt(this._control2.x);
this._control2.y=parseInt(this._control2.y)
}if(change){this._updatePath()
}},isSrcControlPointCustom:function(){return this._customControlPoint_1
},isDestControlPointCustom:function(){return this._customControlPoint_2
},setIsSrcControlPointCustom:function(isCustom){this._customControlPoint_1=isCustom
},setIsDestControlPointCustom:function(isCustom){this._customControlPoint_2=isCustom
},getControlPoints:function(){return[this._control1,this._control2]
},setFrom:function(x1,y1){var change=this._x1!=parseInt(x1)||this._y1!=parseInt(y1);
this._x1=parseInt(x1);
this._y1=parseInt(y1);
if(change){this._updatePath()
}},setTo:function(x2,y2){var change=this._x2!=parseInt(x2)||this._y2!=parseInt(y2);
this._x2=parseInt(x2);
this._y2=parseInt(y2);
if(change){this._updatePath()
}},getFrom:function(){return new core.Point(this._x1,this._y1)
},getTo:function(){return new core.Point(this._x2,this._y2)
},setStrokeWidth:function(width){this._style["stroke-width"]=width;
this._updateStyle()
},setColor:function(color){this._style.stroke=color;
this._style.fill=color;
this._updateStyle()
},updateLine:function(avoidControlPointFix){this._updatePath(avoidControlPointFix)
},setLineStyle:function(style){this._lineStyle=style;
if(this._lineStyle){this._style.fill=this._fill
}else{this._fill=this._style.fill;
this._style.fill="none"
}this._updateStyle();
this.updateLine()
},getLineStyle:function(){return this._lineStyle
},setShowEndArrow:function(visible){this._showEndArrow=visible;
this.updateLine()
},isShowEndArrow:function(){return this._showEndArrow
},setShowStartArrow:function(visible){this._showStartArrow=visible;
this.updateLine()
},isShowStartArrow:function(){return this._showStartArrow
},_updatePath:function(avoidControlPointFix){if($defined(this._x1)&&$defined(this._y1)&&$defined(this._x2)&&$defined(this._y2)){this._calculateAutoControlPoints(avoidControlPointFix);
var path="M"+this._x1+","+this._y1+" C"+(this._control1.x+this._x1)+","+(this._control1.y+this._y1)+" "+(this._control2.x+this._x2)+","+(this._control2.y+this._y2)+" "+this._x2+","+this._y2+(this._lineStyle?" "+(this._control2.x+this._x2)+","+(this._control2.y+this._y2+3)+" "+(this._control1.x+this._x1)+","+(this._control1.y+this._y1+5)+" "+this._x1+","+(this._y1+7)+" Z":"");
this._native.setAttribute("d",path)
}},_updateStyle:function(){var style="";
for(var key in this._style){style+=key+":"+this._style[key]+" "
}this._native.setAttribute("style",style)
},_calculateAutoControlPoints:function(avoidControlPointFix){var defaultpoints=mindplot.util.Shape.calculateDefaultControlPoints(new core.Point(this._x1,this._y1),new core.Point(this._x2,this._y2));
if(!this._customControlPoint_1&&!($defined(avoidControlPointFix)&&avoidControlPointFix==0)){this._control1.x=defaultpoints[0].x;
this._control1.y=defaultpoints[0].y
}if(!this._customControlPoint_2&&!($defined(avoidControlPointFix)&&avoidControlPointFix==1)){this._control2.x=defaultpoints[1].x;
this._control2.y=defaultpoints[1].y
}},setDashed:function(length,spacing){if($defined(length)&&$defined(spacing)){this._native.setAttribute("stroke-dasharray",length+","+spacing)
}else{this._native.setAttribute("stroke-dasharray","")
}}});web2d.peer.svg.ArrowPeer=new Class({Extends:web2d.peer.svg.ElementPeer,initialize:function(){var svgElement=window.document.createElementNS(this.svgNamespace,"path");
this.parent(svgElement);
this._style={};
this._controlPoint=new core.Point();
this._fromPoint=new core.Point()
},setFrom:function(x,y){this._fromPoint.x=x;
this._fromPoint.y=y;
this._redraw()
},setControlPoint:function(point){this._controlPoint=point;
this._redraw()
},setStrokeColor:function(color){this.setStroke(null,null,color,null)
},setStrokeWidth:function(width){this.setStroke(width)
},setDashed:function(isDashed,length,spacing){if($defined(isDashed)&&isDashed&&$defined(length)&&$defined(spacing)){this._native.setAttribute("stroke-dasharray",length+","+spacing)
}else{this._native.setAttribute("stroke-dasharray","")
}},_updateStyle:function(){var style="";
for(var key in this._style){style+=key+":"+this._style[key]+" "
}this._native.setAttribute("style",style)
},_redraw:function(){var x,y,xp,yp;
if($defined(this._fromPoint.x)&&$defined(this._fromPoint.y)&&$defined(this._controlPoint.x)&&$defined(this._controlPoint.y)){if(this._controlPoint.y==0){this._controlPoint.y=1
}var y0=this._controlPoint.y;
var x0=this._controlPoint.x;
var x2=x0+y0;
var y2=y0-x0;
var x3=x0-y0;
var y3=y0+x0;
var m=y2/x2;
var mp=y3/x3;
var l=6;
var pow=Math.pow;
x=(x2==0?0:Math.sqrt(pow(l,2)/(1+pow(m,2))));
x*=Math.sign(x2);
y=(x2==0?l*Math.sign(y2):m*x);
xp=(x3==0?0:Math.sqrt(pow(l,2)/(1+pow(mp,2))));
xp*=Math.sign(x3);
yp=(x3==0?l*Math.sign(y3):mp*xp);
var path="M"+this._fromPoint.x+","+this._fromPoint.y+" L"+(x+this._fromPoint.x)+","+(y+this._fromPoint.y)+"M"+this._fromPoint.x+","+this._fromPoint.y+" L"+(xp+this._fromPoint.x)+","+(yp+this._fromPoint.y);
this._native.setAttribute("d",path)
}}});web2d.peer.svg.TextPeer=new Class({Extends:web2d.peer.svg.ElementPeer,initialize:function(){var svgElement=window.document.createElementNS(this.svgNamespace,"text");
this.parent(svgElement);
this._position={x:0,y:0};
this._font=new web2d.Font("Arial",this)
},append:function(element){this._native.appendChild(element._native)
},setTextAlignment:function(align){this._textAlign=align
},getTextAlignment:function(){return $defined(this._textAlign)?this._textAlign:"left"
},setText:function(text){while(this._native.firstChild){this._native.removeChild(this._native.firstChild)
}this._text=text;
if(text){var lines=text.split("\n");
var me=this;
lines.forEach(function(line){var tspan=window.document.createElementNS(me.svgNamespace,"tspan");
tspan.setAttribute("dy","1em");
tspan.setAttribute("x",me.getPosition().x);
tspan.textContent=line.length==0?" ":line;
me._native.appendChild(tspan)
})
}},getText:function(){return this._text
},setPosition:function(x,y){this._position={x:x,y:y};
this._native.setAttribute("y",y);
this._native.setAttribute("x",x);
$(this._native).children("tspan").attr("x",x)
},getPosition:function(){return this._position
},getNativePosition:function(){return $(this._native).position()
},setFont:function(font,size,style,weight){if($defined(font)){this._font=new web2d.Font(font,this)
}if($defined(style)){this._font.setStyle(style)
}if($defined(weight)){this._font.setWeight(weight)
}if($defined(size)){this._font.setSize(size)
}this._updateFontStyle()
},_updateFontStyle:function(){this._native.setAttribute("font-family",this._font.getFontFamily());
this._native.setAttribute("font-size",this._font.getGraphSize());
this._native.setAttribute("font-style",this._font.getStyle());
this._native.setAttribute("font-weight",this._font.getWeight())
},setColor:function(color){this._native.setAttribute("fill",color)
},getColor:function(){return this._native.getAttribute("fill")
},setTextSize:function(size){this._font.setSize(size);
this._updateFontStyle()
},setContentSize:function(width,height){this._native.xTextSize=width.toFixed(1)+","+height.toFixed(1)
},setStyle:function(style){this._font.setStyle(style);
this._updateFontStyle()
},setWeight:function(weight){this._font.setWeight(weight);
this._updateFontStyle()
},setFontFamily:function(family){var oldFont=this._font;
this._font=new web2d.Font(family,this);
this._font.setSize(oldFont.getSize());
this._font.setStyle(oldFont.getStyle());
this._font.setWeight(oldFont.getWeight());
this._updateFontStyle()
},getFont:function(){return{font:this._font.getFont(),size:parseInt(this._font.getSize()),style:this._font.getStyle(),weight:this._font.getWeight()}
},setSize:function(size){this._font.setSize(size);
this._updateFontStyle()
},getWidth:function(){var computedWidth;
try{computedWidth=this._native.getBBox().width;
if(computedWidth==0){var bbox=this._native.getBBox();
computedWidth=bbox.width
}}catch(e){computedWidth=10
}var width=parseInt(computedWidth);
width=width+this._font.getWidthMargin();
return width
},getHeight:function(){try{var computedHeight=this._native.getBBox().height
}catch(e){computedHeight=10
}return parseInt(computedHeight)
},getHtmlFontSize:function(){return this._font.getHtmlSize()
}});web2d.peer.svg.WorkspacePeer=new Class({Extends:web2d.peer.svg.ElementPeer,initialize:function(element){this._element=element;
var svgElement=window.document.createElementNS(this.svgNamespace,"svg");
this.parent(svgElement);
this._native.setAttribute("focusable","true");
this._native.setAttribute("id","workspace");
this._native.setAttribute("preserveAspectRatio","none")
},setCoordSize:function(width,height){var viewBox=this._native.getAttribute("viewBox");
var coords=[0,0,0,0];
if(viewBox!=null){coords=viewBox.split(/ /)
}if($defined(width)){coords[2]=width
}if($defined(height)){coords[3]=height
}this._native.setAttribute("viewBox",coords.join(" "));
this._native.setAttribute("preserveAspectRatio","none");
web2d.peer.utils.EventUtils.broadcastChangeEvent(this,"strokeStyle")
},getCoordSize:function(){var viewBox=this._native.getAttribute("viewBox");
var coords=[1,1,1,1];
if(viewBox!=null){coords=viewBox.split(/ /)
}return{width:coords[2],height:coords[3]}
},setCoordOrigin:function(x,y){var viewBox=this._native.getAttribute("viewBox");
var coords=[0,0,0,0];
if(viewBox!=null){coords=viewBox.split(/ /)
}if($defined(x)){coords[0]=x
}if($defined(y)){coords[1]=y
}this._native.setAttribute("viewBox",coords.join(" "))
},append:function(child){this.parent(child);
web2d.peer.utils.EventUtils.broadcastChangeEvent(child,"onChangeCoordSize")
},getCoordOrigin:function(child){var viewBox=this._native.getAttribute("viewBox");
var coords=[1,1,1,1];
if(viewBox!=null){coords=viewBox.split(/ /)
}var x=parseFloat(coords[0]);
var y=parseFloat(coords[1]);
return{x:x,y:y}
},getPosition:function(){return{x:0,y:0}
}});web2d.peer.svg.GroupPeer=new Class({Extends:web2d.peer.svg.ElementPeer,initialize:function(){var svgElement=window.document.createElementNS(this.svgNamespace,"g");
this.parent(svgElement);
this._native.setAttribute("preserveAspectRatio","none");
this._coordSize={width:1,height:1};
this._native.setAttribute("focusable","true");
this._position={x:0,y:0};
this._coordOrigin={x:0,y:0}
},setCoordSize:function(width,height){var change=this._coordSize.width!=width||this._coordSize.height!=height;
this._coordSize.width=width;
this._coordSize.height=height;
if(change){this.updateTransform()
}web2d.peer.utils.EventUtils.broadcastChangeEvent(this,"strokeStyle")
},getCoordSize:function(){return{width:this._coordSize.width,height:this._coordSize.height}
},updateTransform:function(){var sx=this._size.width/this._coordSize.width;
var sy=this._size.height/this._coordSize.height;
var cx=this._position.x-this._coordOrigin.x*sx;
var cy=this._position.y-this._coordOrigin.y*sy;
cx=isNaN(cx)?0:cx;
cy=isNaN(cy)?0:cy;
sx=isNaN(sx)?0:sx;
sy=isNaN(sy)?0:sy;
this._native.setAttribute("transform","translate("+cx+","+cy+") scale("+sx+","+sy+")")
},setOpacity:function(value){this._native.setAttribute("opacity",value)
},setCoordOrigin:function(x,y){var change=x!=this._coordOrigin.x||y!=this._coordOrigin.y;
if($defined(x)){this._coordOrigin.x=x
}if($defined(y)){this._coordOrigin.y=y
}if(change){this.updateTransform()
}},setSize:function(width,height){var change=width!=this._size.width||height!=this._size.height;
this.parent(width,height);
if(change){this.updateTransform()
}},setPosition:function(x,y){var change=x!=this._position.x||y!=this._position.y;
if($defined(x)){this._position.x=parseInt(x)
}if($defined(y)){this._position.y=parseInt(y)
}if(change){this.updateTransform()
}},getPosition:function(){return{x:this._position.x,y:this._position.y}
},append:function(child){this.parent(child);
web2d.peer.utils.EventUtils.broadcastChangeEvent(child,"onChangeCoordSize")
},getCoordOrigin:function(){return{x:this._coordOrigin.x,y:this._coordOrigin.y}
}});web2d.peer.svg.RectPeer=new Class({Extends:web2d.peer.svg.ElementPeer,initialize:function(arc){var svgElement=window.document.createElementNS(this.svgNamespace,"rect");
this.parent(svgElement);
this._arc=arc;
this.attachChangeEventListener("strokeStyle",web2d.peer.svg.ElementPeer.prototype.updateStrokeStyle)
},setPosition:function(x,y){if($defined(x)){this._native.setAttribute("x",parseInt(x))
}if($defined(y)){this._native.setAttribute("y",parseInt(y))
}},getPosition:function(){var x=this._native.getAttribute("x");
var y=this._native.getAttribute("y");
return{x:parseInt(x),y:parseInt(y)}
},setSize:function(width,height){this.parent(width,height);
var min=width<height?width:height;
if($defined(this._arc)){var arc=(min/2)*this._arc;
this._native.setAttribute("rx",arc);
this._native.setAttribute("ry",arc)
}}});web2d.peer.svg.ImagePeer=new Class({Extends:web2d.peer.svg.ElementPeer,initialize:function(){var svgElement=window.document.createElementNS(this.svgNamespace,"image");
this.parent(svgElement);
this._position={x:0,y:0};
this._href="";
this._native.setAttribute("preserveAspectRatio","none")
},setPosition:function(x,y){this._position={x:x,y:y};
this._native.setAttribute("y",y);
this._native.setAttribute("x",x)
},getPosition:function(){return this._position
},setHref:function(url){this._native.setAttributeNS(this.linkNamespace,"href",url);
this._href=url
},getHref:function(){return this._href
}});web2d.peer.svg.TimesFont=new Class({Extends:web2d.peer.svg.Font,initialize:function(){this.parent();
this._fontFamily="times"
},getFontFamily:function(){return this._fontFamily
},getFont:function(){return web2d.Font.TIMES
}});web2d.peer.svg.LinePeer=new Class({Extends:web2d.peer.svg.ElementPeer,initialize:function(){var svgElement=window.document.createElementNS(this.svgNamespace,"line");
this.parent(svgElement);
this.attachChangeEventListener("strokeStyle",web2d.peer.svg.ElementPeer.prototype.updateStrokeStyle)
},setFrom:function(x1,y1){this._x1=x1;
this._y1=y1;
this._native.setAttribute("x1",x1);
this._native.setAttribute("y1",y1)
},setTo:function(x2,y2){this._x2=x2;
this._y2=y2;
this._native.setAttribute("x2",x2);
this._native.setAttribute("y2",y2)
},getFrom:function(){return new core.Point(this._x1,this._y1)
},getTo:function(){return new core.Point(this._x2,this._y2)
},setArrowStyle:function(startStyle,endStyle){if($defined(startStyle)){}if($defined(endStyle)){}}});web2d.peer.svg.TahomaFont=new Class({Extends:web2d.peer.svg.Font,initialize:function(){this.parent();
this._fontFamily="tahoma"
},getFontFamily:function(){return this._fontFamily
},getFont:function(){return web2d.Font.TAHOMA
}});web2d.peer.svg.VerdanaFont=new Class({Extends:web2d.peer.svg.Font,initialize:function(){this.parent();
this._fontFamily="verdana"
},getFontFamily:function(){return this._fontFamily
},getFont:function(){return web2d.Font.VERDANA
}});web2d.Element=new Class({initialize:function(peer,attributes){this._peer=peer;
if(peer==null){throw new Error("Element peer can not be null")
}if($defined(attributes)){this._initialize(attributes)
}},_initialize:function(attributes){var batchExecute={};
for(var key in attributes){var funcName=this._attributeNameToFuncName(key,"set");
var funcArgs=batchExecute[funcName];
if(!$defined(funcArgs)){funcArgs=[]
}var signature=web2d.Element._propertyNameToSignature[key];
var argPositions=signature[1];
if(argPositions!=web2d.Element._SIGNATURE_MULTIPLE_ARGUMENTS){funcArgs[argPositions]=attributes[key]
}else{funcArgs=attributes[key].split(" ")
}batchExecute[funcName]=funcArgs
}for(var key in batchExecute){var func=this[key];
if(!$defined(func)){throw new Error("Could not find function: "+key)
}func.apply(this,batchExecute[key])
}},setSize:function(width,height){this._peer.setSize(width,height)
},setPosition:function(cx,cy){this._peer.setPosition(cx,cy)
},addEvent:function(type,listener){this._peer.addEvent(type,listener)
},trigger:function(type,event){this._peer.trigger(type,event)
},cloneEvents:function(from){this._peer.cloneEvents(from)
},removeEvent:function(type,listener){this._peer.removeEvent(type,listener)
},getType:function(){throw new Error("Not implemeneted yet. This method must be implemented by all the inherited objects.")
},getFill:function(){return this._peer.getFill()
},setFill:function(color,opacity){this._peer.setFill(color,opacity)
},getPosition:function(){return this._peer.getPosition()
},getNativePosition:function(){return this._peer.getNativePosition()
},setStroke:function(width,style,color,opacity){if(style!=null&&style!=undefined&&style!="dash"&&style!="dot"&&style!="solid"&&style!="longdash"&&style!="dashdot"){throw new Error("Unsupported stroke style: '"+style+"'")
}this._peer.setStroke(width,style,color,opacity)
},_attributeNameToFuncName:function(attributeKey,prefix){var signature=web2d.Element._propertyNameToSignature[attributeKey];
if(!$defined(signature)){throw"Unsupported attribute: "+attributeKey
}var firstLetter=signature[0].charAt(0);
return prefix+firstLetter.toUpperCase()+signature[0].substring(1)
},setAttribute:function(key,value){var funcName=this._attributeNameToFuncName(key,"set");
var signature=web2d.Element._propertyNameToSignature[key];
if(signature==null){throw"Could not find the signature for:"+key
}var argPositions=signature[1];
var args=[];
if(argPositions!==this._SIGNATURE_MULTIPLE_ARGUMENTS){args[argPositions]=value
}else{if(typeof value=="array"){args=value
}else{var strValue=String(value);
args=strValue.split(" ")
}}var setter=this[funcName];
if(setter==null){throw"Could not find the function name:"+funcName
}setter.apply(this,args)
},getAttribute:function(key){var funcName=this._attributeNameToFuncName(key,"get");
var signature=web2d.Element._propertyNameToSignature[key];
if(signature==null){throw"Could not find the signature for:"+key
}var getter=this[funcName];
if(getter==null){throw"Could not find the function name:"+funcName
}var getterResult=getter.apply(this,[]);
var attibuteName=signature[2];
if(!$defined(attibuteName)){throw"Could not find attribute mapping for:"+key
}var result=getterResult[attibuteName];
if(!$defined(result)){throw"Could not find attribute with name:"+attibuteName
}return result
},setOpacity:function(opacity){this._peer.setStroke(null,null,null,opacity);
this._peer.setFill(null,opacity)
},setVisibility:function(isVisible){this._peer.setVisibility(isVisible)
},isVisible:function(){return this._peer.isVisible()
},moveToFront:function(){this._peer.moveToFront()
},moveToBack:function(){this._peer.moveToBack()
},getStroke:function(){return this._peer.getStroke()
},setCursor:function(type){this._peer.setCursor(type)
},getParent:function(){return this._peer.getParent()
}});
web2d.Element._SIGNATURE_MULTIPLE_ARGUMENTS=-1;
web2d.Element._supportedEvents=["click","dblclick","mousemove","mouseout","mouseover","mousedown","mouseup"];
web2d.Element._propertyNameToSignature={size:["size",-1],width:["size",0,"width"],height:["size",1,"height"],position:["position",-1],x:["position",0,"x"],y:["position",1,"y"],stroke:["stroke",-1],strokeWidth:["stroke",0,"width"],strokeStyle:["stroke",1,"style"],strokeColor:["stroke",2,"color"],strokeOpacity:["stroke",3,"opacity"],fill:["fill",-1],fillColor:["fill",0,"color"],fillOpacity:["fill",1,"opacity"],coordSize:["coordSize",-1],coordSizeWidth:["coordSize",0,"width"],coordSizeHeight:["coordSize",1,"height"],coordOrigin:["coordOrigin",-1],coordOriginX:["coordOrigin",0,"x"],coordOriginY:["coordOrigin",1,"y"],visibility:["visibility",0],opacity:["opacity",0]};web2d.Elipse=new Class({Extends:web2d.Element,initialize:function(attributes){var peer=web2d.peer.Toolkit.createElipse();
var defaultAttributes={width:40,height:40,x:5,y:5,stroke:"1 solid black",fillColor:"blue"};
for(var key in attributes){defaultAttributes[key]=attributes[key]
}this.parent(peer,defaultAttributes)
},getType:function(){return"Elipse"
},getSize:function(){return this._peer.getSize()
}});web2d.Font=new Class({initialize:function(fontFamily,textPeer){var font="web2d.peer.Toolkit.create"+fontFamily+"Font();";
this._peer=eval(font);
this._textPeer=textPeer
},getHtmlSize:function(){var scale=web2d.peer.utils.TransformUtil.workoutScale(this._textPeer);
return this._peer.getHtmlSize(scale)
},getGraphSize:function(){var scale=web2d.peer.utils.TransformUtil.workoutScale(this._textPeer);
return this._peer.getGraphSize(scale)
},getFontScale:function(){return web2d.peer.utils.TransformUtil.workoutScale(this._textPeer).height
},getSize:function(){return this._peer.getSize()
},getStyle:function(){return this._peer.getStyle()
},getWeight:function(){return this._peer.getWeight()
},getFontFamily:function(){return this._peer.getFontFamily()
},setSize:function(size){return this._peer.setSize(size)
},setStyle:function(style){return this._peer.setStyle(style)
},setWeight:function(weight){return this._peer.setWeight(weight)
},getFont:function(){return this._peer.getFont()
},getWidthMargin:function(){return this._peer.getWidthMargin()
}});
web2d.Font.ARIAL="Arial";
web2d.Font.TIMES="Times";
web2d.Font.TAHOMA="Tahoma";
web2d.Font.VERDANA="Verdana";web2d.Group=new Class({Extends:web2d.Element,initialize:function(attributes){var peer=web2d.peer.Toolkit.createGroup();
var defaultAttributes={width:50,height:50,x:50,y:50,coordOrigin:"0 0",coordSize:"50 50"};
for(var key in attributes){defaultAttributes[key]=attributes[key]
}this.parent(peer,defaultAttributes)
},removeChild:function(element){if(!$defined(element)){throw"Child element can not be null"
}if(element==this){throw"It's not possible to add the group as a child of itself"
}var elementType=element.getType();
if(elementType==null){throw"It seems not to be an element ->"+element
}this._peer.removeChild(element._peer)
},append:function(element){if(!$defined(element)){throw"Child element can not be null"
}if(element==this){throw"It's not posible to add the group as a child of itself"
}var elementType=element.getType();
if(elementType==null){throw"It seems not to be an element ->"+element
}if(elementType=="Workspace"){throw"A group can not have a workspace as a child"
}this._peer.append(element._peer)
},getType:function(){return"Group"
},setCoordSize:function(width,height){this._peer.setCoordSize(width,height)
},setCoordOrigin:function(x,y){this._peer.setCoordOrigin(x,y)
},getCoordOrigin:function(){return this._peer.getCoordOrigin()
},getSize:function(){return this._peer.getSize()
},setFill:function(color,opacity){throw"Unsupported operation. Fill can not be set to a group"
},setStroke:function(width,style,color,opacity){throw"Unsupported operation. Stroke can not be set to a group"
},getCoordSize:function(){return this._peer.getCoordSize()
},appendDomChild:function(DomElement){if(!$defined(DomElement)){throw"Child element can not be null"
}if(DomElement==this){throw"It's not possible to add the group as a child of itself"
}this._peer._native.append(DomElement)
},setOpacity:function(value){this._peer.setOpacity(value)
}});web2d.Image=new Class({Extends:web2d.Element,initialize:function(attributes){var peer=web2d.peer.Toolkit.createImage();
this.parent(peer,attributes)
},getType:function(){return"Image"
},setHref:function(href){this._peer.setHref(href)
},getHref:function(){return this._peer.getHref()
},getSize:function(){return this._peer.getSize()
}});web2d.Line=new Class({Extends:web2d.Element,initialize:function(attributes){var peer=web2d.peer.Toolkit.createLine();
var defaultAttributes={strokeColor:"#495879",strokeWidth:1,strokeOpacity:1};
for(var key in attributes){defaultAttributes[key]=attributes[key]
}this.parent(peer,defaultAttributes)
},getType:function(){return"Line"
},setFrom:function(x,y){this._peer.setFrom(x,y)
},setTo:function(x,y){this._peer.setTo(x,y)
},getFrom:function(){return this._peer.getFrom()
},getTo:function(){return this._peer.getTo()
},setArrowStyle:function(startStyle,endStyle){this._peer.setArrowStyle(startStyle,endStyle)
},setPosition:function(cx,cy){throw"Unsupported operation"
},setSize:function(width,height){throw"Unsupported operation"
},setFill:function(color,opacity){throw"Unsupported operation"
}});web2d.PolyLine=new Class({Extends:web2d.Element,initialize:function(attributes){var peer=web2d.peer.Toolkit.createPolyLine();
var defaultAttributes={strokeColor:"blue",strokeWidth:1,strokeStyle:"solid",strokeOpacity:1};
for(var key in attributes){defaultAttributes[key]=attributes[key]
}this.parent(peer,defaultAttributes)
},getType:function(){return"PolyLine"
},setFrom:function(x,y){this._peer.setFrom(x,y)
},setTo:function(x,y){this._peer.setTo(x,y)
},setStyle:function(style){this._peer.setStyle(style)
},getStyle:function(){return this._peer.getStyle()
},buildCurvedPath:function(dist,x1,y1,x2,y2){var signx=1;
var signy=1;
if(x2<x1){signx=-1
}if(y2<y1){signy=-1
}var path;
if(Math.abs(y1-y2)>2){var middlex=x1+((x2-x1>0)?dist:-dist);
path=x1.toFixed(1)+", "+y1.toFixed(1)+" "+middlex.toFixed(1)+", "+y1.toFixed(1)+" "+middlex.toFixed(1)+", "+(y2-5*signy).toFixed(1)+" "+(middlex+5*signx).toFixed(1)+", "+y2.toFixed(1)+" "+x2.toFixed(1)+", "+y2.toFixed(1)
}else{path=x1.toFixed(1)+", "+y1.toFixed(1)+" "+x2.toFixed(1)+", "+y2.toFixed(1)
}return path
},buildStraightPath:function(dist,x1,y1,x2,y2){var middlex=x1+((x2-x1>0)?dist:-dist);
return x1+", "+y1+" "+middlex+", "+y1+" "+middlex+", "+y2+" "+x2+", "+y2
}});web2d.CurvedLine=new Class({Extends:web2d.Element,initialize:function(attributes){var peer=web2d.peer.Toolkit.createCurvedLine();
var defaultAttributes={strokeColor:"blue",strokeWidth:1,strokeStyle:"solid",strokeOpacity:1};
for(var key in attributes){defaultAttributes[key]=attributes[key]
}this.parent(peer,defaultAttributes)
},getType:function(){return"CurvedLine"
},setFrom:function(x,y){$assert(!isNaN(x),"x must be defined");
$assert(!isNaN(y),"y must be defined");
this._peer.setFrom(x,y)
},setTo:function(x,y){$assert(!isNaN(x),"x must be defined");
$assert(!isNaN(y),"y must be defined");
this._peer.setTo(x,y)
},getFrom:function(){return this._peer.getFrom()
},getTo:function(){return this._peer.getTo()
},setShowEndArrow:function(visible){this._peer.setShowEndArrow(visible)
},isShowEndArrow:function(){return this._peer.isShowEndArrow()
},setShowStartArrow:function(visible){this._peer.setShowStartArrow(visible)
},isShowStartArrow:function(){return this._peer.isShowStartArrow()
},setSrcControlPoint:function(control){this._peer.setSrcControlPoint(control)
},setDestControlPoint:function(control){this._peer.setDestControlPoint(control)
},getControlPoints:function(){return this._peer.getControlPoints()
},isSrcControlPointCustom:function(){return this._peer.isSrcControlPointCustom()
},isDestControlPointCustom:function(){return this._peer.isDestControlPointCustom()
},setIsSrcControlPointCustom:function(isCustom){this._peer.setIsSrcControlPointCustom(isCustom)
},setIsDestControlPointCustom:function(isCustom){this._peer.setIsDestControlPointCustom(isCustom)
},updateLine:function(avoidControlPointFix){return this._peer.updateLine(avoidControlPointFix)
},setStyle:function(style){this._peer.setLineStyle(style)
},getStyle:function(){return this._peer.getLineStyle()
},setDashed:function(length,spacing){this._peer.setDashed(length,spacing)
}});
web2d.CurvedLine.SIMPLE_LINE=false;
web2d.CurvedLine.NICE_LINE=true;web2d.Arrow=new Class({Extends:web2d.Element,initialize:function(attributes){var peer=web2d.peer.Toolkit.createArrow();
var defaultAttributes={strokeColor:"black",strokeWidth:1,strokeStyle:"solid",strokeOpacity:1};
for(var key in attributes){defaultAttributes[key]=attributes[key]
}this.parent(peer,defaultAttributes)
},getType:function(){return"Arrow"
},setFrom:function(x,y){this._peer.setFrom(x,y)
},setControlPoint:function(point){this._peer.setControlPoint(point)
},setStrokeColor:function(color){this._peer.setStrokeColor(color)
},setStrokeWidth:function(width){this._peer.setStrokeWidth(width)
},setDashed:function(isDashed,length,spacing){this._peer.setDashed(isDashed,length,spacing)
}});web2d.Rect=new Class({Extends:web2d.Element,initialize:function(arc,attributes){if(arc&&arc>1){throw"Arc must be 0<=arc<=1"
}if(arguments.length<=0){var rx=0;
var ry=0
}var peer=web2d.peer.Toolkit.createRect(arc);
var defaultAttributes={width:40,height:40,x:5,y:5,stroke:"1 solid black",fillColor:"green"};
for(var key in attributes){defaultAttributes[key]=attributes[key]
}this.parent(peer,defaultAttributes)
},getType:function(){return"Rect"
},getSize:function(){return this._peer.getSize()
}});web2d.Text=new Class({Extends:web2d.Element,initialize:function(attributes){var peer=web2d.peer.Toolkit.createText();
this.parent(peer,attributes)
},getType:function(){return"Text"
},setText:function(text){this._peer.setText(text)
},setTextAlignment:function(align){$assert(align,"align can not be null");
this._peer.setTextAlignment(align)
},setTextSize:function(width,height){this._peer.setContentSize(width,height)
},getText:function(){return this._peer.getText()
},setFont:function(font,size,style,weight){this._peer.setFont(font,size,style,weight)
},setColor:function(color){this._peer.setColor(color)
},getColor:function(){return this._peer.getColor()
},setStyle:function(style){this._peer.setStyle(style)
},setWeight:function(weight){this._peer.setWeight(weight)
},setFontFamily:function(family){this._peer.setFontFamily(family)
},getFont:function(){return this._peer.getFont()
},setSize:function(size){this._peer.setSize(size)
},getHtmlFontSize:function(){return this._peer.getHtmlFontSize()
},getWidth:function(){return this._peer.getWidth()
},getHeight:function(){return parseInt(this._peer.getHeight())
},getFontHeight:function(){var lines=this._peer.getText().split("\n").length;
return Math.round(this.getHeight()/lines)
}});web2d.peer.ToolkitSVG={init:function(){},createWorkspace:function(element){return new web2d.peer.svg.WorkspacePeer(element)
},createGroup:function(element){return new web2d.peer.svg.GroupPeer()
},createElipse:function(){return new web2d.peer.svg.ElipsePeer()
},createLine:function(){return new web2d.peer.svg.LinePeer()
},createPolyLine:function(){return new web2d.peer.svg.PolyLinePeer()
},createCurvedLine:function(){return new web2d.peer.svg.CurvedLinePeer()
},createArrow:function(){return new web2d.peer.svg.ArrowPeer()
},createText:function(){return new web2d.peer.svg.TextPeer()
},createImage:function(){return new web2d.peer.svg.ImagePeer()
},createRect:function(arc){return new web2d.peer.svg.RectPeer(arc)
},createArialFont:function(){return new web2d.peer.svg.ArialFont()
},createTimesFont:function(){return new web2d.peer.svg.TimesFont()
},createVerdanaFont:function(){return new web2d.peer.svg.VerdanaFont()
},createTahomaFont:function(){return new web2d.peer.svg.TahomaFont()
}};
web2d.peer.Toolkit=web2d.peer.ToolkitSVG;web2d.Workspace=new Class({Extends:web2d.Element,initialize:function(attributes){this._htmlContainer=this._createDivContainer();
var peer=web2d.peer.Toolkit.createWorkspace(this._htmlContainer);
var defaultAttributes={width:"200px",height:"200px",stroke:"1px solid #edf1be",fillColor:"white",coordOrigin:"0 0",coordSize:"200 200"};
for(var key in attributes){defaultAttributes[key]=attributes[key]
}this.parent(peer,defaultAttributes);
this._htmlContainer.append(this._peer._native)
},getType:function(){return"Workspace"
},append:function(element){if(!$defined(element)){throw"Child element can not be null"
}var elementType=element.getType();
if(elementType==null){throw"It seems not to be an element ->"+element
}if(elementType=="Workspace"){throw"A workspace can not have a workspace as a child"
}this._peer.append(element._peer)
},addItAsChildTo:function(element){if(!$defined(element)){throw"Workspace div container can not be null"
}element.append(this._htmlContainer)
},_createDivContainer:function(){var container=window.document.createElement("div");
container.id="workspaceContainer";
container.style.position="relative";
container.style.top="0px";
container.style.left="0px";
container.style.height="688px";
container.style.border="1px solid red";
return $(container)
},setSize:function(width,height){if($defined(width)){this._htmlContainer.css("width",width)
}if($defined(height)){this._htmlContainer.css("height",height)
}this._peer.setSize(width,height)
},setCoordSize:function(width,height){this._peer.setCoordSize(width,height)
},setCoordOrigin:function(x,y){this._peer.setCoordOrigin(x,y)
},getCoordOrigin:function(){return this._peer.getCoordOrigin()
},_getHtmlContainer:function(){return this._htmlContainer
},setFill:function(color,opacity){this._htmlContainer.css("background-color",color);
if(opacity||opacity===0){throw"Unsupported operation. Opacity not supported."
}},getFill:function(){var color=this._htmlContainer.css("background-color");
return{color:color}
},getSize:function(){var width=this._htmlContainer.css("width");
var height=this._htmlContainer.css("height");
return{width:width,height:height}
},setStroke:function(width,style,color,opacity){if(style!="solid"){throw"Not supported style stroke style:"+style
}this._htmlContainer.css("border",width+" "+style+" "+color);
if(opacity||opacity===0){throw"Unsupported operation. Opacity not supported."
}},getCoordSize:function(){return this._peer.getCoordSize()
},removeChild:function(element){if(!$defined(element)){throw"Child element can not be null"
}if(element==this){throw"It's not possible to add the group as a child of itself"
}var elementType=element.getType();
if(elementType==null){throw"It seems not to be an element ->"+element
}this._peer.removeChild(element._peer)
},dumpNativeChart:function(){var elem=this._htmlContainer;
return elem.innerHTML
}});core.Point=new Class({initialize:function(x,y){this.x=x;
this.y=y
},setValue:function(x,y){this.x=x;
this.y=y
},inspect:function(){return"{x:"+this.x+",y:"+this.y+"}"
},clone:function(){return new core.Point(this.x,this.y)
}});
core.Point.fromString=function(point){var values=point.split(",");
return new core.Point(values[0],values[1])
};mindplot.Messages=new Class({Static:{init:function(d){d=$defined(d)?d:"en";
var c=mindplot.Messages.BUNDLES[d];
if(c==null&&d.indexOf("_")!=-1){d=d.substring(0,d.indexOf("_"));
c=mindplot.Messages.BUNDLES[d]
}mindplot.Messages.__bundle=c
}}});
$msg=function(d){if(!mindplot.Messages.__bundle){mindplot.Messages.init("en")
}var c=mindplot.Messages.__bundle[d];
return c?c:d
};
mindplot.Messages.BUNDLES={};mindplot.TopicEventDispatcher=new Class({Extends:mindplot.Events,Static:{_instance:null,configure:function(b){this._instance=new mindplot.TopicEventDispatcher(b)
},getInstance:function(){return this._instance
}},initialize:function(b){this._readOnly=b;
this._activeEditor=null;
this._multilineEditor=new mindplot.MultilineTextEditor()
},close:function(b){if(this.isVisible()){this._activeEditor.close(b);
this._activeEditor=null
}},show:function(c,d){this.process(mindplot.TopicEvent.EDIT,c,d)
},process:function(g,h,e){$assert(g,"eventType can not be null");
if(this.isVisible()){this.close()
}var f=h.getModel();
if(f.getShapeType()!=mindplot.model.TopicShape.IMAGE&&!this._readOnly&&g==mindplot.TopicEvent.EDIT){this._multilineEditor.show(h,e?e.text:null);
this._activeEditor=this._multilineEditor
}else{this.fireEvent(g,{model:f,readOnly:this._readOnly})
}},isVisible:function(){return this._activeEditor!=null&&this._activeEditor.isVisible()
}});
mindplot.TopicEvent={EDIT:"editnode",CLICK:"clicknode"};mindplot.model.IMindmap=new Class({initialize:function(){throw"Unsupported operation"
},getCentralTopic:function(){return this.getBranches()[0]
},getDescription:function(){throw"Unsupported operation"
},setDescription:function(b){throw"Unsupported operation"
},getId:function(){throw"Unsupported operation"
},setId:function(b){throw"Unsupported operation"
},getVersion:function(){throw"Unsupported operation"
},setVersion:function(b){throw"Unsupported operation"
},addBranch:function(b){throw"Unsupported operation"
},getBranches:function(){throw"Unsupported operation"
},removeBranch:function(b){throw"Unsupported operation"
},getRelationships:function(){throw"Unsupported operation"
},connect:function(d,c){$assert(!c.getParent(),"Child model seems to be already connected");
d.append(c);
this.removeBranch(c)
},disconnect:function(c){var d=c.getParent();
$assert(c,"Child can not be null.");
$assert(d,"Child model seems to be already connected");
d.removeChild(c);
this.addBranch(c)
},hasAlreadyAdded:function(b){throw"Unsupported operation"
},createNode:function(d,c){throw"Unsupported operation"
},createRelationship:function(c,d){throw"Unsupported operation"
},addRelationship:function(b){throw"Unsupported operation"
},deleteRelationship:function(b){throw"Unsupported operation"
},inspect:function(){var f="";
f="{ ";
var e=this.getBranches();
f=f+"version:"+this.getVersion();
f=f+" , [";
for(var h=0;
h<e.length;
h++){var g=e[h];
if(h!=0){f=f+",\n "
}f=f+"("+h+") =>"+g.inspect()
}f=f+"]";
f=f+" } ";
return f
},copyTo:function(i){var j=this;
var g=j.getVersion();
i.setVersion(g);
var h=this.getDescription();
i.setDescription(h);
var f=j.getBranches();
_.each(f,function(b){var a=i.createNode(b.getType(),b.getId());
b.copyTo(a);
i.addBranch(a)
})
}});mindplot.model.Mindmap=new Class({Extends:mindplot.model.IMindmap,initialize:function(c,d){$assert(c,"Id can not be null");
this._branches=[];
this._description=null;
this._relationships=[];
this._version=$defined(d)?d:mindplot.persistence.ModelCodeName.TANGO;
this._id=c
},getDescription:function(){return this._description
},setDescription:function(b){this._description=b
},getId:function(){return this._id
},setId:function(b){this._id=b
},getVersion:function(){return this._version
},setVersion:function(b){this._version=b
},addBranch:function(c){$assert(c&&c.isNodeModel(),"Add node must be invoked with model objects");
var d=this.getBranches();
if(d.length==0){$assert(c.getType()==mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE,"First element must be the central topic");
c.setPosition(0,0)
}else{$assert(c.getType()!=mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE,"Mindmaps only have one cental topic")
}this._branches.push(c)
},removeBranch:function(b){$assert(b&&b.isNodeModel(),"Remove node must be invoked with model objects");
return this._branches.erase(b)
},getBranches:function(){return this._branches
},getRelationships:function(){return this._relationships
},hasAlreadyAdded:function(g){var f=false;
var e=this._branches;
for(var h=0;
h<e.length;
h++){f=e[h]._isChildNode(g);
if(f){break
}}},createNode:function(d,c){d=!$defined(d)?mindplot.model.INodeModel.MAIN_TOPIC_TYPE:d;
return new mindplot.model.NodeModel(d,this,c)
},createRelationship:function(d,c){$assert($defined(d),"from node cannot be null");
$assert($defined(c),"to node cannot be null");
return new mindplot.model.RelationshipModel(d,c)
},addRelationship:function(b){this._relationships.push(b)
},deleteRelationship:function(b){this._relationships.erase(b)
},findNodeById:function(g){var f=null;
for(var e=0;
e<this._branches.length;
e++){var h=this._branches[e];
f=h.findNodeById(g);
if(f){break
}}return f
}});
mindplot.model.Mindmap.buildEmpty=function(d){var e=new mindplot.model.Mindmap(d);
var f=e.createNode(mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE,0);
e.addBranch(f);
return e
};mindplot.model.INodeModel=new Class({initialize:function(b){$assert(b&&b.getBranches,"mindmap can not be null");
this._mindmap=b
},getId:function(){return this.getProperty("id")
},setId:function(b){if($defined(b)&&b>mindplot.model.INodeModel._uuid){mindplot.model.INodeModel._uuid=b
}if(!$defined(b)){b=mindplot.model.INodeModel._nextUUID()
}this.putProperty("id",b)
},getType:function(){return this.getProperty("type")
},setType:function(b){this.putProperty("type",b)
},setText:function(b){this.putProperty("text",b)
},getText:function(){return this.getProperty("text")
},setPosition:function(d,c){$assert(!isNaN(parseInt(d)),"x position is not valid:"+d);
$assert(!isNaN(parseInt(c)),"y position is not valid:"+c);
this.putProperty("position","{x:"+parseInt(d)+",y:"+parseInt(c)+"}")
},getPosition:function(){var value=this.getProperty("position");
var result=null;
if(value!=null){result=eval("("+value+")")
}return result
},setImageSize:function(c,d){this.putProperty("imageSize","{width:"+c+",height:"+d+"}")
},getImageSize:function(){var value=this.getProperty("imageSize");
var result=null;
if(value!=null){result=eval("("+value+")")
}return result
},setImageUrl:function(b){this.putProperty("imageUrl",b)
},getMetadata:function(){return this.getProperty("metadata")
},setMetadata:function(b){this.putProperty("metadata",b)
},getImageUrl:function(){return this.getProperty("imageUrl")
},getMindmap:function(){return this._mindmap
},disconnect:function(){var b=this.getMindmap();
b.disconnect(this)
},getShapeType:function(){return this.getProperty("shapeType")
},setShapeType:function(b){this.putProperty("shapeType",b)
},setOrder:function(b){$assert(typeof b==="number"&&isFinite(b)||b==null,"Order must be null or a number");
this.putProperty("order",b)
},getOrder:function(){return this.getProperty("order")
},setFontFamily:function(b){this.putProperty("fontFamily",b)
},getFontFamily:function(){return this.getProperty("fontFamily")
},setFontStyle:function(b){this.putProperty("fontStyle",b)
},getFontStyle:function(){return this.getProperty("fontStyle")
},setFontWeight:function(b){this.putProperty("fontWeight",b)
},getFontWeight:function(){return this.getProperty("fontWeight")
},setFontColor:function(b){this.putProperty("fontColor",b)
},getFontColor:function(){return this.getProperty("fontColor")
},setFontSize:function(b){this.putProperty("fontSize",b)
},getFontSize:function(){return this.getProperty("fontSize")
},getBorderColor:function(){return this.getProperty("borderColor")
},setBorderColor:function(b){this.putProperty("borderColor",b)
},getBackgroundColor:function(){return this.getProperty("backgroundColor")
},setBackgroundColor:function(b){this.putProperty("backgroundColor",b)
},areChildrenShrunken:function(){var b=this.getProperty("shrunken");
return $defined(b)?b:false
},setChildrenShrunken:function(b){this.putProperty("shrunken",b)
},isNodeModel:function(){return true
},isConnected:function(){return this.getParent()!=null
},append:function(b){throw"Unsupported operation"
},connectTo:function(c){$assert(c,"parent can not be null");
var d=this.getMindmap();
d.connect(c,this)
},copyTo:function(h){var i=this;
var j=i.getPropertiesKeys();
_.each(j,function(b){var a=i.getProperty(b);
h.putProperty(b,a)
});
var f=this.getChildren();
var g=h.getMindmap();
_.each(function(b,c){var a=g.createNode(c.getType(),c.getId());
c.copyTo(a);
h.append(a)
});
return h
},deleteNode:function(){var d=this.getMindmap();
var c=this.getParent();
if($defined(c)){c.removeChild(this)
}else{d.removeBranch(this)
}},getPropertiesKeys:function(){throw"Unsupported operation"
},putProperty:function(d,c){throw"Unsupported operation"
},setParent:function(b){throw"Unsupported operation"
},getChildren:function(){throw"Unsupported operation"
},getParent:function(){throw"Unsupported operation"
},clone:function(){throw"Unsupported operation"
},inspect:function(){var d="{ type: "+this.getType()+" , id: "+this.getId()+" , text: "+this.getText();
var c=this.getChildren();
if(c.length>0){d=d+", children: {(size:"+c.length;
_.each(c,function(a){d=d+"=> (";
var b=a.getPropertiesKeys();
_.each(b,function(h){var g=a.getProperty(h);
d=d+h+":"+g+","
});
d=d+"}"
})
}d=d+" }";
return d
},removeChild:function(b){throw"Unsupported operation"
}});
mindplot.model.TopicShape={RECTANGLE:"rectagle",ROUNDED_RECT:"rounded rectagle",ELLIPSE:"elipse",LINE:"line",IMAGE:"image"};
mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE="CentralTopic";
mindplot.model.INodeModel.MAIN_TOPIC_TYPE="MainTopic";
mindplot.model.INodeModel.MAIN_TOPIC_TO_MAIN_TOPIC_DISTANCE=220;
mindplot.model.INodeModel._nextUUID=function(){if(!$defined(mindplot.model.INodeModel._uuid)){mindplot.model.INodeModel._uuid=0
}mindplot.model.INodeModel._uuid=mindplot.model.INodeModel._uuid+1;
return mindplot.model.INodeModel._uuid
};
mindplot.model.INodeModel._uuid=0;mindplot.model.NodeModel=new Class({Extends:mindplot.model.INodeModel,initialize:function(d,e,f){$assert(d,"Node type can not be null");
$assert(e,"mindmap can not be null");
this._properties={};
this.parent(e);
this.setId(f);
this.setType(d);
this.areChildrenShrunken(false);
this._children=[];
this._feature=[]
},createFeature:function(c,d){return mindplot.TopicFeature.createModel(c,d)
},addFeature:function(b){$assert(b,"feature can not be null");
this._feature.push(b)
},getFeatures:function(){return this._feature
},removeFeature:function(c){$assert(c,"feature can not be null");
var d=this._feature.length;
this._feature=this._feature.filter(function(a){return c.getId()!=a.getId()
});
$assert(d-1==this._feature.length,"Could not be removed ...")
},findFeatureByType:function(b){$assert(b,"type can not be null");
return this._feature.filter(function(a){return a.getType()==b
})
},findFeatureById:function(c){$assert($defined(c),"id can not be null");
var d=this._feature.filter(function(a){return a.getId()==c
});
$assert(d.length==1,"Feature could not be found:"+c);
return d[0]
},getPropertiesKeys:function(){return Object.keys(this._properties)
},putProperty:function(d,c){$defined(d,"key can not be null");
this._properties[d]=c
},getProperties:function(){return this._properties
},getProperty:function(c){$defined(c,"key can not be null");
var d=this._properties[c];
return !$defined(d)?null:d
},clone:function(){var b=new mindplot.model.NodeModel(this.getType(),this._mindmap);
b._children=this._children.map(function(d){var a=d.clone();
a._parent=b;
return a
});
b._properties=Object.clone(this._properties);
b._feature=this._feature.clone();
return b
},deepCopy:function(){var d=new mindplot.model.NodeModel(this.getType(),this._mindmap);
d._children=this._children.map(function(a){var b=a.deepCopy();
b._parent=d;
return b
});
var c=d.getId();
d._properties=Object.clone(this._properties);
d.setId(c);
d._feature=this._feature.clone();
return d
},append:function(b){$assert(b&&b.isNodeModel(),"Only NodeModel can be appended to Mindmap object");
this._children.push(b);
b._parent=this
},removeChild:function(b){$assert(b&&b.isNodeModel(),"Only NodeModel can be appended to Mindmap object.");
this._children.erase(b);
b._parent=null
},getChildren:function(){return this._children
},getParent:function(){return this._parent
},setParent:function(b){$assert(b!=this,"The same node can not be parent and child if itself.");
this._parent=b
},_isChildNode:function(i){var g=false;
if(i==this){g=true
}else{var j=this.getChildren();
for(var f=0;
f<j.length;
f++){var h=j[f];
g=h._isChildNode(i);
if(g){break
}}}return g
},findNodeById:function(h){var g=null;
if(this.getId()==h){g=this
}else{var j=this.getChildren();
for(var f=0;
f<j.length;
f++){var i=j[f];
g=i.findNodeById(h);
if(g){break
}}}return g
}});mindplot.model.RelationshipModel=new Class({Static:{_nextUUID:function(){if(!$defined(mindplot.model.RelationshipModel._uuid)){mindplot.model.RelationshipModel._uuid=0
}mindplot.model.RelationshipModel._uuid=mindplot.model.RelationshipModel._uuid+1;
return mindplot.model.RelationshipModel._uuid
}},initialize:function(c,d){$assert($defined(c),"from node type can not be null");
$assert($defined(d),"to node type can not be null");
this._id=mindplot.model.RelationshipModel._nextUUID();
this._sourceTargetId=c;
this._targetTopicId=d;
this._lineType=mindplot.ConnectionLine.SIMPLE_CURVED;
this._srcCtrlPoint=null;
this._destCtrlPoint=null;
this._endArrow=true;
this._startArrow=false
},getFromNode:function(){return this._sourceTargetId
},getToNode:function(){return this._targetTopicId
},getId:function(){$assert(this._id,"id is null");
return this._id
},getLineType:function(){return this._lineType
},setLineType:function(b){this._lineType=b
},getSrcCtrlPoint:function(){return this._srcCtrlPoint
},setSrcCtrlPoint:function(b){this._srcCtrlPoint=b
},getDestCtrlPoint:function(){return this._destCtrlPoint
},setDestCtrlPoint:function(b){this._destCtrlPoint=b
},getEndArrow:function(){return this._endArrow
},setEndArrow:function(b){this._endArrow=b
},getStartArrow:function(){return this._startArrow
},setStartArrow:function(b){this._startArrow=b
},clone:function(){var b=new mindplot.model.RelationshipModel(this._sourceTargetId,this._targetTopicId);
b._id=this._id;
b._lineType=this._lineType;
b._srcCtrlPoint=this._srcCtrlPoint;
b._destCtrlPoint=this._destCtrlPoint;
b._endArrow=this._endArrow;
b._startArrow=this._startArrow;
return b
},inspect:function(){return"(fromNode:"+this.getFromNode().getId()+" , toNode: "+this.getToNode().getId()+")"
}});mindplot.ActionDispatcher=new Class({Implements:[mindplot.Events],initialize:function(b){$assert(b,"commandContext can not be null")
},addRelationship:function(c,d){throw"method must be implemented."
},addTopics:function(c,d){throw"method must be implemented."
},deleteEntities:function(d,c){throw"method must be implemented."
},dragTopic:function(g,e,f,h){throw"method must be implemented."
},moveTopic:function(c,d){throw"method must be implemented."
},moveControlPoint:function(c,d){throw"method must be implemented."
},changeFontFamilyToTopic:function(c,d){throw"method must be implemented."
},changeFontStyleToTopic:function(b){throw"method must be implemented."
},changeFontColorToTopic:function(c,d){throw"method must be implemented."
},changeFontSizeToTopic:function(d,c){throw"method must be implemented."
},changeBackgroundColorToTopic:function(c,d){throw"method must be implemented."
},changeBorderColorToTopic:function(c,d){throw"method must be implemented."
},changeShapeTypeToTopic:function(d,c){throw"method must be implemented."
},changeFontWeightToTopic:function(b){throw"method must be implemented."
},changeTextToTopic:function(d,c){throw"method must be implemented."
},shrinkBranch:function(d,c){throw"method must be implemented."
},addFeatureToTopic:function(f,d,e){throw"method must be implemented."
},changeFeatureToTopic:function(d,f,e){throw"method must be implemented."
},removeFeatureFromTopic:function(d,c){throw"method must be implemented."
}});
mindplot.ActionDispatcher.setInstance=function(b){mindplot.ActionDispatcher._instance=b
};
mindplot.ActionDispatcher.getInstance=function(){return mindplot.ActionDispatcher._instance
};mindplot.StandaloneActionDispatcher=new Class({Extends:mindplot.ActionDispatcher,initialize:function(b){this.parent(b);
this._actionRunner=new mindplot.DesignerActionRunner(b,this)
},addTopics:function(f,d){var e=new mindplot.commands.AddTopicCommand(f,d);
this.execute(e)
},addRelationship:function(d){var c=new mindplot.commands.AddRelationshipCommand(d);
this.execute(c)
},deleteEntities:function(e,d){var f=new mindplot.commands.DeleteCommand(e,d);
this.execute(f)
},dragTopic:function(i,f,g,j){var h=new mindplot.commands.DragTopicCommand(i,f,g,j);
this.execute(h)
},moveTopic:function(e,f){$assert($defined(e),"topicsId can not be null");
$assert($defined(f),"position can not be null");
var h=function(b,a){var c=b.getPosition();
mindplot.EventBus.instance.fireEvent(mindplot.EventBus.events.NodeMoveEvent,{node:b.getModel(),position:a});
return c
};
var g=new mindplot.commands.GenericFunctionCommand(h,e,f);
this.execute(g)
},moveControlPoint:function(d,e){var f=new mindplot.commands.MoveControlPointCommand(d,e);
this.execute(f)
},changeFontStyleToTopic:function(e){var d=function(b){var c=b.getFontStyle();
var a=(c=="italic")?"normal":"italic";
b.setFontStyle(a,true);
return c
};
var f=new mindplot.commands.GenericFunctionCommand(d,e);
this.execute(f)
},changeTextToTopic:function(f,g){$assert($defined(f),"topicsIds can not be null");
var e=function(b,a){var c=b.getText();
b.setText(a);
return c
};
e.commandType="changeTextToTopic";
var h=new mindplot.commands.GenericFunctionCommand(e,f,g);
this.execute(h)
},changeFontFamilyToTopic:function(g,f){$assert(g,"topicIds can not be null");
$assert(f,"fontFamily can not be null");
var e=function(a,b){var c=a.getFontFamily();
a.setFontFamily(b,true);
a._adjustShapes();
return c
};
var h=new mindplot.commands.GenericFunctionCommand(e,g,f);
this.execute(h)
},changeFontColorToTopic:function(e,f){$assert(e,"topicIds can not be null");
$assert(f,"color can not be null");
var h=function(a,b){var c=a.getFontColor();
a.setFontColor(b,true);
return c
};
var g=new mindplot.commands.GenericFunctionCommand(h,e,f);
g.discardDuplicated="fontColorCommandId";
this.execute(g)
},changeBackgroundColorToTopic:function(e,f){$assert(e,"topicIds can not be null");
$assert(f,"color can not be null");
var h=function(a,b){var c=a.getBackgroundColor();
a.setBackgroundColor(b);
return c
};
var g=new mindplot.commands.GenericFunctionCommand(h,e,f);
g.discardDuplicated="backColor";
this.execute(g)
},changeBorderColorToTopic:function(e,f){$assert(e,"topicIds can not be null");
$assert(f,"topicIds can not be null");
var h=function(a,b){var c=a.getBorderColor();
a.setBorderColor(b);
return c
};
var g=new mindplot.commands.GenericFunctionCommand(h,e,f);
g.discardDuplicated="borderColorCommandId";
this.execute(g)
},changeFontSizeToTopic:function(f,e){$assert(f,"topicIds can not be null");
$assert(e,"size can not be null");
var h=function(b,a){var c=b.getFontSize();
b.setFontSize(a,true);
b._adjustShapes();
return c
};
var g=new mindplot.commands.GenericFunctionCommand(h,f,e);
this.execute(g)
},changeShapeTypeToTopic:function(f,e){$assert(f,"topicsIds can not be null");
$assert(e,"shapeType can not be null");
var h=function(b,a){var c=b.getShapeType();
b.setShapeType(a,true);
return c
};
var g=new mindplot.commands.GenericFunctionCommand(h,f,e);
this.execute(g)
},changeFontWeightToTopic:function(e){$assert(e,"topicsIds can not be null");
var d=function(b){var c=b.getFontWeight();
var a=(c=="bold")?"normal":"bold";
b.setFontWeight(a,true);
b._adjustShapes();
return c
};
var f=new mindplot.commands.GenericFunctionCommand(d,e);
this.execute(f)
},shrinkBranch:function(f,g){$assert(f,"topicsIds can not be null");
var e=function(a,b){a.setChildrenShrunken(b);
return !b
};
var h=new mindplot.commands.GenericFunctionCommand(e,f,g);
this.execute(h,false)
},addFeatureToTopic:function(h,e,f){var g=new mindplot.commands.AddFeatureToTopicCommand(h,e,f);
this.execute(g)
},changeFeatureToTopic:function(e,g,f){var h=new mindplot.commands.ChangeFeatureToTopicCommand(e,g,f);
this.execute(h)
},removeFeatureFromTopic:function(e,f){var d=new mindplot.commands.RemoveFeatureFromTopicCommand(e,f);
this.execute(d)
},execute:function(b){this._actionRunner.execute(b)
}});
mindplot.CommandContext=new Class({initialize:function(b){$assert(b,"designer can not be null");
this._designer=b
},findTopics:function(e){$assert($defined(e),"topicsIds can not be null");
if(!(e instanceof Array)){e=[e]
}var g=this._designer.getModel().getTopics();
var f=g.filter(function(a){return e.contains(a.getId())
});
if(f.length!=e.length){var h=g.map(function(a){return a.getId()
});
$assert(f.length==e.length,"Could not find topic. Result:"+f+", Filter Criteria:"+e+", Current Topics: ["+h+"]")
}return f
},deleteTopic:function(b){this._designer.removeTopic(b)
},createTopic:function(b){$assert(b,"model can not be null");
return this._designer.nodeModelToNodeGraph(b)
},createModel:function(){var b=this._designer.getMindmap();
return b.createNode(mindplot.NodeModel.MAIN_TOPIC_TYPE)
},addTopic:function(c){var d=this._designer.getMindmap();
return d.addBranch(c.getModel())
},connect:function(c,d){c.connectTo(d,this._designer._workspace)
},disconnect:function(b){b.disconnect(this._designer._workspace)
},addRelationship:function(b){$assert(b,"model cannot be null");
return this._designer.addRelationship(b)
},deleteRelationship:function(b){this._designer.deleteRelationship(b)
},findRelationships:function(c){$assert($defined(c),"relId can not be null");
if(!(c instanceof Array)){c=[c]
}var d=this._designer.getModel().getRelationships();
return d.filter(function(a){return c.contains(a.getId())
})
},moveTopic:function(c,d){$assert(c,"topic cannot be null");
$assert(d,"position cannot be null");
mindplot.EventBus.instance.fireEvent(mindplot.EventBus.events.NodeMoveEvent,{node:c.getModel(),position:d})
}});mindplot.DesignerModel=new Class({Implements:[mindplot.Events],initialize:function(b){this._zoom=b.zoom;
this._topics=[];
this._relationships=[]
},getZoom:function(){return this._zoom
},setZoom:function(b){this._zoom=b
},getTopics:function(){return this._topics
},getRelationships:function(){return this._relationships
},getCentralTopic:function(){var b=this.getTopics();
return b[0]
},filterSelectedTopics:function(){var d=[];
for(var c=0;
c<this._topics.length;
c++){if(this._topics[c].isOnFocus()){d.push(this._topics[c])
}}return d
},filterSelectedRelationships:function(){var d=[];
for(var c=0;
c<this._relationships.length;
c++){if(this._relationships[c].isOnFocus()){d.push(this._relationships[c])
}}return d
},getEntities:function(){var b=[].append(this._topics);
b.append(this._relationships);
return b
},removeTopic:function(b){$assert(b,"topic can not be null");
this._topics.erase(b)
},removeRelationship:function(b){$assert(b,"rel can not be null");
this._relationships.erase(b)
},addTopic:function(b){$assert(b,"topic can not be null");
$assert(typeof b.getId()=="number","id is not a number:"+b.getId());
this._topics.push(b)
},addRelationship:function(b){$assert(b,"rel can not be null");
this._relationships.push(b)
},filterTopicsIds:function(k,n){var i=[];
var j=this.filterSelectedTopics();
var l=true;
for(var h=0;
h<j.length;
h++){var m=j[h];
if($defined(k)){l=k(m)
}if(l){i.push(m.getId())
}else{$notify(n)
}}return i
},selectedTopic:function(){var b=this.filterSelectedTopics();
return(b.length>0)?b[0]:null
},findTopicById:function(g){var f=null;
for(var h=0;
h<this._topics.length;
h++){var e=this._topics[h];
if(e.getId()==g){f=e;
break
}}return f
}});mindplot.Designer=new Class({Extends:mindplot.Events,initialize:function(f,g){$assert(f,"options must be defined");
$assert(f.zoom,"zoom must be defined");
$assert(f.size,"size must be defined");
$assert(g,"divElement must be defined");
mindplot.Messages.init(f.locale);
this._options=f;
g.css(f.size);
var j=new mindplot.CommandContext(this);
this._actionDispatcher=new mindplot.StandaloneActionDispatcher(j);
var h=this;
this._actionDispatcher.addEvent("modelUpdate",function(a){h.fireEvent("modelUpdate",a)
});
mindplot.ActionDispatcher.setInstance(this._actionDispatcher);
this._model=new mindplot.DesignerModel(f);
var i=new mindplot.ScreenManager(g);
this._workspace=new mindplot.Workspace(i,this._model.getZoom());
this._eventBussDispatcher=new mindplot.layout.EventBusDispatcher(this.getModel());
if(!this.isReadOnly()){this._registerMouseEvents();
mindplot.DesignerKeyboard.register(this);
this._dragManager=this._buildDragManager(this._workspace)
}this._registerWheelEvents();
this._relPivot=new mindplot.RelationshipPivot(this._workspace,this);
this.setViewPort(f.viewPort);
mindplot.TopicEventDispatcher.configure(this.isReadOnly());
this._clipboard=[]
},_registerWheelEvents:function(){var d=this._workspace;
var c=this;
$(document).on("mousewheel",function(a){if(a.deltaY>0){c.zoomIn(1.05)
}else{c.zoomOut(1.05)
}a.preventDefault()
})
},addEvent:function(d,f){if(d==mindplot.TopicEvent.EDIT||d==mindplot.TopicEvent.CLICK){var e=mindplot.TopicEventDispatcher.getInstance();
e.addEvent(d,f)
}else{this.parent(d,f)
}},_registerMouseEvents:function(){var e=this._workspace;
var g=e.getScreenManager();
var h=this;
g.addEvent("update",function(){var a=h.getModel().getTopics();
_.each(a,function(b){b.closeEditors()
});
if(h._cleanScreen){h._cleanScreen()
}});
g.addEvent("click",function(a){h.onObjectFocusEvent(null,a)
});
g.addEvent("dblclick",function(b){if(e.isWorkspaceEventsEnabled()){var d=g.getWorkspaceMousePosition(b);
var a=h.getModel().getCentralTopic();
var c=h._createChildModel(a,d);
this._actionDispatcher.addTopics([c],[a.getId()])
}}.bind(this));
function f(a){a.stopPropagation();
a.preventDefault()
}},_buildDragManager:function(j){var f=this.getModel();
var g=new mindplot.DragConnector(f,this._workspace);
var i=new mindplot.DragManager(j,this._eventBussDispatcher);
var h=f.getTopics();
i.addEvent("startdragging",function(){for(var a=0;
a<h.length;
a++){h[a].setMouseEventsEnabled(false)
}});
i.addEvent("dragging",function(a,b){b.updateFreeLayout(a);
if(!b.isFreeLayoutOn(a)){g.checkConnection(b);
if(!b.isVisible()&&b.isConnected()){b.setVisibility(true)
}}});
i.addEvent("enddragging",function(a,b){for(var c=0;
c<h.length;
c++){h[c].setMouseEventsEnabled(true)
}b.applyChanges(j)
});
return i
},setViewPort:function(c){this._workspace.setViewPort(c);
var d=this.getModel();
this._workspace.setZoom(d.getZoom(),true)
},_buildNodeGraph:function(p,l){var o=mindplot.NodeGraph.create(p,{readOnly:l});
this.getModel().addTopic(o);
var m=this;
if(!l){o.addEvent("mousedown",function(a){m.onObjectFocusEvent(o,a)
});
if(o.getType()!=mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){this._dragManager.add(o)
}}var q=p.isConnected();
if(q){var n=p.getParent();
var s=null;
var t=this.getModel().getTopics();
for(var r=0;
r<t.length;
r++){var i=t[r];
if(i.getModel()==n){s=i;
p.disconnect();
break
}}$assert(s,"Could not find a topic to connect");
o.connectTo(s,this._workspace)
}o.addEvent("ontblur",function(){var a=m.getModel().filterSelectedTopics();
var b=m.getModel().filterSelectedRelationships();
if(a.length==0||b.length==0){m.fireEvent("onblur")
}});
o.addEvent("ontfocus",function(){var a=m.getModel().filterSelectedTopics();
var b=m.getModel().filterSelectedRelationships();
if(a.length==1||b.length==1){m.fireEvent("onfocus")
}});
return o
},onObjectFocusEvent:function(g,j){var h=this.getModel().getTopics();
_.each(h,function(a){a.closeEditors()
});
var f=this.getModel();
var i=f.getEntities();
_.each(i,function(a){if(!$defined(j)||(!j.ctrlKey&&!j.metaKey)){if(a.isOnFocus()&&a!=g){a.setOnFocus(false)
}}})
},selectAll:function(){var d=this.getModel();
var c=d.getEntities();
_.each(c,function(a){a.setOnFocus(true)
})
},deselectAll:function(){var b=this.getModel().getEntities();
_.each(b,function(a){a.setOnFocus(false)
})
},setZoom:function(b){if(b>1.9||b<0.3){$notify($msg("ZOOM_IN_ERROR"));
return
}this.getModel().setZoom(b);
this._workspace.setZoom(b)
},zoomOut:function(d){if(!d){d=1.2
}var e=this.getModel();
var f=e.getZoom()*d;
if(f<=1.9){e.setZoom(f);
this._workspace.setZoom(f)
}else{$notify($msg("ZOOM_ERROR"))
}},zoomIn:function(d){if(!d){d=1.2
}var e=this.getModel();
var f=e.getZoom()/d;
if(f>=0.3){e.setZoom(f);
this._workspace.setZoom(f)
}else{$notify($msg("ZOOM_ERROR"))
}},copyToClipboard:function(){var b=this.getModel().filterSelectedTopics();
if(b.length<=0){$notify($msg("AT_LEAST_ONE_TOPIC_MUST_BE_SELECTED"));
return
}b=b.filter(function(a){return !a.isCentralTopic()
});
this._clipboard=b.map(function(a){var e=a.getModel().deepCopy();
var f=e.getPosition();
e.setPosition(f.x+(60*Math.sign(f.x)),f.y+30);
return e
});
$notify($msg("SELECTION_COPIED_TO_CLIPBOARD"))
},pasteClipboard:function(){if(this._clipboard.length==0){$notify($msg("CLIPBOARD_IS_EMPTY"));
return
}this._actionDispatcher.addTopics(this._clipboard);
this._clipboard=[]
},getModel:function(){return this._model
},shrinkSelectedBranch:function(){var d=this.getModel().filterSelectedTopics();
if(d.length<=0||d.length!=1){$notify($msg("ONLY_ONE_TOPIC_MUST_BE_SELECTED_COLLAPSE"));
return
}var c=d[0];
if(c.getType()!=mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){this._actionDispatcher.shrinkBranch([c.getId()],!c.areChildrenShrunken())
}},createChildForSelectedNode:function(){var e=this.getModel().filterSelectedTopics();
if(e.length<=0){$notify($msg("ONE_TOPIC_MUST_BE_SELECTED"));
return
}if(e.length!=1){$notify($msg("ONLY_ONE_TOPIC_MUST_BE_SELECTED"));
return
}var h=e[0];
var f=h.getId();
var g=this._createChildModel(h);
this._actionDispatcher.addTopics([g],[f])
},_copyNodeProps:function(q,t){if(q.getType()!=mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){var k=q.getFontSize();
if(k){t.setFontSize(k)
}}var n=q.getFontFamily();
if(n){t.setFontFamily(n)
}var o=q.getFontColor();
if(o){t.setFontColor(o)
}var m=q.getFontWeight();
if(m){t.setFontWeight(m)
}var r=q.getFontStyle();
if(r){t.setFontStyle(r)
}var p=q.getShapeType();
if(p){t.setShapeType(p)
}var s=q.getBorderColor();
if(s){t.setBorderColor(s)
}var l=q.getBackgroundColor();
if(l){t.setBackgroundColor(l)
}},_createChildModel:function(l,p){var m=l.getModel();
var n=m.getMindmap();
var k=n.createNode();
var o=this._eventBussDispatcher.getLayoutManager();
var i=o.predict(l.getId(),null,p);
k.setOrder(i.order);
var j=i.position;
k.setPosition(j.x,j.y);
this._copyNodeProps(m,k);
return k
},addDraggedNode:function(f,d){$assert(f,"event can not be null");
$assert(d,"model can not be null");
d.setPosition(1000,1000);
this._actionDispatcher.addTopics([d]);
var e=this.getModel().findTopicById(d.getId());
e.fireEvent("mousedown",f)
},createSiblingForSelectedNode:function(){var f=this.getModel().filterSelectedTopics();
if(f.length<=0){$notify($msg("ONE_TOPIC_MUST_BE_SELECTED"));
return
}if(f.length>1){$notify($msg("ONLY_ONE_TOPIC_MUST_BE_SELECTED"));
return
}var i=f[0];
if(!i.getOutgoingConnectedTopic()){this.createChildForSelectedNode()
}else{var j=i.getOutgoingConnectedTopic();
var h=this._createSiblingModel(i);
if(j.getType()==mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){h.setOrder(i.getOrder()+2)
}var g=j.getId();
this._actionDispatcher.addTopics([h],[g])
}},_createSiblingModel:function(i){var g=null;
var j=i.getOutgoingConnectedTopic();
if(j!=null){var k=i.getModel();
var l=k.getMindmap();
g=l.createNode();
var h=i.getOrder()+1;
g.setOrder(h);
g.setPosition(10,10)
}this._copyNodeProps(k,g);
return g
},showRelPivot:function(h){var e=this.getModel().filterSelectedTopics();
if(e.length<=0){$notify($msg("RELATIONSHIP_COULD_NOT_BE_CREATED"));
return
}var f=this._workspace.getScreenManager();
var g=f.getWorkspaceMousePosition(h);
this._relPivot.start(e[0],g)
},getMindmapProperties:function(){var b=this.getModel();
return{zoom:b.getZoom()}
},loadMap:function(u){$assert(u,"mindmapModel can not be null");
this._mindmap=u;
var i={width:25,height:25};
var s=new mindplot.layout.LayoutManager(u.getCentralTopic().getId(),i);
var n=this;
s.addEvent("change",function(a){var c=a.getId();
var b=n.getModel().findTopicById(c);
b.setPosition(a.getPosition());
b.setOrder(a.getOrder())
});
this._eventBussDispatcher.setLayoutManager(s);
var j=u.getBranches();
for(var q=0;
q<j.length;
q++){var t=j[q];
var o=this.nodeModelToNodeGraph(t);
o.setBranchVisibility(true)
}var p=u.getRelationships();
for(var r=0;
r<p.length;
r++){this._relationshipModelToRelationship(p[r])
}var v=this.getModel().getCentralTopic();
this.goToNode(v);
mindplot.EventBus.instance.fireEvent(mindplot.EventBus.events.DoLayout);
this.fireEvent("loadSuccess")
},getMindmap:function(){return this._mindmap
},undo:function(){this._actionDispatcher._actionRunner.undo()
},redo:function(){this._actionDispatcher._actionRunner.redo()
},isReadOnly:function(){return this._options.readOnly
},nodeModelToNodeGraph:function(h){$assert(h,"Node model can not be null");
var j=h.getChildren().slice();
j=j.sort(function(a,b){return a.getOrder()-b.getOrder()
});
var g=this._buildNodeGraph(h,this.isReadOnly());
g.setVisibility(false);
this._workspace.append(g);
for(var f=0;
f<j.length;
f++){var i=j[f];
if($defined(i)){this.nodeModelToNodeGraph(i)
}}return g
},_relationshipModelToRelationship:function(g){$assert(g,"Node model can not be null");
var e=this._buildRelationshipShape(g);
var f=e.getSourceTopic();
f.addRelationship(e);
var h=e.getTargetTopic();
h.addRelationship(e);
e.setVisibility(f.isVisible()&&h.isVisible());
this._workspace.append(e);
return e
},addRelationship:function(c){var d=this.getMindmap();
d.addRelationship(c);
return this._relationshipModelToRelationship(c)
},deleteRelationship:function(e){var f=e.getSourceTopic();
f.deleteRelationship(e);
var g=e.getTargetTopic();
g.deleteRelationship(e);
this.getModel().removeRelationship(e);
this._workspace.removeChild(e);
var h=this.getMindmap();
h.deleteRelationship(e.getModel())
},_buildRelationshipShape:function(o){var l=this.getModel();
var m=o.getFromNode();
var i=l.findTopicById(m);
var n=o.getToNode();
var p=l.findTopicById(n);
$assert(p,"targetTopic could not be found:"+n+l.getTopics().map(function(a){return a.getId()
}));
var j=new mindplot.Relationship(i,p,o);
var k=this;
j.addEvent("ontblur",function(){var a=k.getModel().filterSelectedTopics();
var b=k.getModel().filterSelectedRelationships();
if(a.length==0||b.length==0){k.fireEvent("onblur")
}});
j.addEvent("ontfocus",function(){var a=k.getModel().filterSelectedTopics();
var b=k.getModel().filterSelectedRelationships();
if(a.length==1||b.length==1){k.fireEvent("onfocus")
}});
l.addRelationship(j);
return j
},removeTopic:function(f){if(!f.isCentralTopic()){var d=f._parent;
f.disconnect(this._workspace);
while(f.getChildren().length>0){this.removeTopic(f.getChildren()[0])
}this._workspace.removeChild(f);
this.getModel().removeTopic(f);
var e=f.getModel();
e.deleteNode();
if($defined(d)){this.goToNode(d)
}}},_resetEdition:function(){var b=this._workspace.getScreenManager();
b.fireEvent("update");
b.fireEvent("mouseup");
this._relPivot.dispose()
},deleteSelectedEntities:function(){this._resetEdition();
var g=this.getModel().filterSelectedTopics();
var e=this.getModel().filterSelectedRelationships();
if(g.length<=0&&e.length<=0){$notify($msg("ENTITIES_COULD_NOT_BE_DELETED"));
return
}else{if(g.length==1&&g[0].isCentralTopic()){$notify($msg("CENTRAL_TOPIC_CAN_NOT_BE_DELETED"));
return
}}var h=g.filter(function(a){return !a.isCentralTopic()
}).map(function(a){return a.getId()
});
var f=e.map(function(a){return a.getId()
});
if(h.length>0||f.length>0){this._actionDispatcher.deleteEntities(h,f)
}},changeFontFamily:function(c){var d=this.getModel().filterTopicsIds();
if(d.length>0){this._actionDispatcher.changeFontFamilyToTopic(d,c)
}},changeFontStyle:function(){var b=this.getModel().filterTopicsIds();
if(b.length>0){this._actionDispatcher.changeFontStyleToTopic(b)
}},changeFontColor:function(c){$assert(c,"color can not be null");
var d=this.getModel().filterTopicsIds();
if(d.length>0){this._actionDispatcher.changeFontColorToTopic(d,c)
}},changeBackgroundColor:function(h){var g=function(a){return a.getShapeType()!=mindplot.model.TopicShape.LINE
};
var f="Color can not be set to line topics.";
var e=this.getModel().filterTopicsIds(g,f);
if(e.length>0){this._actionDispatcher.changeBackgroundColorToTopic(e,h)
}},changeBorderColor:function(h){var g=function(a){return a.getShapeType()!=mindplot.model.TopicShape.LINE
};
var f="Color can not be set to line topics.";
var e=this.getModel().filterTopicsIds(g,f);
if(e.length>0){this._actionDispatcher.changeBorderColorToTopic(e,h)
}},changeFontSize:function(c){var d=this.getModel().filterTopicsIds();
if(d.length>0){this._actionDispatcher.changeFontSizeToTopic(d,c)
}},changeTopicShape:function(h){var g=function(a){return !(a.getType()==mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE&&h==mindplot.model.TopicShape.LINE)
};
var f="Central Topic shape can not be changed to line figure.";
var e=this.getModel().filterTopicsIds(g,f);
if(e.length>0){this._actionDispatcher.changeShapeTypeToTopic(e,h)
}},changeFontWeight:function(){var b=this.getModel().filterTopicsIds();
if(b.length>0){this._actionDispatcher.changeFontWeightToTopic(b)
}},addIconType:function(d){var c=this.getModel().filterTopicsIds();
if(c.length>0){this._actionDispatcher.addFeatureToTopic(c[0],mindplot.TopicFeature.Icon.id,{id:d})
}},addLink:function(){var c=this.getModel();
var d=c.selectedTopic();
if(d){d.showLinkEditor();
this.onObjectFocusEvent()
}},addNote:function(){var c=this.getModel();
var d=c.selectedTopic();
if(d){d.showNoteEditor();
this.onObjectFocusEvent()
}},goToNode:function(b){b.setOnFocus(true);
this.onObjectFocusEvent(b)
},getWorkSpace:function(){return this._workspace
}});mindplot.ScreenManager=new Class({initialize:function(b){$assert(b,"can not be null");
this._divContainer=b;
this._padding={x:0,y:0};
this._clickEvents=[];
this._divContainer.bind("click",function(a){a.stopPropagation()
});
this._divContainer.bind("dblclick",function(a){a.stopPropagation();
a.preventDefault()
})
},setScale:function(b){$assert(b,"Screen scale can not be null");
this._scale=b
},addEvent:function(d,c){if(d=="click"){this._clickEvents.push(c)
}else{this._divContainer.bind(d,c)
}},removeEvent:function(d,c){if(d=="click"){this._clickEvents.remove(c)
}else{this._divContainer.unbind(d,c)
}},fireEvent:function(d,c){if(d=="click"){_.each(this._clickEvents,function(a){a(d,c)
})
}else{this._divContainer.trigger(d,c)
}},_getElementPosition:function(h){var e=h.getPosition();
var f=e.x;
var g=e.y;
f=f-this._padding.x;
g=g-this._padding.y;
f=f/this._scale;
g=g/this._scale;
return{x:f,y:g}
},getWorkspaceIconPosition:function(v){var B=v.getImage();
var A=B.getPosition();
var u=v.getSize();
var z=v.getGroup();
var q=z.getNativeElement();
var w=q.getCoordOrigin();
var e=q.getSize();
var r=q.getCoordSize();
var C={x:r.width/parseInt(e.width),y:r.height/parseInt(e.height)};
var s=(A.x-w.x-(parseInt(u.width)/2))/C.x;
var t=(A.y-w.y-(parseInt(u.height)/2))/C.y;
var D=z.getPosition();
s=s+D.x;
t=t+D.y;
var x=z.getTopic();
var y=this._getElementPosition(x);
y.x=y.x-(parseInt(x.getSize().width)/2);
return{x:s+y.x,y:t+y.y}
},getWorkspaceMousePosition:function(d){var e=d.clientX;
var f=d.clientY;
e=e*this._scale;
f=f*this._scale;
e=e+this._padding.x;
f=f+this._padding.y;
return new core.Point(e,f)
},getContainer:function(){return this._divContainer
},setOffset:function(d,c){this._padding.x=d;
this._padding.y=c
}});mindplot.Workspace=new Class({initialize:function(g,h){$assert(g,"Div container can not be null");
$assert(h,"zoom container can not be null");
this._zoom=h;
this._screenManager=g;
var e=g.getContainer();
this._screenWidth=parseInt(e.css("width"));
this._screenHeight=parseInt(e.css("height"));
var f=this._createWorkspace();
this._workspace=f;
f.addItAsChildTo(e);
this.setZoom(h,true);
this._registerDragEvents();
this._eventsEnabled=true
},_createWorkspace:function(){var f=-(this._screenWidth/2);
var d=-(this._screenHeight/2);
var e={width:this._screenWidth+"px",height:this._screenHeight+"px",coordSizeWidth:this._screenWidth,coordSizeHeight:this._screenHeight,coordOriginX:f,coordOriginY:d,fillColor:"transparent",strokeWidth:0};
web2d.peer.Toolkit.init();
return new web2d.Workspace(e)
},append:function(b){if($defined(b.addToWorkspace)){b.addToWorkspace(this)
}else{this._workspace.append(b)
}},removeChild:function(b){if($defined(b.removeFromWorkspace)){b.removeFromWorkspace(this)
}else{this._workspace.removeChild(b)
}},addEvent:function(d,c){this._workspace.addEvent(d,c)
},removeEvent:function(d,c){$assert(d,"type can not be null");
$assert(c,"listener can not be null");
this._workspace.removeEvent(d,c)
},getSize:function(){return this._workspace.getCoordSize()
},setZoom:function(l,j){this._zoom=l;
var o=this._workspace;
var p=l*this._screenWidth;
var k=l*this._screenHeight;
o.setCoordSize(p,k);
if(this._viewPort){this._viewPort.width=this._viewPort.width*l;
this._viewPort.height=this._viewPort.height*l
}var m;
var n;
if(j){if(this._viewPort){m=-(this._viewPort.width/2);
n=-(this._viewPort.height/2)
}else{m=-(p/2);
n=-(k/2)
}}else{var i=o.getCoordOrigin();
m=i.x;
n=i.y
}o.setCoordOrigin(m,n);
this._screenManager.setOffset(m,n);
this._screenManager.setScale(l);
this._screenManager.fireEvent("update")
},getScreenManager:function(){return this._screenManager
},enableWorkspaceEvents:function(b){this._eventsEnabled=b
},isWorkspaceEventsEnabled:function(){return this._eventsEnabled
},dumpNativeChart:function(){return this._workspace.dumpNativeChart()
},_registerDragEvents:function(){var e=this._workspace;
var g=this._screenManager;
var h=this;
var f=function(b){if(!$defined(e._mouseMoveListener)){if(h.isWorkspaceEventsEnabled()){h.enableWorkspaceEvents(false);
var c=g.getWorkspaceMousePosition(b);
var d=e.getCoordOrigin();
var a=false;
e._mouseMoveListener=function(p){var t=g.getWorkspaceMousePosition(p);
var s=t.x-c.x;
var q=-s+d.x;
var o=t.y-c.y;
var r=-o+d.y;
e.setCoordOrigin(q,r);
if(Browser.firefox){window.document.body.style.cursor="-moz-grabbing"
}else{window.document.body.style.cursor="move"
}p.preventDefault();
g.fireEvent("update");
a=true
};
g.addEvent("mousemove",e._mouseMoveListener);
e._mouseUpListener=function(k){g.removeEvent("mousemove",e._mouseMoveListener);
g.removeEvent("mouseup",e._mouseUpListener);
e._mouseUpListener=null;
e._mouseMoveListener=null;
window.document.body.style.cursor="default";
var l=e.getCoordOrigin();
g.setOffset(l.x,l.y);
h.enableWorkspaceEvents(true);
if(!a){g.fireEvent("click")
}};
g.addEvent("mouseup",e._mouseUpListener)
}}else{e._mouseUpListener()
}};
g.addEvent("mousedown",f)
},setViewPort:function(b){this._viewPort=b
}});mindplot.ShirinkConnector=new Class({initialize:function(e){var g=new web2d.Elipse(mindplot.Topic.prototype.INNER_RECT_ATTRIBUTES);
this._ellipse=g;
g.setFill("rgb(62,118,179)");
g.setSize(mindplot.Topic.CONNECTOR_WIDTH,mindplot.Topic.CONNECTOR_WIDTH);
g.addEvent("click",function(c){var j=e.getModel();
var a=!j.areChildrenShrunken();
var d=e.getId();
var b=mindplot.ActionDispatcher.getInstance();
b.shrinkBranch([d],a);
c.stopPropagation()
});
g.addEvent("mousedown",function(a){a.stopPropagation()
});
g.addEvent("dblclick",function(a){a.stopPropagation()
});
g.addEvent("mouseover",function(a){g.setFill("rgb(153, 0, 255)")
});
var h=this;
g.addEvent("mouseout",function(a){var b=e.getBackgroundColor();
h.setFill(b)
});
g.setCursor("default");
this._fillColor="#f7f7f7";
var f=e.getModel();
this.changeRender(f.areChildrenShrunken())
},changeRender:function(d){var c=this._ellipse;
if(d){c.setStroke("2","solid")
}else{c.setStroke("1","solid")
}},setVisibility:function(b){this._ellipse.setVisibility(b)
},setOpacity:function(b){this._ellipse.setOpacity(b)
},setFill:function(b){this._fillColor=b;
this._ellipse.setFill(b)
},setAttribute:function(d,c){this._ellipse.setAttribute(d,c)
},addToWorkspace:function(b){b.append(this._ellipse)
},setPosition:function(d,c){this._ellipse.setPosition(d,c)
},moveToBack:function(){this._ellipse.moveToBack()
},moveToFront:function(){this._ellipse.moveToFront()
}});mindplot.Keyboard=new Class({initialize:function(){},addShortcut:function(d,c){if(!$.isArray(d)){d=[d]
}_.each(d,function(a){$(document).bind("keydown",a,c)
})
}});mindplot.DesignerKeyboard=new Class({Extends:mindplot.Keyboard,Static:{register:function(b){this._instance=new mindplot.DesignerKeyboard(b)
},getInstance:function(){return this._instance
}},initialize:function(b){$assert(b,"designer can not be null");
this._registerEvents(b)
},_registerEvents:function(e){var f=e.getModel();
this.addShortcut(["backspace"],function(a){a.preventDefault();
a.stopPropagation();
e.deleteSelectedEntities()
});
this.addShortcut(["space"],function(){e.shrinkSelectedBranch()
});
this.addShortcut(["f2"],function(a){a.stopPropagation();
a.preventDefault();
var b=f.selectedTopic();
if(b){b.showTextEditor()
}});
this.addShortcut(["del"],function(a){e.deleteSelectedEntities();
a.preventDefault();
a.stopPropagation()
});
this.addShortcut(["enter"],function(){e.createSiblingForSelectedNode()
});
this.addShortcut(["insert"],function(a){e.createChildForSelectedNode();
a.preventDefault();
a.stopPropagation()
});
this.addShortcut(["tab"],function(a){e.createChildForSelectedNode();
a.preventDefault();
a.stopPropagation()
});
this.addShortcut(["meta+enter"],function(a){a.preventDefault();
a.stopPropagation();
e.createChildForSelectedNode()
});
this.addShortcut(["ctrl+z","meta+z"],function(a){a.preventDefault(a);
a.stopPropagation();
e.undo()
});
this.addShortcut(["ctrl+c","meta+c"],function(a){a.preventDefault(a);
a.stopPropagation();
e.copyToClipboard()
});
this.addShortcut(["ctrl+v","meta+v"],function(a){a.preventDefault(a);
a.stopPropagation();
e.pasteClipboard()
});
this.addShortcut(["ctrl+shift+z","meta+shift+z","ctrl+y","meta+y"],function(a){a.preventDefault();
a.stopPropagation();
e.redo()
});
this.addShortcut(["ctrl+a","meta+a"],function(a){a.preventDefault();
a.stopPropagation();
e.selectAll()
});
this.addShortcut(["ctrl+b","meta+b"],function(a){a.preventDefault();
a.stopPropagation();
e.changeFontWeight()
});
this.addShortcut(["ctrl+s","meta+s"],function(a){a.preventDefault();
a.stopPropagation();
$(document).find("#save").trigger("click")
});
this.addShortcut(["ctrl+i","meta+i"],function(a){a.preventDefault();
a.stopPropagation();
e.changeFontStyle()
});
this.addShortcut(["ctrl+shift+a","meta+shift+a"],function(a){a.preventDefault();
a.stopPropagation();
e.deselectAll()
});
this.addShortcut(["meta+=","ctrl+="],function(a){a.preventDefault();
a.stopPropagation();
e.zoomIn()
});
this.addShortcut(["meta+-","ctrl+-"],function(a){a.preventDefault();
a.stopPropagation();
e.zoomOut()
});
var h=this;
this.addShortcut("right",function(b){var c=f.selectedTopic();
if(c){if(c.isCentralTopic()){h._goToSideChild(e,c,"RIGHT")
}else{if(c.getPosition().x<0){h._goToParent(e,c)
}else{if(!c.areChildrenShrunken()){h._goToChild(e,c)
}}}}else{var a=f.getCentralTopic();
h._goToNode(e,a)
}b.preventDefault();
b.stopPropagation()
});
this.addShortcut("left",function(b){var c=f.selectedTopic();
if(c){if(c.isCentralTopic()){h._goToSideChild(e,c,"LEFT")
}else{if(c.getPosition().x>0){h._goToParent(e,c)
}else{if(!c.areChildrenShrunken()){h._goToChild(e,c)
}}}}else{var a=f.getCentralTopic();
h._goToNode(e,a)
}b.preventDefault();
b.stopPropagation()
});
this.addShortcut("up",function(b){var c=f.selectedTopic();
if(c){if(!c.isCentralTopic()){h._goToBrother(e,c,"UP")
}}else{var a=f.getCentralTopic();
h._goToNode(e,a)
}b.preventDefault();
b.stopPropagation()
});
this.addShortcut("down",function(b){var c=f.selectedTopic();
if(c){if(!c.isCentralTopic()){h._goToBrother(e,c,"DOWN")
}}else{var a=f.getCentralTopic();
h._goToNode(e,a)
}b.preventDefault();
b.stopPropagation()
});
var g=["esc","escape","f1","f3","f4","f5","f6","f7","f8","f9","f10","f11","f12"];
$(document).on("keypress",function(d){var b;
if(d.key&&g.contains(d.key.toLowerCase())){return
}if(d.key&&d.keyCode==0){b=d.charCode
}else{b=d.keyCode
}var c=jQuery.hotkeys.specialKeys[b];
if(["enter","capslock"].indexOf(c)==-1&&!jQuery.hotkeys.shiftNums[b]){var j=e.getModel().filterSelectedTopics();
if(j.length>0){var a=String.fromCharCode(b);
if(d.ctrlKey||d.altKey||d.metaKey){return
}j[0].showTextEditor(a);
d.stopPropagation()
}}})
},_goToBrother:function(i,B,t){var r=B.getParent();
if(r){var z=r.getChildren();
var w=B;
var u=B.getPosition().y;
var s=B.getPosition().x;
var v=null;
for(var A=0;
A<z.length;
A++){var y=(s*z[A].getPosition().x)>=0;
if(z[A]!=B&&y){var q=z[A];
var x=q.getPosition().y;
if(t=="DOWN"&&x>u){var C=u-x;
if(C<0){C=C*(-1)
}if(v==null||v>C){v=C;
w=z[A]
}}else{if(t=="UP"&&x<u){var D=u-x;
if(D<0){D=D*(-1)
}if(v==null||v>D){v=D;
w=z[A]
}}}}}this._goToNode(i,w)
}},_goToSideChild:function(i,p,m){var q=p.getChildren();
if(q.length>0){var n=q[0];
var l=null;
for(var o=0;
o<q.length;
o++){var r=q[o];
var k=r.getPosition().y;
if(m=="LEFT"&&r.getPosition().x<0){if(l==null||k<l){n=r;
l=k
}}if(m=="RIGHT"&&r.getPosition().x>0){if(l==null||k<l){n=r;
l=k
}}}this._goToNode(i,n)
}},_goToParent:function(e,f){var d=f.getParent();
if(d){this._goToNode(e,d)
}},_goToChild:function(n,m){var h=m.getChildren();
if(h.length>0){var k=h[0];
var l=k.getPosition().y;
for(var i=0;
i<h.length;
i++){var j=h[i];
if(j.getPosition().y<l){l=j.getPosition().y;
k=j
}}this._goToNode(n,k)
}},_goToNode:function(d,c){d.deselectAll();
c.setOnFocus(true)
}});
mindplot.DesignerKeyboard.specialKeys={8:"backspace",9:"tab",10:"return",13:"enter",16:"shift",17:"ctrl",18:"alt",19:"pause",20:"capslock",27:"esc",32:"space",33:"pageup",34:"pagedown",35:"end",36:"home",37:"left",38:"up",39:"right",40:"down",45:"insert",46:"del",96:"0",97:"1",98:"2",99:"3",100:"4",101:"5",102:"6",103:"7",104:"8",105:"9",106:"*",107:"+",109:"-",110:".",111:"/",112:"f1",113:"f2",114:"f3",115:"f4",116:"f5",117:"f6",118:"f7",119:"f8",120:"f9",121:"f10",122:"f11",123:"f12",144:"numlock",145:"scroll",186:";",191:"/",220:"\\",222:"'",224:"meta"};mindplot.TopicStyle=new Class({Static:{_getStyles:function(f){$assert(f,"topic can not be null");
var e;
if(f.isCentralTopic()){e=mindplot.TopicStyle.STYLES.CENTRAL_TOPIC
}else{var d=f.getOutgoingConnectedTopic();
if($defined(d)){if(d.isCentralTopic()){e=mindplot.TopicStyle.STYLES.MAIN_TOPIC
}else{e=mindplot.TopicStyle.STYLES.SUB_TOPIC
}}else{e=mindplot.TopicStyle.STYLES.ISOLATED_TOPIC
}}return e
},defaultText:function(d){var c=this._getStyles(d).msgKey;
return $msg(c)
},defaultFontStyle:function(b){return this._getStyles(b).fontStyle
},defaultBackgroundColor:function(b){return this._getStyles(b).backgroundColor
},defaultBorderColor:function(b){return this._getStyles(b).borderColor
},getInnerPadding:function(b){return this._getStyles(b).innerPadding
},defaultShapeType:function(b){return this._getStyles(b).shapeType
}}});
mindplot.TopicStyle.STYLES={CENTRAL_TOPIC:{borderColor:"rgb(57,113,177)",backgroundColor:"rgb(80,157,192)",fontStyle:{font:"Verdana",size:10,style:"normal",weight:"bold",color:"#ffffff"},msgKey:"CENTRAL_TOPIC",innerPadding:11,shapeType:mindplot.model.TopicShape.ROUNDED_RECT},MAIN_TOPIC:{borderColor:"rgb(2,59,185)",backgroundColor:"rgb(224,229,239)",fontStyle:{font:"Arial",size:8,style:"normal",weight:"normal",color:"rgb(82,92,97)"},msgKey:"MAIN_TOPIC",innerPadding:3,shapeType:mindplot.model.TopicShape.LINE},SUB_TOPIC:{borderColor:"rgb(2,59,185)",backgroundColor:"rgb(224,229,239)",fontStyle:{font:"Arial",size:6,style:"normal",weight:"normal",color:"rgb(82,92,97)"},msgKey:"SUB_TOPIC",innerPadding:3,shapeType:mindplot.model.TopicShape.LINE},ISOLATED_TOPIC:{borderColor:"rgb(2,59,185)",backgroundColor:"rgb(224,229,239)",fontStyle:{font:"Verdana",size:8,style:"normal",weight:"normal",color:"rgb(82,92,97)"},msgKey:"ISOLATED_TOPIC",innerPadding:4,shapeType:mindplot.model.TopicShape.LINE}};mindplot.NodeGraph=new Class({initialize:function(c,d){$assert(c,"model can not be null");
this._options=d;
this._mouseEvents=true;
this.setModel(c);
this._onFocus=false;
this._size={width:50,height:20}
},isReadOnly:function(){return this._options.readOnly
},getType:function(){var b=this.getModel();
return b.getType()
},setId:function(b){$assert(typeof topic.getId()=="number","id is not a number:"+b);
this.getModel().setId(b)
},_set2DElement:function(b){this._elem2d=b
},get2DElement:function(){$assert(this._elem2d,"NodeGraph has not been initialized properly");
return this._elem2d
},setPosition:function(d,c){throw"Unsupported operation"
},addEvent:function(e,f){var d=this.get2DElement();
d.addEvent(e,f)
},removeEvent:function(e,f){var d=this.get2DElement();
d.removeEvent(e,f)
},fireEvent:function(e,f){var d=this.get2DElement();
d.trigger(e,f)
},setMouseEventsEnabled:function(b){this._mouseEvents=b
},isMouseEventsEnabled:function(){return this._mouseEvents
},getSize:function(){return this._size
},setSize:function(b){this._size.width=parseInt(b.width);
this._size.height=parseInt(b.height)
},getModel:function(){$assert(this._model,"Model has not been initialized yet");
return this._model
},setModel:function(b){$assert(b,"Model can not be null");
this._model=b
},getId:function(){return this._model.getId()
},setOnFocus:function(d){if(this._onFocus!=d){this._onFocus=d;
var c=this.getOuterShape();
if(d){c.setFill(mindplot.Topic.OUTER_SHAPE_ATTRIBUTES_FOCUS.fillColor);
c.setOpacity(1)
}else{c.setFill(mindplot.Topic.OUTER_SHAPE_ATTRIBUTES.fillColor);
c.setOpacity(0)
}this.setCursor("move");
this.closeEditors();
this.fireEvent(d?"ontfocus":"ontblur",this)
}},isOnFocus:function(){return this._onFocus
},dispose:function(b){this.setOnFocus(false);
b.removeChild(this)
},createDragNode:function(d){var c=this._buildDragShape();
return new mindplot.DragTopic(c,this,d)
},_buildDragShape:function(){$assert(false,"_buildDragShape must be implemented by all nodes.")
},getPosition:function(){var b=this.getModel();
return b.getPosition()
}});
mindplot.NodeGraph.create=function(g,e){$assert(g,"Model can not be null");
var h=g.getType();
$assert(h,"Node model type can not be null");
var f;
if(h==mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){f=new mindplot.CentralTopic(g,e)
}else{if(h==mindplot.model.INodeModel.MAIN_TOPIC_TYPE){f=new mindplot.MainTopic(g,e)
}else{$assert(false,"unsupported node type:"+h)
}}return f
};mindplot.Topic=new Class({Extends:mindplot.NodeGraph,initialize:function(d,e){this.parent(d,e);
this._children=[];
this._parent=null;
this._relationships=[];
this._isInWorkspace=false;
this._buildTopicShape();
var f=d.getPosition();
if(f!=null&&this.isCentralTopic()){this.setPosition(f)
}if(!this.isReadOnly()){this._registerEvents()
}},_registerEvents:function(){this.setMouseEventsEnabled(true);
this.addEvent("click",function(a){a.stopPropagation()
});
var b=this;
this.addEvent("dblclick",function(a){b._getTopicEventDispatcher().show(b);
a.stopPropagation()
})
},setShapeType:function(b){this._setShapeType(b,true)
},getParent:function(){return this._parent
},_setShapeType:function(n,q){var o=this.getModel();
if($defined(q)&&q){o.setShapeType(n)
}var p=this.getInnerShape();
if(p!=null){this._removeInnerShape();
var t=this.getInnerShape();
var k=this.getSize();
this.setSize(k,true);
var m=this.get2DElement();
m.append(t);
var l=this.getTextShape();
l.moveToFront();
var r=this.getIconGroup();
if($defined(r)){r.moveToFront()
}var s=this.getShrinkConnector();
if($defined(s)){s.moveToFront()
}}},getShapeType:function(){var c=this.getModel();
var d=c.getShapeType();
if(!$defined(d)){d=mindplot.TopicStyle.defaultShapeType(this)
}return d
},_removeInnerShape:function(){var c=this.get2DElement();
var d=this.getInnerShape();
c.removeChild(d);
this._innerShape=null;
return d
},getInnerShape:function(){if(!$defined(this._innerShape)){this._innerShape=this._buildShape(mindplot.Topic.INNER_RECT_ATTRIBUTES,this.getShapeType());
var d=this.getBackgroundColor();
this._setBackgroundColor(d,false);
var c=this.getBorderColor();
this._setBorderColor(c,false);
if(!this.isCentralTopic()&&!this.isReadOnly()){this._innerShape.setCursor("move")
}else{this._innerShape.setCursor("default")
}}return this._innerShape
},_buildShape:function(g,i){$assert(g,"attributes can not be null");
$assert(i,"shapeType can not be null");
var h;
if(i==mindplot.model.TopicShape.RECTANGLE){h=new web2d.Rect(0,g)
}else{if(i==mindplot.model.TopicShape.IMAGE){var k=this.getModel();
var l=k.getImageUrl();
var j=k.getImageSize();
h=new web2d.Image();
h.setHref(l);
h.setSize(j.width,j.height);
h.getSize=function(){return k.getImageSize()
};
h.setPosition=function(){}
}else{if(i==mindplot.model.TopicShape.ELLIPSE){h=new web2d.Rect(0.9,g)
}else{if(i==mindplot.model.TopicShape.ROUNDED_RECT){h=new web2d.Rect(0.3,g)
}else{if(i==mindplot.model.TopicShape.LINE){h=new web2d.Line({strokeColor:"#495879",strokeWidth:1});
h.setSize=function(b,c){this.size={width:b,height:c};
h.setFrom(0,c);
h.setTo(b,c);
var a=mindplot.ConnectionLine.getStrokeColor();
h.setStroke(1,"solid",a)
};
h.getSize=function(){return this.size
};
h.setPosition=function(){};
h.setFill=function(){};
h.setStroke=function(){}
}else{$assert(false,"Unsupported figure shapeType:"+i)
}}}}}h.setPosition(0,0);
return h
},setCursor:function(e){var g=this.getInnerShape();
g.setCursor(e);
var f=this.getOuterShape();
f.setCursor(e);
var h=this.getTextShape();
h.setCursor(e)
},getOuterShape:function(){if(!$defined(this._outerShape)){var b=this._buildShape(mindplot.Topic.OUTER_SHAPE_ATTRIBUTES,mindplot.model.TopicShape.ROUNDED_RECT);
b.setPosition(-2,-3);
b.setOpacity(0);
this._outerShape=b
}return this._outerShape
},getTextShape:function(){if(!$defined(this._text)){this._text=this._buildTextShape(false);
var b=this.getText();
this._setText(b,false)
}return this._text
},getOrBuildIconGroup:function(){if(!$defined(this._iconsGroup)){this._iconsGroup=this._buildIconGroup();
var b=this.get2DElement();
b.append(this._iconsGroup.getNativeElement());
this._iconsGroup.moveToFront()
}return this._iconsGroup
},getIconGroup:function(){return this._iconsGroup
},_buildIconGroup:function(){var k=this.getTextShape().getFontHeight();
var j=new mindplot.IconGroup(this.getId(),k);
var m=mindplot.TopicStyle.getInnerPadding(this);
j.setPosition(m,m);
var i=this.getModel();
var n=i.getFeatures();
for(var p=0;
p<n.length;
p++){var l=n[p];
var o=mindplot.TopicFeature.createIcon(this,l,this.isReadOnly());
j.addIcon(o,l.getType()==mindplot.TopicFeature.Icon.id&&!this.isReadOnly())
}return j
},addFeature:function(g){var e=this.getOrBuildIconGroup();
this.closeEditors();
var h=this.getModel();
h.addFeature(g);
var f=mindplot.TopicFeature.createIcon(this,g,this.isReadOnly());
e.addIcon(f,g.getType()==mindplot.TopicFeature.Icon.id&&!this.isReadOnly());
this._adjustShapes();
return f
},findFeatureById:function(c){var d=this.getModel();
return d.findFeatureById(c)
},removeFeature:function(f){$assert(f,"featureModel could not be null");
var d=this.getModel();
d.removeFeature(f);
var e=this.getIconGroup();
if($defined(e)){e.removeIconByModel(f)
}this._adjustShapes()
},addRelationship:function(b){this._relationships.push(b)
},deleteRelationship:function(b){this._relationships.erase(b)
},getRelationships:function(){return this._relationships
},_buildTextShape:function(j){var i=new web2d.Text();
var l=this.getFontFamily();
var n=this.getFontSize();
var k=this.getFontWeight();
var m=this.getFontStyle();
i.setFont(l,n,m,k);
var h=this.getFontColor();
i.setColor(h);
if(!j){if(!this.isCentralTopic()){i.setCursor("move")
}else{i.setCursor("default")
}}return i
},setFontFamily:function(h,g){var e=this.getTextShape();
e.setFontFamily(h);
if($defined(g)&&g){var f=this.getModel();
f.setFontFamily(h)
}this._adjustShapes(g)
},setFontSize:function(h,g){var e=this.getTextShape();
e.setSize(h);
if($defined(g)&&g){var f=this.getModel();
f.setFontSize(h)
}this._adjustShapes(g)
},setFontStyle:function(h,g){var e=this.getTextShape();
e.setStyle(h);
if($defined(g)&&g){var f=this.getModel();
f.setFontStyle(h)
}this._adjustShapes(g)
},setFontWeight:function(h,g){var e=this.getTextShape();
e.setWeight(h);
if($defined(g)&&g){var f=this.getModel();
f.setFontWeight(h)
}this._adjustShapes()
},getFontWeight:function(){var f=this.getModel();
var e=f.getFontWeight();
if(!$defined(e)){var d=mindplot.TopicStyle.defaultFontStyle(this);
e=d.weight
}return e
},getFontFamily:function(){var f=this.getModel();
var e=f.getFontFamily();
if(!$defined(e)){var d=mindplot.TopicStyle.defaultFontStyle(this);
e=d.font
}return e
},getFontColor:function(){var f=this.getModel();
var e=f.getFontColor();
if(!$defined(e)){var d=mindplot.TopicStyle.defaultFontStyle(this);
e=d.color
}return e
},getFontStyle:function(){var f=this.getModel();
var e=f.getFontStyle();
if(!$defined(e)){var d=mindplot.TopicStyle.defaultFontStyle(this);
e=d.style
}return e
},getFontSize:function(){var f=this.getModel();
var e=f.getFontSize();
if(!$defined(e)){var d=mindplot.TopicStyle.defaultFontStyle(this);
e=d.size
}return e
},setFontColor:function(h,g){var e=this.getTextShape();
e.setColor(h);
if($defined(g)&&g){var f=this.getModel();
f.setFontColor(h)
}},_setText:function(g,h){var e=this.getTextShape();
e.setText(g==null?mindplot.TopicStyle.defaultText(this):g);
if($defined(h)&&h){var f=this.getModel();
f.setText(g)
}},setText:function(b){if(!b||$.trim(b).length==0){b=null
}this._setText(b,true);
this._adjustShapes()
},getText:function(){var c=this.getModel();
var d=c.getText();
if(!$defined(d)){d=mindplot.TopicStyle.defaultText(this)
}return d
},setBackgroundColor:function(b){this._setBackgroundColor(b,true)
},_setBackgroundColor:function(f,h){var i=this.getInnerShape();
i.setFill(f);
var g=this.getShrinkConnector();
if(g){g.setFill(f)
}if($defined(h)&&h){var j=this.getModel();
j.setBackgroundColor(f)
}},getBackgroundColor:function(){var c=this.getModel();
var d=c.getBackgroundColor();
if(!$defined(d)){d=mindplot.TopicStyle.defaultBackgroundColor(this)
}return d
},setBorderColor:function(b){this._setBorderColor(b,true)
},_setBorderColor:function(f,h){var i=this.getInnerShape();
i.setAttribute("strokeColor",f);
var g=this.getShrinkConnector();
if(g){g.setAttribute("strokeColor",f)
}if($defined(h)&&h){var j=this.getModel();
j.setBorderColor(f)
}},getBorderColor:function(){var c=this.getModel();
var d=c.getBorderColor();
if(!$defined(d)){d=mindplot.TopicStyle.defaultBorderColor(this)
}return d
},_buildTopicShape:function(){var j={width:100,height:100,coordSizeWidth:100,coordSizeHeight:100};
var k=new web2d.Group(j);
this._set2DElement(k);
var n=this.getOuterShape();
var l=this.getInnerShape();
var m=this.getTextShape();
k.append(n);
k.append(l);
k.append(m);
var h=this.getModel();
if(h.getFeatures().length!=0){this.getOrBuildIconGroup()
}var i=this.getShrinkConnector();
if($defined(i)){i.addToWorkspace(k)
}this._registerDefaultListenersToElement(k,this)
},_registerDefaultListenersToElement:function(i,g){var h=function(a){if(g.isMouseEventsEnabled()){g.handleMouseOver(a)
}};
i.addEvent("mouseover",h);
var j=function(a){if(g.isMouseEventsEnabled()){g.handleMouseOut(a)
}};
i.addEvent("mouseout",j);
var f=this;
i.addEvent("mousedown",function(b){if(!f.isReadOnly()){var a=true;
if((b.metaKey&&Browser.Platform.mac)||(b.ctrlKey&&!Browser.Platform.mac)){a=!f.isOnFocus();
b.stopPropagation();
b.preventDefault()
}g.setOnFocus(a)
}var c=f._getTopicEventDispatcher();
c.process(mindplot.TopicEvent.CLICK,f);
b.stopPropagation()
})
},areChildrenShrunken:function(){var b=this.getModel();
return b.areChildrenShrunken()&&!this.isCentralTopic()
},isCollapsed:function(){var d=false;
var c=this.getParent();
while(c&&!d){d=c.areChildrenShrunken();
c=c.getParent()
}return d
},setChildrenShrunken:function(j){var g=this.getModel();
g.setChildrenShrunken(j);
var h=this.getShrinkConnector();
if($defined(h)){h.changeRender(j)
}var k=this._flatten2DElements(this);
var i=new mindplot.util.FadeEffect(k,!j);
var l=this;
i.addEvent("complete",function(){if(j){l.setOnFocus(true)
}k.forEach(function(a){if(a.setOnFocus){a.setOnFocus(false)
}})
});
i.start();
mindplot.EventBus.instance.fireEvent(mindplot.EventBus.events.NodeShrinkEvent,g)
},getShrinkConnector:function(){var b=this._connector;
if(this._connector==null){this._connector=new mindplot.ShirinkConnector(this);
this._connector.setVisibility(false);
b=this._connector
}return b
},handleMouseOver:function(){var b=this.getOuterShape();
b.setOpacity(1)
},handleMouseOut:function(){var b=this.getOuterShape();
if(!this.isOnFocus()){b.setOpacity(0)
}},showTextEditor:function(b){this._getTopicEventDispatcher().show(this,{text:b})
},showNoteEditor:function(){var h=this.getId();
var f=this.getModel();
var g={getValue:function(){var a=f.findFeatureByType(mindplot.TopicFeature.Note.id);
var b;
if(a.length>0){b=a[0].getText()
}return b
},setValue:function(b){var c=mindplot.ActionDispatcher.getInstance();
var d=f.findFeatureByType(mindplot.TopicFeature.Note.id);
if(!$defined(b)){var a=d[0].getId();
c.removeFeatureFromTopic(h,a)
}else{if(d.length>0){c.changeFeatureToTopic(h,d[0].getId(),{text:b})
}else{c.addFeatureToTopic(h,mindplot.TopicFeature.Note.id,{text:b})
}}}};
var e=new mindplot.widget.NoteEditor(g);
this.closeEditors();
e.show()
},showLinkEditor:function(){var h=this.getId();
var f=this.getModel();
var g={getValue:function(){var a=f.findFeatureByType(mindplot.TopicFeature.Link.id);
var b;
if(a.length>0){b=a[0].getUrl()
}return b
},setValue:function(b){var c=mindplot.ActionDispatcher.getInstance();
var d=f.findFeatureByType(mindplot.TopicFeature.Link.id);
if(!$defined(b)){var a=d[0].getId();
c.removeFeatureFromTopic(h,a)
}else{if(d.length>0){c.changeFeatureToTopic(h,d[0].getId(),{url:b})
}else{c.addFeatureToTopic(h,mindplot.TopicFeature.Link.id,{url:b})
}}}};
this.closeEditors();
var e=new mindplot.widget.LinkEditor(g);
e.show()
},closeEditors:function(){this._getTopicEventDispatcher().close(true)
},_getTopicEventDispatcher:function(){return mindplot.TopicEventDispatcher.getInstance()
},setPosition:function(f){$assert(f,"position can not be null");
f.x=Math.ceil(f.x);
f.y=Math.ceil(f.y);
var j=this.getModel();
j.setPosition(f.x,f.y);
var i=this.getSize();
var g=f.x-(i.width/2);
var h=f.y-(i.height/2);
this._elem2d.setPosition(g,h);
this._updateConnectionLines();
this.invariant()
},getOutgoingLine:function(){return this._outgoingLine
},getIncomingLines:function(){var g=[];
var i=this.getChildren();
for(var j=0;
j<i.length;
j++){var h=i[j];
var f=h.getOutgoingLine();
if($defined(f)){g.push(f)
}}return g
},getOutgoingConnectedTopic:function(){var d=null;
var c=this.getOutgoingLine();
if($defined(c)){d=c.getTargetTopic()
}return d
},_updateConnectionLines:function(){var h=this.getOutgoingLine();
if($defined(h)){h.redraw()
}var g=this.getIncomingLines();
for(var e=0;
e<g.length;
e++){g[e].redraw()
}for(var f=0;
f<this._relationships.length;
f++){this._relationships[f].redraw()
}},setBranchVisibility:function(d){var f=this;
var e=this;
while(e!=null&&!e.isCentralTopic()){f=e;
e=f.getParent()
}f.setVisibility(d)
},setVisibility:function(c){this._setTopicVisibility(c);
this._setChildrenVisibility(c);
this._setRelationshipLinesVisibility(c);
var d=this.getOutgoingLine();
if(d){d.setVisibility(c)
}},moveToBack:function(){for(var c=0;
c<this._relationships.length;
c++){this._relationships[c].moveToBack()
}var d=this.getShrinkConnector();
if($defined(d)){d.moveToBack()
}this.get2DElement().moveToBack()
},moveToFront:function(){this.get2DElement().moveToFront();
var d=this.getShrinkConnector();
if($defined(d)){d.moveToFront()
}for(var c=0;
c<this._relationships.length;
c++){this._relationships[c].moveToFront()
}},isVisible:function(){var b=this.get2DElement();
return b.isVisible()
},_setRelationshipLinesVisibility:function(b){_.each(this._relationships,function(g){var a=g.getSourceTopic();
var i=g.getTargetTopic();
var h=i.getModel().getParent();
var j=a.getModel().getParent();
g.setVisibility(b&&(h==null||!h.areChildrenShrunken())&&(j==null||!j.areChildrenShrunken()))
})
},_setTopicVisibility:function(g){var h=this.get2DElement();
h.setVisibility(g);
if(this.getIncomingLines().length>0){var f=this.getShrinkConnector();
if($defined(f)){f.setVisibility(g)
}}var e=this.getTextShape();
e.setVisibility(this.getShapeType()!=mindplot.model.TopicShape.IMAGE?g:false)
},setOpacity:function(e){var g=this.get2DElement();
g.setOpacity(e);
var f=this.getShrinkConnector();
if($defined(f)){f.setOpacity(e)
}var h=this.getTextShape();
h.setOpacity(e)
},_setChildrenVisibility:function(h){var k=this.getChildren();
var g=this.getModel();
h=h?!g.areChildrenShrunken():h;
for(var l=0;
l<k.length;
l++){var i=k[l];
i.setVisibility(h);
var j=i.getOutgoingLine();
j.setVisibility(h)
}},invariant:function(){var e=this._outgoingLine;
var d=this.getModel();
var f=d.isConnected();
if((f&&!e)||(!f&&e)){}},setSize:function(g,j){$assert(g,"size can not be null");
$assert($defined(g.width),"size seem not to be a valid element");
g={width:Math.ceil(g.width),height:Math.ceil(g.height)};
var k=this.getSize();
var h=k.width!=g.width||k.height!=g.height;
if(h||j){mindplot.NodeGraph.prototype.setSize.call(this,g);
var l=this.getOuterShape();
var i=this.getInnerShape();
l.setSize(g.width+4,g.height+6);
i.setSize(g.width,g.height);
this._updatePositionOnChangeSize(k,g);
if(h){mindplot.EventBus.instance.fireEvent(mindplot.EventBus.events.NodeResizeEvent,{node:this.getModel(),size:g})
}}},_updatePositionOnChangeSize:function(){$assert(false,"this method must be overwrited.")
},disconnect:function(p){var m=this.getOutgoingLine();
if($defined(m)){$assert(p,"workspace can not be null");
this._outgoingLine=null;
var i=m.getTargetTopic();
i.removeChild(this);
var k=this.getModel();
k.disconnect();
this._parent=null;
m.removeFromWorkspace(p);
mindplot.EventBus.instance.fireEvent(mindplot.EventBus.events.NodeDisconnectEvent,this.getModel());
var o=this.getModel();
if(!o.getText()){var l=this.getText();
this._setText(l,false)
}if(!o.getFontSize()){var n=this.getFontSize();
this.setFontSize(n,false)
}if(i.getChildren().length==0){var j=i.getShrinkConnector();
if($defined(j)){j.setVisibility(false)
}}}},getOrder:function(){var b=this.getModel();
return b.getOrder()
},setOrder:function(c){var d=this.getModel();
d.setOrder(c)
},connectTo:function(q,o){$assert(!this._outgoingLine,"Could not connect an already connected node");
$assert(q!=this,"Circular connection are not allowed");
$assert(q,"Parent Graph can not be null");
$assert(o,"Workspace can not be null");
q.append(this);
this._parent=q;
var p=q.getModel();
var m=this.getModel();
m.connectTo(p);
var r=new mindplot.ConnectionLine(this,q);
r.setVisibility(false);
this._outgoingLine=r;
o.append(r);
this.updateTopicShape(q);
var l=this.getModel();
if(!l.getText()){var k=this.getText();
this._setText(k,false)
}if(!l.getFontSize()){var j=this.getFontSize();
this.setFontSize(j,false)
}this.getTextShape();
var n=q.getShrinkConnector();
if($defined(n)){n.setVisibility(true)
}r.redraw();
if(this.isInWorkspace()){mindplot.EventBus.instance.fireEvent(mindplot.EventBus.events.NodeConnectEvent,{parentNode:q.getModel(),childNode:this.getModel()})
}},append:function(c){var d=this.getChildren();
d.push(c)
},removeChild:function(c){var d=this.getChildren();
d.erase(c)
},getChildren:function(){var b=this._children;
if(!$defined(b)){this._children=[];
b=this._children
}return b
},removeFromWorkspace:function(d){var f=this.get2DElement();
d.removeChild(f);
var e=this.getOutgoingLine();
if($defined(e)){d.removeChild(e)
}this._isInWorkspace=false;
mindplot.EventBus.instance.fireEvent(mindplot.EventBus.events.NodeRemoved,this.getModel())
},addToWorkspace:function(d){var c=this.get2DElement();
d.append(c);
if(!this.isInWorkspace()){if(!this.isCentralTopic()){mindplot.EventBus.instance.fireEvent(mindplot.EventBus.events.NodeAdded,this.getModel())
}if(this.getModel().isConnected()){mindplot.EventBus.instance.fireEvent(mindplot.EventBus.events.NodeConnectEvent,{parentNode:this.getOutgoingConnectedTopic().getModel(),childNode:this.getModel()})
}}this._isInWorkspace=true;
this._adjustShapes()
},isInWorkspace:function(){return this._isInWorkspace
},createDragNode:function(d){var e=this.parent(d);
var f=this.getOutgoingConnectedTopic();
if($defined(f)){e.connectTo(f);
e.setVisibility(false)
}this._getTopicEventDispatcher().close();
return e
},_adjustShapes:function(){if(this._isInWorkspace){var s=this.getTextShape();
if(this.getShapeType()!=mindplot.model.TopicShape.IMAGE){var p=s.getWidth();
var o=s.getHeight();
o=o!=0?o:20;
var n=mindplot.TopicStyle.getInnerPadding(this);
var q=this.getOrBuildIconGroup();
var r=this.getTextShape().getFontHeight();
q.setPosition(n,n);
q.seIconSize(r,r);
var k=q.getSize().width;
if(k!=0){k=k+(o/4)
}var m=o+(n*2);
var t=p+k+(n*2);
this.setSize({width:t,height:m});
s.setPosition(n+k,n)
}else{var l=this.getModel().getImageSize();
this.setSize(l)
}}},_flatten2DElements:function(h){var i=[];
var k=h.getChildren();
for(var l=0;
l<k.length;
l++){var j=k[l];
i.push(j);
i.push(j.getOutgoingLine());
var m=j.getRelationships();
i=i.concat(m);
if(!j.areChildrenShrunken()){var n=this._flatten2DElements(j);
i=i.concat(n)
}}return i
},isChildTopic:function(h){var g=(this.getId()==h.getId());
if(!g){var j=this.getChildren();
for(var f=0;
f<j.length;
f++){var i=j[f];
g=i.isChildTopic(h);
if(g){break
}}}return g
},isCentralTopic:function(){return this.getModel().getType()==mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE
}});
mindplot.Topic.CONNECTOR_WIDTH=6;
mindplot.Topic.OUTER_SHAPE_ATTRIBUTES={fillColor:"rgb(252,235,192)",stroke:"1 dot rgb(241,163,39)",x:0,y:0};
mindplot.Topic.OUTER_SHAPE_ATTRIBUTES_FOCUS={fillColor:"rgb(244,184,45)",x:0,y:0};
mindplot.Topic.INNER_RECT_ATTRIBUTES={stroke:"2 solid"};mindplot.CentralTopic=new Class({Extends:mindplot.Topic,initialize:function(c,d){this.parent(c,d)
},_registerEvents:function(){this.parent();
this.addEvent("mousedown",function(b){b.stopPropagation()
})
},workoutIncomingConnectionPoint:function(){return this.getPosition()
},setCursor:function(b){b=(b=="move")?"default":b;
this.parent(b)
},updateTopicShape:function(){},_updatePositionOnChangeSize:function(){var b=new core.Point(0,0);
this.setPosition(b)
},getShrinkConnector:function(){return null
},workoutOutgoingConnectionPoint:function(e){$assert(e,"targetPoint can not be null");
var g=this.getPosition();
var h=mindplot.util.Shape.isAtRight(e,g);
var f=this.getSize();
return mindplot.util.Shape.calculateRectConnectionPoint(g,f,!h)
}});mindplot.MainTopic=new Class({Extends:mindplot.Topic,initialize:function(c,d){this.parent(c,d)
},INNER_RECT_ATTRIBUTES:{stroke:"0.5 solid #009900"},_buildDragShape:function(){var m=this._buildShape(this.INNER_RECT_ATTRIBUTES,this.getShapeType());
var j=this.getSize();
m.setSize(j.width,j.height);
m.setPosition(0,0);
m.setOpacity(0.5);
m.setCursor("default");
m.setVisibility(true);
var k=this.getBorderColor();
m.setAttribute("strokeColor",k);
var p=this.getBackgroundColor();
m.setAttribute("fillColor",p);
var n={width:100,height:100,coordSizeWidth:100,coordSizeHeight:100};
var o=new web2d.Group(n);
o.append(m);
if(this.getShapeType()!=mindplot.model.TopicShape.IMAGE){var i=this._buildTextShape(true);
var l=this.getText();
i.setText(l);
i.setOpacity(0.5);
o.append(i)
}return o
},updateTopicShape:function(e,f){var h=this.getModel();
var g=h.getShapeType();
if(!e.isCentralTopic()){if(!$defined(g)){g=this.getShapeType();
this._setShapeType(g,false)
}}},disconnect:function(g){this.parent(g);
var j=this.getSize();
var f=this.getModel();
var i=f.getShapeType();
if(!$defined(i)){i=this.getShapeType();
this._setShapeType(mindplot.model.TopicShape.ROUNDED_RECT,false)
}var h=this.getInnerShape();
h.setVisibility(true)
},_updatePositionOnChangeSize:function(h,f){var e=Math.round((f.width-h.width)/2);
var g=this.getPosition();
if($defined(g)){if(g.x>0){g.x=g.x+e
}else{g.x=g.x-e
}this.setPosition(g)
}},workoutIncomingConnectionPoint:function(b){return mindplot.util.Shape.workoutIncomingConnectionPoint(this,b)
},workoutOutgoingConnectionPoint:function(m){$assert(m,"targetPoint can not be null");
var k=this.getPosition();
var l=mindplot.util.Shape.isAtRight(m,k);
var o=this.getSize();
var j;
if(this.getShapeType()==mindplot.model.TopicShape.LINE){j=new core.Point();
var i=this._elem2d.getPosition();
var p=this.getInnerShape().getSize();
if(p){var n=0.3;
if(!l){j.x=i.x+p.width-n
}else{j.x=i.x+n
}j.y=i.y+p.height
}else{if(!l){j.x=k.x+(o.width/2)
}else{j.x=k.x-(o.width/2)
}j.y=k.y+(o.height/2)
}}else{j=mindplot.util.Shape.calculateRectConnectionPoint(k,o,l,true)
}return j
}});mindplot.DragTopic=new Class({initialize:function(d,f,e){$assert(d,"Rect can not be null.");
$assert(f,"draggedNode can not be null.");
$assert(e,"layoutManger can not be null.");
this._elem2d=d;
this._order=null;
this._draggedNode=f;
this._layoutManager=e;
this._position=new core.Point();
this._isInWorkspace=false;
this._isFreeLayoutEnabled=false
},setOrder:function(b){this._order=b
},setPosition:function(r,s){var w={x:r,y:s};
if(this.isFreeLayoutOn()&&this.isConnected()){var o=this._layoutManager;
var t=this.getConnectedToTopic();
w=o.predict(t.getId(),this._draggedNode.getId(),w,true).position
}this._position.setValue(w.x,w.y);
var p=this._draggedNode;
var n=p.getSize();
var x=w.x-(w.x>0?0:n.width);
var y=Math.ceil(w.y-(n.height/2));
this._elem2d.setPosition(x,y);
if(this.isConnected()&&!this.isFreeLayoutOn()){var q=this.getConnectedToTopic();
var z=this._layoutManager.predict(q.getId(),this._draggedNode.getId(),this.getPosition());
if(this._order!=z.order){var u=this._getDragPivot();
var v=z.position;
u.connectTo(q,v);
this.setOrder(z.order)
}}},updateFreeLayout:function(f){var d=(f.metaKey&&Browser.Platform.mac)||(f.ctrlKey&&!Browser.Platform.mac);
if(this.isFreeLayoutOn()!=d){var e=this._getDragPivot();
e.setVisibility(!d);
this._isFreeLayoutEnabled=d
}},setVisibility:function(c){var d=this._getDragPivot();
d.setVisibility(c)
},isVisible:function(){var b=this._getDragPivot();
return b.isVisible()
},getInnerShape:function(){return this._elem2d
},disconnect:function(c){var d=this._getDragPivot();
d.disconnect(c)
},connectTo:function(h){$assert(h,"Parent connection node can not be null.");
var g=designer._eventBussDispatcher._layoutManager.predict(h.getId(),this._draggedNode.getId(),this.getPosition());
var e=this._getDragPivot();
var f=g.position;
e.connectTo(h,f);
e.setVisibility(true);
this.setOrder(g.order)
},getDraggedTopic:function(){return this._draggedNode
},removeFromWorkspace:function(c){if(this._isInWorkspace){c.removeChild(this._elem2d);
var d=this._getDragPivot();
d.setVisibility(false);
this._isInWorkspace=false
}},isInWorkspace:function(){return this._isInWorkspace
},addToWorkspace:function(c){if(!this._isInWorkspace){c.append(this._elem2d);
var d=this._getDragPivot();
d.addToWorkspace(c);
this._isInWorkspace=true
}},_getDragPivot:function(){return mindplot.DragTopic.__getDragPivot()
},getPosition:function(){return this._position
},isDragTopic:function(){return true
},applyChanges:function(p){$assert(p,"workspace can not be null");
var m=mindplot.ActionDispatcher.getInstance();
var o=this.getDraggedTopic();
var k=o.getId();
var l=this.getPosition();
if(!this.isFreeLayoutOn()){var n=null;
var j=null;
var r=this.isConnected();
if(r){var q=this.getConnectedToTopic();
n=this._order;
j=q
}m.dragTopic(k,l,n,j)
}else{m.moveTopic(k,l)
}},getConnectedToTopic:function(){var b=this._getDragPivot();
return b.getTargetTopic()
},isConnected:function(){return this.getConnectedToTopic()!=null
},isFreeLayoutOn:function(){return false
}});
mindplot.DragTopic.PIVOT_SIZE={width:50,height:6};
mindplot.DragTopic.init=function(c){$assert(c,"workspace can not be null");
var d=mindplot.DragTopic.__getDragPivot();
c.append(d)
};
mindplot.DragTopic.__getDragPivot=function(){var b=mindplot.DragTopic._dragPivot;
if(!$defined(b)){b=new mindplot.DragPivot();
mindplot.DragTopic._dragPivot=b
}return b
};mindplot.DragManager=new Class({initialize:function(c,d){this._workspace=c;
this._designerModel=c;
this._listeners={};
this._isDragInProcess=false;
this._eventDispatcher=d;
mindplot.DragTopic.init(this._workspace)
},add:function(i){var l=this._workspace;
var g=l.getScreenManager();
var k=this;
var j=this;
var h=function(c){if(l.isWorkspaceEventsEnabled()){l.enableWorkspaceEvents(false);
var e=j._eventDispatcher.getLayoutManager();
var b=i.createDragNode(e);
var d=k._buildMouseMoveListener(l,b,k);
g.addEvent("mousemove",d);
var a=k._buildMouseUpListener(l,i,b,k);
g.addEvent("mouseup",a);
window.document.body.style.cursor="move"
}};
i.addEvent("mousedown",h)
},remove:function(h){var g=this._topics;
var i=false;
var f=-1;
for(var j=0;
j<g.length;
j++){if(g[j]==h){i=true;
f=j
}}},_buildMouseMoveListener:function(l,i,k){var g=l.getScreenManager();
var j=this;
var h=function(b){if(!j._isDragInProcess){var d=k._listeners.startdragging;
d(b,i);
l.append(i);
j._isDragInProcess=true
}var a=g.getWorkspaceMousePosition(b);
i.setPosition(a.x,a.y);
var c=k._listeners.dragging;
if($defined(c)){c(b,i)
}b.preventDefault()
};
k._mouseMoveListener=h;
return h
},_buildMouseUpListener:function(n,k,j,m){var h=n.getScreenManager();
var l=this;
var i=function(a){$assert(j.isDragTopic,"dragNode must be an DragTopic");
h.removeEvent("mousemove",m._mouseMoveListener);
h.removeEvent("mouseup",m._mouseUpListener);
m._mouseMoveListener=null;
m._mouseUpListener=null;
n.enableWorkspaceEvents(true);
window.document.body.style.cursor="default";
if(l._isDragInProcess){var b=m._listeners.enddragging;
b(a,j);
j.removeFromWorkspace(n);
l._isDragInProcess=false
}};
m._mouseUpListener=i;
return i
},addEvent:function(d,c){this._listeners[d]=c
}});mindplot.DragPivot=new Class({initialize:function(){this._position=new core.Point();
this._size=mindplot.DragTopic.PIVOT_SIZE;
this._straightLine=this._buildStraightLine();
this._curvedLine=this._buildCurvedLine();
this._dragPivot=this._buildRect();
this._connectRect=this._buildRect();
this._targetTopic=null;
this._isVisible=false
},isVisible:function(){return this._isVisible
},getTargetTopic:function(){return this._targetTopic
},_buildStraightLine:function(){var b=new web2d.CurvedLine();
b.setStyle(web2d.CurvedLine.SIMPLE_LINE);
b.setStroke(1,"solid","#CC0033");
b.setOpacity(0.4);
b.setVisibility(false);
return b
},_buildCurvedLine:function(){var b=new web2d.CurvedLine();
b.setStyle(web2d.CurvedLine.SIMPLE_LINE);
b.setStroke(1,"solid","#CC0033");
b.setOpacity(0.4);
b.setVisibility(false);
return b
},_redrawLine:function(){$assert(this.getTargetTopic(),"Illegal invocation. Target node can not be null");
var n=this._getPivotRect();
var v=this.getTargetTopic();
var p=this._position;
var m=this._size;
var r=v.getPosition();
var l=this._getConnectionLine();
var t=mindplot.util.Shape.isAtRight(r,p);
var o=mindplot.util.Shape.calculateRectConnectionPoint(p,m,t);
l.setFrom(o.x,o.y);
var s=p.x-(parseInt(m.width)/2);
var u=p.y-(parseInt(m.height)/2);
n.setPosition(s,u);
var q=v.workoutIncomingConnectionPoint(o);
l.setTo(q.x,q.y)
},setPosition:function(b){this._position=b;
this._redrawLine()
},getPosition:function(){return this._position
},_buildRect:function(){var e=this._size;
var f={fillColor:"#CC0033",opacity:0.4,width:e.width,height:e.height,strokeColor:"#FF9933"};
var d=new web2d.Rect(0,f);
d.setVisibility(false);
return d
},_getPivotRect:function(){return this._dragPivot
},getSize:function(){var b=this._getPivotRect();
return b.getSize()
},setVisibility:function(g){if(this.isVisible()!=g){var h=this._getPivotRect();
h.setVisibility(g);
var e=this._connectRect;
e.setVisibility(g);
var f=this._getConnectionLine();
if(f){f.setVisibility(g)
}this._isVisible=g
}},_getConnectionLine:function(){var d=null;
var c=this._targetTopic;
if(c){if(c.getType()==mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){d=this._straightLine
}else{d=this._curvedLine
}}return d
},addToWorkspace:function(l){var j=this._getPivotRect();
l.append(j);
var h=this._connectRect;
l.append(h);
var k=this._straightLine;
k.setVisibility(false);
l.append(k);
k.moveToBack();
var i=this._curvedLine;
i.setVisibility(false);
l.append(i);
i.moveToBack();
var g=this._connectRect;
g.setVisibility(false);
l.append(g);
g.moveToBack()
},removeFromWorkspace:function(f){var d=this._getPivotRect();
f.removeChild(d);
var e=this._connectRect;
f.removeChild(e);
if($defined(this._straightLine)){f.removeChild(this._straightLine)
}if($defined(this._curvedLine)){f.removeChild(this._curvedLine)
}},connectTo:function(t,n){$assert(!this._outgoingLine,"Could not connect an already connected node");
$assert(t!=this,"Circular connection are not allowed");
$assert(n,"position can not be null");
$assert(t,"parent can not be null");
this._position=n;
this._targetTopic=t;
var m=this._connectRect;
var q=t.getSize();
var s=q.width+4;
var k=q.height+4;
m.setSize(s,k);
var o=t.getPosition();
var p=Math.ceil(o.x-(s/2));
var r=Math.ceil(o.y-(k/2));
m.setPosition(p,r);
var l=this._getPivotRect();
l.moveToFront();
l.setPosition(n.x,n.y);
this._redrawLine()
},disconnect:function(b){$assert(b,"workspace can not be null.");
$assert(this._targetTopic,"There are not connected topic.");
this.setVisibility(false);
this._targetTopic=null
}});mindplot.ConnectionLine=new Class({initialize:function(l,i,j){$assert(i,"parentNode node can not be null");
$assert(l,"childNode node can not be null");
$assert(l!=i,"Circular connection");
this._targetTopic=i;
this._sourceTopic=l;
var h;
var g=this._getCtrlPoints(l,i);
if(i.getType()==mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){h=this._createLine(j,mindplot.ConnectionLine.CURVED);
h.setSrcControlPoint(g[0]);
h.setDestControlPoint(g[1])
}else{h=this._createLine(j,mindplot.ConnectionLine.SIMPLE_CURVED);
h.setSrcControlPoint(g[0]);
h.setDestControlPoint(g[1])
}var k=mindplot.ConnectionLine.getStrokeColor();
h.setStroke(1,"solid",k,1);
h.setFill(k,1);
this._line2d=h
},_getCtrlPoints:function(i,h){var j=i.workoutOutgoingConnectionPoint(h.getPosition());
var f=h.workoutIncomingConnectionPoint(i.getPosition());
var g=(j.x-f.x)/3;
return[new core.Point(g,0),new core.Point(-g,0)]
},_createLine:function(d,f){if(!$defined(d)){d=f
}d=parseInt(d);
this._lineType=d;
var e=null;
switch(d){case mindplot.ConnectionLine.POLYLINE:e=new web2d.PolyLine();
break;
case mindplot.ConnectionLine.CURVED:e=new web2d.CurvedLine();
break;
case mindplot.ConnectionLine.SIMPLE_CURVED:e=new web2d.CurvedLine();
e.setStyle(web2d.CurvedLine.SIMPLE_LINE);
break;
default:e=new web2d.Line();
break
}return e
},setVisibility:function(b){this._line2d.setVisibility(b)
},isVisible:function(){return this._line2d.isVisible()
},setOpacity:function(b){this._line2d.setOpacity(b)
},redraw:function(){var l=this._line2d;
var j=this._sourceTopic;
var n=j.getPosition();
var p=this._targetTopic;
var m=p.getPosition();
var i,k;
i=j.workoutOutgoingConnectionPoint(m);
k=p.workoutIncomingConnectionPoint(n);
l.setFrom(k.x,k.y);
l.setTo(i.x,i.y);
if(l.getType()=="CurvedLine"){var o=this._getCtrlPoints(this._sourceTopic,this._targetTopic);
l.setSrcControlPoint(o[0]);
l.setDestControlPoint(o[1])
}this._positionateConnector(p)
},_positionateConnector:function(n){var m=n.getPosition();
var l=mindplot.Topic.CONNECTOR_WIDTH/2;
var j=n.getSize();
var k,i;
if(n.getShapeType()==mindplot.model.TopicShape.LINE){k=j.height
}else{k=j.height/2
}k=k-l;
var h=n.getShrinkConnector();
if($defined(h)){if(Math.sign(m.x)>0){i=j.width;
h.setPosition(i,k)
}else{i=-mindplot.Topic.CONNECTOR_WIDTH
}h.setPosition(i,k)
}},setStroke:function(e,f,d){this._line2d.setStroke(null,null,e,d)
},addToWorkspace:function(b){b.append(this._line2d);
this._line2d.moveToBack()
},removeFromWorkspace:function(b){b.removeChild(this._line2d)
},getTargetTopic:function(){return this._targetTopic
},getSourceTopic:function(){return this._sourceTopic
},getLineType:function(){return this._lineType
},getLine:function(){return this._line2d
},getModel:function(){return this._model
},setModel:function(b){this._model=b
},getType:function(){return"ConnectionLine"
},getId:function(){return this._model.getId()
},moveToBack:function(){this._line2d.moveToBack()
},moveToFront:function(){this._line2d.moveToFront()
}});
mindplot.ConnectionLine.getStrokeColor=function(){return"#495879"
};
mindplot.ConnectionLine.SIMPLE=0;
mindplot.ConnectionLine.POLYLINE=1;
mindplot.ConnectionLine.CURVED=2;
mindplot.ConnectionLine.SIMPLE_CURVED=3;mindplot.Relationship=new Class({Extends:mindplot.ConnectionLine,Static:{getStrokeColor:function(){return"#9b74e6"
},type:"Relationship"},initialize:function(l,j,m){$assert(l,"sourceNode can not be null");
$assert(j,"targetNode can not be null");
this.parent(l,j,m.getLineType());
this.setModel(m);
var k=mindplot.Relationship.getStrokeColor();
this._line2d.setIsSrcControlPointCustom(false);
this._line2d.setIsDestControlPointCustom(false);
this._line2d.setCursor("pointer");
this._line2d.setStroke(1,"solid",k);
this._line2d.setDashed(4,2);
this._focusShape=this._createLine(this.getLineType(),mindplot.ConnectionLine.SIMPLE_CURVED);
this._focusShape.setStroke(2,"solid","#3f96ff");
var n=this._line2d.getControlPoints();
this._focusShape.setSrcControlPoint(n[0]);
this._focusShape.setDestControlPoint(n[1]);
this._focusShape.setVisibility(false);
this._onFocus=false;
this._isInWorkspace=false;
this._controlPointsController=new mindplot.ControlPoint();
this._startArrow=new web2d.Arrow();
this._startArrow.setStrokeColor(k);
this._startArrow.setStrokeWidth(2);
this.setShowStartArrow(true);
if(this._showEndArrow){this._endArrow=new web2d.Arrow();
this._endArrow.setStrokeColor(k);
this._endArrow.setStrokeWidth(2)
}if($defined(m.getSrcCtrlPoint())){var h=m.getSrcCtrlPoint().clone();
this.setSrcControlPoint(h)
}if($defined(m.getDestCtrlPoint())){var i=m.getDestCtrlPoint().clone();
this.setDestControlPoint(i)
}},setStroke:function(e,f,d){this.parent(e,f,d);
this._startArrow.setStrokeColor(e)
},redraw:function(){var n=this._line2d;
var p=this._sourceTopic;
var s=p.getPosition();
var t=this._targetTopic;
var q=t.getPosition();
if(t.getType()==mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){q=mindplot.util.Shape.workoutIncomingConnectionPoint(t,s)
}var m,o;
this._line2d.setStroke(2);
var l=this._line2d.getControlPoints();
if(!this._line2d.isDestControlPointCustom()&&!this._line2d.isSrcControlPointCustom()){var v=mindplot.util.Shape.calculateDefaultControlPoints(s,q);
l[0].x=v[0].x;
l[0].y=v[0].y;
l[1].x=v[1].x;
l[1].y=v[1].y
}var u=new core.Point();
u.x=parseInt(l[0].x)+parseInt(s.x);
u.y=parseInt(l[0].y)+parseInt(s.y);
var r=new core.Point();
r.x=parseInt(l[1].x)+parseInt(q.x);
r.y=parseInt(l[1].y)+parseInt(q.y);
m=mindplot.util.Shape.calculateRelationShipPointCoordinates(p,u);
o=mindplot.util.Shape.calculateRelationShipPointCoordinates(t,r);
n.setFrom(m.x,m.y);
n.setTo(o.x,o.y);
n.moveToFront();
this._positionArrows();
this._positionateConnector(t);
if(this.isOnFocus()){this._refreshShape()
}this._focusShape.moveToBack();
this._controlPointsController.redraw()
},_positionArrows:function(){var d=this._line2d.getTo();
var f=this._line2d.getFrom();
this._startArrow.setFrom(f.x,f.y);
this._startArrow.moveToBack();
if(this._endArrow){this._endArrow.setFrom(d.x,d.y);
this._endArrow.moveToBack()
}if(this._line2d.getType()=="CurvedLine"){var e=this._line2d.getControlPoints();
this._startArrow.setControlPoint(e[0]);
if(this._endArrow){this._endArrow.setControlPoint(e[1])
}}else{this._startArrow.setControlPoint(this._line2d.getTo());
if(this._endArrow){this._endArrow.setControlPoint(this._line2d.getFrom())
}}if(this._showEndArrow){this._endArrow.setVisibility(this.isVisible())
}this._startArrow.setVisibility(this.isVisible()&&this._showStartArrow)
},addToWorkspace:function(b){b.append(this._focusShape);
b.append(this._controlPointsController);
this._controlPointControllerListener=this._initializeControlPointController.bind(this);
this._line2d.addEvent("click",this._controlPointControllerListener);
this._isInWorkspace=true;
b.append(this._startArrow);
if(this._endArrow){b.append(this._endArrow)
}this.parent(b);
this._positionArrows();
this.redraw()
},_initializeControlPointController:function(){this.setOnFocus(true)
},removeFromWorkspace:function(b){b.removeChild(this._focusShape);
b.removeChild(this._controlPointsController);
this._line2d.removeEvent("click",this._controlPointControllerListener);
this._isInWorkspace=false;
b.removeChild(this._startArrow);
if(this._endArrow){b.removeChild(this._endArrow)
}this.parent(b)
},getType:function(){return mindplot.Relationship.type
},setOnFocus:function(b){if(this.isOnFocus()!=b){if(b){this._refreshShape();
this._controlPointsController.setLine(this)
}this._focusShape.setVisibility(b);
this._controlPointsController.setVisibility(b);
this._onFocus=b;
this.fireEvent(b?"ontfocus":"ontblur",this)
}},_refreshShape:function(){var f=this._line2d.getFrom();
var g=this._line2d.getTo();
var e=this._line2d.getControlPoints();
this._focusShape.setFrom(f.x,f.y);
this._focusShape.setTo(g.x,g.y);
var h=this._focusShape.getControlPoints();
h[0].x=e[0].x;
h[0].y=e[0].y;
h[1].x=e[1].x;
h[1].y=e[1].y;
this._focusShape.updateLine()
},addEvent:function(d,f){if(d=="onfocus"){d="mousedown"
}var e=this._line2d;
e.addEvent(d,f)
},isOnFocus:function(){return this._onFocus
},isInWorkspace:function(){return this._isInWorkspace
},setVisibility:function(b){this.parent(b);
if(this._showEndArrow){this._endArrow.setVisibility(this._showEndArrow)
}this._startArrow.setVisibility(this._showStartArrow&&b)
},setOpacity:function(b){this.parent(b);
if(this._showEndArrow){this._endArrow.setOpacity(b)
}if(this._showStartArrow){this._startArrow.setOpacity(b)
}},setShowEndArrow:function(b){this._showEndArrow=b;
if(this._isInWorkspace){this.redraw()
}},setShowStartArrow:function(b){this._showStartArrow=b;
if(this._isInWorkspace){this.redraw()
}},setFrom:function(d,c){$assert($defined(d),"x must be defined");
$assert($defined(c),"y must be defined");
this._line2d.setFrom(d,c);
this._startArrow.setFrom(d,c)
},setTo:function(d,c){$assert($defined(d),"x must be defined");
$assert($defined(c),"y must be defined");
this._line2d.setTo(d,c);
if(this._endArrow){this._endArrow.setFrom(d,c)
}},setSrcControlPoint:function(b){this._line2d.setSrcControlPoint(b);
this._startArrow.setControlPoint(b)
},setDestControlPoint:function(b){this._line2d.setDestControlPoint(b);
if(this._showEndArrow){this._endArrow.setControlPoint(b)
}},getControlPoints:function(){return this._line2d.getControlPoints()
},isSrcControlPointCustom:function(){return this._line2d.isSrcControlPointCustom()
},isDestControlPointCustom:function(){return this._line2d.isDestControlPointCustom()
},setIsSrcControlPointCustom:function(b){this._line2d.setIsSrcControlPointCustom(b)
},setIsDestControlPointCustom:function(b){this._line2d.setIsDestControlPointCustom(b)
},getId:function(){return this._model.getId()
},fireEvent:function(e,f){var d=this._line2d;
d.trigger(e,f)
}});mindplot.DragConnector=new Class({initialize:function(d,c){$assert(d,"designerModel can not be null");
$assert(c,"workspace can not be null");
this._designerModel=d;
this._workspace=c
},checkConnection:function(f){var g=this._designerModel.getTopics();
var h=this._searchConnectionCandidates(f);
var e=f.getConnectedToTopic();
if(e&&(h.length==0||h[0]!=e)){f.disconnect(this._workspace)
}if(!f.isConnected()&&h.length>0){f.connectTo(h[0])
}},_searchConnectionCandidates:function(p){var k=this._designerModel.getTopics();
var i=p.getDraggedTopic();
var l=p.getSize?p.getSize().width:0;
var m=p.getPosition().x>0?0:l;
var j={x:p.getPosition().x-m,y:p.getPosition().y};
k=k.filter(function(a){var b=i!=a;
b=b&&a!=i;
b=b&&!a.areChildrenShrunken()&&!a.isCollapsed();
b=b&&!i.isChildTopic(a);
return b
});
k=k.filter(function(b){var c=b.getPosition();
var d=c.x+(b.getSize().width/2)*Math.sign(j.x);
var a=(j.x-d)*Math.sign(j.x);
return a>0&&(a<mindplot.DragConnector.MAX_VERTICAL_CONNECTION_TOLERANCE)
});
var o=p.getConnectedToTopic();
var n=this;
k=k.sort(function(b,d){var c=b.getPosition();
var f=d.getPosition();
var a=n._isVerticallyAligned(b.getSize(),c,j);
var e=n._isVerticallyAligned(d.getSize(),f,j);
return n._proximityWeight(a,b,j,o)-n._proximityWeight(e,d,j,o)
});
return k
},_proximityWeight:function(g,i,f,j){var h=i.getPosition();
return(g?0:200)+Math.abs(h.x-f.x)+Math.abs(h.y-f.y)+(j==i?0:100)
},_isVerticallyAligned:function(f,d,e){return Math.abs(e.y-d.y)<f.height/2
}});
mindplot.DragConnector.MAX_VERTICAL_CONNECTION_TOLERANCE=80;mindplot.TextEditor=new Class({initialize:function(b){this._topic=b
},_buildEditor:function(){this._size={width:500,height:100};
var g=new Element("div");
g.setStyles({position:"absolute",display:"none",zIndex:"8",width:"500px",height:"100px"});
var h=new Element("div");
h.setStyles({border:"none",overflow:"auto"});
h.inject(g);
var j=new Element("input",{type:"text",tabindex:"-1",value:""});
j.setStyles({border:"none",background:"transparent"});
j.inject(h);
var i=new Element("div");
i.setStyle("visibility","hidden");
i.inject(g);
var f=new Element("span",{tabindex:"-1"});
f.setStyle("white-space","nowrap");
f.setStyle("nowrap","nowrap");
f.inject(i);
return g
},_registerEvents:function(g){var h=this._getTextareaElem();
var f=this._getSpanElem();
var e=this;
g.addEvent("keydown",function(a){switch(a.key){case"esc":e.close(false);
break;
case"enter":e.close(true);
break;
default:f.innerHTML=h.value;
var b=h.value.length+1;
h.size=b;
if(f.offsetWidth>(parseInt(g.style.width)-100)){g.style.width=(f.offsetWidth+100)+"px"
}break
}a.stopPropagation()
});
g.addEvent("click",function(a){a.stopPropagation()
});
g.addEvent("dblclick",function(a){a.stopPropagation()
});
g.addEvent("mousedown",function(a){a.stopPropagation()
})
},isVisible:function(){return $defined(this._containerElem)&&this._containerElem.getStyle("display")=="block"
},_updateModel:function(){if(this._topic.getText()!=this._getText()){var f=this._getText();
var e=this._topic.getId();
var d=mindplot.ActionDispatcher.getInstance();
d.changeTextToTopic([e],f)
}},show:function(c){if(!this.isVisible()){var d=this._buildEditor();
d.inject($(document.body)[0]);
this._containerElem=d;
this._registerEvents(d);
this._showEditor(c)
}},_showEditor:function(n){var h=this._topic;
h.getTextShape().setVisibility(false);
var l=h.getTextShape();
var i=l.getFont();
i.size=l.getHtmlFontSize();
i.color=l.getColor();
this._setStyle(i);
var j=$defined(n)?n:h.getText();
this._setText(j);
var k=this;
var m=function(){var c=k._topic.getTextShape();
k._containerElem.css("display","block");
k._containerElem.offset(c.getNativePosition());
var b=h.getSize();
k._setEditorSize(b.width,b.height);
var a=k._getTextareaElem();
a.focus();
k._positionCursor(a,!$defined(n))
};
m.delay(10)
},_setStyle:function(f){var e=this._getTextareaElem();
var d=this._getSpanElem();
if(!$defined(f.font)){f.font="Arial"
}if(!$defined(f.style)){f.style="normal"
}if(!$defined(f.weight)){f.weight="normal"
}if(!$defined(f.size)){f.size=12
}e.style.fontSize=f.size+"px";
e.style.fontFamily=f.font;
e.style.fontStyle=f.style;
e.style.fontWeight=f.weight;
e.style.color=f.color;
d.style.fontFamily=f.font;
d.style.fontStyle=f.style;
d.style.fontWeight=f.weight;
d.style.fontSize=f.size+"px"
},_setText:function(f){var e=this._getTextareaElem();
e.size=f.length+1;
this._containerElem.style.width=(e.size*parseInt(e.style.fontSize)+100)+"px";
var d=this._getSpanElem();
d.innerHTML=f;
e.value=f
},_getText:function(){return this._getTextareaElem().value
},_getTextareaElem:function(){return this._containerElem.getElement("input")
},_getSpanElem:function(){return this._containerElem.getElement("span")
},_setEditorSize:function(e,f){var h=this._topic.getTextShape();
var g=web2d.peer.utils.TransformUtil.workoutScale(h._peer);
this._size={width:e*g.width,height:f*g.height};
this._containerElem.style.width=this._size.width*2+"px";
this._containerElem.style.height=this._size.height+"px"
},_positionCursor:function(h,e){if(h.createTextRange){var f=h.createTextRange();
var g=h.value.length;
if(!e){f.select();
f.move("character",g)
}else{f.move("character",g);
f.select()
}}else{if(!e){h.setSelectionRange(0,h.value.length)
}}},close:function(b){if(this.isVisible()){if(!$defined(b)||b){this._updateModel()
}this._topic.getTextShape().setVisibility(true);
this._containerElem.dispose();
this._containerElem=null
}}});mindplot.MultilineTextEditor=new Class({Extends:mindplot.Events,initialize:function(){this._topic=null;
this._timeoutId=-1
},_buildEditor:function(){var d=$("<div></div>").attr("id","textContainer").css({display:"none",zIndex:"8",overflow:"hidden",border:"0 none"});
var c=$('<textarea tabindex="-1" value="" wrap="off" ></textarea>').css({border:"1px gray dashed",background:"rgba(98, 135, 167, .3)",outline:"0 none",resize:"none",overflow:"hidden"});
d.append(c);
return d
},_registerEvents:function(e){var f=this._getTextareaElem();
var d=this;
f.on("keydown",function(c){switch(jQuery.hotkeys.specialKeys[c.keyCode]){case"esc":d.close(false);
break;
case"enter":if(c.metaKey||c.ctrlKey){var b=f.val();
var p=b.length;
if(f.selectionStart){p=f.selectionStart
}var l=b.substring(0,p);
var m="";
if(p<b.length){m=b.substring(p,b.length)
}f.val(l+"\n"+m);
if(f[0].setSelectionRange){f.focus();
f[0].setSelectionRange(p+1,p+1)
}else{if(f.createTextRange){var n=f.createTextRange();
n.moveStart("character",p+1);
n.select()
}}}else{d.close(true)
}break;
case"tab":c.preventDefault();
var a=$(this).get(0).selectionStart;
var o=$(this).get(0).selectionEnd;
$(this).val($(this).val().substring(0,a)+"\t"+$(this).val().substring(o));
$(this).get(0).selectionStart=$(this).get(0).selectionEnd=a+1;
break
}c.stopPropagation()
});
f.on("keypress",function(a){a.stopPropagation()
});
f.on("keyup",function(b){var a=d._getTextareaElem().val();
d.fireEvent("input",[b,a]);
d._adjustEditorSize()
});
e.on("click",function(a){a.stopPropagation()
});
e.on("dblclick",function(a){a.stopPropagation()
});
e.on("mousedown",function(a){a.stopPropagation()
})
},_adjustEditorSize:function(){if(this.isVisible()){var f=this._getTextareaElem();
var d=f.val().split("\n");
var e=1;
_.each(d,function(a){if(e<a.length){e=a.length
}});
f.attr("cols",e);
f.attr("rows",d.length);
this._containerElem.css({width:(e+3)+"em",height:f.height()})
}},isVisible:function(){return $defined(this._containerElem)&&this._containerElem.css("display")=="block"
},_updateModel:function(){if(this._topic.getText()!=this._getText()){var f=this._getText();
var e=this._topic.getId();
var d=mindplot.ActionDispatcher.getInstance();
d.changeTextToTopic([e],f)
}},show:function(d,f){if(this._topic){this.close(false)
}this._topic=d;
if(!this.isVisible()){var e=this._buildEditor();
$("body").append(e);
this._containerElem=e;
this._registerEvents(e);
this._showEditor(f)
}},_showEditor:function(l){var g=this._topic;
g.getTextShape().setVisibility(false);
var j=g.getTextShape();
var h=j.getFont();
h.size=j.getHtmlFontSize();
h.color=j.getColor();
this._setStyle(h);
var i=this;
var k=function(){var c=g.getTextShape();
i._containerElem.css("display","block");
var b=c.getNativePosition();
i._containerElem.offset(b);
var a=$defined(l)?l:g.getText();
i._setText(a);
var d=i._getTextareaElem();
i._positionCursor(d,!$defined(l))
};
this._timeoutId=k.delay(10)
},_setStyle:function(f){var e=this._getTextareaElem();
if(!$defined(f.font)){f.font="Arial"
}if(!$defined(f.style)){f.style="normal"
}if(!$defined(f.weight)){f.weight="normal"
}if(!$defined(f.size)){f.size=12
}var d={fontSize:f.size+"px",fontFamily:f.font,fontStyle:f.style,fontWeight:f.weight,color:f.color};
e.css(d);
this._containerElem.css(d)
},_setText:function(d){var c=this._getTextareaElem();
c.val(d);
this._adjustEditorSize()
},_getText:function(){return this._getTextareaElem().val()
},_getTextareaElem:function(){return this._containerElem.find("textarea")
},_positionCursor:function(h,i){h.focus();
var j=h.val().length;
if(i){if(h.createTextRange){var f=h.createTextRange();
f.select();
f.move("character",j)
}else{h[0].setSelectionRange(0,j)
}}else{if(h.createTextRange){var g=h.createTextRange();
g.move("character",j)
}else{if(Browser.ie11){h[0].selectionStart=j;
h[0].selectionEnd=j
}else{h.selectionStart=j;
h.selectionEnd=j
}h.focus()
}}},close:function(b){if(this.isVisible()&&this._topic){clearTimeout(this._timeoutId);
if(!$defined(b)||b){this._updateModel()
}this._topic.getTextShape().setVisibility(true);
this._containerElem.remove();
this._containerElem=null;
this._timeoutId=-1
}this._topic=null
}});mindplot.TextEditorFactory={};
mindplot.TextEditorFactory.getTextEditorFromName=function(c){var d=null;
if(c=="RichTextEditor"){d=mindplot.RichTextEditor
}else{d=mindplot.TextEditor
}return d
};mindplot.util.Shape={isAtRight:function(d,c){$assert(d,"Source can not be null");
$assert(c,"Target can not be null");
return d.x<c.x
},calculateRectConnectionPoint:function(j,f,h){$assert(j,"rectCenterPoint can  not be null");
$assert(f,"rectSize can  not be null");
$assert($defined(h),"isRight can  not be null");
var g=new core.Point();
var i=2;
if(h){g.setValue(j.x-(f.width/2)+i,j.y)
}else{g.setValue(parseFloat(j.x)+(f.width/2)-i,j.y)
}return g
},calculateRelationShipPointCoordinates:function(s,p){var l=s.getSize();
var r=s.getPosition();
var u;
var v=r.y-p.y;
var q=r.x-p.x;
var t=Math.abs(v)<5||Math.abs(q)<5||Math.abs(v-q)<5;
var o,m;
var n=5;
if(p.y>r.y+(l.height/2)){o=r.y+(l.height/2)+n;
m=!t?r.x-((r.y-o)/(v/q)):r.x;
if(m>r.x+(l.width/2)){m=r.x+(l.width/2)
}else{if(m<r.x-(l.width/2)){m=r.x-(l.width/2)
}}}else{if(p.y<r.y-(l.height/2)){o=r.y-(l.height/2)-n;
m=!t?r.x-((r.y-o)/(v/q)):r.x;
if(m>r.x+(l.width/2)){m=r.x+(l.width/2)
}else{if(m<r.x-(l.width/2)){m=r.x-(l.width/2)
}}}else{if(p.x<(r.x-l.width/2)){m=r.x-(l.width/2)-n;
o=!t?r.y-((v/q)*(r.x-m)):r.y
}else{m=r.x+(l.width/2)+n;
o=!t?r.y-((v/q)*(r.x-m)):r.y
}}}return new core.Point(m,o)
},calculateDefaultControlPoints:function(s,u){var o=s.y-u.y;
var l=s.x-u.x;
var w=(Math.abs(l)>0.1?l:0.1);
var t=o/w;
var r=Math.sqrt(o*o+l*l)/3;
var q=1;
if(s.x>u.x){q=-1
}var v=s.x+Math.sqrt(r*r/(1+(t*t)))*q;
var m=t*(v-s.x)+s.y;
var x=u.x+Math.sqrt(r*r/(1+(t*t)))*q*-1;
var p=t*(x-u.x)+u.y;
return[new core.Point(-s.x+v,-s.y+m),new core.Point(-u.x+x,-u.y+p)]
},workoutIncomingConnectionPoint:function(m,n){$assert(n,"sourcePoint can not be null");
var j=m.getPosition();
var h=m.getSize();
var k=mindplot.util.Shape.isAtRight(n,j);
var i=mindplot.util.Shape.calculateRectConnectionPoint(j,h,k);
if(m.getShapeType()==mindplot.model.TopicShape.LINE){i.y=i.y+(m.getSize().height/2)
}var l=mindplot.Topic.CONNECTOR_WIDTH/2;
if(!k){i.x=i.x+l
}else{i.x=i.x-l
}i.x=Math.ceil(i.x);
i.y=Math.ceil(i.y);
return i
}};mindplot.util.FadeEffect=new Class({Extends:mindplot.Events,initialize:function(c,d){this._isVisible=d;
this._element=c
},start:function(){var b=this._isVisible;
_.each(this._element,function(a){if(a){a.setVisibility(b)
}});
this._isVisible=!b;
this.fireEvent("complete")
}});mindplot.persistence.ModelCodeName={BETA:"beta",PELA:"pela",TANGO:"tango"};mindplot.persistence.XMLSerializer_Pela=new Class({toXML:function(i){$assert(i,"Can not save a null mindmap");
var p=core.Utils.createDocument();
var j=p.createElement("map");
var y=i.getId();
if($defined(y)){j.setAttribute("name",this.rmXmlInv(y))
}var q=i.getVersion();
if($defined(q)){j.setAttribute("version",q)
}p.appendChild(j);
var z=i.getBranches();
for(var u=0;
u<z.length;
u++){var t=z[u];
var w=this._topicToXML(p,t);
j.appendChild(w)
}var r=i.getRelationships();
if(r.length>0){for(var v=0;
v<r.length;
v++){var s=r[v];
if(i.findNodeById(s.getFromNode())!==null&&i.findNodeById(s.getToNode())!==null){var x=this._relationshipToXML(p,s);
j.appendChild(x)
}}}return p
},_topicToXML:function(j,T){var I=j.createElement("topic");
if(T.getType()==mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){I.setAttribute("central","true")
}else{var U=T.getPosition();
I.setAttribute("position",U.x+","+U.y);
var Q=T.getOrder();
if(typeof Q==="number"&&isFinite(Q)){I.setAttribute("order",Q)
}}var K=T.getText();
if($defined(K)){this._noteTextToXML(j,I,K)
}var ag=T.getShapeType();
if($defined(ag)){I.setAttribute("shape",ag);
if(ag==mindplot.model.TopicShape.IMAGE){I.setAttribute("image",T.getImageSize().width+","+T.getImageSize().height+":"+T.getImageUrl())
}}if(T.areChildrenShrunken()&&T.getType()!=mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){I.setAttribute("shrink","true")
}var G=T.getId();
I.setAttribute("id",G);
var H="";
var L=T.getFontFamily();
H+=(L?L:"")+";";
var ac=T.getFontSize();
H+=(ac?ac:"")+";";
var af=T.getFontColor();
H+=(af?af:"")+";";
var O=T.getFontWeight();
H+=(O?O:"")+";";
var aa=T.getFontStyle();
H+=(aa?aa:"")+";";
if($defined(L)||$defined(ac)||$defined(af)||$defined(O)||$defined(aa)){I.setAttribute("fontStyle",H)
}var V=T.getBackgroundColor();
if($defined(V)){I.setAttribute("bgColor",V)
}var ad=T.getBorderColor();
if($defined(ad)){I.setAttribute("brColor",ad)
}var M=T.getMetadata();
if($defined(M)){I.setAttribute("metadata",M)
}var W=T.getFeatures();
for(var N=0;
N<W.length;
N++){var X=W[N];
var Y=X.getType();
var ae=j.createElement(Y);
var P=X.getAttributes();
for(var J in P){var i=P[J];
if(J=="text"){var ah=j.createCDATASection(this.rmXmlInv(i));
ae.appendChild(ah)
}else{ae.setAttribute(J,this.rmXmlInv(i))
}}I.appendChild(ae)
}var S=T.getChildren();
for(var R=0;
R<S.length;
R++){var ab=S[R];
var Z=this._topicToXML(j,ab);
I.appendChild(Z)
}return I
},_noteTextToXML:function(g,j,h){if(h.indexOf("\n")==-1){j.setAttribute("text",this.rmXmlInv(h))
}else{var f=g.createElement("text");
var i=g.createCDATASection(this.rmXmlInv(h));
f.appendChild(i);
j.appendChild(f)
}},_relationshipToXML:function(k,i){var l=k.createElement("relationship");
l.setAttribute("srcTopicId",i.getFromNode());
l.setAttribute("destTopicId",i.getToNode());
var j=i.getLineType();
l.setAttribute("lineType",j);
if(j==mindplot.ConnectionLine.CURVED||j==mindplot.ConnectionLine.SIMPLE_CURVED){if($defined(i.getSrcCtrlPoint())){var g=i.getSrcCtrlPoint();
l.setAttribute("srcCtrlPoint",Math.round(g.x)+","+Math.round(g.y))
}if($defined(i.getDestCtrlPoint())){var h=i.getDestCtrlPoint();
l.setAttribute("destCtrlPoint",Math.round(h.x)+","+Math.round(h.y))
}}l.setAttribute("endArrow",i.getEndArrow());
l.setAttribute("startArrow",i.getStartArrow());
return l
},loadFromDom:function(q,i){$assert(q,"dom can not be null");
$assert(i,"mapId can not be null");
var r=q.documentElement;
$assert(r.tagName==mindplot.persistence.XMLSerializer_Pela.MAP_ROOT_NODE,"This seem not to be a map document.");
this._idsMap={};
var m=r.getAttribute("version");
var l=new mindplot.model.Mindmap(i,m);
var s=r.childNodes;
for(var p=0;
p<s.length;
p++){var t=s[p];
if(t.nodeType==1){switch(t.tagName){case"topic":var o=this._deserializeNode(t,l);
l.addBranch(o);
break;
case"relationship":var n=this._deserializeRelationship(t,l);
if(n!=null){l.addRelationship(n)
}break
}}}this._idsMap=null;
l.setId(i);
return l
},_deserializeNode:function(ak,al){var af=(ak.getAttribute("central")!=null)?mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE:mindplot.model.INodeModel.MAIN_TOPIC_TYPE;
var j=ak.getAttribute("id");
if($defined(j)){j=parseInt(j)
}if(this._idsMap[j]){j=null
}else{this._idsMap[j]=ak
}var W=al.createNode(af,j);
var Q=ak.getAttribute("text");
if($defined(Q)&&Q){W.setText(Q)
}var ag=ak.getAttribute("fontStyle");
if($defined(ag)&&ag){var M=ag.split(";");
if(M[0]){W.setFontFamily(M[0])
}if(M[1]){W.setFontSize(M[1])
}if(M[2]){W.setFontColor(M[2])
}if(M[3]){W.setFontWeight(M[3])
}if(M[4]){W.setFontStyle(M[4])
}}var ai=ak.getAttribute("shape");
if($defined(ai)){W.setShapeType(ai);
if(ai==mindplot.model.TopicShape.IMAGE){var I=ak.getAttribute("image");
var K=I.substring(0,I.indexOf(":"));
var ad=I.substring(I.indexOf(":")+1,I.length);
W.setImageUrl(ad);
var Y=K.split(",");
W.setImageSize(Y[0],Y[1])
}}var aa=ak.getAttribute("bgColor");
if($defined(aa)){W.setBackgroundColor(aa)
}var aj=ak.getAttribute("brColor");
if($defined(aj)){W.setBorderColor(aj)
}var S=ak.getAttribute("order");
if($defined(S)&&S!="NaN"){W.setOrder(parseInt(S))
}var J=ak.getAttribute("shrink");
if($defined(J)&&af!=mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){W.setChildrenShrunken(J)
}var L=ak.getAttribute("position");
if($defined(L)){var X=L.split(",");
W.setPosition(X[0],X[1])
}var R=ak.getAttribute("metadata");
if($defined(R)){W.setMetadata(R)
}var ae=ak.childNodes;
for(var O=0;
O<ae.length;
O++){var Z=ae[O];
if(Z.nodeType==Node.ELEMENT_NODE){if(Z.tagName=="topic"){var ah=this._deserializeNode(Z,al);
ah.connectTo(W)
}else{if(mindplot.TopicFeature.isSupported(Z.tagName)){var P=Z.attributes;
var V={};
for(var U=0;
U<P.length;
U++){var T=P.item(U);
V[T.name]=T.value
}var i=this._deserializeTextAttr(Z);
if(i){V.text=i
}var ac=Z.tagName;
var ab=mindplot.TopicFeature.createModel(ac,V);
W.addFeature(ab)
}else{if(Z.tagName=="text"){var N=this._deserializeNodeText(Z);
W.setText(N)
}}}}}return W
},_deserializeTextAttr:function(j){var i=j.getAttribute("text");
if(!$defined(i)){var f=j.childNodes;
for(var g=0;
g<f.length;
g++){var h=f[g];
if(h.nodeType==Node.CDATA_SECTION_NODE){i=h.nodeValue
}}}else{i=unescape(i);
if(i==""){i=" "
}}return i
},_deserializeNodeText:function(j){var f=j.childNodes;
var i=null;
for(var g=0;
g<f.length;
g++){var h=f[g];
if(h.nodeType==Node.CDATA_SECTION_NODE){i=h.nodeValue
}}return i
},_deserializeRelationship:function(p,l){var t=p.getAttribute("srcTopicId");
var q=p.getAttribute("destTopicId");
var n=p.getAttribute("lineType");
var r=p.getAttribute("srcCtrlPoint");
var k=p.getAttribute("destCtrlPoint");
var m=p.getAttribute("endArrow");
var s=p.getAttribute("startArrow");
if(t==q){return null
}if(l.findNodeById(t)==null||l.findNodeById(q)==null){return null
}var o=l.createRelationship(t,q);
o.setLineType(n);
if($defined(r)&&r!=""){o.setSrcCtrlPoint(core.Point.fromString(r))
}if($defined(k)&&k!=""){o.setDestCtrlPoint(core.Point.fromString(k))
}o.setEndArrow("false");
o.setStartArrow("true");
return o
},rmXmlInv:function(h){if(h==null||h==undefined){return null
}var f="";
for(var c=0;
c<h.length;
c++){var g=h.charCodeAt(c);
if((g==9)||(g==10)||(g==13)||((g>=32)&&(g<=55295))||((g>=57344)&&(g<=65533))||((g>=65536)&&(g<=1114111))){f=f+h.charAt(c)
}}return f
}});
mindplot.persistence.XMLSerializer_Pela.MAP_ROOT_NODE="map";mindplot.persistence.XMLSerializer_Tango=new Class({Extends:mindplot.persistence.XMLSerializer_Pela});mindplot.persistence.Pela2TangoMigrator=new Class({initialize:function(b){this._pelaSerializer=b;
this._tangoSerializer=new mindplot.persistence.XMLSerializer_Tango()
},toXML:function(b){return this._tangoSerializer.toXML(b)
},loadFromDom:function(f,d){$assert($defined(d),"mapId can not be null");
var e=this._pelaSerializer.loadFromDom(f,d);
e.setVersion(mindplot.persistence.ModelCodeName.TANGO);
this._fixOrder(e);
this._fixPosition(e);
return e
},_fixOrder:function(i){var m=i.getBranches()[0];
var n=m.getChildren();
var o=[];
var l=[];
for(var p=0;
p<n.length;
p++){var k=n[p];
var j=k.getPosition();
if(j.x<0){o.push(k)
}else{l.push(k)
}}l.sort(function(a,b){return a.getOrder()>b.getOrder()
});
o.sort(function(a,b){return a.getOrder()>b.getOrder()
});
for(p=0;
p<l.length;
p++){l[p].setOrder(p*2)
}for(p=0;
p<o.length;
p++){o[p].setOrder(p*2+1)
}},_fixPosition:function(g){var j=g.getBranches()[0];
var k=j.getChildren();
for(var l=0;
l<k.length;
l++){var i=k[l];
var h=i.getPosition();
this._fixNodePosition(i,h)
}},_fixNodePosition:function(k,j){var h=k.getPosition();
if(!h){h={x:j.x+30,y:j.y};
k.setPosition(h.x,h.y)
}var l=k.getChildren();
for(var g=0;
g<l.length;
g++){var i=l[g];
this._fixNodePosition(i,h)
}}});mindplot.persistence.XMLSerializer_Beta=new Class({toXML:function(i){$assert(i,"Can not save a null mindmap");
var j=core.Utils.createDocument();
var l=j.createElement("map");
var n=i.getId();
if($defined(n)){l.setAttribute("name",n)
}j.append(l);
var k=i.getBranches();
for(var m=0;
m<k.length;
m++){var o=k[m];
var p=this._topicToXML(j,o);
l.append(p)
}return j
},_topicToXML:function(H,G){var J=H.createElement("topic");
if(G.getType()==mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){J.setAttribute("central",true)
}else{var R=G.getParent();
if(R==null||R.getType()==mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){var U=G.getPosition();
J.setAttribute("position",U.x+","+U.y)
}else{var E=G.getOrder();
J.setAttribute("order",E)
}}var L=G.getText();
if($defined(L)){J.setAttribute("text",L)
}var af=G.getShapeType();
if($defined(af)){J.setAttribute("shape",af)
}if(G.areChildrenShrunken()){J.setAttribute("shrink",true)
}var I="";
var P=G.getFontFamily();
I+=(P?P:"")+";";
var aa=G.getFontSize();
I+=(aa?aa:"")+";";
var ad=G.getFontColor();
I+=(ad?ad:"")+";";
var N=G.getFontWeight();
I+=(N?N:"")+";";
var Y=G.getFontStyle();
I+=(Y?Y:"")+";";
if($defined(P)||$defined(aa)||$defined(ad)||$defined(N)||$defined(Y)){J.setAttribute("fontStyle",I)
}var V=G.getBackgroundColor();
if($defined(V)){J.setAttribute("bgColor",V)
}var ab=G.getBorderColor();
if($defined(ab)){J.setAttribute("brColor",ab)
}var i;
var K=G.getIcons();
for(i=0;
i<K.length;
i++){var M=K[i];
var Q=this._iconToXML(H,M);
J.append(Q)
}var ae=G.getLinks();
for(i=0;
i<ae.length;
i++){var S=ae[i];
var W=this._linkToXML(H,S);
J.append(W)
}var O=G.getNotes();
for(i=0;
i<O.length;
i++){var ac=O[i];
var F=this._noteToXML(H,ac);
J.append(F)
}var T=G.getChildren();
for(i=0;
i<T.length;
i++){var Z=T[i];
var X=this._topicToXML(H,Z);
J.append(X)
}return J
},_iconToXML:function(e,f){var d=e.createElement("icon");
d.setAttribute("id",f.getIconType());
return d
},_linkToXML:function(e,d){var f=e.createElement("link");
f.setAttribute("url",d.getUrl());
return f
},_noteToXML:function(e,f){var d=e.createElement("note");
d.setAttribute("text",f.getText());
return d
},loadFromDom:function(p,i){$assert(p,"Dom can not be null");
$assert(i,"mapId can not be null");
var k=p.documentElement;
$assert(k.nodeName!="parsererror","Error while parsing: '"+k.childNodes[0].nodeValue);
$assert(k.tagName==mindplot.persistence.XMLSerializer_Beta.MAP_ROOT_NODE,"This seem not to be a map document. Root Tag: '"+k.tagName+",',HTML:"+p.innerHTML+",XML:"+core.Utils.innerXML(p));
var m=k.getAttribute("version");
m=!$defined(m)?mindplot.persistence.ModelCodeName.BETA:m;
var l=new mindplot.model.Mindmap(i,m);
var q=k.childNodes;
for(var o=0;
o<q.length;
o++){var r=q[o];
if(r.nodeType==1){var n=this._deserializeNode(r,l);
l.addBranch(n)
}}l.setId(i);
return l
},_deserializeNode:function(O,P){var I=(O.getAttribute("central")!=null)?mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE:mindplot.model.INodeModel.MAIN_TOPIC_TYPE;
var A=P.createNode(I);
var C=O.getAttribute("text");
if($defined(C)){A.setText(C)
}var z=O.getAttribute("order");
if($defined(z)){A.setOrder(parseInt(z))
}var M=O.getAttribute("shape");
if($defined(M)){A.setShapeType(M)
}var i=O.getAttribute("shrink");
if($defined(i)){A.setChildrenShrunken(i)
}var J=O.getAttribute("fontStyle");
if($defined(J)){var B=J.split(";");
if(B[0]){A.setFontFamily(B[0])
}if(B[1]){A.setFontSize(B[1])
}if(B[2]){A.setFontColor(B[2])
}if(B[3]){A.setFontWeight(B[3])
}if(B[4]){A.setFontStyle(B[4])
}}var G=O.getAttribute("bgColor");
if($defined(G)){A.setBackgroundColor(G)
}var N=O.getAttribute("brColor");
if($defined(N)){A.setBorderColor(N)
}var w=O.getAttribute("position");
if($defined(w)){var F=w.split(",");
A.setPosition(F[0],F[1])
}var H=O.childNodes;
for(var y=0;
y<H.length;
y++){var E=H[y];
if(E.nodeType==1){$assert(E.tagName=="topic"||E.tagName=="icon"||E.tagName=="link"||E.tagName=="note","Illegal node type:"+E.tagName);
if(E.tagName=="topic"){var K=this._deserializeNode(E,P);
K.connectTo(A)
}else{if(E.tagName=="icon"){var x=this._deserializeIcon(E,A);
A.addFeature(x)
}else{if(E.tagName=="link"){var D=this._deserializeLink(E,A);
A.addFeature(D)
}else{if(E.tagName=="note"){var L=this._deserializeNote(E,A);
A.addFeature(L)
}}}}}}return A
},_deserializeIcon:function(c){var d=c.getAttribute("id");
d=d.replace("images/","icons/legacy/");
return mindplot.TopicFeature.createModel(mindplot.TopicFeature.Icon.id,{id:d})
},_deserializeLink:function(b){return mindplot.TopicFeature.createModel(mindplot.TopicFeature.Link.id,{url:b.getAttribute("url")})
},_deserializeNote:function(d){var c=d.getAttribute("text");
return mindplot.TopicFeature.createModel(mindplot.TopicFeature.Note.id,{text:c==null?" ":c})
}});
mindplot.persistence.XMLSerializer_Beta.MAP_ROOT_NODE="map";mindplot.persistence.Beta2PelaMigrator=new Class({initialize:function(b){this._betaSerializer=b;
this._pelaSerializer=new mindplot.persistence.XMLSerializer_Pela()
},toXML:function(b){return this._pelaSerializer.toXML(b)
},loadFromDom:function(h,j){$assert($defined(j),"mapId can not be null");
var g=this._betaSerializer.loadFromDom(h,j);
g.setVersion(mindplot.persistence.ModelCodeName.PELA);
var f=g.getBranches();
var i=this;
_.each(f,function(a){i._fixPosition(a)
});
return g
},_fixPosition:function(f){var g=f.getPosition();
var h=g.x>0;
var e=this;
_.each(f.getChildren(),function(a){if(!a.getPosition()){a.setPosition(g.x+(50*h?1:-1),g.y)
}e._fixPosition(a)
})
}});mindplot.persistence.XMLSerializerFactory={};
mindplot.persistence.XMLSerializerFactory.getSerializerFromMindmap=function(b){return mindplot.persistence.XMLSerializerFactory.getSerializer(b.getVersion())
};
mindplot.persistence.XMLSerializerFactory.getSerializerFromDocument=function(c){var d=c.documentElement;
return mindplot.persistence.XMLSerializerFactory.getSerializer(d.getAttribute("version"))
};
mindplot.persistence.XMLSerializerFactory.getSerializer=function(g){if(!$defined(g)){g=mindplot.persistence.ModelCodeName.BETA
}var h=mindplot.persistence.XMLSerializerFactory._codeNames;
var i=false;
var j=null;
for(var k=0;
k<h.length;
k++){if(!i){i=h[k].codeName==g;
if(i){j=new (h[k].serializer)()
}}else{var l=h[k].migrator;
j=new l(j)
}}return j
};
mindplot.persistence.XMLSerializerFactory._codeNames=[{codeName:mindplot.persistence.ModelCodeName.BETA,serializer:mindplot.persistence.XMLSerializer_Beta,migrator:function(){}},{codeName:mindplot.persistence.ModelCodeName.PELA,serializer:mindplot.persistence.XMLSerializer_Pela,migrator:mindplot.persistence.Beta2PelaMigrator},{codeName:mindplot.persistence.ModelCodeName.TANGO,serializer:mindplot.persistence.XMLSerializer_Tango,migrator:mindplot.persistence.Pela2TangoMigrator}];mindplot.PersistenceManager=new Class({Static:{loadFromDom:function(d,e){$assert(d,"mapId can not be null");
$assert(e,"mapDom can not be null");
var f=mindplot.persistence.XMLSerializerFactory.getSerializerFromDocument(e);
return f.loadFromDom(e,d)
}},initialize:function(){},save:function(p,t,o,m,s){$assert(p,"mindmap can not be null");
$assert(t,"editorProperties can not be null");
var n=p.getId();
$assert(n,"mapId can not be null");
var q=mindplot.persistence.XMLSerializerFactory.getSerializerFromMindmap(p);
var r=q.toXML(p);
var v=core.Utils.innerXML(r);
var e=JSON.stringify(t);
try{this.saveMapXml(n,v,e,o,m,s)
}catch(u){console.log(u);
m.onError(this._buildError())
}},load:function(d){$assert(d,"mapId can not be null");
var c=this.loadMapDom(d);
return mindplot.PersistenceManager.loadFromDom(d,c)
},discardChanges:function(b){throw new Error("Method must be implemented")
},loadMapDom:function(b){throw new Error("Method must be implemented")
},saveMapXml:function(l,j,h,i,g,k){throw new Error("Method must be implemented")
},unlockMap:function(b){throw new Error("Method must be implemented")
}});
mindplot.PersistenceManager.init=function(b){mindplot.PersistenceManager._instance=b
};
mindplot.PersistenceManager.getInstance=function(){return mindplot.PersistenceManager._instance
};mindplot.RESTPersistenceManager=new Class({Extends:mindplot.PersistenceManager,initialize:function(b){this.parent();
$assert(b.documentUrl,"documentUrl can not be null");
$assert(b.revertUrl,"revertUrl can not be null");
$assert(b.lockUrl,"lockUrl can not be null");
$assert(b.session,"session can not be null");
$assert(b.timestamp,"timestamp can not be null");
this.documentUrl=b.documentUrl;
this.revertUrl=b.revertUrl;
this.lockUrl=b.lockUrl;
this.timestamp=b.timestamp;
this.session=b.session
},saveMapXml:function(l,r,j,m,k,o){var q={id:l,xml:r,properties:j};
var n=this;
var p="minor="+!m;
p=p+"&timestamp="+this.timestamp;
p=p+"&session="+this.session;
if(!n.onSave){n.onSave=true;
n.clearTimeout=setTimeout(function(){n.clearTimeout=null;
n.onSave=false
},10000);
$.ajax({url:this.documentUrl.replace("{id}",l)+"?"+p,type:"put",dataType:"json",data:JSON.stringify(q),contentType:"application/json; charset=utf-8",async:!o,success:function(b,a,c){n.timestamp=b;
k.onSuccess()
},error:function(c,a,b){k.onError(n._buildError())
},complete:function(){if(n.clearTimeout){clearTimeout(n.clearTimeout)
}n.onSave=false
},fail:function(g,e){var b=g.responseText;
var d={severity:"SEVERE",message:$msg("SAVE_COULD_NOT_BE_COMPLETED")};
var f=g.getResponseHeader("Content-Type");
if(f!=null&&f.indexOf("application/json")!=-1){var c=null;
try{c=$.parseJSON(b);
c=c.globalSeverity?c:null
}catch(a){}d=n._buildError(c)
}else{if(this.status==405){d={severity:"SEVERE",message:$msg("SESSION_EXPIRED")}
}}k.onError(d);
n.onSave=false
}})
}},discardChanges:function(b){$.ajax({url:this.revertUrl.replace("{id}",b),async:false,method:"post",headers:{"Content-Type":"application/json; charset=utf-8",Accept:"application/json"}})
},unlockMap:function(d){var c=d.getId();
$.ajax({url:this.lockUrl.replace("{id}",c),async:false,method:"put",headers:{"Content-Type":"text/plain"},data:"false"})
},_buildError:function(f){var d=f?f.globalErrors[0]:null;
var e=f?f.globalSeverity:null;
if(!d){d=$msg("SAVE_COULD_NOT_BE_COMPLETED")
}if(!e){e="INFO"
}return{severity:e,message:d}
},loadMapDom:function(c){var d;
$.ajax({url:this.documentUrl.replace("{id}",c)+"/xml",method:"get",async:false,headers:{"Content-Type":"text/plain",Accept:"application/xml"},success:function(a){d=a
}});
if(d==null){throw new Error("Map could not be loaded")
}return d
}});mindplot.LocalStorageManager=new Class({Extends:mindplot.PersistenceManager,initialize:function(d,c){this.parent();
this.documentUrl=d;
this.forceLoad=c
},saveMapXml:function(j,i,g,h,f){localStorage.setItem(j+"-xml",i)
},discardChanges:function(b){localStorage.removeItem(b+"-xml")
},loadMapDom:function(c){var d=localStorage.getItem(c+"-xml");
if(d==null||this.forceLoad){$.ajax({url:this.documentUrl.replace("{id}",c),headers:{"Content-Type":"text/plain",Accept:"application/xml"},type:"get",dataType:"text",async:false,success:function(a){d=a
}});
if(d==null){throw new Error("Map could not be loaded")
}}return jQuery.parseXML(d)
},unlockMap:function(b){}});mindplot.EditorProperties=new Class({initialize:function(){this._zoom=0;
this._position=0
},setZoom:function(b){this._zoom=b
},getZoom:function(){return this._zoom
},asProperties:function(){return"zoom="+this._zoom+"\n"
}});mindplot.IconGroup=new Class({initialize:function(c,d){$assert($defined(c),"topicId can not be null");
$assert($defined(d),"iconSize can not be null");
this._icons=[];
this._group=new web2d.Group({width:0,height:d,x:0,y:0,coordSizeWidth:0,coordSizeHeight:100});
this._removeTip=new mindplot.IconGroup.RemoveTip(this._group,c);
this.seIconSize(d,d);
this._registerListeners()
},setPosition:function(d,c){this._group.setPosition(d,c)
},getPosition:function(){return this._group.getPosition()
},getNativeElement:function(){return this._group
},getSize:function(){return this._group.getSize()
},seIconSize:function(c,d){this._iconSize={width:c,height:d};
this._resize(this._icons.length)
},addIcon:function(d,e){$defined(d,"icon is not defined");
d.setGroup(this);
this._icons.push(d);
this._resize(this._icons.length);
this._positionIcon(d,this._icons.length-1);
var f=d.getImage();
this._group.append(f);
if(e){this._removeTip.decorate(this._topicId,d)
}},_findIconFromModel:function(c){var d=null;
_.each(this._icons,function(a){var b=a.getModel();
if(b.getId()==c.getId()){d=a
}},this);
if(d==null){throw new Error("Icon can no be found:"+c.getId()+", Icons:"+this._icons)
}return d
},removeIconByModel:function(c){$assert(c,"featureModel can not be null");
var d=this._findIconFromModel(c);
this._removeIcon(d)
},_removeIcon:function(d){$assert(d,"icon can not be null");
this._removeTip.close(0);
this._group.removeChild(d.getImage());
this._icons.erase(d);
this._resize(this._icons.length);
var c=this;
_.each(this._icons,function(a,b){c._positionIcon(a,b)
})
},moveToFront:function(){this._group.moveToFront()
},_registerListeners:function(){this._group.addEvent("click",function(b){b.stopPropagation()
});
this._group.addEvent("dblclick",function(b){b.stopPropagation()
})
},_resize:function(c){this._group.setSize(c*this._iconSize.width,this._iconSize.height);
var d=mindplot.Icon.SIZE+(mindplot.IconGroup.ICON_PADDING*2);
this._group.setCoordSize(c*d,d)
},_positionIcon:function(f,d){var e=mindplot.Icon.SIZE+(mindplot.IconGroup.ICON_PADDING*2);
f.getImage().setPosition(e*d+mindplot.IconGroup.ICON_PADDING,mindplot.IconGroup.ICON_PADDING)
}});
mindplot.IconGroup.ICON_PADDING=5;
mindplot.IconGroup.RemoveTip=new Class({initialize:function(b){$assert(b,"group can not be null");
this._fadeElem=b
},show:function(f,g){$assert(g,"icon can not be null");
if(this._activeIcon!=g){if(this._activeIcon){this.close(0)
}var h=g.getPosition();
var i=this._buildWeb2d();
i.addEvent("click",function(){g.remove()
});
var j=this;
i.addEvent("mouseover",function(){j.show(f,g)
});
i.addEvent("mouseout",function(){j.hide()
});
i.setPosition(h.x+80,h.y-50);
this._fadeElem.append(i);
this._activeIcon=g;
this._widget=i
}else{clearTimeout(this._closeTimeoutId)
}},hide:function(){this.close(200)
},close:function(f){if(this._closeTimeoutId){clearTimeout(this._closeTimeoutId)
}var e=this;
if(this._activeIcon){var h=this._widget;
var g=function(){e._activeIcon=null;
e._fadeElem.removeChild(h);
e._widget=null;
e._closeTimeoutId=null
};
if(!$defined(f)||f==0){g()
}else{this._closeTimeoutId=g.delay(f)
}}},_buildWeb2d:function(){var f=new web2d.Group({width:10,height:10,x:0,y:0,coordSizeWidth:10,coordSizeHeight:10});
var i=new web2d.Rect(0,{x:0,y:0,width:10,height:10,stroke:"0",fillColor:"black"});
f.append(i);
i.setCursor("pointer");
var h=new web2d.Rect(0,{x:1,y:1,width:8,height:8,stroke:"1 solid white",fillColor:"gray"});
f.append(h);
var j=new web2d.Line({stroke:"1 solid white"});
j.setFrom(1,1);
j.setTo(9,9);
f.append(j);
var g=new web2d.Line({stroke:"1 solid white"});
g.setFrom(1,9);
g.setTo(9,1);
f.append(g);
f.addEvent("mouseover",function(){h.setFill("#CC0033")
});
f.addEvent("mouseout",function(){h.setFill("gray")
});
f.setSize(50,50);
return f
},decorate:function(d,e){var f=this;
if(!e.__remove){e.addEvent("mouseover",function(){f.show(d,e)
});
e.addEvent("mouseout",function(){f.hide()
});
e.__remove=true
}}});mindplot.Icon=new Class({initialize:function(b){$assert(b,"topic can not be null");
this._image=new web2d.Image();
this._image.setHref(b);
this._image.setSize(mindplot.Icon.SIZE,mindplot.Icon.SIZE)
},getImage:function(){return this._image
},setGroup:function(b){this._group=b
},getGroup:function(){return this._group
},getSize:function(){return this._image.getSize()
},getPosition:function(){return this._image.getPosition()
},addEvent:function(d,c){this._image.addEvent(d,c)
},remove:function(){throw"Unsupported operation"
}});
mindplot.Icon.SIZE=90;mindplot.LinkIcon=new Class({Extends:mindplot.Icon,initialize:function(d,e,f){$assert(d,"topic can not be null");
$assert(e,"linkModel can not be null");
this.parent(mindplot.LinkIcon.IMAGE_URL);
this._linksModel=e;
this._topic=d;
this._readOnly=f;
this._registerEvents()
},_registerEvents:function(){this._image.setCursor("pointer");
this._tip=new mindplot.widget.LinkIconTooltip(this);
var b=this;
if(!this._readOnly){this.addEvent("click",function(a){b._tip.hide();
b._topic.showLinkEditor();
a.stopPropagation()
});
this.addEvent("mouseleave",function(a){window.setTimeout(function(){if(!$("#linkPopover:hover").length){b._tip.hide()
}a.stopPropagation()
},100)
})
}$(this.getImage()._peer._native).mouseenter(function(){b._tip.show()
})
},getModel:function(){return this._linksModel
}});
mindplot.LinkIcon.IMAGE_URL="images/links.png";mindplot.NoteIcon=new Class({Extends:mindplot.Icon,initialize:function(e,f,d){$assert(e,"topic can not be null");
this.parent(mindplot.NoteIcon.IMAGE_URL);
this._linksModel=f;
this._topic=e;
this._readOnly=d;
this._registerEvents()
},_registerEvents:function(){this._image.setCursor("pointer");
var b=this;
if(!this._readOnly){this.addEvent("click",function(a){b._topic.showNoteEditor();
a.stopPropagation()
})
}this._tip=new mindplot.widget.FloatingTip($(b.getImage()._peer._native),{title:$msg("NOTE"),container:"body",content:function(){return b._buildTooltipContent()
},html:true,placement:"bottom",destroyOnExit:true})
},_buildTooltipContent:function(){if($("body").find("#textPopoverNote").length==1){var c=$("body").find("#textPopoverNote");
c.text(this._linksModel.getText())
}else{var d=$('<div id="textPopoverNote"></div>').css({padding:"5px"});
var c=$("<div></div>").text(this._linksModel.getText()).css({"white-space":"pre-wrap","word-wrap":"break-word"});
d.append(c);
return d
}},getModel:function(){return this._linksModel
}});
mindplot.NoteIcon.IMAGE_URL="images/notes.png";mindplot.ActionIcon=new Class({Extends:mindplot.Icon,initialize:function(c,d){this.parent(d);
this._node=c
},getNode:function(){return this._node
},setPosition:function(e,f){var d=this.getSize();
this.getImage().setPosition(e-d.width/2,f-d.height/2)
},addEvent:function(c,d){this.getImage().addEvent(c,d)
},addToGroup:function(b){b.append(this.getImage())
},setVisibility:function(b){this.getImage().setVisibility(b)
},isVisible:function(){return this.getImage().isVisible()
},setCursor:function(b){return this.getImage().setCursor(b)
},moveToBack:function(b){return this.getImage().moveToBack(b)
},moveToFront:function(b){return this.getImage().moveToFront(b)
}});mindplot.ImageIcon=new Class({Extends:mindplot.Icon,initialize:function(h,m,j){$assert(m,"iconModel can not be null");
$assert(h,"topic can not be null");
this._topicId=h.getId();
this._featureModel=m;
var i=m.getIconType();
var k=this._getImageUrl(i);
this.parent(k);
if(!j){var l=this.getImage();
var n=this;
l.addEvent("click",function(){var c=m.getIconType();
var b=n._getNextFamilyIconId(c);
m.setIconType(b);
var a=n._getImageUrl(b);
n._image.setHref(a)
});
this._image.setCursor("pointer")
}},_getImageUrl:function(b){return"icons/"+b+".png"
},getModel:function(){return this._featureModel
},_getNextFamilyIconId:function(h){var g=this._getFamilyIcons(h);
$assert(g!=null,"Family Icon not found!");
var f=null;
for(var e=0;
e<g.length&&f==null;
e++){if(g[e]==h){if(e==(g.length-1)){f=g[0]
}else{f=g[e+1]
}break
}}return f
},_getFamilyIcons:function(h){$assert(h!=null,"id must not be null");
$assert(h.indexOf("_")!=-1,"Invalid icon id (it must contain '_')");
var g=null;
for(var f=0;
f<mindplot.ImageIcon.prototype.ICON_FAMILIES.length;
f++){var i=mindplot.ImageIcon.prototype.ICON_FAMILIES[f];
var j=h.substr(0,h.indexOf("_"));
if(i.id==j){g=i.icons;
break
}}return g
},remove:function(){var d=mindplot.ActionDispatcher.getInstance();
var f=this._featureModel.getId();
var e=this._topicId;
d.removeFeatureFromTopic(e,f)
}});
mindplot.ImageIcon.prototype.ICON_FAMILIES=[{id:"face",icons:["face_plain","face_sad","face_crying","face_smile","face_surprise","face_wink"]},{id:"funy",icons:["funy_angel","funy_devilish","funy_glasses","funy_grin","funy_kiss","funy_monkey"]},{id:"conn",icons:["conn_connect","conn_disconnect"]},{id:"sport",icons:["sport_basketball","sport_football","sport_golf","sport_raquet","sport_shuttlecock","sport_soccer","sport_tennis"]},{id:"bulb",icons:["bulb_light_on","bulb_light_off"]},{id:"thumb",icons:["thumb_thumb_up","thumb_thumb_down"]},{id:"tick",icons:["tick_tick","tick_cross"]},{id:"onoff",icons:["onoff_clock","onoff_clock_red","onoff_add","onoff_delete","onoff_status_offline","onoff_status_online"]},{id:"money",icons:["money_money","money_dollar","money_euro","money_pound","money_yen","money_coins","money_ruby"]},{id:"time",icons:["time_calendar","time_clock","time_hourglass"]},{id:"number",icons:["number_1","number_2","number_3","number_4","number_5","number_6","number_7","number_8","number_9"]},{id:"chart",icons:["chart_bar","chart_line","chart_curve","chart_pie","chart_organisation"]},{id:"sign",icons:["sign_warning","sign_info","sign_stop","sign_help","sign_cancel"]},{id:"hard",icons:["hard_cd","hard_computer","hard_controller","hard_driver_disk","hard_ipod","hard_keyboard","hard_mouse","hard_printer"]},{id:"soft",icons:["soft_bug","soft_cursor","soft_database_table","soft_database","soft_feed","soft_folder_explore","soft_rss","soft_penguin"]},{id:"arrow",icons:["arrow_up","arrow_down","arrow_left","arrow_right"]},{id:"arrowc",icons:["arrowc_rotate_anticlockwise","arrowc_rotate_clockwise","arrowc_turn_left","arrowc_turn_right"]},{id:"people",icons:["people_group","people_male1","people_male2","people_female1","people_female2"]},{id:"mail",icons:["mail_envelop","mail_mailbox","mail_edit","mail_list"]},{id:"flag",icons:["flag_blue","flag_green","flag_orange","flag_pink","flag_purple","flag_yellow"]},{id:"bullet",icons:["bullet_black","bullet_blue","bullet_green","bullet_orange","bullet_red","bullet_pink","bullet_purple"]},{id:"tag",icons:["tag_blue","tag_green","tag_orange","tag_red","tag_pink","tag_yellow"]},{id:"object",icons:["object_bell","object_clanbomber","object_key","object_pencil","object_phone","object_magnifier","object_clip","object_music","object_star","object_wizard","object_house","object_cake","object_camera","object_palette","object_rainbow"]},{id:"weather",icons:["weather_clear-night","weather_clear","weather_few-clouds-night","weather_few-clouds","weather_overcast","weather_severe-alert","weather_showers-scattered","weather_showers","weather_snow","weather_storm"]},{id:"task",icons:["task_0","task_25","task_50","task_75","task_100"]}];mindplot.model.FeatureModel=new Class({Static:{_nextUUID:function(){if(!$defined(mindplot.model.FeatureModel._uuid)){mindplot.model.FeatureModel._uuid=0
}mindplot.model.FeatureModel._uuid=mindplot.model.FeatureModel._uuid+1;
return mindplot.model.FeatureModel._uuid
}},initialize:function(b){$assert(b,"type can not be null");
this._id=mindplot.model.FeatureModel._nextUUID();
this._type=b;
this._attributes={};
this["is"+$.camelCase(b)+"Model"]=function(){return true
}
},getAttributes:function(){return Object.clone(this._attributes)
},setAttributes:function(b){for(key in b){this["set"+key.capitalize()](b[key])
}},setAttribute:function(d,c){$assert(d,"key id can not be null");
this._attributes[d]=c
},getAttribute:function(b){$assert(b,"key id can not be null");
return this._attributes[b]
},getId:function(){return this._id
},setId:function(b){this._id=b
},getType:function(){return this._type
}});mindplot.model.IconModel=new Class({Extends:mindplot.model.FeatureModel,initialize:function(b){this.parent(mindplot.model.IconModel.FEATURE_TYPE);
this.setIconType(b.id)
},getIconType:function(){return this.getAttribute("id")
},setIconType:function(b){$assert(b,"iconType id can not be null");
this.setAttribute("id",b)
}});
mindplot.model.IconModel.FEATURE_TYPE="icon";mindplot.model.LinkModel=new Class({Extends:mindplot.model.FeatureModel,initialize:function(b){this.parent(mindplot.model.LinkModel.FEATURE_TYPE);
this.setUrl(b.url)
},getUrl:function(){return this.getAttribute("url")
},setUrl:function(e){$assert(e,"url can not be null");
var f=this._fixUrl(e);
this.setAttribute("url",f);
var d=f.contains("mailto:")?"mail":"url";
this.setAttribute("urlType",d)
},_fixUrl:function(c){var d=c;
if(!d.contains("http://")&&!d.contains("https://")&&!d.contains("mailto://")){d="http://"+d
}return d
},setUrlType:function(b){$assert(b,"urlType can not be null");
this.setAttribute("urlType",b)
}});
mindplot.model.LinkModel.FEATURE_TYPE="link";mindplot.model.NoteModel=new Class({Extends:mindplot.model.FeatureModel,initialize:function(c){this.parent(mindplot.model.NoteModel.FEATURE_TYPE);
var d=c.text?c.text:" ";
this.setText(d)
},getText:function(){return this.getAttribute("text")
},setText:function(b){$assert(b,"text can not be null");
this.setAttribute("text",b)
}});
mindplot.model.NoteModel.FEATURE_TYPE="note";mindplot.Command=new Class({initialize:function(){this._id=mindplot.Command._nextUUID()
},execute:function(b){throw"execute must be implemented."
},undoExecute:function(b){throw"undo must be implemented."
},getId:function(){return this._id
}});
mindplot.Command._nextUUID=function(){if(!$defined(mindplot.Command._uuid)){mindplot.Command._uuid=1
}mindplot.Command._uuid=mindplot.Command._uuid+1;
return mindplot.Command._uuid
};mindplot.DesignerActionRunner=new Class({initialize:function(d,c){$assert(d,"commandContext can not be null");
this._undoManager=new mindplot.DesignerUndoManager();
this._context=d;
this._notifier=c
},execute:function(b){$assert(b,"command can not be null");
b.execute(this._context);
this._undoManager.enqueue(b);
this.fireChangeEvent();
mindplot.EventBus.instance.fireEvent(mindplot.EventBus.events.DoLayout)
},undo:function(){this._undoManager.execUndo(this._context);
this.fireChangeEvent();
mindplot.EventBus.instance.fireEvent(mindplot.EventBus.events.DoLayout)
},redo:function(){this._undoManager.execRedo(this._context);
this.fireChangeEvent();
mindplot.EventBus.instance.fireEvent(mindplot.EventBus.events.DoLayout)
},fireChangeEvent:function(){var b=this._undoManager.buildEvent();
this._notifier.fireEvent("modelUpdate",b)
}});mindplot.DesignerUndoManager=new Class({initialize:function(b){this._undoQueue=[];
this._redoQueue=[];
this._baseId=0
},enqueue:function(f){$assert(f,"Command can  not be null");
var d=this._undoQueue.length;
if(f.discardDuplicated&&d>0){var e=this._undoQueue[d-1];
if(e.discardDuplicated!=f.discardDuplicated){this._undoQueue.push(f)
}}else{this._undoQueue.push(f)
}this._redoQueue=[]
},execUndo:function(d){if(this._undoQueue.length>0){var c=this._undoQueue.pop();
this._redoQueue.push(c);
c.undoExecute(d)
}},execRedo:function(d){if(this._redoQueue.length>0){var c=this._redoQueue.pop();
this._undoQueue.push(c);
c.execute(d)
}},buildEvent:function(){return{undoSteps:this._undoQueue.length,redoSteps:this._redoQueue.length}
},markAsChangeBase:function(){var d=this._undoQueue.length;
if(d>0){var c=this._undoQueue[d-1];
this._baseId=c.getId()
}else{this._baseId=0
}},hasBeenChanged:function(){var e=true;
var d=this._undoQueue.length;
if(d==0&&this._baseId==0){e=false
}else{if(d>0){var f=this._undoQueue[d-1];
e=(this._baseId!=f.getId())
}}return e
}});mindplot.ControlPoint=new Class({initialize:function(){var d=new web2d.Elipse({width:6,height:6,stroke:"1 solid #6589de",fillColor:"gray",visibility:false});
d.setCursor("pointer");
var e=new web2d.Elipse({width:6,height:6,stroke:"1 solid #6589de",fillColor:"gray",visibility:false});
e.setCursor("pointer");
this._controlPointsController=[d,e];
this._controlLines=[new web2d.Line({strokeColor:"#6589de",strokeWidth:1,opacity:0.3}),new web2d.Line({strokeColor:"#6589de",strokeWidth:1,opacity:0.3})];
this._isBinded=false;
var f=this;
this._controlPointsController[0].addEvent("mousedown",function(a){(f._mouseDown)(a,mindplot.ControlPoint.FROM,f)
});
this._controlPointsController[0].addEvent("click",function(a){(f._mouseClick)(a)
});
this._controlPointsController[0].addEvent("dblclick",function(a){(f._mouseClick)(a)
});
this._controlPointsController[1].addEvent("mousedown",function(a){(f._mouseDown)(a,mindplot.ControlPoint.TO,f)
});
this._controlPointsController[1].addEvent("click",function(a){(f._mouseClick)(a)
});
this._controlPointsController[1].addEvent("dblclick",function(a){(f._mouseClick)(a)
})
},setLine:function(b){if($defined(this._line)){this._removeLine()
}this._line=b;
this._createControlPoint();
this._endPoint=[];
this._orignalCtrlPoint=[];
this._orignalCtrlPoint[0]=this._controls[0].clone();
this._orignalCtrlPoint[1]=this._controls[1].clone();
this._endPoint[0]=this._line.getLine().getFrom().clone();
this._endPoint[1]=this._line.getLine().getTo().clone()
},redraw:function(){if($defined(this._line)){this._createControlPoint()
}},_createControlPoint:function(){this._controls=this._line.getLine().getControlPoints();
var b=this._line.getLine().getFrom();
this._controlPointsController[0].setPosition(this._controls[mindplot.ControlPoint.FROM].x+b.x,this._controls[mindplot.ControlPoint.FROM].y+b.y-3);
this._controlLines[0].setFrom(b.x,b.y);
this._controlLines[0].setTo(this._controls[mindplot.ControlPoint.FROM].x+b.x+3,this._controls[mindplot.ControlPoint.FROM].y+b.y);
b=this._line.getLine().getTo();
this._controlLines[1].setFrom(b.x,b.y);
this._controlLines[1].setTo(this._controls[mindplot.ControlPoint.TO].x+b.x+3,this._controls[mindplot.ControlPoint.TO].y+b.y);
this._controlPointsController[1].setPosition(this._controls[mindplot.ControlPoint.TO].x+b.x,this._controls[mindplot.ControlPoint.TO].y+b.y-3)
},_removeLine:function(){},_mouseDown:function(f,e,d){if(!this._isBinded){this._isBinded=true;
this._mouseMoveFunction=function(a){(d._mouseMoveEvent)(a,e,d)
};
this._workspace.getScreenManager().addEvent("mousemove",this._mouseMoveFunction);
this._mouseUpFunction=function(a){(d._mouseUp)(a,e,d)
};
this._workspace.getScreenManager().addEvent("mouseup",this._mouseUpFunction)
}f.preventDefault();
f.stopPropagation();
return false
},_mouseMoveEvent:function(j,g){var f=this._workspace.getScreenManager();
var h=f.getWorkspaceMousePosition(j);
var i;
if(g==0){i=mindplot.util.Shape.calculateRelationShipPointCoordinates(this._line.getSourceTopic(),h);
this._line.setFrom(i.x,i.y);
this._line.setSrcControlPoint(new core.Point(h.x-i.x,h.y-i.y))
}else{i=mindplot.util.Shape.calculateRelationShipPointCoordinates(this._line.getTargetTopic(),h);
this._line.setTo(i.x,i.y);
this._line.setDestControlPoint(new core.Point(h.x-i.x,h.y-i.y))
}this._controls[g].x=(h.x-i.x);
this._controls[g].y=(h.y-i.y);
this._controlPointsController[g].setPosition(h.x-5,h.y-3);
this._controlLines[g].setFrom(i.x,i.y);
this._controlLines[g].setTo(h.x-2,h.y);
this._line.getLine().updateLine(g)
},_mouseUp:function(d,e){this._workspace.getScreenManager().removeEvent("mousemove",this._mouseMoveFunction);
this._workspace.getScreenManager().removeEvent("mouseup",this._mouseUpFunction);
var f=mindplot.ActionDispatcher.getInstance();
f.moveControlPoint(this,e);
this._isBinded=false
},_mouseClick:function(b){b.preventDefault();
b.stopPropagation();
return false
},setVisibility:function(b){if(b){this._controlLines[0].moveToFront();
this._controlLines[1].moveToFront();
this._controlPointsController[0].moveToFront();
this._controlPointsController[1].moveToFront()
}this._controlPointsController[0].setVisibility(b);
this._controlPointsController[1].setVisibility(b);
this._controlLines[0].setVisibility(b);
this._controlLines[1].setVisibility(b)
},addToWorkspace:function(b){this._workspace=b;
b.append(this._controlPointsController[0]);
b.append(this._controlPointsController[1]);
b.append(this._controlLines[0]);
b.append(this._controlLines[1])
},removeFromWorkspace:function(b){this._workspace=null;
b.removeChild(this._controlPointsController[0]);
b.removeChild(this._controlPointsController[1]);
b.removeChild(this._controlLines[0]);
b.removeChild(this._controlLines[1])
},getControlPoint:function(b){return this._controls[b]
},getOriginalEndPoint:function(b){return this._endPoint[b]
},getOriginalCtrlPoint:function(b){return this._orignalCtrlPoint[b]
}});
mindplot.ControlPoint.FROM=0;
mindplot.ControlPoint.TO=1;mindplot.EditorOptions={LayoutManager:"OriginalLayout",textEditor:"TextEditor"};mindplot.RelationshipPivot=new Class({initialize:function(d,c){$assert(d,"workspace can not be null");
$assert(c,"designer can not be null");
this._workspace=d;
this._designer=c;
this._mouseMoveEvent=this._mouseMove.bind(this);
this._onClickEvent=this._cleanOnMouseClick.bind(this);
this._onTopicClick=this._connectOnFocus.bind(this)
},start:function(h,l){$assert(h,"sourceTopic can not be null");
$assert(l,"targetPos can not be null");
this.dispose();
this._sourceTopic=h;
if(h!=null){this._workspace.enableWorkspaceEvents(false);
var i=h.getPosition();
var k=mindplot.Relationship.getStrokeColor();
this._pivot=new web2d.CurvedLine();
this._pivot.setStyle(web2d.CurvedLine.SIMPLE_LINE);
var m=this._calculateFromPosition(i);
this._pivot.setFrom(m.x,m.y);
this._pivot.setTo(l.x,l.y);
this._pivot.setStroke(2,"solid",k);
this._pivot.setDashed(4,2);
this._startArrow=new web2d.Arrow();
this._startArrow.setStrokeColor(k);
this._startArrow.setStrokeWidth(2);
this._startArrow.setFrom(i.x,i.y);
this._workspace.append(this._pivot);
this._workspace.append(this._startArrow);
this._workspace.addEvent("mousemove",this._mouseMoveEvent);
this._workspace.addEvent("click",this._onClickEvent);
var n=this._designer.getModel();
var j=n.getTopics();
_.each(j,function(a){a.addEvent("ontfocus",this._onTopicClick)
}.bind(this))
}},dispose:function(){var f=this._workspace;
if(this._isActive()){f.removeEvent("mousemove",this._mouseMoveEvent);
f.removeEvent("click",this._onClickEvent);
var e=this._designer.getModel();
var g=e.getTopics();
var h=this;
_.each(g,function(a){a.removeEvent("ontfocus",h._onTopicClick)
});
f.removeChild(this._pivot);
f.removeChild(this._startArrow);
f.enableWorkspaceEvents(true);
this._sourceTopic=null;
this._pivot=null;
this._startArrow=null
}},_mouseMove:function(k){var n=this._workspace.getScreenManager();
var j=n.getWorkspaceMousePosition(k);
var l=this._sourceTopic.getPosition();
var m=Math.sign(j.x-l.x)*5;
var h=this._calculateFromPosition(j);
this._pivot.setFrom(h.x,h.y);
this._pivot.setTo(j.x-m,j.y);
var i=this._pivot.getControlPoints();
this._startArrow.setFrom(j.x-m,j.y);
this._startArrow.setControlPoint(i[1]);
k.stopPropagation();
return false
},_cleanOnMouseClick:function(b){this.dispose();
b.stopPropagation()
},_calculateFromPosition:function(g){var e=this._sourceTopic.getPosition();
if(this._sourceTopic.getType()==mindplot.model.INodeModel.CENTRAL_TOPIC_TYPE){e=mindplot.util.Shape.workoutIncomingConnectionPoint(this._sourceTopic,g)
}var f=mindplot.util.Shape.calculateDefaultControlPoints(e,g);
var h=new core.Point();
h.x=parseInt(f[0].x)+parseInt(e.x);
h.y=parseInt(f[0].y)+parseInt(e.y);
return mindplot.util.Shape.calculateRelationShipPointCoordinates(this._sourceTopic,h)
},_connectOnFocus:function(h,i){var g=this._sourceTopic;
var f=this._designer.getMindmap();
if(i.getId()!=g.getId()){var j=f.createRelationship(i.getId(),g.getId());
this._designer._actionDispatcher.addRelationship(j)
}this.dispose()
},_isActive:function(){return this._pivot!=null
}});mindplot.TopicFeature={Icon:{id:mindplot.model.IconModel.FEATURE_TYPE,model:mindplot.model.IconModel,icon:mindplot.ImageIcon},Link:{id:mindplot.model.LinkModel.FEATURE_TYPE,model:mindplot.model.LinkModel,icon:mindplot.LinkIcon},Note:{id:mindplot.model.NoteModel.FEATURE_TYPE,model:mindplot.model.NoteModel,icon:mindplot.NoteIcon},isSupported:function(b){return mindplot.TopicFeature._featuresMetadataById.some(function(a){return a.id==b
})
},createModel:function(f,e){$assert(f,"type can not be null");
$assert(e,"attributes can not be null");
var d=mindplot.TopicFeature._featuresMetadataById.filter(function(a){return a.id==f
})[0].model;
return new d(e)
},createIcon:function(e,f,g){$assert(e,"topic can not be null");
$assert(f,"model can not be null");
var h=mindplot.TopicFeature._featuresMetadataById.filter(function(a){return a.id==f.getType()
})[0].icon;
return new h(e,f,g)
}};
mindplot.TopicFeature._featuresMetadataById=[mindplot.TopicFeature.Icon,mindplot.TopicFeature.Link,mindplot.TopicFeature.Note];mindplot.commands.GenericFunctionCommand=new Class({Extends:mindplot.Command,initialize:function(f,e,d){$assert(f,"commandFunc must be defined");
$assert($defined(e),"topicsIds must be defined");
this.parent();
this._value=d;
this._topicsId=e;
this._commandFunc=f;
this._oldValues=[]
},execute:function(f){if(!this.applied){var g=null;
try{g=f.findTopics(this._topicsId)
}catch(h){if(this._commandFunc.commandType!="changeTextToTopic"){throw h
}}if(g!=null){var e=this;
_.each(g,function(a){var b=e._commandFunc(a,e._value);
e._oldValues.push(b)
})
}this.applied=true
}else{throw"Command can not be applied two times in a row."
}},undoExecute:function(e){if(this.applied){var f=e.findTopics(this._topicsId);
var d=this;
_.each(f,function(a,b){d._commandFunc(a,d._oldValues[b])
});
this.applied=false;
this._oldValues=[]
}else{throw"undo can not be applied."
}}});mindplot.commands.DeleteCommand=new Class({Extends:mindplot.Command,initialize:function(c,d){$assert($defined(d),"topicIds can not be null");
this.parent();
this._relIds=d;
this._topicIds=c;
this._deletedTopicModels=[];
this._deletedRelModel=[];
this._parentTopicIds=[]
},execute:function(d){var f=this._filterChildren(this._topicIds,d);
if(f.length>0){_.each(f,function(c){c.closeEditors();
var j=c.getModel();
var b=this._collectInDepthRelationships(c);
this._deletedRelModel.append(b.map(function(g){return g.getModel().clone()
}));
_.each(b,function(g){d.deleteRelationship(g)
});
var k=j.clone();
this._deletedTopicModels.push(k);
var a=c.getOutgoingConnectedTopic();
var l=null;
if(a!=null){l=a.getId()
}this._parentTopicIds.push(l);
d.deleteTopic(c)
},this)
}var e=d.findRelationships(this._relIds);
if(e.length>0){_.each(e,function(a){this._deletedRelModel.push(a.getModel().clone());
d.deleteRelationship(a)
},this)
}},undoExecute:function(f){_.each(this._deletedTopicModels,function(a){f.createTopic(a)
},this);
_.each(this._deletedTopicModels,function(c,i){var a=f.findTopics(c.getId());
var b=this._parentTopicIds[i];
if(b){var j=f.findTopics(b);
f.connect(a[0],j[0])
}},this);
_.each(this._deletedRelModel,function(a){f.addRelationship(a)
});
_.each(this._deletedTopicModels,function(b){var a=f.findTopics(b.getId());
a[0].setBranchVisibility(true)
},this);
if(this._deletedTopicModels.length>0){var e=this._deletedTopicModels[0];
var d=f.findTopics(e.getId())[0];
d.setOnFocus(true)
}this._deletedTopicModels=[];
this._parentTopicIds=[];
this._deletedRelModel=[]
},_filterChildren:function(g,e){var h=e.findTopics(g);
var f=[];
_.each(h,function(c){var b=c.getParent();
var a=false;
while(b!=null&&!a){a=g.contains(b.getId());
if(a){break
}b=b.getParent()
}if(!a){f.push(c)
}});
return f
},_collectInDepthRelationships:function(k){var h=[];
h.append(k.getRelationships());
var i=k.getChildren();
var l=i.map(function(a){return this._collectInDepthRelationships(a)
},this);
h.append(l.flatten());
if(h.length>0){h=h.sort(function(a,b){return a.getModel().getId()-b.getModel().getId()
});
var g=[h[0]];
for(var j=1;
j<h.length;
j++){if(h[j-1]!==h[j]){g.push(h[j])
}}h=g
}return h
}});mindplot.commands.DragTopicCommand=new Class({Extends:mindplot.Command,initialize:function(g,e,f,h){$assert(g,"topicId must be defined");
this._topicsId=g;
if($defined(h)){this._parentId=h.getId()
}this.parent();
this._position=e;
this._order=f
},execute:function(j){var l=j.findTopics(this._topicsId)[0];
l.setVisibility(false);
var g=l.getOutgoingConnectedTopic();
var k=l.getOrder();
var i=l.getPosition();
if($defined(g)&&g!=this._parentId){j.disconnect(l)
}if(this._order!=null){l.setOrder(this._order)
}else{if(this._position!=null){j.moveTopic(l,this._position)
}else{$assert("Illegal command state exception.")
}}if(g!=this._parentId){if($defined(this._parentId)){var h=j.findTopics(this._parentId)[0];
j.connect(l,h)
}this._parentId=null;
if($defined(g)){this._parentId=g.getId()
}}l.setVisibility(true);
this._order=k;
this._position=i
},undoExecute:function(b){this.execute(b)
}});mindplot.commands.AddTopicCommand=new Class({Extends:mindplot.Command,initialize:function(c,d){$assert(c,"models can not be null");
$assert(d==null||d.length==c.length,"parents and models must have the same size");
this.parent();
this._models=c;
this._parentsIds=d
},execute:function(d){var c=this;
_.each(this._models,function(i,j){var k=d.createTopic(i);
if(c._parentsIds){var a=c._parentsIds[j];
if($defined(a)){var l=d.findTopics(a)[0];
d.connect(k,l)
}}else{d.addTopic(k)
}var b=d._designer;
b.onObjectFocusEvent(k);
k.setOnFocus(true);
k.setVisibility(true)
})
},undoExecute:function(c){var d=[];
_.each(this._models,function(a){d.push(a.clone())
});
_.each(this._models,function(b){var a=b.getId();
var f=c.findTopics(a)[0];
c.deleteTopic(f)
});
this._models=d
}});mindplot.commands.ChangeFeatureToTopicCommand=new Class({Extends:mindplot.Command,initialize:function(d,f,e){$assert($defined(d),"topicId can not be null");
$assert($defined(f),"featureId can not be null");
$assert($defined(e),"attributes can not be null");
this.parent();
this._topicId=d;
this._featureId=f;
this._attributes=e
},execute:function(g){var e=g.findTopics(this._topicId)[0];
var h=e.findFeatureById(this._featureId);
var f=h.getAttributes();
h.setAttributes(this._attributes);
this._attributes=f
},undoExecute:function(b){this.execute(b)
}});mindplot.commands.RemoveFeatureFromTopicCommand=new Class({Extends:mindplot.Command,initialize:function(d,c){$assert($defined(d),"topicId can not be null");
$assert(c,"iconModel can not be null");
this.parent();
this._topicId=d;
this._featureId=c;
this._oldFeature=null
},execute:function(f){var e=f.findTopics(this._topicId)[0];
var d=e.findFeatureById(this._featureId);
e.removeFeature(d);
this._oldFeature=d
},undoExecute:function(c){var d=c.findTopics(this._topicId)[0];
d.addFeature(this._oldFeature);
this._oldFeature=null
}});mindplot.commands.AddFeatureToTopicCommand=new Class({Extends:mindplot.Command,initialize:function(f,d,e){$assert($defined(f),"topicId can not be null");
$assert(d,"featureType can not be null");
$assert(e,"attributes can not be null");
this.parent();
this._topicId=f;
this._featureType=d;
this._attributes=e;
this._featureModel=null
},execute:function(f){var d=f.findTopics(this._topicId)[0];
if(!this._featureModel){var e=d.getModel();
this._featureModel=e.createFeature(this._featureType,this._attributes)
}d.addFeature(this._featureModel)
},undoExecute:function(c){var d=c.findTopics(this._topicId)[0];
d.removeFeature(this._featureModel)
}});mindplot.commands.AddRelationshipCommand=new Class({Extends:mindplot.Command,initialize:function(b){$assert(b,"Relationship model can not be null");
this.parent();
this._model=b
},execute:function(d){var c=d.addRelationship(this._model);
c.setOnFocus(true)
},undoExecute:function(c){var d=c.findRelationships(this._model.getId());
c.deleteRelationship(d[0])
}});mindplot.commands.MoveControlPointCommand=new Class({Extends:mindplot.Command,initialize:function(c,d){$assert(c,"line can not be null");
$assert($defined(d),"point can not be null");
this.parent();
this._ctrlPointControler=c;
this._line=c._line;
this._controlPoint=this._ctrlPointControler.getControlPoint(d).clone();
this._oldControlPoint=this._ctrlPointControler.getOriginalCtrlPoint(d).clone();
this._originalEndPoint=this._ctrlPointControler.getOriginalEndPoint(d).clone();
switch(d){case 0:this._wasCustom=this._line.getLine().isSrcControlPointCustom();
this._endPoint=this._line.getLine().getFrom().clone();
break;
case 1:this._wasCustom=this._line.getLine().isDestControlPointCustom();
this._endPoint=this._line.getLine().getTo().clone();
break
}this._point=d
},execute:function(c){var d=this._line.getModel();
switch(this._point){case 0:d.setSrcCtrlPoint(this._controlPoint.clone());
this._line.setFrom(this._endPoint.x,this._endPoint.y);
this._line.setIsSrcControlPointCustom(true);
this._line.setSrcControlPoint(this._controlPoint.clone());
break;
case 1:d.setDestCtrlPoint(this._controlPoint.clone());
this._wasCustom=this._line.getLine().isDestControlPointCustom();
this._line.setTo(this._endPoint.x,this._endPoint.y);
this._line.setIsDestControlPointCustom(true);
this._line.setDestControlPoint(this._controlPoint.clone());
break
}if(this._line.isOnFocus()){this._line._refreshShape();
this._ctrlPointControler.setLine(this._line)
}this._line.getLine().updateLine(this._point)
},undoExecute:function(f){var e=this._line;
var d=e.getModel();
switch(this._point){case 0:if($defined(this._oldControlPoint)){e.setFrom(this._originalEndPoint.x,this._originalEndPoint.y);
d.setSrcCtrlPoint(this._oldControlPoint.clone());
e.setSrcControlPoint(this._oldControlPoint.clone());
e.setIsSrcControlPointCustom(this._wasCustom)
}break;
case 1:if($defined(this._oldControlPoint)){e.setTo(this._originalEndPoint.x,this._originalEndPoint.y);
d.setDestCtrlPoint(this._oldControlPoint.clone());
e.setDestControlPoint(this._oldControlPoint.clone());
e.setIsDestControlPointCustom(this._wasCustom)
}break
}this._line.getLine().updateLine(this._point);
if(this._line.isOnFocus()){this._ctrlPointControler.setLine(e);
e._refreshShape()
}}});mindplot.widget.ModalDialogNotifier=new Class({initialize:function(){},show:function(j,h){$assert(j,"message can not be null");
var f=$('<div class="modal fade"><div class="modal-dialog"><div class="modal-content"><div class="modal-body"></div><div class="alert alert-block alert-warning"><img src="images/alert-sign.png"><div style="display: inline-block" class="alert-content"></div></div><div class="modal-footer"><button type="button" class="btn btn-default" data-dismiss="modal">Close</button></div></div></div></div>');
var i="<p>"+j+"</p>";
var g=h?"<h4>"+h+"</h4>":"";
f.find(".alert-content").append(g+i);
f.modal()
}});
var dialogNotifier=new mindplot.widget.ModalDialogNotifier();
$notifyModal=dialogNotifier.show.bind(dialogNotifier);mindplot.widget.ToolbarNotifier=new Class({initialize:function(){this.container=$("#headerNotifier")
},hide:function(){this.container.hide()
},logMessage:function(c,d){$assert(c,"msg can not be null");
if(this.container&&!this.container.data("transitioning")){this.container.data("transitioning",true);
this.container.text(c);
this.container.css({top:"5px",left:($(window).width()-this.container.width())/2-9});
this.container.show().fadeOut(5000)
}this.container.data("transitioning",false)
}});
var toolbarNotifier=new mindplot.widget.ToolbarNotifier();
$notify=function(b){toolbarNotifier.logMessage(b)
};mindplot.widget.ToolbarItem=new Class({Implements:mindplot.Events,initialize:function(f,d,e){$assert(f,"buttonId can not be null");
$assert(d,"fn can not be null");
this._buttonId=f;
this._fn=d;
this._options=e;
this._enable=false;
this.enable()
},getButtonElem:function(){var b=$("#"+this._buttonId);
$assert(b,"Could not find element for "+this._buttonId);
return b
}.protect(),show:function(){this.fireEvent("show")
},hide:function(){this.fireEvent("hide")
},isTopicAction:function(){return this._options.topicAction
},isRelAction:function(){return this._options.relAction
},disable:function(){var b=this.getButtonElem();
if(this._enable){b.unbind("click",this._fn);
b.removeClass("buttonOn");
b.addClass("buttonOff");
this._enable=false
}},enable:function(){var b=this.getButtonElem();
if(!this._enable){b.bind("click",this._fn);
b.removeClass("buttonOff");
b.addClass("buttonOn");
this._enable=true
}},getTip:function(){return this._tip
}.protect()});mindplot.widget.ToolbarPaneItem=new Class({Extends:mindplot.widget.ToolbarItem,initialize:function(h,f){$assert(h,"buttonId can not be null");
$assert(f,"model can not be null");
this._model=f;
var g=this;
var e=function(){g.isVisible()?g.hide():g.show()
};
this.parent(h,e,{topicAction:true,relAction:false});
this._panelElem=this._init();
this._visible=false
},_init:function(){var e=this.buildPanel();
e.css("cursor","default");
var d=this.getButtonElem();
var f=this;
this._tip=new mindplot.widget.FloatingTip(d,{html:true,placement:"bottom",content:function(){return f._updateSelectedItem()
},className:"toolbarPaneTip",trigger:"manual",template:'<div class="popover popoverGray" role="tooltip"><div class="arrow arrowGray"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'});
this._tip.addEvent("hide",function(){f._visible=false
});
this._tip.addEvent("show",function(){f._visible=true
});
return e
},getModel:function(){return this._model
},getPanelElem:function(){return this._panelElem
}.protect(),show:function(){if(!this.isVisible()){this.parent();
this._tip.show();
this.getButtonElem().className="buttonExtActive"
}},hide:function(){if(this.isVisible()){this.parent();
this._tip.hide();
this.getButtonElem().className="buttonExtOn"
}},isVisible:function(){return this._visible
},disable:function(){this.hide();
var b=this.getButtonElem();
if(this._enable){b.unbind("click",this._fn);
b.removeClass("buttonExtOn");
b.removeClass("buttonOn");
b.addClass("buttonExtOff");
this._enable=false
}},enable:function(){var b=this.getButtonElem();
if(!this._enable){b.bind("click",this._fn);
b.removeClass("buttonExtOff");
b.addClass("buttonExtOn");
this._enable=true
}},buildPanel:function(){throw"Method must be implemented"
}.protect()});mindplot.widget.NoteEditor=new Class({Extends:BootstrapDialog,initialize:function(c){$assert(c,"model can not be null");
this._model=c;
this.parent($msg("Note"),{cancelButton:true,closeButton:true,acceptButton:true,removeButton:typeof c.getValue()!="undefined",onEventData:{model:this._model}});
this.css({margin:"150px auto"});
var d=this._buildPanel(c);
this.setContent(d)
},_buildPanel:function(e){var f=$("<div></div>").css("padding-top","5px");
var h=$("<form></form>").attr({action:"none",id:"noteFormId"});
var g=$("<textarea></textarea autofocus>").attr({placeholder:$msg("WRITE_YOUR_TEXT_HERE"),required:"true","class":"form-control"});
g.css({width:"100%",height:80,resize:"none"});
g.on("keypress",function(a){a.stopPropagation()
});
h.append(g);
if(e.getValue()!=null){g.val(e.getValue())
}f.append(h);
return f
},onAcceptClick:function(b){b.data.dialog._submitForm(b.data.model)
},_submitForm:function(c){var d=this._native.find("textarea");
if(d.val()){c.setValue(d.val())
}this.close()
},onDialogShown:function(){$(this).find("textarea").focus()
},onRemoveClick:function(b){b.data.model.setValue(null);
b.data.dialog.close()
}});mindplot.widget.LinkEditor=new Class({Extends:BootstrapDialog,initialize:function(c){$assert(c,"model can not be null");
this._model=c;
this.parent($msg("LINK"),{cancelButton:true,closeButton:true,acceptButton:true,removeButton:typeof c.getValue()!="undefined",errorMessage:true,onEventData:{model:this._model}});
this.css({margin:"150px auto"});
var d=this._buildPanel(c);
this.setContent(d)
},_buildPanel:function(o){var j=$("<div></div>").css("padding-top","5px");
this.form=$("<form></form>").attr({action:"none",id:"linkFormId"});
var k=$("<p></p>").text("Paste your url here:");
k.css("margin","0px 0px 20px");
this.form.append(k);
var m=$("<div></div>").attr({"class":"input-group"});
var p=$('<input id="inputUrl"/>').attr({placeholder:"http://www.example.com/",required:"true",autofocus:"autofocus","class":"form-control"});
p.on("keypress",function(a){a.stopPropagation()
});
if(o.getValue()!=null){p.val(o.getValue())
}var i=$("<button></button>").attr({type:"button","class":"btn btn-default"});
i.html($msg("OPEN_LINK")).css("margin-left","0px");
i.click(function(){window.open(p.val(),"_blank","status=1,width=700,height=450,resize=1")
});
var l=$('<span class="input-group-btn"></span>').append(i);
m.append(p);
m.append(l);
this.form.append(m);
var n=this;
this.form.unbind("submit").submit(function(a){a.preventDefault();
if(n.checkURL(p.val())){n.cleanError();
var b=p.val();
if(b!=null&&$.trim(b)!=""){o.setValue(b)
}n.close();
this.formSubmitted=true
}else{n.alertError($msg("URL_ERROR"));
a.stopPropagation()
}});
j.append(this.form);
return j
},checkURL:function(d){var c=/^(http|https|ftp):\/\/[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/i;
return(c.test(d))
},onAcceptClick:function(b){this.formSubmitted=false;
$("#linkFormId").trigger("submit");
if(!this.formSubmitted){b.stopPropagation()
}},onDialogShown:function(){$(this).find("#inputUrl").focus()
},onRemoveClick:function(b){b.data.model.setValue(null);
b.data.dialog.close()
}});mindplot.widget.FloatingTip=new Class({Implements:[Options,mindplot.Events],options:{animation:true,html:false,placement:"right",selector:false,trigger:"hover",title:"",content:"",delay:0,container:false,destroyOnExit:false},initialize:function(c,d){this.setOptions(d);
this.element=c;
this._createPopover()
},_createPopover:function(){this.element.popover(this.options);
var b=this;
if(this.options.destroyOnExit){this.element.one("hidden.bs.popover",function(){b.element.popover("destroy");
b._createPopover()
})
}},show:function(){this.element.popover("show");
this.fireEvent("show");
return this
},hide:function(){this.element.popover("hide");
this.fireEvent("hide");
return this
}});mindplot.widget.LinkIconTooltip=new Class({Extends:mindplot.widget.FloatingTip,initialize:function(d){$assert(d,"linkIcon can not be null");
var c=$(d.getImage()._peer._native);
this.parent(c,{content:this._buildContent(d),html:true,placement:"bottom",container:"body",title:$msg("LINK"),trigger:"manual",template:'<div id="linkPopover" class="popover" onmouseover="$(this).mouseleave(function() {$(this).fadeOut(200); });" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'})
},_buildContent:function(g){var h=$("<div></div>").css({padding:"5px",width:"100%"});
var i=$("<div></div>").text("URL: "+g.getModel().getUrl()).css({"white-space":"pre-wrap","word-wrap":"break-word"});
h.append(i);
var l=$("<div></div>").css({width:"100%",textAlign:"right","padding-bottom":"5px","padding-top":"5px"});
var k=$("<img>").prop("src","http://free.pagepeeker.com/v2/thumbs.php?size=m&url="+g.getModel().getUrl()).prop("img",g.getModel().getUrl()).prop("alt",g.getModel().getUrl());
k.css("padding","5px");
var j=$("<a></a>").attr({href:g.getModel().getUrl(),alt:"Open in new window ...",target:"_blank"});
j.append(k);
l.append(j);
h.append(l);
return h
}});mindplot.widget.KeyboardShortcutTooltip=new Class({Extends:mindplot.widget.FloatingTip,initialize:function(g,i){$assert(g,"buttonElem can not be null");
$assert(i,"text can not be null");
this._text=i;
var f=g.children().first();
var j=g.attr("id")+"Tip";
var h=$("<div></div>").attr("id",j);
h.append(f);
g.append(h);
this.parent(h,{content:this._buildContent(),html:true,placement:"bottom",className:"keyboardShortcutTip",template:'<div class="popover popoverBlack" role="tooltip"><div class="arrow arrowBlack"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'});
h.on("click",function(a){h.trigger("mouseleave",a)
})
},_buildContent:function(){var d=$("<div></div>");
d.css({padding:"3px 0px",width:"100%",color:"white"});
var c=$("<div></div>").text(this._text);
c.css({width:"100%","font-size":"90%",textAlign:"center","font-weight":"bold"});
d.append(c);
return d
}});mindplot.widget.ColorPalettePanel=new Class({Extends:mindplot.widget.ToolbarPaneItem,initialize:function(d,e,f){this._baseUrl=f;
this.parent(d,e);
$assert($defined(f),"baseUrl can not be null")
},_load:function(){if(!mindplot.widget.ColorPalettePanel._panelContent){$("<link>").appendTo($("head")).attr({type:"text/css",rel:"stylesheet"}).attr("href",this._baseUrl+"/colorPalette.css");
var b;
$.ajax({url:this._baseUrl+"/colorPalette.html",method:"get",async:false,success:function(a){b=a
},error:function(){b="<div>Sorry, your request failed :(</div>"
}});
mindplot.widget.ColorPalettePanel._panelContent=b
}return mindplot.widget.ColorPalettePanel._panelContent
},buildPanel:function(){var h=$('<div class="toolbarPanel"></div>').attr("id",this._buttonId+"colorPalette");
h.html(this._load());
var g=h.find("div[class=palette-colorswatch]");
var f=this.getModel();
var e=this;
_.each(g,function(a){$(a).on("click",function(){var b=$(a).css("background-color");
f.setValue(b);
e.hide()
})
});
return h
},_updateSelectedItem:function(){var e=this.getPanelElem();
e.find("td[class='palette-cell palette-cell-selected']").attr("class","palette-cell");
var g=e.find("div[class=palette-colorswatch]");
var h=this.getModel();
var f=h.getValue();
_.each(g,function(a){var b=$(a).css("background-color").rgbToHex();
if(f!=null&&f[0]=="r"){f=f.rgbToHex()
}if(f!=null&&f.toUpperCase()==b.toUpperCase()){$(a).parent().attr("class","palette-cell palette-cell-selected")
}});
return e
}});mindplot.widget.ListToolbarPanel=new Class({Extends:mindplot.widget.ToolbarPaneItem,initialize:function(c,d){this.parent(c,d);
this._initPanel()
},_initPanel:function(){var b=this;
this.getPanelElem().children("div").bind("click",function(a){a.stopPropagation();
b.hide();
var d=$defined($(this).attr("model"))?$(this).attr("model"):$(this).attr("id");
b.getModel().setValue(d)
})
},_updateSelectedItem:function(){var e=this.getPanelElem();
var d=e.find("div");
var f=this.getModel().getValue();
_.each(d,function(a){var b=$defined($(a).attr("model"))?$(a).attr("model"):$(a).attr("id");
$assert(b,"elemValue can not be null");
if(b==f){$(a).attr("class","toolbarPanelLinkSelectedLink")
}else{$(a).attr("class","toolbarPanelLink")
}});
return e
}});mindplot.widget.FontFamilyPanel=new Class({Extends:mindplot.widget.ListToolbarPanel,initialize:function(c,d){this.parent(c,d)
},buildPanel:function(){var b=$("<div class='toolbarPanel' id='fontFamilyPanel'></div>");
b.html('<div id="times" model="Times" class="toolbarPanelLink" style="font-family:times;">Times</div><div id="arial"  model="Arial" style="font-family:arial;">Arial</div><div id="tahoma" model="Tahoma" style="font-family:tahoma;">Tahoma</div><div id="verdana" model="Verdana" style="font-family:verdana;">Verdana</div>');
return b
}});mindplot.widget.FontSizePanel=new Class({Extends:mindplot.widget.ListToolbarPanel,initialize:function(c,d){this.parent(c,d)
},buildPanel:function(){var b=$("<div class='toolbarPanel' id='fontSizePanel'></div>");
b[0].innerHTML='<div id="small" model="6" style="font-size:8px">Small</div><div id="normal" model="8" style="font-size:12px">Normal</div><div id="large" model="10" style="font-size:15px">Large</div><div id="huge"  model="15" style="font-size:24px">Huge</div>';
return b
}});mindplot.widget.TopicShapePanel=new Class({Extends:mindplot.widget.ListToolbarPanel,initialize:function(c,d){this.parent(c,d)
},buildPanel:function(){var b=$("<div class='toolbarPanel' id='topicShapePanel'></div>");
b[0].innerHTML='<div id="rectagle" model="rectagle"><img src="images/shape-rectangle.png" alt="Rectangle"></div><div id="rounded_rectagle" model="rounded rectagle" ><img src="images/shape-rectangle-round.png" alt="Rounded Rectangle"></div><div id="line" model="line"><img src="images/shape-line.png" alt="Line"></div><div id="elipse" model="elipse"><img src="images/shape-circle.png"></div>';
return b
}});mindplot.widget.IconPanel=new Class({Extends:mindplot.widget.ToolbarPaneItem,initialize:function(c,d){this.parent(c,d)
},_updateSelectedItem:function(){return this.getPanelElem()
},buildPanel:function(){var n=$('<div class="toolbarPanel" id="IconsPanel"></div>').css({width:245,height:230});
n.on("click",function(a){a.stopPropagation()
});
var o=0;
for(var r=0;
r<mindplot.ImageIcon.prototype.ICON_FAMILIES.length;
r=r+1){var i=mindplot.ImageIcon.prototype.ICON_FAMILIES[r].icons;
for(var s=0;
s<i.length;
s=s+1){var m;
if((o%12)==0){m=$("<div></div>");
n.append(m)
}var j=i[s];
var q=$("<img>").attr("id",j).attr("src",mindplot.ImageIcon.prototype._getImageUrl(j)).attr("class","panelIcon");
m.append(q);
var t=this;
var p=this.getModel();
q.on("click",function(a){p.setValue($(this).attr("id"));
t.hide()
});
o=o+1
}}return n
}});mindplot.widget.IMenu=new Class({initialize:function(h,f,e){$assert(h,"designer can not be null");
$assert(f,"containerId can not be null");
this._designer=h;
this._toolbarElems=[];
this._containerId=f;
this._mapId=e;
this._mindmapUpdated=false;
var g=this;
this._designer.addEvent("modelUpdate",function(){g.setRequireChange(true)
})
},clear:function(){_.each(this._toolbarElems,function(b){b.hide()
})
},discardChanges:function(d){this.setRequireChange(false);
var f=mindplot.PersistenceManager.getInstance();
var e=d.getMindmap();
f.discardChanges(e.getId());
this.unlockMap(d);
window.location.reload()
},unlockMap:function(d){var e=d.getMindmap();
var f=mindplot.PersistenceManager.getInstance();
f.unlockMap(e)
},save:function(k,i,m,p){var j=i.getMindmap();
var n=i.getMindmapProperties();
if(m){$notify($msg("SAVING"));
k.css("cursor","wait")
}var l=this;
var o=mindplot.PersistenceManager.getInstance();
o.save(j,n,m,{onSuccess:function(){if(m){k.css("cursor","pointer");
$notify($msg("SAVE_COMPLETE"))
}l.setRequireChange(false)
},onError:function(a){if(m){k.css("cursor","pointer");
if(a.severity!="FATAL"){$notify(a.message)
}else{$notifyModal(a.message)
}}}},p)
},isSaveRequired:function(){return this._mindmapUpdated
},setRequireChange:function(b){this._mindmapUpdated=b
}});mindplot.widget.Menu=new Class({Extends:mindplot.widget.IMenu,initialize:function(O,Q,ai,ac,ao){this.parent(O,Q,ai);
ao=!$defined(ao)?"":ao;
var ap=ao+"css/widget";
$("#"+this._containerId).bind("click",function(a){a.stopPropagation();
return false
});
$("#"+this._containerId).bind("dblclick",function(a){a.stopPropagation();
return false
});
var ad=O.getModel();
var ag=$("#fontFamily");
if(ag){var ak={getValue:function(){var a=ad.filterSelectedTopics();
var b=null;
for(var d=0;
d<a.length;
d++){var c=a[d].getFontFamily();
if(b!=null&&b!=c){b=null;
break
}b=c
}return b
},setValue:function(a){O.changeFontFamily(a)
}};
this._toolbarElems.push(new mindplot.widget.FontFamilyPanel("fontFamily",ak));
this._registerTooltip("fontFamily",$msg("FONT_FAMILY"))
}var ah=$("#fontSize");
if(ah){var af={getValue:function(){var b=ad.filterSelectedTopics();
var c=null;
for(var a=0;
a<b.length;
a++){var d=b[a].getFontSize();
if(c!=null&&c!=d){c=null;
break
}c=d
}return c
},setValue:function(a){O.changeFontSize(a)
}};
this._toolbarElems.push(new mindplot.widget.FontSizePanel("fontSize",af));
this._registerTooltip("fontSize",$msg("FONT_SIZE"))
}var U=$("#topicShape");
if(U){var ae={getValue:function(){var b=ad.filterSelectedTopics();
var c=null;
for(var a=0;
a<b.length;
a++){var d=b[a].getShapeType();
if(c!=null&&c!=d){c=null;
break
}c=d
}return c
},setValue:function(a){O.changeTopicShape(a)
}};
this._toolbarElems.push(new mindplot.widget.TopicShapePanel("topicShape",ae));
this._registerTooltip("topicShape",$msg("TOPIC_SHAPE"))
}var Y=$("#topicIcon");
if(Y){var am={getValue:function(){return null
},setValue:function(a){O.addIconType(a)
}};
this._toolbarElems.push(new mindplot.widget.IconPanel("topicIcon",am));
this._registerTooltip("topicIcon",$msg("TOPIC_ICON"))
}var W=$("#topicColor");
if(W){var aa={getValue:function(){var a=ad.filterSelectedTopics();
var c=null;
for(var d=0;
d<a.length;
d++){var b=a[d].getBackgroundColor();
if(c!=null&&c!=b){c=null;
break
}c=b
}return c
},setValue:function(a){O.changeBackgroundColor(a)
}};
this._toolbarElems.push(new mindplot.widget.ColorPalettePanel("topicColor",aa,ap));
this._registerTooltip("topicColor",$msg("TOPIC_COLOR"))
}var Z=$("#topicBorder");
if(Z){var an={getValue:function(){var a=ad.filterSelectedTopics();
var c=null;
for(var d=0;
d<a.length;
d++){var b=a[d].getBorderColor();
if(c!=null&&c!=b){c=null;
break
}c=b
}return c
},setValue:function(a){O.changeBorderColor(a)
}};
this._toolbarElems.push(new mindplot.widget.ColorPalettePanel("topicBorder",an,ap));
this._registerTooltip("topicBorder",$msg("TOPIC_BORDER_COLOR"))
}var J=$("#fontColor");
if(J){var M={getValue:function(){var c=null;
var a=ad.filterSelectedTopics();
for(var d=0;
d<a.length;
d++){var b=a[d].getFontColor();
if(c!=null&&c!=b){c=null;
break
}c=b
}return c
},setValue:function(a){O.changeFontColor(a)
}};
this._toolbarElems.push(new mindplot.widget.ColorPalettePanel("fontColor",M,ao));
this._registerTooltip("fontColor",$msg("FONT_COLOR"))
}this._addButton("export",false,false,function(){BootstrapDialog.Request.active=new BootstrapDialog.Request("c/maps/"+ai+"/exportf",$msg("EXPORT"),{cancelButton:true,closeButton:true})
});
this._registerTooltip("export",$msg("EXPORT"));
var S=this;
this._addButton("print",false,false,function(){S.save(X,O,false);
var a=window.location.href.substring(0,window.location.href.lastIndexOf("c/maps/"));
window.open(a+"c/maps/"+ai+"/print")
});
this._registerTooltip("print",$msg("PRINT"));
this._addButton("zoomIn",false,false,function(){O.zoomIn()
});
this._registerTooltip("zoomIn",$msg("ZOOM_IN"));
this._addButton("zoomOut",false,false,function(){O.zoomOut()
});
this._registerTooltip("zoomOut",$msg("ZOOM_OUT"));
var aq=this._addButton("undoEdition",false,false,function(){O.undo()
});
if(aq){aq.disable()
}this._registerTooltip("undoEdition",$msg("UNDO"),"meta+Z");
var P=this._addButton("redoEdition",false,false,function(){O.redo()
});
if(P){P.disable()
}this._registerTooltip("redoEdition",$msg("REDO"),"meta+shift+Z");
if(P&&aq){O.addEvent("modelUpdate",function(a){if(a.undoSteps>0){aq.enable()
}else{aq.disable()
}if(a.redoSteps>0){P.enable()
}else{P.disable()
}})
}this._addButton("addTopic",true,false,function(){O.createSiblingForSelectedNode()
});
this._registerTooltip("addTopic",$msg("ADD_TOPIC"),"Enter");
this._addButton("deleteTopic",true,true,function(){O.deleteSelectedEntities()
});
this._registerTooltip("deleteTopic",$msg("TOPIC_DELETE"),"Delete");
this._addButton("topicLink",true,false,function(){O.addLink()
});
this._registerTooltip("topicLink",$msg("TOPIC_LINK"));
this._addButton("topicRelation",true,false,function(a){O.showRelPivot(a)
});
this._registerTooltip("topicRelation",$msg("TOPIC_RELATIONSHIP"));
this._addButton("topicNote",true,false,function(){O.addNote()
});
this._registerTooltip("topicNote",$msg("TOPIC_NOTE"));
this._addButton("fontBold",true,false,function(){O.changeFontWeight()
});
this._registerTooltip("fontBold",$msg("FONT_BOLD"),"meta+B");
this._addButton("fontItalic",true,false,function(){O.changeFontStyle()
});
this._registerTooltip("fontItalic",$msg("FONT_ITALIC"),"meta+I");
var X=$("#save");
if(X){this._addButton("save",false,false,function(){S.save(X,O,true)
});
this._registerTooltip("save",$msg("SAVE"),"meta+S");
if(!ac){$(window).bind("unload",function(){if(S.isSaveRequired()){S.save(X,O,false,true)
}S.unlockMap(O)
});
setInterval(function(){if(S.isSaveRequired()){S.save(X,O,false)
}},30000)
}}var ab=$("#discard");
if(ab){this._addButton("discard",false,false,function(){S.discardChanges(O)
});
this._registerTooltip("discard",$msg("DISCARD_CHANGES"))
}var T=$("#shareIt");
if(T){this._addButton("shareIt",false,false,function(){BootstrapDialog.Request.active=new BootstrapDialog.Request("c/maps/"+ai+"/sharef",$msg("COLLABORATE"),{closeButton:true,cancelButton:true});
O.onObjectFocusEvent()
});
this._registerTooltip("shareIt",$msg("COLLABORATE"))
}var ar=$("#publishIt");
if(ar){this._addButton("publishIt",false,false,function(){BootstrapDialog.Request.active=new BootstrapDialog.Request("c/maps/"+ai+"/publishf",$msg("PUBLISH"),{closeButton:true,cancelButton:true});
O.onObjectFocusEvent()
});
this._registerTooltip("publishIt",$msg("PUBLISH"))
}var K=$("#history");
if(K){this._addButton("history",false,false,function(){BootstrapDialog.Request.active=new BootstrapDialog.Request("c/maps/"+ai+"/historyf",$msg("HISTORY"),{closeButton:true,cancelButton:true});
O.onObjectFocusEvent()
});
this._registerTooltip("history",$msg("HISTORY"))
}this._registerEvents(O);
var aj=$("#keyboardShortcuts");
if(aj){aj.bind("click",function(a){BootstrapDialog.Request.active=new BootstrapDialog.Request("c/keyboard",$msg("SHORTCUTS"),{closeButton:true,cancelButton:true});
O.onObjectFocusEvent();
a.preventDefault()
})
}var L=$("#tutorialVideo");
if(L){var N=900;
var R=500;
var al=(screen.width/2)-(N/2);
var V=(screen.height/2)-(R/2);
L.bind("click",function(a){window.open("https://www.youtube.com/tv?vq=medium#/watch?v=rKxZwNKs9cE","_blank","toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=yes, copyhistory=no, width="+N+", height="+R+", top="+V+", left="+al);
a.preventDefault()
})
}},_registerEvents:function(d){var c=this;
_.each(this._toolbarElems,function(a){a.addEvent("show",function(){c.clear()
})
});
d.addEvent("onblur",function(){var a=d.getModel().filterSelectedTopics();
var b=d.getModel().filterSelectedRelationships();
_.each(c._toolbarElems,function(h){var i=h.isTopicAction();
var j=h.isRelAction();
if(i||j){if((i&&a.length!=0)||(j&&b.length!=0)){h.enable()
}else{h.disable()
}}})
});
d.addEvent("onfocus",function(){var a=d.getModel().filterSelectedTopics();
var b=d.getModel().filterSelectedRelationships();
_.each(c._toolbarElems,function(h){var i=h.isTopicAction();
var j=h.isRelAction();
if(i||j){if(i&&a.length>0){h.enable()
}if(j&&b.length>0){h.enable()
}}})
})
},_addButton:function(k,n,h,l){var j=this;
var i=null;
if($("#"+k)){var m=new mindplot.widget.ToolbarItem(k,function(a){l(a);
j.clear()
},{topicAction:n,relAction:h});
this._toolbarElems.push(m);
i=m
}return i
},_registerTooltip:function(e,g,f){if($("#"+e)){var h=g;
if(f){f=Browser.Platform.mac?f.replace("meta+","⌘"):f.replace("meta+","ctrl+");
h=h+" ("+f+")"
}new mindplot.widget.KeyboardShortcutTooltip($("#"+e),h)
}}});mindplot.layout.EventBusDispatcher=new Class({initialize:function(){this.registerBusEvents()
},setLayoutManager:function(b){this._layoutManager=b
},registerBusEvents:function(){mindplot.EventBus.instance.addEvent(mindplot.EventBus.events.NodeAdded,this._nodeAdded.bind(this));
mindplot.EventBus.instance.addEvent(mindplot.EventBus.events.NodeRemoved,this._nodeRemoved.bind(this));
mindplot.EventBus.instance.addEvent(mindplot.EventBus.events.NodeResizeEvent,this._nodeResizeEvent.bind(this));
mindplot.EventBus.instance.addEvent(mindplot.EventBus.events.NodeMoveEvent,this._nodeMoveEvent.bind(this));
mindplot.EventBus.instance.addEvent(mindplot.EventBus.events.NodeDisconnectEvent,this._nodeDisconnectEvent.bind(this));
mindplot.EventBus.instance.addEvent(mindplot.EventBus.events.NodeConnectEvent,this._nodeConnectEvent.bind(this));
mindplot.EventBus.instance.addEvent(mindplot.EventBus.events.NodeShrinkEvent,this._nodeShrinkEvent.bind(this));
mindplot.EventBus.instance.addEvent(mindplot.EventBus.events.DoLayout,this._doLayout.bind(this))
},_nodeResizeEvent:function(b){this._layoutManager.updateNodeSize(b.node.getId(),b.size)
},_nodeMoveEvent:function(b){this._layoutManager.moveNode(b.node.getId(),b.position)
},_nodeDisconnectEvent:function(b){this._layoutManager.disconnectNode(b.getId())
},_nodeConnectEvent:function(b){this._layoutManager.connectNode(b.parentNode.getId(),b.childNode.getId(),b.childNode.getOrder())
},_nodeShrinkEvent:function(b){this._layoutManager.updateShrinkState(b.getId(),b.areChildrenShrunken())
},_nodeAdded:function(b){if(b.getId()!=0){this._layoutManager.addNode(b.getId(),{width:10,height:10},b.getPosition());
this._layoutManager.updateShrinkState(b.getId(),b.areChildrenShrunken())
}},_nodeRemoved:function(b){this._layoutManager.removeNode(b.getId())
},_doLayout:function(){this._layoutManager.layout(true)
},getLayoutManager:function(){return this._layoutManager
}});mindplot.layout.ChangeEvent=new Class({initialize:function(b){$assert(!isNaN(b),"id can not be null");
this._id=b;
this._position=null;
this._order=null
},getId:function(){return this._id
},getOrder:function(){return this._order
},getPosition:function(){return this._position
},setOrder:function(b){$assert(!isNaN(b),"value can not be null");
this._order=b
},setPosition:function(b){$assert(b,"value can not be null");
this._position=b
},toString:function(){return"[order:"+this.getOrder()+", position: {"+this.getPosition().x+","+this.getPosition().y+"}]"
}});mindplot.layout.LayoutManager=new Class({Extends:mindplot.Events,initialize:function(g,e){$assert($defined(g),"rootNodeId can not be null");
$assert(e,"rootSize can not be null");
var f=f||{x:0,y:0};
this._treeSet=new mindplot.layout.RootedTreeSet();
this._layout=new mindplot.layout.OriginalLayout(this._treeSet);
var h=this._layout.createNode(g,e,f,"root");
this._treeSet.setRoot(h);
this._events=[]
},updateNodeSize:function(f,e){$assert($defined(f),"id can not be null");
var d=this._treeSet.find(f);
d.setSize(e)
},updateShrinkState:function(f,d){$assert($defined(f),"id can not be null");
$assert($defined(d),"value can not be null");
var e=this._treeSet.find(f);
e.setShrunken(d);
return this
},find:function(b){return this._treeSet.find(b)
},moveNode:function(f,e){$assert($defined(f),"id cannot be null");
$assert($defined(e),"position cannot be null");
$assert($defined(e.x),"x can not be null");
$assert($defined(e.y),"y can not be null");
var d=this._treeSet.find(f);
d.setPosition(e)
},connectNode:function(f,d,e){$assert($defined(f),"parentId cannot be null");
$assert($defined(d),"childId cannot be null");
$assert($defined(e),"order cannot be null");
this._layout.connectNode(f,d,e);
return this
},disconnectNode:function(b){$assert($defined(b),"id can not be null");
this._layout.disconnectNode(b);
return this
},addNode:function(g,h,e){$assert($defined(g),"id can not be null");
var f=this._layout.createNode(g,h,e,"topic");
this._treeSet.add(f);
return this
},removeNode:function(c){$assert($defined(c),"id can not be null");
var d=this._treeSet.find(c);
if(this._treeSet.getParent(d)){this.disconnectNode(c)
}this._treeSet.remove(c);
return this
},predict:function(k,m,i,n){$assert($defined(k),"parentId can not be null");
var p=this._treeSet.find(k);
var o=m?this._treeSet.find(m):null;
var l=p.getSorter();
var j=l.predict(this._treeSet,p,o,i,n);
return{order:j[0],position:j[1]}
},dump:function(){console.log(this._treeSet.dump())
},plot:function(f,h){$assert(f,"containerId cannot be null");
h=h||{width:200,height:200};
var g=10;
var e=Raphael(f,h.width,h.height);
e.drawGrid(0,0,h.width,h.height,h.width/g,h.height/g);
this._treeSet.plot(e);
return e
},layout:function(b){this._layout.layout();
this._collectChanges();
if($(b).length>0||b){this._flushEvents()
}return this
},_flushEvents:function(){_.each(this._events,function(b){this.fireEvent("change",b)
},this);
this._events=[]
},_collectChanges:function(b){if(!b){b=this._treeSet.getTreeRoots()
}_.each(b,function(f){if(f.hasOrderChanged()||f.hasPositionChanged()){var e=f.getId();
var a=this._events.some(function(c){return c.id==e
});
if(!a){a=new mindplot.layout.ChangeEvent(e)
}a.setOrder(f.getOrder());
a.setPosition(f.getPosition());
f.resetPositionState();
f.resetOrderState();
f.resetFreeState();
this._events.push(a)
}this._collectChanges(this._treeSet.getChildren(f))
},this)
}});mindplot.layout.Node=new Class({initialize:function(g,e,f,h){$assert(typeof g==="number"&&isFinite(g),"id can not be null");
$assert(e,"size can not be null");
$assert(f,"position can not be null");
$assert(h,"sorter can not be null");
this._id=g;
this._sorter=h;
this._properties={};
this.setSize(e);
this.setPosition(f);
this.setShrunken(false)
},getId:function(){return this._id
},setFree:function(b){this._setProperty("free",b)
},isFree:function(){return this._getProperty("free")
},hasFreeChanged:function(){return this._isPropertyChanged("free")
},hasFreeDisplacementChanged:function(){return this._isPropertyChanged("freeDisplacement")
},setShrunken:function(b){this._setProperty("shrink",b)
},areChildrenShrunken:function(){return this._getProperty("shrink")
},setOrder:function(b){$assert(typeof b==="number"&&isFinite(b),"Order can not be null. Value:"+b);
this._setProperty("order",b)
},resetPositionState:function(){var b=this._properties.position;
if(b){b.hasChanged=false
}},resetOrderState:function(){var b=this._properties.order;
if(b){b.hasChanged=false
}},resetFreeState:function(){var b=this._properties.freeDisplacement;
if(b){b.hasChanged=false
}},getOrder:function(){return this._getProperty("order")
},hasOrderChanged:function(){return this._isPropertyChanged("order")
},hasPositionChanged:function(){return this._isPropertyChanged("position")
},hasSizeChanged:function(){return this._isPropertyChanged("size")
},getPosition:function(){return this._getProperty("position")
},setSize:function(b){$assert($defined(b),"Size can not be null");
this._setProperty("size",Object.clone(b))
},getSize:function(){return this._getProperty("size")
},setFreeDisplacement:function(e){$assert($defined(e),"Position can not be null");
$assert($defined(e.x),"x can not be null");
$assert($defined(e.y),"y can not be null");
var d=this.getFreeDisplacement();
var f={x:d.x+e.x,y:d.y+e.y};
this._setProperty("freeDisplacement",Object.clone(f))
},resetFreeDisplacement:function(){this._setProperty("freeDisplacement",{x:0,y:0})
},getFreeDisplacement:function(){var b=this._getProperty("freeDisplacement");
return(b||{x:0,y:0})
},setPosition:function(d){$assert($defined(d),"Position can not be null");
$assert($defined(d.x),"x can not be null");
$assert($defined(d.y),"y can not be null");
var c=this.getPosition();
if(c==null||Math.abs(c.x-d.x)>2||Math.abs(c.y-d.y)>2){this._setProperty("position",d)
}},_setProperty:function(e,d){var f=this._properties[e];
if(!f){f={hasChanged:false,value:null,oldValue:null}
}if(JSON.stringify(f.value)!=JSON.stringify(d)){f.oldValue=f.value;
f.value=d;
f.hasChanged=true
}this._properties[e]=f
},_getProperty:function(d){var c=this._properties[d];
return $defined(c)?c.value:null
},_isPropertyChanged:function(d){var c=this._properties[d];
return c?c.hasChanged:false
},getSorter:function(){return this._sorter
},toString:function(){return"[id:"+this.getId()+", order:"+this.getOrder()+", position: {"+this.getPosition().x+","+this.getPosition().y+"}, size: {"+this.getSize().width+","+this.getSize().height+"}, shrink:"+this.areChildrenShrunken()+"]"
}});mindplot.layout.RootedTreeSet=new Class({initialize:function(){this._rootNodes=[]
},setRoot:function(b){$assert(b,"root can not be null");
this._rootNodes.push(this._decodate(b))
},getTreeRoots:function(){return this._rootNodes
},_decodate:function(b){b._children=[];
return b
},add:function(b){$assert(b,"node can not be null");
$assert(!this.find(b.getId(),false),"node already exits with this id. Id:"+b.getId());
$assert(!b._children,"node already added");
this._rootNodes.push(this._decodate(b))
},remove:function(c){$assert($defined(c),"nodeId can not be null");
var d=this.find(c);
this._rootNodes.erase(d)
},connect:function(g,e){$assert($defined(g),"parent can not be null");
$assert($defined(e),"child can not be null");
var f=this.find(g);
var h=this.find(e,true);
$assert(!h._parent,"node already connected. Id:"+h.getId()+",previous:"+h._parent);
f._children.push(h);
h._parent=f;
this._rootNodes.erase(h)
},disconnect:function(c){$assert($defined(c),"nodeId can not be null");
var d=this.find(c);
$assert(d._parent,"Node is not connected");
d._parent._children.erase(d);
this._rootNodes.push(d);
d._parent=null
},find:function(i,j){$assert($defined(i),"id can not be null");
var g=this._rootNodes;
var h=null;
for(var l=0;
l<g.length;
l++){var k=g[l];
h=this._find(i,k);
if(h){break
}}j=!$defined(j)?true:j;
$assert(j?h:true,"node could not be found id:"+i+"\n,RootedTreeSet"+this.dump());
return h
},_find:function(i,k){if(k.getId()==i){return k
}var h=null;
var l=k._children;
for(var g=0;
g<l.length;
g++){var j=l[g];
h=this._find(i,j);
if(h){break
}}return h
},getChildren:function(b){$assert(b,"node cannot be null");
return b._children
},getRootNode:function(c){$assert(c,"node cannot be null");
var d=this.getParent(c);
if($defined(d)){return this.getRootNode(d)
}return c
},getAncestors:function(b){$assert(b,"node cannot be null");
return this._getAncestors(this.getParent(b),[])
},_getAncestors:function(f,d){var e=d;
if(f){e.push(f);
this._getAncestors(this.getParent(f),e)
}return e
},getSiblings:function(d){$assert(d,"node cannot be null");
if(!$defined(d._parent)){return[]
}var c=d._parent._children.filter(function(a){return a!=d
});
return c
},hasSinglePathToSingleLeaf:function(b){$assert(b,"node cannot be null");
return this._hasSinglePathToSingleLeaf(b)
},_hasSinglePathToSingleLeaf:function(c){var d=this.getChildren(c);
if(d.length==1){return this._hasSinglePathToSingleLeaf(d[0])
}return d.length==0
},isStartOfSubBranch:function(b){return this.getSiblings(b).length>0&&this.getChildren(b).length==1
},isLeaf:function(b){$assert(b,"node cannot be null");
return this.getChildren(b).length==0
},getParent:function(b){$assert(b,"node cannot be null");
return b._parent
},dump:function(){var e=this._rootNodes;
var f="";
for(var h=0;
h<e.length;
h++){var g=e[h];
f+=this._dump(g,"")
}return f
},_dump:function(j,g){var h=g+j+"\n";
var k=this.getChildren(j);
for(var l=0;
l<k.length;
l++){var i=k[l];
h+=this._dump(i,g+"   ")
}return h
},plot:function(e){var f=this._rootNodes;
for(var h=0;
h<f.length;
h++){var g=f[h];
this._plot(e,g)
}},_plot:function(y,x,q){var z=this.getChildren(x);
var u=x.getPosition().x+y.width/2-x.getSize().width/2;
var v=x.getPosition().y+y.height/2-x.getSize().height/2;
var r=y.rect(u,v,x.getSize().width,x.getSize().height);
var w=x.getOrder()==null?"r":x.getOrder();
var i=y.text(x.getPosition().x+y.width/2,x.getPosition().y+y.height/2,x.getId()+"["+w+"]");
i.attr("fill","#FFF");
var B=this._rootNodes.contains(x)?"#000":(x.isFree()?"#abc":"#c00");
r.attr("fill",B);
var s={x:r.attr("x")-y.width/2+r.attr("width")/2,y:r.attr("y")-y.height/2+r.attr("height")/2};
var p={width:r.attr("width"),height:r.attr("height")};
r.click(function(){console.log("[id:"+x.getId()+", order:"+x.getOrder()+", position:("+s.x+","+s.y+"), size:"+p.width+"x"+p.height+", freeDisplacement:("+x.getFreeDisplacement().x+","+x.getFreeDisplacement().y+")]")
});
i.click(function(){console.log("[id:"+x.getId()+", order:"+x.getOrder()+", position:("+s.x+","+s.y+"), size:"+p.width+"x"+p.height+", freeDisplacement:("+x.getFreeDisplacement().x+","+x.getFreeDisplacement().y+")]")
});
for(var t=0;
t<z.length;
t++){var A=z[t];
this._plot(y,A)
}},updateBranchPosition:function(k,i){var h=k.getPosition();
k.setPosition(i);
var m=h.x-i.x;
var j=h.y-i.y;
var n=this.getChildren(k);
var l=this;
_.each(n,function(a){l.shiftBranchPosition(a,m,j)
})
},shiftBranchPosition:function(j,l,i){var h=j.getPosition();
j.setPosition({x:h.x+l,y:h.y+i});
var g=this.getChildren(j);
var k=this;
_.each(g,function(a){k.shiftBranchPosition(a,l,i)
})
},getSiblingsInVerticalDirection:function(e,g){var f=this.getParent(e);
var h=this.getSiblings(e).filter(function(a){var b=e.getPosition().x>f.getPosition().x?a.getPosition().x>f.getPosition().x:a.getPosition().x<f.getPosition().x;
var c=g<0?a.getOrder()<e.getOrder():a.getOrder()>e.getOrder();
return c&&b
});
if(g<0){h.reverse()
}return h
},getBranchesInVerticalDirection:function(k,i){var g=this.getRootNode(k);
var h=this.getChildren(g).filter(function(a){return this._find(k.getId(),a)
},this);
var l=h[0];
var j=this.getSiblings(l).filter(function(b){var c=k.getPosition().x>g.getPosition().x?b.getPosition().x>g.getPosition().x:b.getPosition().x<g.getPosition().x;
var a=i<0?b.getOrder()<l.getOrder():b.getOrder()>l.getOrder();
return c&&a
},this);
return j
}});mindplot.layout.ChildrenSorterStrategy=new Class({initialize:function(){},computeChildrenIdByHeights:function(c,d){throw"Method must be implemented"
},computeOffsets:function(c,d){throw"Method must be implemented"
},insert:function(h,e,g,f){throw"Method must be implemented"
},detach:function(c,d){throw"Method must be implemented"
},predict:function(i,f,j,g,h){throw"Method must be implemented"
},verify:function(c,d){throw"Method must be implemented"
},getChildDirection:function(c,d){throw"Method must be implemented"
},toString:function(){throw"Method must be implemented: print name"
}});mindplot.layout.AbstractBasicSorter=new Class({Extends:mindplot.layout.ChildrenSorterStrategy,computeChildrenIdByHeights:function(f,d){var e={};
this._computeChildrenHeight(f,d,e);
return e
},_getVerticalPadding:function(){return mindplot.layout.AbstractBasicSorter.INTERNODE_VERTICAL_PADDING
},_computeChildrenHeight:function(j,k,n){var h=k.getSize().height+(this._getVerticalPadding()*2);
var i;
var l=j.getChildren(k);
if(l.length==0||k.areChildrenShrunken()){i=h
}else{var m=0;
_.each(l,function(a){m+=this._computeChildrenHeight(j,a,n)
},this);
i=Math.max(h,m)
}if(n){n[k.getId()]=i
}return i
},_getSortedChildren:function(f,d){var e=f.getChildren(d);
e.sort(function(a,b){return a.getOrder()-b.getOrder()
});
return e
},_getRelativeDirection:function(d,e){var f=e.x-d.x;
return f>=0?1:-1
}});
mindplot.layout.AbstractBasicSorter.INTERNODE_VERTICAL_PADDING=5;
mindplot.layout.AbstractBasicSorter.INTERNODE_HORIZONTAL_PADDING=30;mindplot.layout.BalancedSorter=new Class({Extends:mindplot.layout.AbstractBasicSorter,initialize:function(){},predict:function(N,G,A,u,B){if(B){$assert($defined(u),"position cannot be null for predict in free positioning");
$assert($defined(A),"node cannot be null for predict in free positioning");
var x=N.getRootNode(G);
var w=this._getRelativeDirection(x.getPosition(),A.getPosition());
var C=G.getPosition().x+w*(G.getSize().width/2+A.getSize().width/2+mindplot.layout.BalancedSorter.INTERNODE_HORIZONTAL_PADDING);
var D=w>0?(u.x>=C?u.x:C):(u.x<=C?u.x:C);
return[0,{x:D,y:u.y}]
}var x=N.getRootNode(G);
if(A){$assert($defined(u),"position cannot be null for predict in dragging");
var M=this._getRelativeDirection(x.getPosition(),A.getPosition());
var F=this._getRelativeDirection(x.getPosition(),u);
var I=N.getSiblings(A);
var v=G==N.getParent(A);
if(I.length==0&&M==F&&v){return[A.getOrder(),A.getPosition()]
}}if(!u){var y=this._getChildrenForOrder(G,N,0);
var L=this._getChildrenForOrder(G,N,1)
}var z=u?(u.x>x.getPosition().x?0:1):((y.length-L.length)>0?1:0);
var w=z%2==0?1:-1;
var K=this._getChildrenForOrder(G,N,z).filter(function(a){return a!=A
});
if(K.length==0){return[z,{x:G.getPosition().x+w*(G.getSize().width/2+mindplot.layout.BalancedSorter.INTERNODE_HORIZONTAL_PADDING*2),y:G.getPosition().y}]
}var E=null;
var H=K.getLast();
u=u||{x:H.getPosition().x,y:H.getPosition().y+1};
_.each(K,function(a,c){var b=a.getPosition();
if(u.y>b.y){yOffset=a==H?a.getSize().height+mindplot.layout.BalancedSorter.INTERNODE_VERTICAL_PADDING*2:(K[c+1].getPosition().y-a.getPosition().y)/2;
E=[a.getOrder()+2,{x:b.x,y:b.y+yOffset}]
}});
if(!E){var J=K[0];
E=[u.x>0?0:1,{x:J.getPosition().x,y:J.getPosition().y-J.getSize().height-mindplot.layout.BalancedSorter.INTERNODE_VERTICAL_PADDING*2}]
}return E
},insert:function(l,i,r,o){var q=this._getChildrenForOrder(i,l,o);
if(q.length==0){r.setOrder(o%2);
return
}var k=0;
for(var n=0;
n<q.length;
n++){var p=q[n];
k=Math.max(k,p.getOrder());
if(p.getOrder()>=o){k=Math.max(k,p.getOrder()+2);
p.setOrder(p.getOrder()+2)
}}var m=o>(k+1)?(k+2):o;
r.setOrder(m)
},detach:function(g,h){var e=g.getParent(h);
var f=this._getChildrenForOrder(e,g,h.getOrder());
_.each(f,function(a,b){if(a.getOrder()>h.getOrder()){a.setOrder(a.getOrder()-2)
}});
h.setOrder(h.getOrder()%2==0?0:1)
},computeOffsets:function(t,z){$assert(t,"treeSet can no be null.");
$assert(z,"node can no be null.");
var A=this._getSortedChildren(t,z);
var s=A.map(function(a){return{id:a.getId(),order:a.getOrder(),width:a.getSize().width,height:this._computeChildrenHeight(t,a)}
},this).reverse();
var x=0;
var B=0;
_.each(s,function(a){if(a.order%2==0){x+=a.height
}else{B+=a.height
}});
var w=x/2;
var q=B/2;
var i=0;
var p={};
for(var v=0;
v<s.length;
v++){var r=s[v].order%2?-1:1;
if(r>0){w=w-s[v].height;
i=w
}else{q=q-s[v].height;
i=q
}var y=i+s[v].height/2;
var u=r*(z.getSize().width/2+s[v].width/2+ +mindplot.layout.BalancedSorter.INTERNODE_HORIZONTAL_PADDING);
$assert(!isNaN(u),"xOffset can not be null");
$assert(!isNaN(y),"yOffset can not be null");
p[s[v].id]={x:u,y:y}
}return p
},verify:function(i,j){var k=this._getChildrenForOrder(j,i,j.getOrder());
var l=j.getOrder()%2==0?2:1;
for(var g=0;
g<k.length;
g++){var h=g==0&&l==1?1:(l*g);
$assert(k[g].getOrder()==h,"Missing order elements. Missing order: "+(g*l)+". Parent:"+j.getId()+",Node:"+k[g].getId())
}},getChildDirection:function(d,c){return c.getOrder()%2==0?1:-1
},toString:function(){return"Balanced Sorter"
},_getChildrenForOrder:function(d,f,e){return this._getSortedChildren(f,d).filter(function(a){return a.getOrder()%2==e%2
})
},_getVerticalPadding:function(){return mindplot.layout.BalancedSorter.INTERNODE_VERTICAL_PADDING
}});
mindplot.layout.BalancedSorter.INTERNODE_VERTICAL_PADDING=5;
mindplot.layout.BalancedSorter.INTERNODE_HORIZONTAL_PADDING=30;mindplot.layout.SymmetricSorter=new Class({Extends:mindplot.layout.AbstractBasicSorter,initialize:function(){},predict:function(T,N,F,y,I){var H=this;
var B=T.getRootNode(N);
if(I){$assert($defined(y),"position cannot be null for predict in free positioning");
$assert($defined(F),"node cannot be null for predict in free positioning");
var A=this._getRelativeDirection(B.getPosition(),N.getPosition());
var J=N.getPosition().x+A*(N.getSize().width/2+F.getSize().width/2+mindplot.layout.SymmetricSorter.INTERNODE_HORIZONTAL_PADDING);
var K=A>0?(y.x>=J?y.x:J):(y.x<=J?y.x:J);
return[0,{x:K,y:y.y}]
}if(!F){var C=H._getRelativeDirection(B.getPosition(),N.getPosition());
var y={x:N.getPosition().x+C*(N.getSize().width+mindplot.layout.SymmetricSorter.INTERNODE_HORIZONTAL_PADDING),y:N.getPosition().y};
return[T.getChildren(N).length,y]
}$assert($defined(y),"position cannot be null for predict in dragging");
var R=this._getRelativeDirection(B.getPosition(),F.getPosition());
var M=this._getRelativeDirection(B.getPosition(),y);
var P=T.getSiblings(F);
var z=N==T.getParent(F);
if(P.length==0&&R==M&&z){return[F.getOrder(),F.getPosition()]
}var i=T.getChildren(N);
if(i.length==0){var y={x:N.getPosition().x+M*(N.getSize().width+mindplot.layout.SymmetricSorter.INTERNODE_HORIZONTAL_PADDING),y:N.getPosition().y};
return[0,y]
}else{var L=null;
var O=i.getLast();
for(var D=0;
D<i.length;
D++){var S=i[D];
var G=(D+1)==S.length?null:i[D+1];
if(!G&&y.y>S.getPosition().y){var E=(T.getParent(F)&&T.getParent(F).getId()==N.getId())?O.getOrder():O.getOrder()+1;
var y={x:S.getPosition().x,y:S.getPosition().y+S.getSize().height+mindplot.layout.SymmetricSorter.INTERNODE_VERTICAL_PADDING*2};
return[E,y]
}if(G&&y.y>S.getPosition().y&&y.y<G.getPosition().y){if(G.getId()==F.getId()||S.getId()==F.getId()){return[F.getOrder(),F.getPosition()]
}else{var E=y.y>F.getPosition().y?G.getOrder()-1:S.getOrder()+1;
var y={x:S.getPosition().x,y:S.getPosition().y+(G.getPosition().y-S.getPosition().y)/2};
return[E,y]
}}}}var Q=i[0];
var y={x:Q.getPosition().x,y:Q.getPosition().y-Q.getSize().height-mindplot.layout.SymmetricSorter.INTERNODE_VERTICAL_PADDING*2};
return[0,y]
},insert:function(k,m,j,i){var n=this._getSortedChildren(k,m);
$assert(i<=n.length,"Order must be continues and can not have holes. Order:"+i);
for(var h=i;
h<n.length;
h++){var l=n[h];
l.setOrder(h+1)
}j.setOrder(i)
},detach:function(k,l){var m=k.getParent(l);
var n=this._getSortedChildren(k,m);
var i=l.getOrder();
$assert(n[i]===l,"Node seems not to be in the right position");
for(var h=l.getOrder()+1;
h<n.length;
h++){var j=n[h];
j.setOrder(j.getOrder()-1)
}l.setOrder(0)
},computeOffsets:function(r,v){$assert(r,"treeSet can no be null.");
$assert(v,"node can no be null.");
var w=this._getSortedChildren(r,v);
var p=w.map(function(a){return{id:a.getId(),order:a.getOrder(),position:a.getPosition(),width:a.getSize().width,height:this._computeChildrenHeight(r,a)}
},this).reverse();
var q=0;
_.each(p,function(a){q+=a.height
});
var n=q/2;
var i={};
for(var t=0;
t<p.length;
t++){n=n-p[t].height;
var x=r.find(p[t].id);
var o=this.getChildDirection(r,x);
var u=n+p[t].height/2;
var s=o*(p[t].width/2+v.getSize().width/2+mindplot.layout.SymmetricSorter.INTERNODE_HORIZONTAL_PADDING);
$assert(!isNaN(s),"xOffset can not be null");
$assert(!isNaN(u),"yOffset can not be null");
i[p[t].id]={x:s,y:u}
}return i
},verify:function(g,h){var e=this._getSortedChildren(g,h);
for(var f=0;
f<e.length;
f++){$assert(e[f].getOrder()==f,"missing order elements")
}},getChildDirection:function(m,j){$assert(m,"treeSet can no be null.");
$assert(m.getParent(j),"This should not happen");
var i;
var h=m.getRootNode(j);
if(m.getParent(j)==h){i=Math.sign(h.getPosition().x)
}else{var n=m.getParent(j);
var l=m.getParent(n);
var k=l.getSorter();
i=k.getChildDirection(m,n)
}return i
},toString:function(){return"Symmetric Sorter"
},_getVerticalPadding:function(){return mindplot.layout.SymmetricSorter.INTERNODE_VERTICAL_PADDING
}});
mindplot.layout.SymmetricSorter.INTERNODE_VERTICAL_PADDING=5;
mindplot.layout.SymmetricSorter.INTERNODE_HORIZONTAL_PADDING=30;mindplot.layout.GridSorter=new Class({Extends:mindplot.layout.AbstractBasicSorter,computeOffsets:function(t,z){$assert(t,"treeSet can no be null.");
$assert(z,"node can no be null.");
$assert("order can no be null.");
var A=this._getSortedChildren(t,z);
var q=this;
var r=A.map(function(a){return{id:a.getId(),height:q._computeChildrenHeight(t,a)}
});
var i={};
for(var v=0;
v<r.length;
v++){var w=v%2==0?1:-1;
var s=v==0?0:r[0].height/2*w;
var j=0;
for(var x=v-2;
x>0;
x=x-2){j+=r[x].height*w
}var B=v==0?0:r[v].height/2*w;
var y=s+j+B;
var u=z.getSize().width+mindplot.layout.GridSorter.GRID_HORIZONTAR_SIZE;
$assert(!isNaN(u),"xOffset can not be null");
$assert(!isNaN(y),"yOffset can not be null");
i[r[v].id]={x:u,y:y}
}return i
},toString:function(){return"Grid Sorter"
}});
mindplot.layout.GridSorter.GRID_HORIZONTAR_SIZE=20;
mindplot.layout.GridSorter.INTER_NODE_VERTICAL_DISTANCE=50;mindplot.layout.OriginalLayout=new Class({initialize:function(b){this._treeSet=b
},createNode:function(h,f,g,j){$assert($defined(h),"id can not be null");
$assert(f,"size can not be null");
$assert(g,"position can not be null");
$assert(j,"type can not be null");
var i=j==="root"?mindplot.layout.OriginalLayout.BALANCED_SORTER:mindplot.layout.OriginalLayout.SYMMETRIC_SORTER;
return new mindplot.layout.Node(h,f,g,i)
},connectNode:function(i,l,h){var g=this._treeSet.find(i);
var j=this._treeSet.find(l);
var k=g.getSorter();
k.insert(this._treeSet,g,j,h);
this._treeSet.connect(i,l);
k.verify(this._treeSet,g)
},disconnectNode:function(h){var e=this._treeSet.find(h);
var f=this._treeSet.getParent(e);
$assert(f,"Node already disconnected");
e.setFree(false);
e.resetFreeDisplacement();
var g=f.getSorter();
g.detach(this._treeSet,e);
this._treeSet.disconnect(h);
f.getSorter().verify(this._treeSet,f)
},layout:function(){var b=this._treeSet.getTreeRoots();
_.each(b,function(f){var e=f.getSorter();
var a=e.computeChildrenIdByHeights(this._treeSet,f);
this._layoutChildren(f,a);
this._fixOverlapping(f,a)
},this)
},_layoutChildren:function(y,v){var z=y.getId();
var A=this._treeSet.getChildren(y);
var r=this._treeSet.getParent(y);
var t=A.some(function(a){return a.hasOrderChanged()
});
var q=A.some(function(a){return a.hasSizeChanged()
});
var x=v[z];
var B=$defined(r)?r._heightChanged:false;
var w=y._branchHeight!=x;
y._heightChanged=w||B;
if(t||q||w||B){var o=y.getSorter();
var p=o.computeOffsets(this._treeSet,y);
var s=y.getPosition();
var u=this;
_.each(A,function(a){var b=p[a.getId()];
var g=a.getFreeDisplacement();
var c=y.getSorter().getChildDirection(u._treeSet,a);
if((c>0&&g.x<0)||(c<0&&g.x>0)){a.resetFreeDisplacement();
a.setFreeDisplacement({x:-g.x,y:g.y})
}b.x+=a.getFreeDisplacement().x;
b.y+=a.getFreeDisplacement().y;
var d=s.x;
var e=s.y;
var f={x:d+b.x,y:e+b.y+u._calculateAlignOffset(y,a,v)};
u._treeSet.updateBranchPosition(a,f)
});
y._branchHeight=x
}_.each(A,function(a){this._layoutChildren(a,v)
},this)
},_calculateAlignOffset:function(l,i,g){if(i.isFree()){return 0
}var j=0;
var k=l.getSize().height;
var h=i.getSize().height;
if(this._treeSet.isStartOfSubBranch(i)&&this._branchIsTaller(i,g)){if(this._treeSet.hasSinglePathToSingleLeaf(i)){j=g[i.getId()]/2-(h+i.getSorter()._getVerticalPadding()*2)/2
}else{j=this._treeSet.isLeaf(i)?0:-(h-k)/2
}}else{if(k>h){if(this._treeSet.getSiblings(i).length>0){j=0
}else{j=k/2-h/2
}}else{if(h>k){if(this._treeSet.getSiblings(i).length>0){j=0
}else{j=-(h/2-k/2)
}}}}return j
},_branchIsTaller:function(c,d){return d[c.getId()]>(c.getSize().height+c.getSorter()._getVerticalPadding()*2)
},_fixOverlapping:function(f,d){var e=this._treeSet.getChildren(f);
if(f.isFree()){this._shiftBranches(f,d)
}_.each(e,function(a){this._fixOverlapping(a,d)
},this)
},_shiftBranches:function(i,k){var h=[i];
var g=this._treeSet.getSiblingsInVerticalDirection(i,i.getFreeDisplacement().y);
var j=i;
_.each(g,function(b){var c=h.some(function(d){return this._branchesOverlap(d,b,k)
},this);
if(!b.isFree()||c){var a=i.getFreeDisplacement().y;
this._treeSet.shiftBranchPosition(b,0,a);
h.push(b)
}},this);
var l=this._treeSet.getBranchesInVerticalDirection(i,i.getFreeDisplacement().y).filter(function(a){return !h.contains(a)
});
_.each(l,function(a){var b=i.getFreeDisplacement().y;
this._treeSet.shiftBranchPosition(a,0,b);
h.push(a);
j=a
},this)
},_branchesOverlap:function(j,k,m){if(j==k){return false
}var n=j.getPosition().y-m[j.getId()]/2;
var l=j.getPosition().y+m[j.getId()]/2;
var i=k.getPosition().y-m[k.getId()]/2;
var h=k.getPosition().y+m[k.getId()]/2;
return !(n>=h||l<=i)
}});
mindplot.layout.OriginalLayout.SYMMETRIC_SORTER=new mindplot.layout.SymmetricSorter();
mindplot.layout.OriginalLayout.BALANCED_SORTER=new mindplot.layout.BalancedSorter();mindplot.EventBus=new Class({Implements:mindplot.Events,initialize:function(){}});
mindplot.EventBus.events={NodeResizeEvent:"NodeResizeEvent",NodeMoveEvent:"NodeMoveEvent",NodeShrinkEvent:"NodeShrinkEvent",NodeConnectEvent:"NodeConnectEvent",NodeDisconnectEvent:"NodeDisconnectEvent",NodeAdded:"NodeAdded",NodeRemoved:"NodeRemoved",DoLayout:"DoLayout"};
mindplot.EventBus.instance=new mindplot.EventBus();mindplot.Messages.BUNDLES.en={ZOOM_IN:"Zoom In",ZOOM_OUT:"Zoom Out",TOPIC_SHAPE:"Topic Shape",TOPIC_ADD:"Add Topic",TOPIC_DELETE:"Delete Topic",TOPIC_ICON:"Add Icon",TOPIC_LINK:"Add Link",TOPIC_RELATIONSHIP:"Relationship",TOPIC_COLOR:"Topic Color",TOPIC_BORDER_COLOR:"Topic Border Color",TOPIC_NOTE:"Add Note",FONT_FAMILY:"Font Type",FONT_SIZE:"Text Size",FONT_BOLD:"Text Bold",FONT_ITALIC:"Text Italic",UNDO:"Undo",REDO:"Redo",INSERT:"Insert",SAVE:"Save",NOTE:"Note",ADD_TOPIC:"Add Topic",LOADING:"Loading ...",EXPORT:"Export",PRINT:"Print",PUBLISH:"Publish",COLLABORATE:"Share",HISTORY:"History",DISCARD_CHANGES:"Discard Changes",FONT_COLOR:"Text Color",SAVING:"Saving ...",SAVE_COMPLETE:"Save Complete",ZOOM_IN_ERROR:"Zoom too high.",ZOOM_ERROR:"No more zoom can be applied.",ONLY_ONE_TOPIC_MUST_BE_SELECTED:"Could not create a topic. Only one topic must be selected.",ONE_TOPIC_MUST_BE_SELECTED:"Could not create a topic. One topic must be selected.",ONLY_ONE_TOPIC_MUST_BE_SELECTED_COLLAPSE:"Children can not be collapsed. One topic must be selected.",SAVE_COULD_NOT_BE_COMPLETED:"Save could not be completed, please try again latter.",UNEXPECTED_ERROR_LOADING:"We're sorry, an unexpected error has occurred.\nTry again reloading the editor.If the problem persists, contact us to support@wisemapping.com.",MAIN_TOPIC:"Main Topic",SUB_TOPIC:"Sub Topic",ISOLATED_TOPIC:"Isolated Topic",CENTRAL_TOPIC:"Central Topic",SHORTCUTS:"Keyboard Shortcuts",ENTITIES_COULD_NOT_BE_DELETED:"Could not delete topic or relation. At least one map entity must be selected.",AT_LEAST_ONE_TOPIC_MUST_BE_SELECTED:"At least one topic must be selected.",CLIPBOARD_IS_EMPTY:"Nothing to copy. Clipboard is empty.",CENTRAL_TOPIC_CAN_NOT_BE_DELETED:"Central topic can not be deleted.",RELATIONSHIP_COULD_NOT_BE_CREATED:"Relationship could not be created. A parent relationship topic must be selected first.",SELECTION_COPIED_TO_CLIPBOARD:"Topics copied to the clipboard",WRITE_YOUR_TEXT_HERE:"Write your note here ...",REMOVE:"Remove",ACCEPT:"Accept",CANCEL:"Cancel",LINK:"Link",OPEN_LINK:"Open URL",SESSION_EXPIRED:"Your session has expired, please log-in again.",URL_ERROR:"URL not valid",DUMMY:""};mindplot.Messages.BUNDLES.es={DISCARD_CHANGES:"Descartar Cambios",SAVE:"Guardar",INSERT:"Insertar",ZOOM_IN:"Acercar",ZOOM_OUT:"Alejar",TOPIC_BORDER_COLOR:"Color del Borde",TOPIC_SHAPE:"Forma del Tópico",TOPIC_ADD:"Agregar Tópico",TOPIC_DELETE:"Borrar Tópico",TOPIC_ICON:"Agregar Icono",TOPIC_LINK:"Agregar Enlace",TOPIC_NOTE:"Agregar Nota",TOPIC_COLOR:"Color Tópico",TOPIC_RELATIONSHIP:"Relación",FONT_FAMILY:"Tipo de Fuente",FONT_SIZE:"Tamaño de Texto",FONT_BOLD:"Negrita",FONT_ITALIC:"Italica",FONT_COLOR:"Color de Texto",UNDO:"Rehacer",NOTE:"Nota",LOADING:"Cargando ...",PRINT:"Imprimir",PUBLISH:"Publicar",REDO:"Deshacer",ADD_TOPIC:"Agregar Tópico",COLLABORATE:"Compartir",EXPORT:"Exportar",HISTORY:"Historial",SAVE_COMPLETE:"Grabado Completo",SAVING:"Grabando ...",ONE_TOPIC_MUST_BE_SELECTED:"No ha sido posible crear un nuevo tópico. Al menos un tópico debe ser seleccionado.",ONLY_ONE_TOPIC_MUST_BE_SELECTED:"No ha sido posible crear un nuevo tópico. Sólo un tópico debe ser seleccionado.",SAVE_COULD_NOT_BE_COMPLETED:"Grabación no pudo ser completada. Intentelo mas tarde.",UNEXPECTED_ERROR_LOADING:"Lo sentimos, un error inesperado ha ocurrido. Intentelo nuevamente recargando el editor. Si el problema persiste, contactenos a support@wisemapping.com.",ZOOM_ERROR:"No es posible aplicar mas zoom.",ZOOM_IN_ERROR:"El zoom es muy alto.",MAIN_TOPIC:"Tópico Principal",SUB_TOPIC:"Tópico Secundario",ISOLATED_TOPIC:"Tópico Aislado",CENTRAL_TOPIC:"Tópico Central",ONLY_ONE_TOPIC_MUST_BE_SELECTED_COLLAPSE:"Tópicos hijos no pueden ser colapsados. Sólo un tópico debe ser seleccionado.",SHORTCUTS:"Accesos directos",ENTITIES_COULD_NOT_BE_DELETED:"El tópico o la relación no pudo ser borrada. Debe selecionar al menos una.",AT_LEAST_ONE_TOPIC_MUST_BE_SELECTED:"Al menos un tópico debe ser seleccionado.",CLIPBOARD_IS_EMPTY:"Nada que copiar. Clipboard está vacio.",CENTRAL_TOPIC_CAN_NOT_BE_DELETED:"El tópico central no puede ser borrado.",RELATIONSHIP_COULD_NOT_BE_CREATED:"La relación no pudo ser creada. Una relación padre debe ser seleccionada primero.",SELECTION_COPIED_TO_CLIPBOARD:"Tópicos copiados al clipboard",WRITE_YOUR_TEXT_HERE:"Escribe tu nota aquí ...",REMOVE:"Borrar",ACCEPT:"Aceptar",CANCEL:"Cancelar",LINK:"Enlace",OPEN_LINK:"Abrir Enlace",SESSION_EXPIRED:"Su session ha expirado. Por favor, ingrese nuevamente.",DUMMY:""};mindplot.Messages.BUNDLES.de={ZOOM_IN:"Ansicht vergrößern",ZOOM_OUT:"Ansicht verkleinern",TOPIC_SHAPE:"Themen Gestaltung",TOPIC_ADD:"Thema hinzufügen",TOPIC_DELETE:"Thema löschen",TOPIC_ICON:"Symbol hinzufügen",TOPIC_LINK:"Verbindung hinzufügen",TOPIC_RELATIONSHIP:"Beziehung",TOPIC_COLOR:"Themenfarbe",TOPIC_BORDER_COLOR:"Thema Randfarbe",TOPIC_NOTE:"Notiz hinzufügen",FONT_FAMILY:"Schrifttyp",FONT_SIZE:"Schriftgröße",FONT_BOLD:"Fette Schrift",FONT_ITALIC:"Kursive Schrift",UNDO:"Rückgängig machen",REDO:"Wiederholen",INSERT:"Einfügen",SAVE:"Sichern",NOTE:"Notiz",ADD_TOPIC:"Thema hinzufügen",LOADING:"Laden ...",EXPORT:"Exportieren",PRINT:"Drucken",PUBLISH:"Publizieren",COLLABORATE:"Mitbenutzen",HISTORY:"Historie",DISCARD_CHANGES:"Änderungen verwerfen",FONT_COLOR:"Textfarbe",SAVING:"Sichern ...",SAVE_COMPLETE:"Sichern abgeschlossen",ZOOM_IN_ERROR:"Zoom zu hoch.",ZOOM_ERROR:"Es kann nicht weiter vergrößert bzw. verkelinert werden.",ONLY_ONE_TOPIC_MUST_BE_SELECTED:"Thema konnte nicht angelegt werden. Bitte wählen Sie nur ein Thema aus.",ONE_TOPIC_MUST_BE_SELECTED:"Thema konnte nicht angelegt werden. Es muss ein Thema ausgewählt werden.",ONLY_ONE_TOPIC_MUST_BE_SELECTED_COLLAPSE:"Kinderknoten können nicht eingefaltet werden. Es muss ein Thema ausgewäht werden.",SAVE_COULD_NOT_BE_COMPLETED:"Sichern wurde nicht abgeschlossen. Versuchen Sie es später nocheinmal.",UNEXPECTED_ERROR_LOADING:"E tut uns Leid, ein unerwarteter Fehler ist aufgetreten.\nVersuchen Sie, den Editor neu zu laden. Falls das Problem erneut auftritt, bitte kontaktieren Sie uns unter support@wisemapping.com.",MAIN_TOPIC:"Hauptthema",SUB_TOPIC:"Unterthema",ISOLATED_TOPIC:"Isoliertes Thema",CENTRAL_TOPIC:"Zentrales Thema",SHORTCUTS:"Tastaturkürzel",ENTITIES_COULD_NOT_BE_DELETED:"Konnte das Thema oder die Beziehung nicht löschen. Es muss mindest ein Eintrag ausgewählt sein.",AT_LEAST_ONE_TOPIC_MUST_BE_SELECTED:"Es muss mindestens ein Thema ausgewählt sein.",CLIPBOARD_IS_EMPTY:"Es gibt nichts zu kopieren. Die Zwischenablage ist leer.",CENTRAL_TOPIC_CAN_NOT_BE_DELETED:"Das zentrale Thema kann nicht gelöscht werden.",RELATIONSHIP_COULD_NOT_BE_CREATED:"Die Beziehung konnte nicht angelegt werden. Es muss erst ein Vater-Thema ausgewählt werden, um die Beziehung herzustellen.",SELECTION_COPIED_TO_CLIPBOARD:"Themen in der Zwischenablage",WRITE_YOUR_TEXT_HERE:"Schreiben Sie ihre Notiz hier ...",REMOVE:"Entfernen",ACCEPT:"Akzeptieren",CANCEL:"Abbrechen",LINK:"Verbindung",OPEN_LINK:"Öffne URL",DUMMY:""};mindplot.Messages.BUNDLES.fr={ZOOM_IN:"Agrandir affichage",ZOOM_OUT:"Réduire affichage",TOPIC_SHAPE:"Forme du noeud",TOPIC_ADD:"Ajouter un noeud",TOPIC_DELETE:"Supprimer le noeud",TOPIC_ICON:"Ajouter une icône",TOPIC_LINK:"Ajouter un lien",TOPIC_RELATIONSHIP:"Relation du noeud",TOPIC_COLOR:"Couleur du noeud",TOPIC_BORDER_COLOR:"Couleur de bordure du noeud",TOPIC_NOTE:"Ajouter une note",FONT_FAMILY:"Type de police",FONT_SIZE:"Taille de police",FONT_BOLD:"Caractères gras",FONT_ITALIC:"Caractères italiques",UNDO:"Annuler",REDO:"Refaire",INSERT:"Insérer",SAVE:"Enregistrer",NOTE:"Note",ADD_TOPIC:"Ajouter un noeud",LOADING:"Chargement ...",EXPORT:"Exporter",PRINT:"Imprimer",PUBLISH:"Publier",COLLABORATE:"Partager",HISTORY:"Historique",DISCARD_CHANGES:"Annuler les changements",FONT_COLOR:"Couleur de police",SAVING:"Enregistrement ...",SAVE_COMPLETE:"Enregistrement terminé",ZOOM_IN_ERROR:"Zoom trop grand.",ZOOM_ERROR:"Impossible de zoomer plus.",ONLY_ONE_TOPIC_MUST_BE_SELECTED:"Impossible de créer un noeud. Un seul noeud doit être sélectionné.",ONE_TOPIC_MUST_BE_SELECTED:"Impossible de créer un noeud. Un noeud parent doit être sélectionné au préalable.",ONLY_ONE_TOPIC_MUST_BE_SELECTED_COLLAPSE:"Un noeud enfant ne peut pas être réduit. Un noeud doit être sélectionné.",SAVE_COULD_NOT_BE_COMPLETED:"Enregistrement impossible. Essayer ultérieurement.",UNEXPECTED_ERROR_LOADING:"Nous sommes désolés, une erreur vient de survenir.\nEssayez de recharger l'éditeur. Si le problème persiste, contactez-nous : support@wisemapping.com.",MAIN_TOPIC:"Noeud titre principal",SUB_TOPIC:"Noeud sous-titre",ISOLATED_TOPIC:"Noeud isolé",CENTRAL_TOPIC:"Noeud racine",SHORTCUTS:"Raccourcis clavier",ENTITIES_COULD_NOT_BE_DELETED:"Impossible d'effacer un noeud ou une relation. Au moins un objet de la carte doit être sélectionné.",AT_LEAST_ONE_TOPIC_MUST_BE_SELECTED:"Au moins un objet de la carte doit être sélectionné.",CLIPBOARD_IS_EMPTY:"Rien à copier. Presse-papier vide.",CENTRAL_TOPIC_CAN_NOT_BE_DELETED:"Le noeud racine ne peut pas être effacé.",RELATIONSHIP_COULD_NOT_BE_CREATED:"Impossible de créer relation. Un noeud parent doit être sélectionné au préalable.",SELECTION_COPIED_TO_CLIPBOARD:"Noeuds sélectionnés copiés dans le presse-papiers.",ACCEPT:"Accepter",CANCEL:"Annuler",REMOVE:"Supprimer",WRITE_YOUR_TEXT_HERE:"Écrivez votre texte ici ...",LINK:"Lien",DUMMY:""};mindplot.Messages.BUNDLES.pt_br={ZOOM_IN:"Ampliar",ZOOM_OUT:"Reduzir",TOPIC_SHAPE:"Forma do T\u00f3pico",TOPIC_ADD:"Adicionar T\u00f3pico",TOPIC_DELETE:"Deletar T\u00f3pico",TOPIC_ICON:"Adicionar \u00cdcone",TOPIC_LINK:"Adicionar Link",TOPIC_RELATIONSHIP:"Relacionamento",TOPIC_COLOR:"Cor do T\u00f3pico",TOPIC_BORDER_COLOR:"Cor da Borda do T\u00f3pico",TOPIC_NOTE:"Adicionar Nota",FONT_FAMILY:"Tipo de Fonte",FONT_SIZE:"Tamanho da Fonte",FONT_BOLD:"Fonte Negrito",FONT_ITALIC:"Fonte It\u00e1lico",UNDO:"Desfazer",REDO:"Refazer",INSERT:"Inserir",SAVE:"Salvar",NOTE:"Nota",ADD_TOPIC:"Adicionar T\u00f3pico",LOADING:"Carregando ...",EXPORT:"Exportar",PRINT:"Imprimir",PUBLISH:"Publicar",COLLABORATE:"Colaborar",HISTORY:"Hist\u00f3ria",DISCARD_CHANGES:"Descartar Altera\u00e7\u00f5es",FONT_COLOR:"Cor da Fonte",SAVING:"Salvando ...",SAVE_COMPLETE:"Salvamento Completo",ZOOM_IN_ERROR:"Zoom excessivo.",ZOOM_ERROR:"N\u00e3o \u00e9 poss\u00edvel aplicar mais zoom.",ONLY_ONE_TOPIC_MUST_BE_SELECTED:"N\u00e3o foi poss\u00edvel criar t\u00f3pico. Apenas um t\u00f3pico deve ser selecionado.",ONE_TOPIC_MUST_BE_SELECTED:"N\u00e3o foi poss\u00edvel criar t\u00f3pico. Um t\u00f3pico deve ser selecionado.",SAVE_COULD_NOT_BE_COMPLETED:"Salvamento n\u00e3o pode ser completado. Tente novamente mais tarde.",UNEXPECTED_ERROR_LOADING:"Ocorreu um erro inesperado.\nTente recarregar novamente o editor. Se o problema persistir, contacte-nos em support@wisemapping.com.",MAIN_TOPIC:"T\u00f3pico Principal",SUB_TOPIC:"Sub T\u00f3pico",ISOLATED_TOPIC:"T\u00f3pico Isolado",CENTRAL_TOPIC:"T\u00f3pico Central",SHORTCUTS:"Atalho",ENTITIES_COULD_NOT_BE_DELETED:"O tópico ou a relação não pode ser apagado. Seleccionar pelo menos um.",AT_LEAST_ONE_TOPIC_MUST_BE_SELECTED:"Pelo menos um tópico deve ser selecionado",CLIPBOARD_IS_EMPTY:"Nada para copiar. Clipboard está vazio.",CENTRAL_TOPIC_CAN_NOT_BE_DELETED:"O tópico central não pode ser apagado.",RELATIONSHIP_COULD_NOT_BE_CREATED:"A relação não pode ser criada. Uma relação pai deve ser selecionada primeiro.",SELECTION_COPIED_TO_CLIPBOARD:"Tópicos copiados ao clipboard.",ONLY_ONE_TOPIC_MUST_BE_SELECTED_COLLAPSE:"Tópicos filhos não podem ser colapsados. Só um tópico deve ser selecionado.",DUMMY:""};mindplot.Messages.BUNDLES.zh_cn={ZOOM_IN:"放大",ZOOM_OUT:"缩小",TOPIC_SHAPE:"节点外形",TOPIC_ADD:"添加节点",TOPIC_DELETE:"删除节点",TOPIC_ICON:"加入图标",TOPIC_LINK:"添加链接",TOPIC_RELATIONSHIP:"关系",TOPIC_COLOR:"节点颜色",TOPIC_BORDER_COLOR:"边框颜色",TOPIC_NOTE:"添加注释",FONT_FAMILY:"字体",FONT_SIZE:"文字大小",FONT_BOLD:"粗体",FONT_ITALIC:"斜体",UNDO:"撤销",REDO:"重做",INSERT:"插入",SAVE:"保存",NOTE:"注释",ADD_TOPIC:"添加节点",LOADING:"载入中……",EXPORT:"导出",PRINT:"打印",PUBLISH:"公开",COLLABORATE:"共享",HISTORY:"历史",DISCARD_CHANGES:"清除改变",FONT_COLOR:"文本颜色",SAVING:"保存中……",SAVE_COMPLETE:"完成保存",ZOOM_IN_ERROR:"缩放过多。",ZOOM_ERROR:"不能再缩放。",ONLY_ONE_TOPIC_MUST_BE_SELECTED:"不能创建节点。仅能选择一个节点。",ONE_TOPIC_MUST_BE_SELECTED:"不能创建节点。必须选择一个节点。",ONLY_ONE_TOPIC_MUST_BE_SELECTED_COLLAPSE:"子节点不能折叠。必须选择一个节点。",SAVE_COULD_NOT_BE_COMPLETED:"保存未完成。稍后再试。",UNEXPECTED_ERROR_LOADING:"抱歉，突遭错误，我们无法处理你的请求。\n尝试重新装载编辑器。如果问题依然存在请联系support@wisemapping.com。",MAIN_TOPIC:"主节点",SUB_TOPIC:"子节点",ISOLATED_TOPIC:"独立节点",CENTRAL_TOPIC:"中心节点",SHORTCUTS:"快捷键",ENTITIES_COULD_NOT_BE_DELETED:"不能删除节点或者关系。至少应选择一个对象。",AT_LEAST_ONE_TOPIC_MUST_BE_SELECTED:"至少应选择一个节点。",CLIPBOARD_IS_EMPTY:"无法拷贝。 粘贴板是空的。",CENTRAL_TOPIC_CAN_NOT_BE_DELETED:"不能删除根节点。",RELATIONSHIP_COULD_NOT_BE_CREATED:"不能创建关系。 应先选择创建关系的一对上级节点。",SELECTION_COPIED_TO_CLIPBOARD:"节点已拷贝到粘贴板。",DUMMY:""};mindplot.Messages.BUNDLES.zh_tw={ZOOM_IN:"放大",ZOOM_OUT:"縮小",TOPIC_SHAPE:"節點外形",TOPIC_ADD:"添加節點",TOPIC_DELETE:"刪除節點",TOPIC_ICON:"加入圖示",TOPIC_LINK:"添加鏈接",TOPIC_RELATIONSHIP:"關係",TOPIC_COLOR:"節點顏色",TOPIC_BORDER_COLOR:"邊框顏色",TOPIC_NOTE:"添加注釋",FONT_FAMILY:"字體",FONT_SIZE:"文字大小",FONT_BOLD:"粗體",FONT_ITALIC:"斜體",UNDO:"撤銷",REDO:"重做",INSERT:"插入",SAVE:"保存",NOTE:"注釋",ADD_TOPIC:"添加節點",LOADING:"載入中……",EXPORT:"導出",PRINT:"列印",PUBLISH:"公開",COLLABORATE:"共用",HISTORY:"歷史",DISCARD_CHANGES:"清除改變",FONT_COLOR:"文本顏色",SAVING:"保存中……",SAVE_COMPLETE:"完成保存",ZOOM_IN_ERROR:"縮放過多。",ZOOM_ERROR:"不能再縮放。",ONLY_ONE_TOPIC_MUST_BE_SELECTED:"不能創建節點。僅能選擇一個節點。",ONE_TOPIC_MUST_BE_SELECTED:"不能創建節點。必須選擇一個節點。",ONLY_ONE_TOPIC_MUST_BE_SELECTED_COLLAPSE:"子節點不能折疊。必須選擇一個節點。",SAVE_COULD_NOT_BE_COMPLETED:"保存未完成。稍後再試。",UNEXPECTED_ERROR_LOADING:"抱歉，突遭錯誤，我們無法處理你的請求。\n嘗試重新裝載編輯器。如果問題依然存在請聯繫support@wisemapping.com。",MAIN_TOPIC:"主節點",SUB_TOPIC:"子節點",ISOLATED_TOPIC:"獨立節點",CENTRAL_TOPIC:"中心節點",SHORTCUTS:"快捷鍵",ENTITIES_COULD_NOT_BE_DELETED:"不能刪除節點或者關係。至少應選擇一個對象。",AT_LEAST_ONE_TOPIC_MUST_BE_SELECTED:"至少應選擇一個節點。",CLIPBOARD_IS_EMPTY:"無法拷貝。 粘貼板是空的。",CENTRAL_TOPIC_CAN_NOT_BE_DELETED:"不能刪除根節點。",RELATIONSHIP_COULD_NOT_BE_CREATED:"不能創建關係。 應先選擇創建關係的一對上級節點。",SELECTION_COPIED_TO_CLIPBOARD:"節點已拷貝到粘貼板。",DUMMY:""};mindplot.Messages.BUNDLES.ca={DISCARD_CHANGES:"Descartar els canvis",SAVE:"Desar",INSERT:"Inserir",ZOOM_IN:"Apropar",ZOOM_OUT:"Allunyar",TOPIC_BORDER_COLOR:"Color del bord",TOPIC_SHAPE:"Forma del Tòpic",TOPIC_ADD:"Afegir Tòpic",TOPIC_DELETE:"Esborrar Tòpic",TOPIC_ICON:"Afegir Icona",TOPIC_LINK:"Afegir Enllaç",TOPIC_NOTE:"Afegir Nota",TOPIC_COLOR:"Color del Tòpic",TOPIC_RELATIONSHIP:"Relació",FONT_FAMILY:"Tipus de font",FONT_SIZE:"Mida del text",FONT_BOLD:"Negreta",FONT_ITALIC:"Itàlica",FONT_COLOR:"Color del Text",UNDO:"Refer",NOTE:"Nota",LOADING:"Carregant ...",PRINT:"Imprimir",PUBLISH:"Publicar",REDO:"Desfer",ADD_TOPIC:"Afegir Tòpic",COLLABORATE:"Compartir",EXPORT:"Exportar",HISTORY:"Història",SAVE_COMPLETE:"Desat completat",SAVING:"Gravant ...",ONE_TOPIC_MUST_BE_SELECTED:"No ha estat possible crear un nou tòpic. Com a mínim ha de seleccionar un tòpic.",ONLY_ONE_TOPIC_MUST_BE_SELECTED:"No ha estar possible crear un nou tòpic. Només un tòpic ha d'estar seleccionat.",SAVE_COULD_NOT_BE_COMPLETED:"No s'ha pogut desar. Provi més tard.",UNEXPECTED_ERROR_LOADING:"Ho sentim, un error ha esdevingut inesperadament. Provi recarregant l'editor, si el problema continua contacti a support@wisemapping.com.",ZOOM_ERROR:"No es pot fer més zoom.",ZOOM_IN_ERROR:"El zoom és massa creixent.",MAIN_TOPIC:"Tòpic principal",SUB_TOPIC:"Tòpic secundari",ISOLATED_TOPIC:"Tòpic aïllat",CENTRAL_TOPIC:"Tòpic central",ONLY_ONE_TOPIC_MUST_BE_SELECTED_COLLAPSE:"Els tòpics fills no es poden col·lapsar. Només un tòpic ha d'estar seleccionat.",SHORTCUTS:"Accessos directes",ENTITIES_COULD_NOT_BE_DELETED:"El tòpic o la relució no poden ser esborrats. Com a mínim ha de seleccionar un.",AT_LEAST_ONE_TOPIC_MUST_BE_SELECTED:"Com a mínim ha de seleccionar un tòpic.",CLIPBOARD_IS_EMPTY:"Res a copiar.",CENTRAL_TOPIC_CAN_NOT_BE_DELETED:"El tòpic central no pot esborrar-se.",RELATIONSHIP_COULD_NOT_BE_CREATED:"La relució no s'ha pout drear. Primer has de seleccionar una relució pare.",SELECTION_COPIED_TO_CLIPBOARD:"Tòpics copiats",WRITE_YOUR_TEXT_HERE:"Escriu aquí la teva nota ...",REMOVE:"Esborrar",ACCEPT:"Acceptar",CANCEL:"Cancel·lar",LINK:"Enllaç",OPEN_LINK:"Obrir Enllaç",SESSION_EXPIRED:"La seva sessió ha finalitzat. Si us plau, torni a connectar-se.",DUMMY:""};try{$(document).trigger("loadcomplete","mind")
}catch(e){console.error(e.stack)
};