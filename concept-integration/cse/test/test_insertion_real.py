#!/usr/bin/env python3

import urllib
import urllib.request
import json
import codecs


url = "http://localhost:9999"

#deleting all
req = urllib.request.Request(url+"/delete_all")



data = {
"id": "9",
"project_id": "22",
"component":"BA (Brief Analysis)",
"title": "Brief Analysis - Client A cartoon",
"keywords":"Chair,Wood",
"categories":"Furniture,Consumer,Market Analysis,Material",
#"categories":"Furniture%2.5, Consumer%3.1, Market Analysis%0.3",
"url":"document/9",
"content": '''<p><strong style="margin: 0px; padding: 0px; color: #555555; font-family: 'trebuchet ms', tahoma, sans-serif; font-size: 12px;">Chair Design Guidelines</strong></p>
<ol style="margin: 10px; padding: 0px 0px 0px 30px; color: #555555; font-family: 'trebuchet ms', tahoma, sans-serif; font-size: 12px;">
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The occupant should be able to sit in and get up from the chair without difficulty. The feet should rest flat on the floor without the knees projecting above the upper leg. A seat height of 16 to 18 inches fits the bill for most adults. Arm rests should support the forearms without raising the shoulders (7" to 9" above seat). Half arm rests enable the chair to be drawn up close to a table.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The depth of the seat should allow clearance from the front edge of the seat to the back of the occupant's leg. A seat that is too deep will press against the back of the legs forcing the occupant to slouch forward. A seat that is too shallow may be unstable and feel precarious. A seat depth of 15 to 18 inches is recommended for most adults.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The width of the seat often tapers by 2" to 3" from the front to the rear to allow clearance for legs and clothes in front while allowing elbow room in back. Many chairs have seats that are about 15" wide in the rear and 18" wide in the front.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">For relaxed seating, the seat should slant slightly toward the back (about 5&deg; to 8&deg;) to keep the occupant from slipping out of the chair. However, an office desk or typist's chair often has a flat seat to facilitate leaning forward.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The back of the chair is often slanted backwards for comfort - up to 5&deg; for a dining chair and 10&deg; to 15&deg; for a more casual chair. As the chair back angle increases, the seat should be tilted further backward to prevent forward sliding and lowered to prevent the front edge of the seat from pressing against the back of the legs.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The seat back should support the lumbar region without being so high as to interfere with the shoulder blades. A back height of about 12" to 16" above the seat is ideal for most adults. Note that this guideline is often ignored for formal "high-backed" dining chairs.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The lower portion of the seat back (first 4"-8") should curve out or be left open to allow room for the buttocks<strong style="margin: 0px; padding: 0px;">.</strong></div>
</li>
</ol>
<p style="margin: 10px 10px 15px; padding: 0px; color: #555555; font-family: 'trebuchet ms', tahoma, sans-serif; font-size: 12px;"><strong style="margin: 0px; padding: 0px;">Chair Dimensions For Average-Sized Adults</strong></p>
<p style="margin: 10px 10px 15px; padding: 0px; color: #555555; font-family: 'trebuchet ms', tahoma, sans-serif; font-size: 12px;">The following dimensions apply to chairs designed for average-sized adults sitting in an upright or alert posture.</p>
<ul style="margin: 10px; padding: 0px 0px 0px 20px; color: #555555; font-family: 'trebuchet ms', tahoma, sans-serif; font-size: 12px;">
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Seat width 16"-20"</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Seat depth 15"-18"</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Seat height from floor 16"-18"</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Slope of seat front to rear 5&deg; to 8&deg; (3/4" to 1" drop)</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Arm rest height above seat 7"-9"</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Arm rest length (full arm rest) 8" minimum</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Arm rest width 2" average</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Set back of arm rest from front 2"-3"</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Seat back height 12"-16" above seat</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Seat back recline angle 0&deg;-5&deg; (formal); 10&deg;-15&deg; (casual)</div>
</li>
</ul>
<p style="margin: 10px 10px 15px; padding: 0px; color: #555555; font-family: 'trebuchet ms', tahoma, sans-serif; font-size: 12px;"><strong style="margin: 0px; padding: 0px;">Chair Dimensions for Children</strong></p>
<p style="margin: 10px 10px 15px; padding: 0px; color: #555555; font-family: 'trebuchet ms', tahoma, sans-serif; font-size: 12px;">The following table presents seat heights for children of various ages. Other chair dimensions can be derived<strong style="margin: 0px; padding: 0px;">&nbsp;</strong>proportionately based on the chair dimensions for adults. For most elementary school age children</p>
<p>&nbsp;</p>
<ul style="margin: 10px; padding: 0px 0px 0px 20px; color: #555555; font-family: 'trebuchet ms', tahoma, sans-serif; font-size: 12px;">
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">a seat width and depth of 12 to 14 inces,</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">a backrest height of 9 to 11 inches,</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">an arm rest height of 5 to 7 inhces</div>
</li>
</ul>
<p dir="ltr" style="margin: 10px 10px 15px; padding: 0px;">Specifications for Different Types of Seats</p>
<p dir="ltr" style="margin: 10px 10px 15px; padding: 0px; color: #555555; font-family: 'trebuchet ms', tahoma, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: 1; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #ffffff;">(Listed dimensions are based on average-sized adults).</p>
<ul style="margin: 10px; padding: 0px 0px 0px 20px; color: #555555; font-family: 'trebuchet ms', tahoma, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: 1; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #ffffff;">
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Dining Chair:</div>
</li>
<ul style="margin: 10px; padding: 0px 0px 0px 20px;">
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The seat height averages 16" to 17",</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">seat width averages about 15-1/2" in back and 18" in front,&nbsp;</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">average seat depth is 16" to 16-1/2".</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">if arm rests are used, they should be 7" to 9" above the seat but able to fit under the table apron.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">average width between arm rests at the front of the chair is approximately 19".</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The seat is usually level or has a maximum front to back slope of about 1".</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The seat back is reclined no more than 5 degrees and ranges in height from 12" to 20" above the seat (or higher in very formal chairs).</div>
</li>
</ul>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Desk Chair:</div>
</li>
<ul style="margin: 10px; padding: 0px 0px 0px 20px;">
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Most specifications for a dining chair apply here</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">except that the seat back often protrudes no higher than the middle of the back - about 14" to 16".&nbsp;</div>
</li>
</ul>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Easy Chair:</div>
</li>
<ul style="margin: 10px; padding: 0px 0px 0px 20px;">
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Should provide a more relaxed, reclining position than a dining chair</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">seat height is lower (about 16") with allowance made for compression of seat cushion.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The seat is angled backwards about 10 degrees&nbsp;</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">a seat to back angle of 95 to 120 degrees.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">For maximum comfort, the seat back should be no more than 14" to 16" above the seat.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Arm rests are recommended for easy in/out and they average 5" to 8" in height and 2" to 4" in width.</div>
</li>
</ul>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Sofa/Love-seat:</div>
</li>
<ul style="margin: 10px; padding: 0px 0px 0px 20px;">
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Seat depth ranges from 18" to 22"&nbsp;</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">seat height ranges from 14" to 18" (16" average).</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The seat back typically rises 15" to 18" above the seat&nbsp;</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">usually angled backwards at up to 25 degrees.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Upholstered seats generally slope 1" from front to back.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Arm rests generally protrude 4" to 8" above seat.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The overall width of a love-seat is about 56" to 60" - 24" per person, plus 4" to 6" for each arm rest.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">A full-size sofa measures about 90" in overall width.</div>
</li>
</ul>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Bar Stool:</div>
</li>
<ul style="margin: 10px; padding: 0px 0px 0px 20px;">
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">In general, the seat of a bar stool should be 12" to 15" below the top surface of the bar, but never higher than 30". For a normal bar that is 40" to 45" high,</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">a seat height of 28" to 30" is standard.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">For a 30" high bar (most tables), a seat height of 22" to 24" is standard.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The seat back is typically 10" to 14" above the seat.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The seat width ranges from 15" to 18"</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">the seat depth ranges from 12" to 16" (16" to 17" diameter if the seat is round).</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">A rung 20" below the seat provides a comfortable resting spot for the occupant's feet.</div>
</li>
</ul>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Rocker:</div>
</li>
<ul style="margin: 10px; padding: 0px 0px 0px 20px;">
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">In a stationary position, the seat back should tilt back approximately 25 degrees from the vertical</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">a seat to seat back angle of about 95 degrees.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The seat height in front should be no higher than 16" to 17".</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Standard seat widths and depths are 18" to 22" and 16" to 18" respectively.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The seat back is higher than most chairs - about 40" above the floor.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">The runners commonly have a horizontal extension of about 30" with a curvature radius of 36" to 38".</div>
</li>
</ul>
</ul>
<p style="margin: 10px 10px 15px; padding: 0px; color: #555555; font-family: 'trebuchet ms', tahoma, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: left; text-indent: 0px; text-transform: none; white-space: normal; widows: 1; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: #ffffff;"><strong style="margin: 0px; padding: 0px;">Wood Selection for Chairs</strong></p>
<p>&nbsp;</p>
<ul style="margin: 10px; padding: 0px 0px 0px 20px; color: #555555; font-family: 'trebuchet ms', tahoma, sans-serif; font-size: 12px;">
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Use hardwoods for pieces where shocks, abrasions, and other stresses will occur.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Use softwoods in larger thicknesses to enable greater penetration of hardwood pieces (e.g., spindles).</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Do not join softwood to softwood.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">For bent chair parts, select woods such as white oak or ash that can be steam-bent without fracturing.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Legs: The legs of a chair are subject to extreme stresses and abrasive forces. Select a wood such as hard maple that is hard, resists impression, and does not splinter.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Stretchers: Abrasion resistance is less of an issue but a hard wood such as maple is recommended. Bending strength may be important (e.g., feet placed on the stretcher), so consider white oak or hickory.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Seat: Many woods will suffice, but soft woods such as pine or poplar are much easier to sculpt if you are intending to use hand tools (early craftsmen typically chose soft woods for this reason).</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Back: Use hard woods such as maple, oak or ash for spindles. For longer spindles, select a resilient wood that allows for movement - hickory is ideal.</div>
</li>
<li style="margin: 0px; padding: 0px;">
<div style="margin: 0px; padding: 0px; position: relative;">Softwoods in thicker dimensions may be used for arms and rails.</div>
</li>
</ul>
<p style="margin: 0.5em 0px; line-height: 22.4px; color: #252525; font-family: sans-serif; font-size: 14px;">Chair seats vary widely in construction and may or may not match construction of the chair's back (backrest).</p>
<p style="margin: 0.5em 0px; line-height: 22.4px; color: #252525; font-family: sans-serif; font-size: 14px;">Some systems include:</p>
<ul style="margin: 0.3em 0px 0px 1.6em; padding: 0px; color: #252525; font-family: sans-serif; font-size: 14px; line-height: 22.4px;">
<li style="margin-bottom: 0.1em;">Center seats where a solid material forms the chair seat
<ul style="list-style-type: disc; margin: 0.3em 0px 0px 1.6em; padding: 0px;">
<li style="margin-bottom: 0.1em;">Solid wood, may or may not be shaped to human contours</li>
<li style="margin-bottom: 0.1em;">Wood slats, often seen on outdoor chairs</li>
<li style="margin-bottom: 0.1em;">Padded leather, generally a flat wood base covered in padding and contained in soft leather</li>
<li style="margin-bottom: 0.1em;">Stuffed fabric, similar to padded leather</li>
<li style="margin-bottom: 0.1em;">Metal seats of solid or open design</li>
<li style="margin-bottom: 0.1em;">Molded plastic</li>
<li style="margin-bottom: 0.1em;">Stone, often&nbsp;<a style="text-decoration: none; color: #0b0080; background: none;" title="Marble" href="https://en.wikipedia.org/wiki/Marble">marble</a></li>
</ul>
</li>
<li style="margin-bottom: 0.1em;">Open center seats where a soft material is attached to the tops of chair legs or between stretchers to form the seat
<ul style="list-style-type: disc; margin: 0.3em 0px 0px 1.6em; padding: 0px;">
<li style="margin-bottom: 0.1em;">Wicker, woven to provide a surface with give to it</li>
<li style="margin-bottom: 0.1em;">Leather, may be tooled with a design</li>
<li style="margin-bottom: 0.1em;">Fabric, simple covering without support</li>
<li style="margin-bottom: 0.1em;">Tape, wide fabric tape woven into seat, seen in lawn chairs and some old chairs</li>
<li style="margin-bottom: 0.1em;"><a style="text-decoration: none; color: #0b0080; background: none;" title="Caning (furniture)" href="https://en.wikipedia.org/wiki/Caning_(furniture)">Caning</a>,</li>
<li style="margin-bottom: 0.1em;">Rush, wrapped from&nbsp;<a style="text-decoration: none; color: #0b0080; background: none;" title="Juncaceae" href="https://en.wikipedia.org/wiki/Juncaceae">rush</a>, heavy paper, strong grasses, or hand twisted while wrapped with&nbsp;<a class="mw-redirect" style="text-decoration: none; color: #0b0080; background: none;" title="Cattail" href="https://en.wikipedia.org/wiki/Cattail">cattails</a>&nbsp;to form the seat, usually in a pattern of four trapezoids meeting in the center, and on rare occasions, in elaborate patterns</li>
<li style="margin-bottom: 0.1em;"><a style="text-decoration: none; color: #0b0080; background: none;" title="Phragmites" href="https://en.wikipedia.org/wiki/Phragmites">Reed</a>,</li>
<li style="margin-bottom: 0.1em;"><a class="mw-redirect" style="text-decoration: none; color: #0b0080; background: none;" title="Rawhide (textile)" href="https://en.wikipedia.org/wiki/Rawhide_(textile)">Rawhide</a></li>
<li style="margin-bottom: 0.1em;">Splint,&nbsp;<a class="mw-redirect" style="text-decoration: none; color: #0b0080; background: none;" title="Ash tree" href="https://en.wikipedia.org/wiki/Ash_tree">ash</a>,&nbsp;<a style="text-decoration: none; color: #0b0080; background: none;" title="Oak" href="https://en.wikipedia.org/wiki/Oak">oak</a>&nbsp;or&nbsp;<a style="text-decoration: none; color: #0b0080; background: none;" title="Hickory" href="https://en.wikipedia.org/wiki/Hickory">hickory</a>&nbsp;strips are woven</li>
<li style="margin-bottom: 0.1em;">Metal, Metal mesh or wire woven to form seat</li>
</ul>
</li>
</ul>'''
}
print("Inserting "+str(data))
data = urllib.parse.urlencode(data).encode('utf-8')
#POST
req = urllib.request.Request(url+"/insert_item",data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))
print(r)

