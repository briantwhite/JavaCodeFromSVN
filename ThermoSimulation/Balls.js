/**
 * Created by j4 on 2/3/2015.
 */
$(document).ready(function() {
    var ballradius = 10;
    var nballswanted = 10;
    var canvas = document.getElementById("canvas");
    var ctx = canvas.getContext("2d");

    var allballs = new Array();
    var barriers = new Array();
    var nballs = 0;
    var nbarriers = 0;
    var animTimer = null;
    var w1 = new Wall(0, 0, canvas.width, "horizontal", "below");
    var w2 = new Wall(canvas.height, 0, canvas.width, "horizontal", "above");
    var w3 = new Wall(0, 0, canvas.height, "vertical", "right");
    var w4 = new Wall(canvas.width, 0, canvas.height, "vertical", "left");
    reset();


    $("#reset").click(function(){
        reset();
        $('#samplestr').html("");
        $('#stats').html("");
    });

    function reset(){
        clearInterval(animTimer);
        allballs = new Array();
        nballs = 0;
        addballs(nballswanted);
        showballs();
    }

    $("#addballs").click(function(){
        addballs(nballswanted);
        showballs();
    });

    $("#faster").click(function(){
        changeballspeed(1.5);
    });

    $("#slower").click(function(){
        changeballspeed(0.66666);
    });

    $("#start").click(function(){
        clearInterval(animTimer);
        animTimer = setInterval(animate, 20);
    });

    $("#sample").click(function(){
        var diststr = "{";
        var ttlspeed = 0;
        var ttlKE = 0;  //kinetic energy
        var ttlpx = 0;  //momentum, x direction
        var ttlpy = 0;  //momentum, y direction
        for(var i = 0; i < nballs; i++){
            var speed = allballs[i].speed();
            var p = allballs[i].p();
            ttlpx += p.x;
            ttlpy += p.y;
            ttlKE += allballs[i].ke();
            diststr = diststr + nd(speed, 4) + ", ";
            ttlspeed += speed;
        }
        diststr = diststr.substr(0, diststr.length - 2) + "}";
        var avgspeed = ttlspeed / nballs;
        var sumsqrdev = 0;
        var maxspeed = 0;
        var minspeed = 100000000;
        for(i = 0; i < nballs; i++) {
            speed = allballs[i].speed();
            if (speed > maxspeed){maxspeed = speed;}
            if (speed < minspeed){minspeed = speed;}
            sumsqrdev += Math.pow(avgspeed - speed, 2);
        }
        var rmsdev = Math.sqrt(sumsqrdev/nballs);
        $('#samplestr').html(diststr);
        $('#stats').html("Speeds: Average: " + nd(avgspeed, 5) + " Max: " + nd(maxspeed,5) + " Min: "
            + nd(minspeed, 5) + " RMS Dev: " + nd(rmsdev, 5) + " Ttl Px: " + nd(ttlpx,3) + " Ttl Py: "
            + nd(ttlpy, 3) + " Ttl KE: " + nd(ttlKE, 3)
        );
    });

    function nd(n, dec){
        var df = Math.pow(10, dec);
        return '' + Math.round(df * n)/df;
    }

    function addballs(nballswanted){
        for(var i = 0; i < nballswanted; i++){
            var btemp = new Ball(200,200,5,0,ballradius,"808080");
            btemp.randomizeposition();
            btemp.randomizedirection();
        }
    }

    function showballs(){
        blankscreen();
        for(var i = 0; i < nballs; i++){
            allballs[i].draw()
        }
    }

    function changeballspeed(deltafactor){ //both components of v are multiplied by deltafactor
        for(var i = 0; i < nballs; i++){
            allballs[i].changespeed(deltafactor);
        }
    }


    function animate(){
        blankscreen();
        for(var i = 0; i < nballs; i++){
            allballs[i].draw()
        }
        for(i = 0; i < nballs; i++){
            allballs[i].move()
        }
        for(i = 0; i < nballs; i++) {
            for (var j = i + 1; j < nballs; j++) {
                allballs[i].collidewith(allballs[j]);
            }
        }
        for(i = 0; i < nbarriers; i++){
            for(j = 0; j < nballs; j++){
                barriers[i].collidewith(allballs[j]);
            }
        }
    }


    function blankscreen() {
        if (ctx) {
            ctx.fillStyle = "FFFFFF";
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            for (var i = 0; i < nbarriers; i++) {
                barriers[i].draw()
            }
        }
    }






    function Ball(x, y, dx, dy, r, h) {
        this.x = x;
        this.y = y;
        this.xlast = x;
        this.ylast = y;
        this.v = new Vector2d(dx, dy);
        this.r = r;
        this.m = Math.PI * r * r / 1000;
        this.h = h; // color, rgb or rgba string, in quotes
        allballs.push(this);
        this.ballno = nballs;
        nballs++;

        this.randomizeposition = function(){
            this.x = this.r + (canvas.width - (this.r * 2)) * Math.random();
            this.y = this.r + (canvas.height - (this.r * 2)) * Math.random();
        };

        this.randomizedirection = function(){
            var angle = Math.PI * 2 * Math.random();
            this.v = this.v.rotatetoangle(angle);
        };

        this.collidewith = function(b){  //b is a Ball object
            var test1 = this.distto(b);
            if(this.distto(b)>(this.r + b.r))return;
            //compute new direction vector
            var xyunit = new Vector2d(b.x - this.x, b.y - this.y); //difference vector joining centers of balls
            //rotate both ball's vel vector coord sys to match axis of join
            var vr = this.v.rotateto(xyunit);
            var bvr = b.v.rotateto(xyunit);
            //compute total momentum pxy and kinetic energy kexy
            var pxy = this.m*vr.x + b.m*bvr.x;
            var kexy = this.m*vr.x*vr.x + b.m*bvr.x*bvr.x;
            //compute v.y and b.v.y after collision based on conservation of p and ke
            vr.x = (this.m*pxy - Math.sqrt(b.m*this.m*(b.m*kexy + kexy*this.m - pxy*pxy)))/(b.m*this.m + this.m*this.m);
            bvr.x =(b.m*pxy + Math.sqrt(b.m*this.m*(b.m*kexy + kexy*this.m - pxy*pxy)))/(b.m*b.m + b.m*this.m);
            //rotate back to original coords
            var oldunit = new Vector2d(xyunit.x, -xyunit.y);
            this.v = vr.rotateto(oldunit);
            b.v=bvr.rotateto(oldunit);
        };

        this.draw = function(){   //g is graphics context for the canvas
            ctx.fillStyle = this.h;
            ctx.beginPath();
            ctx.arc(this.x, this.y, this.r, 0.0, 2*Math.PI);
            ctx.fill();
        };

        this.move = function () {
            this.xlast = this.x;
            this.ylast = this.y;
            this.x += this.v.x;
            this.y += this.v.y;
            //var j = this.ballno + 1;
            //while (j < nballs) {
            //    this.collidewith(allballs[j]);
            //    j++;
            //}
            //for (var i = 0; i < nbarriers; i++) {
            //    barriers[i].collidewith(this);
            //}
        };

        this.speed = function(){
            return this.v.magnitude();
        };

        this.p = function(){
            return this.v.scalarmult(this.m);
        }

        this.ke = function(){
            return this.m * Math.pow(this.v.magnitude(), 2) / 2;
        }
        this.changespeed = function(deltafactor){
            this.v.x = this.v.x * deltafactor;
            this.v.y = this.v.y * deltafactor;
        };


        this.resetxy = function(x, y){
            this.v = new vector2d(x - this.x, y - this.y);
            this.xlast = this.x;
            this.ylast = this.y;
            this.x = x;
            this.y = y;
        };

        this.distto = function (b) {  //b is a Ball object
            return Math.sqrt(Math.pow((this.x - b.x), 2.0) + Math.pow((this.y - b.y), 2.0));
        };
    }

    function Wall(pos, begin, end, orient, collidefrom){
        this.pos = pos; // x position of vertical wall or y position of horizontal wall
        this.begin = begin; // y position of top end of vertical wall or x position of left end horizontal wall
        this.end = end; // y position of bottom end of vertical wall or x position of right end horizontal wall
        this.ctype = 0; // 00 for vert wall approached from right
                        // 01 for vert wall approached from left
                        // 10 for horiz wall approached from below
                        // 11 for horiz wall approached from above
        if(orient == "vertical" && collidefrom == "right"){this.ctype =0;}
        if(orient == "vertical" && collidefrom == "left"){this.ctype =1;}
        if(orient == "horizontal" && collidefrom == "below"){this.ctype =10;}
        if(orient == "horizontal" && collidefrom == "above"){this.ctype =11;}
        barriers.push(this);
        nbarriers++;

        this.draw = function(){   //g is graphics context for the canvas
            if(this.ctype < 10) {  //vertical wall
                ctx.lineWidth = 2;
                ctx.beginPath();
                ctx.moveTo(this.pos, this.begin);
                ctx.lineTo(this.pos, this.end);
                ctx.stroke();
            } else {  //horizontal wall
                ctx.lineWidth = 2;
                ctx.beginPath();
                ctx.moveTo(this.begin, this.pos);
                ctx.lineTo(this.end, this.pos);
                ctx.stroke();
            }
        };

        this.collidewith = function(b) {  // b is Ball object
            switch (this.ctype) {
                case 0:	//vertical wall, approach from right
                    if ((b.x - b.r <= this.pos)) {
                        if ((b.xlast - b.r > this.pos) && ((b.ylast > this.begin) || (b.y > this.begin)) && ((b.ylast < this.end) || (b.y < this.end))) {
                            b.xlast = b.x;
                            b.x = this.pos * 2 + b.r * 2 - b.x; //= this.pos+(r-(x-this.pos))
                            b.v.x = -b.v.x;
                        }
                    }
                    break;
                case 1:   //vertical wall, approach from left
                    if ((b.x + b.r >= this.pos)) {
                        if ((b.xlast + b.r < this.pos) && ((b.ylast > this.begin) || (b.y > this.begin)) && ((b.ylast < this.end) || (b.y < this.end))) {
                            b.xlast = b.x;
                            b.x = (this.pos - b.r) - (b.x - (this.pos - b.r));
                            b.v.x = -b.v.x;
                        }
                    }
                    break;
                case 10:  //horizontal wall, approach from below
                    if ((b.y - b.r <= this.pos)) {
                        if ((b.ylast - b.r > this.pos) && ((b.xlast > this.begin) || (b.x > this.begin)) && ((b.xlast < this.end) || (b.x < this.end))) {
                            b.ylast = b.y;
                            b.y = this.pos * 2 + b.r * 2 - b.y;
                            b.v.y = -b.v.y;
                        }
                    }
                    break;
                case 11:  //horizontal wall, approach from above
                    if ((b.y + b.r >= this.pos)) {
                        if ((b.ylast + b.r < this.pos) && ((b.xlast > this.begin) || (b.x > this.begin)) && ((b.xlast < this.end) || (b.x < this.end))) {
                            b.ylast = b.y;
                            b.y = (this.pos - b.r) - (b.y - (this.pos - b.r));
                            b.v.y = -b.v.y;
                        }
                    }
            }
        }
    }

    function Vector2d(x, y){
        this.x = x;
        this.y = y;
        this.drawvec = function(){
            ctx.lineTo(this.x, this.y);
        };

        this.addvec = function(v){
            return new Vector2d(this.x+v.x, this.y+v.y);
        };

        this.subtrvec = function(v){
            return new Vector2d(this.x-v.x, this.y-v.y);
        };

        this.dotprod = function(v){
            return this.x*v.x + this.y*v.y;
        };

        this.scalarmult = function(s){
            return new Vector2d(s*this.x, s*this.y);
        };

        this.magnitude = function(){
            return Math.sqrt(this.x*this.x + this.y*this.y);
        };

        this.unitvec = function(){
            var m = this.magnitude(this);
            return new Vector2d(this.x/m,this.y/m);
        };

        this.rotatetoangle = function(angle){  //angle is radians counterclockwise from x axis
            unitvec = new Vector2d(Math.cos(angle), Math.sin(angle));
            return this.rotateto(unitvec);
        };

        this.rotateto = function(v){
            cos = (new Vector2d(1,0)).dotprod(v.unitvec());
            sin = Math.sqrt(1. - cos*cos);
            if(v.y < 0)sin = -sin;
            return new Vector2d(this.x*cos + this.y*sin, -this.x*sin + this.y*cos);
        };

        this.rotate90 = function(){
            return new Vector2d(this.y,-this.x);
        };

        this.reflect = function(){
            return new Vector2d(this.x,-this.y);
        };

        this.minus = function(){
            return new Vector2d(-this.x,-this.y);
        };



    }



});