data = {
"ID": "49",
"Project ID": "22",
"COMPONENT": "MM (Mindmap)",
"TITLE": "Mindmap automaticaly generated Fri Nov 27 16:23:02 EET 2015",
"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
"url":"document/49",
"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
'''
}
print("Inserting "+str(data))
data = urllib.parse.urlencode(data).encode('utf-8')
#POST
req = urllib.request.Request(url+"/insert_item",data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))
print(r)

data = {
"ID": "12",
"PROJECT ID": "22",
"COMPONENT": "SK (Sketch)",
"TITLE": "Early draft sketch toon",
"KEYWORDS": "sketch prototype,iteration 1,wooden chair",
"CATEGORIES": "Consumer, Furniture, Usability, Aggressive",
"URL":"document/12",
"CONTENT": '''{"shapes":[{"className":"LinePath","data":{"order":3,"tailSize":3,"smooth":true,
"pointCoordinatePairs":[[199,219],[200,219],[199,223],[194,237],
[187,261],[183,292],[182,321],[183,346],
[195,369],[211,383],[224,389],[250,392],[270,383],[282,369],[293,340],[294,298],[282,271],[259,240],
[232,219],[213,212],[193,211],[184,211],[179,215],[175,223],[175,234],[184,245],[198,253],[217,257],
[232,257],[238,256],[243,253],[244,250],[244,247],[238,244],[224,243],[209,252],[191,290],[189,320],
[189,332],[196,342],[220,347],[236,334],[248,314],[256,283],[258,245],[252,227],[232,212],[222,216],
[207,252],[206,269],[206,277],[210,283],[217,285],[230,285],[234,283],[239,277],[240,275],[241,274],
[239,274],[233,279]],"pointSize":5,"pointColor":"#000"},"id":"3111de75-a1c5-9dd9-55bf-3d7f3e29fc89"},
{"className":"LinePath","data":{"order":3,"tailSize":3,"smooth":true,"pointCoordinatePairs":[[666,283],
[655,274],[629,270],[601,281],[580,310],[563,353],[562,398],[589,432],[615,448],[665,457],[703,454],
[725,432],[745,395],[748,343],[739,332],[645,333],[637,347],[630,367],[630,390],[638,398],[656,399],
[682,387],[693,364],[697,326],[682,290],[653,267],[619,257],[603,260],[589,286],[589,318],[609,354],[624,365],[653,379],[674,381],[684,381],[676,327],[665,317],[637,303],[625,303],[615,313],[613,332],[619,351],[645,368],[676,372],[689,372],[702,371],[706,362],[706,353],[693,346],[676,345]],"pointSize":5,"pointColor":"#000"},"id":"a766e38e-c911-49e8-6901-78c352adf58f"},{"className":"LinePath","data":{"order":3,"tailSize":3,"smooth":true,"pointCoordinatePairs":[[541,163],[538,165],[542,166],[575,166],[613,166],[678,165],[695,164],[718,162],[725,162],[728,161],[724,160],[682,159],[602,159],[546,164],[530,168],[515,173],[513,174],[526,174],[575,168],[651,159],[675,157],[691,157],[703,155],[686,155],[629,174],[570,190],[530,201],[516,205],[505,207],[513,203],[573,174],[632,147],[658,135],[677,127],[689,120],[690,119],[653,119],[609,127],[597,129],[593,129],[605,125],[642,104],[653,96],[663,87
],[634,87],[574,105],[519,126]],"pointSize":5,"pointColor":"#000"},
"id":"3897d580-cc91-c70a-7a93-13ec392197a9"}],"colors":{"primary":"#000","secondary":"#fff",
"background":"whiteSmoke"}}'''
}
print("Inserting "+str(data))
data = urllib.parse.urlencode(data).encode('utf-8')
#POST
req = urllib.request.Request(url+"/insert_item",data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))
print(r)


data = {
"ID": "10",
"PROJECT ID": "22",
"COMPONENT": "SB (Storyboard)",
"TITLE": "SB2",
"KEYWORDS": "client demo,client meeting,chair design",
"CATEGORIES": "Furniture, Exhibition, Kitchenware, Consumer",
"url":"document/10",
"CONTENT": '''<svg width="640" height="480" xmlns="http://www.w3.org/2000/svg">
<g style="pointer-events:all"><title style="pointer-events:inherit">Layer 1</title>
<rect fill="#FF0000" stroke="#000000" stroke-width="5" style="pointer-events:inherit" x="151" y="161"
width="243" height="199" id="svg_1" fill-opacity="1" stroke-opacity="1"></rect></g></svg>'''
}
print("Inserting "+str(data))
data = urllib.parse.urlencode(data).encode('utf-8')
#POST
req = urllib.request.Request(url+"/insert_item",data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))
print(r)

f = codecs.open("imagen_base64.txt","r","ASCII")
coded_file = f.read()
f.close()

data = {
"ID": "78",
"Project ID": "22",
"COMPONENT": "FM (File Management)",
"TITLE": "21141_PE106141_S5.JPG",
#"KEYWORDS": "wooden,chair,comfortable,light,portable,waterproof,no arms,soft materials",
#"CATEGORIES": "Market Analaysis, Technology, Usability, Consumer",
"KEYWORDS":"",
"CATEGORIES":"", 
"url":"document/89",
"CONTENT": "data:image/jpeg;base64,"+coded_file
}
#print("Inserting "+str(data))
data = urllib.parse.urlencode(data).encode('utf-8')
#POST
req = urllib.request.Request(url+"/insert_item",data)
response = urllib.request.urlopen(req)
r = json.loads(response.read().decode('utf-8'))
print(r)


##data = {
##"ID": "201",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 201",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##
##data = {
##"ID": "202",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 202",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##
##data = {
##"ID": "203",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 203",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "204",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 204",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "205",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 205",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "206",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 206",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "207",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 207",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "208",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 208",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "209",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 209",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "210",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 210",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "211",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 211",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "212",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 212",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "213",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 213",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "214",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 214",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "215",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 215",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "216",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 216",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "217",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 217",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "218",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 218",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "219",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 219",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "220",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 220",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##data = {
##"ID": "221",
##"Project ID": "22",
##"COMPONENT": "MM (Mindmap)",
##"TITLE": "Mindmap automaticaly generated fake 221",
##"KEYWORDS": "chair design,iteration,consumer chair,medical chair",
##"CATEGORIES": "Modern, Steel, Warm, Exhibition, Furtniture",
##"url":"document/49",
##"CONTENT": '''<map name="49" version="tango"><topic central="true" text="COnCEPT Mindmap" id="1"><note>
##<![CDATA[Simple to operate and widely used in multiple (project) chair installations because you can change the back and seat angles quickly and simultaneously, allowing posture changes for different activities such as keyboard work or reading. Suits many people but will not suit all. Chair geometry varies between manufacturers and from model to model so, if you like this feature on one chair, do not necessarily assume you will like it on another.]]></note><topic position="194,-72" order="0" text="Synchronous Mechanism " id="2"/><topic position="204,-43" order="2" text="Free Float (Rocking) Feature" id="3"/><topic position="192,-14" order="4" text="Seat Height Adjustment" id="4"/><topic position="192,15" order="6" text="Back Height Adjustment" id="5"/><topic position="201,44" order="8" text="Adjustable Lumbar Support" id="6"/><topic position="166,73" order="10" text="Pelvic Support" id="7"/></topic></map>
##'''
##}
##print("Inserting "+str(data))
##data = urllib.parse.urlencode(data).encode('utf-8')
###POST
##req = urllib.request.Request(url+"/insert_item",data)
##response = urllib.request.urlopen(req)
##r = json.loads(response.read().decode('utf-8'))
##print(r)
##
##
##
##
##
##
##
