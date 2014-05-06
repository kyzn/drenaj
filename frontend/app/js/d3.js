(function () {
  'use strict';

  angular.module('d3')
    .factory('d3',[function(){
      var d3;
      // d3 version 3 from http://d3js.org/d3.v3.min.js
      d3=function(){function n(n){return null!=n&&!isNaN(n)}function t(n){return n.length}function e(n){for(var t=1;n*t%1;)t*=10;return t}function r(n,t){try{for(var e in t)Object.defineProperty(n.prototype,e,{value:t[e],enumerable:!1})}catch(r){n.prototype=t}}function u(){}function i(){}function o(n,t,e){return function(){var r=e.apply(t,arguments);return r===t?n:r}}function a(n,t){if(t in n)return t;t=t.charAt(0).toUpperCase()+t.substring(1);for(var e=0,r=Do.length;r>e;++e){var u=Do[e]+t;if(u in n)return u}}function c(){}function l(){}function s(n){function t(){for(var t,r=e,u=-1,i=r.length;++u<i;)(t=r[u].on)&&t.apply(this,arguments);return n}var e=[],r=new u;return t.on=function(t,u){var i,o=r.get(t);return arguments.length<2?o&&o.on:(o&&(o.on=null,e=e.slice(0,i=e.indexOf(o)).concat(e.slice(i+1)),r.remove(t)),u&&e.push(r.set(t,{on:u})),n)},t}function f(){mo.event.preventDefault()}function h(){for(var n,t=mo.event;n=t.sourceEvent;)t=n;return t}function g(n){for(var t=new l,e=0,r=arguments.length;++e<r;)t[arguments[e]]=s(t);return t.of=function(e,r){return function(u){try{var i=u.sourceEvent=mo.event;u.target=n,mo.event=u,t[u.type].apply(e,r)}finally{mo.event=i}}},t}function p(n){return Lo(n,Ro),n}function d(n){return"function"==typeof n?n:function(){return Ho(n,this)}}function v(n){return"function"==typeof n?n:function(){return Fo(n,this)}}function m(n,t){function e(){this.removeAttribute(n)}function r(){this.removeAttributeNS(n.space,n.local)}function u(){this.setAttribute(n,t)}function i(){this.setAttributeNS(n.space,n.local,t)}function o(){var e=t.apply(this,arguments);null==e?this.removeAttribute(n):this.setAttribute(n,e)}function a(){var e=t.apply(this,arguments);null==e?this.removeAttributeNS(n.space,n.local):this.setAttributeNS(n.space,n.local,e)}return n=mo.ns.qualify(n),null==t?n.local?r:e:"function"==typeof t?n.local?a:o:n.local?i:u}function y(n){return n.trim().replace(/\s+/g," ")}function M(n){return new RegExp("(?:^|\\s+)"+mo.requote(n)+"(?:\\s+|$)","g")}function x(n,t){function e(){for(var e=-1;++e<u;)n[e](this,t)}function r(){for(var e=-1,r=t.apply(this,arguments);++e<u;)n[e](this,r)}n=n.trim().split(/\s+/).map(b);var u=n.length;return"function"==typeof t?r:e}function b(n){var t=M(n);return function(e,r){if(u=e.classList)return r?u.add(n):u.remove(n);var u=e.getAttribute("class")||"";r?(t.lastIndex=0,t.test(u)||e.setAttribute("class",y(u+" "+n))):e.setAttribute("class",y(u.replace(t," ")))}}function _(n,t,e){function r(){this.style.removeProperty(n)}function u(){this.style.setProperty(n,t,e)}function i(){var r=t.apply(this,arguments);null==r?this.style.removeProperty(n):this.style.setProperty(n,r,e)}return null==t?r:"function"==typeof t?i:u}function w(n,t){function e(){delete this[n]}function r(){this[n]=t}function u(){var e=t.apply(this,arguments);null==e?delete this[n]:this[n]=e}return null==t?e:"function"==typeof t?u:r}function S(n){return"function"==typeof n?n:(n=mo.ns.qualify(n)).local?function(){return xo.createElementNS(n.space,n.local)}:function(){return xo.createElementNS(this.namespaceURI,n)}}function E(n){return{__data__:n}}function k(n){return function(){return Oo(this,n)}}function A(n){return arguments.length||(n=mo.ascending),function(t,e){return t&&e?n(t.__data__,e.__data__):!t-!e}}function N(n,t){for(var e=0,r=n.length;r>e;e++)for(var u,i=n[e],o=0,a=i.length;a>o;o++)(u=i[o])&&t(u,o,e);return n}function T(n){return Lo(n,Io),n}function q(n){var t,e;return function(r,u,i){var o,a=n[i].update,c=a.length;for(i!=e&&(e=i,t=0),u>=t&&(t=u+1);!(o=a[t])&&++t<c;);return o}}function z(){var n=this.__transition__;n&&++n.active}function C(n,t,e){function r(){var t=this[o];t&&(this.removeEventListener(n,t,t.$),delete this[o])}function u(){var u=l(t,Mo(arguments));r.call(this),this.addEventListener(n,this[o]=u,u.$=e),u._=t}function i(){var t,e=new RegExp("^__on([^.]+)"+mo.requote(n)+"$");for(var r in this)if(t=r.match(e)){var u=this[r];this.removeEventListener(t[1],u,u.$),delete this[r]}}var o="__on"+n,a=n.indexOf("."),l=D;a>0&&(n=n.substring(0,a));var s=Zo.get(n);return s&&(n=s,l=j),a?t?u:r:t?c:i}function D(n,t){return function(e){var r=mo.event;mo.event=e,t[0]=this.__data__;try{n.apply(this,t)}finally{mo.event=r}}}function j(n,t){var e=D(n,t);return function(n){var t=this,r=n.relatedTarget;r&&(r===t||8&r.compareDocumentPosition(t))||e.call(t,n)}}function L(){var n=".dragsuppress-"+ ++Xo,t="touchmove"+n,e="selectstart"+n,r="dragstart"+n,u="click"+n,i=mo.select(_o).on(t,f).on(e,f).on(r,f),o=bo.style,a=o[Vo];return o[Vo]="none",function(t){function e(){i.on(u,null)}i.on(n,null),o[Vo]=a,t&&(i.on(u,function(){f(),e()},!0),setTimeout(e,0))}}function H(n,t){t.changedTouches&&(t=t.changedTouches[0]);var e=n.ownerSVGElement||n;if(e.createSVGPoint){var r=e.createSVGPoint();if(0>$o&&(_o.scrollX||_o.scrollY)){e=mo.select("body").append("svg").style({position:"absolute",top:0,left:0,margin:0,padding:0,border:"none"},"important");var u=e[0][0].getScreenCTM();$o=!(u.f||u.e),e.remove()}return $o?(r.x=t.pageX,r.y=t.pageY):(r.x=t.clientX,r.y=t.clientY),r=r.matrixTransform(n.getScreenCTM().inverse()),[r.x,r.y]}var i=n.getBoundingClientRect();return[t.clientX-i.left-n.clientLeft,t.clientY-i.top-n.clientTop]}function F(n){return n>0?1:0>n?-1:0}function P(n){return n>1?0:-1>n?Bo:Math.acos(n)}function O(n){return n>1?Bo/2:-1>n?-Bo/2:Math.asin(n)}function R(n){return(Math.exp(n)-Math.exp(-n))/2}function Y(n){return(Math.exp(n)+Math.exp(-n))/2}function I(n){return R(n)/Y(n)}function U(n){return(n=Math.sin(n/2))*n}function Z(){}function V(n,t,e){return new X(n,t,e)}function X(n,t,e){this.h=n,this.s=t,this.l=e}function $(n,t,e){function r(n){return n>360?n-=360:0>n&&(n+=360),60>n?i+(o-i)*n/60:180>n?o:240>n?i+(o-i)*(240-n)/60:i}function u(n){return Math.round(255*r(n))}var i,o;return n=isNaN(n)?0:(n%=360)<0?n+360:n,t=isNaN(t)?0:0>t?0:t>1?1:t,e=0>e?0:e>1?1:e,o=.5>=e?e*(1+t):e+t-e*t,i=2*e-o,ot(u(n+120),u(n),u(n-120))}function B(n,t,e){return new W(n,t,e)}function W(n,t,e){this.h=n,this.c=t,this.l=e}function J(n,t,e){return isNaN(n)&&(n=0),isNaN(t)&&(t=0),G(e,Math.cos(n*=Go)*t,Math.sin(n)*t)}function G(n,t,e){return new K(n,t,e)}function K(n,t,e){this.l=n,this.a=t,this.b=e}function Q(n,t,e){var r=(n+16)/116,u=r+t/500,i=r-e/200;return u=tt(u)*ca,r=tt(r)*la,i=tt(i)*sa,ot(rt(3.2404542*u-1.5371385*r-.4985314*i),rt(-.969266*u+1.8760108*r+.041556*i),rt(.0556434*u-.2040259*r+1.0572252*i))}function nt(n,t,e){return n>0?B(Math.atan2(e,t)*Ko,Math.sqrt(t*t+e*e),n):B(0/0,0/0,n)}function tt(n){return n>.206893034?n*n*n:(n-4/29)/7.787037}function et(n){return n>.008856?Math.pow(n,1/3):7.787037*n+4/29}function rt(n){return Math.round(255*(.00304>=n?12.92*n:1.055*Math.pow(n,1/2.4)-.055))}function ut(n){return ot(n>>16,255&n>>8,255&n)}function it(n){return ut(n)+""}function ot(n,t,e){return new at(n,t,e)}function at(n,t,e){this.r=n,this.g=t,this.b=e}function ct(n){return 16>n?"0"+Math.max(0,n).toString(16):Math.min(255,n).toString(16)}function lt(n,t,e){var r,u,i,o=0,a=0,c=0;if(r=/([a-z]+)\((.*)\)/i.exec(n))switch(u=r[2].split(","),r[1]){case"hsl":return e(parseFloat(u[0]),parseFloat(u[1])/100,parseFloat(u[2])/100);case"rgb":return t(gt(u[0]),gt(u[1]),gt(u[2]))}return(i=ga.get(n))?t(i.r,i.g,i.b):(null!=n&&"#"===n.charAt(0)&&(4===n.length?(o=n.charAt(1),o+=o,a=n.charAt(2),a+=a,c=n.charAt(3),c+=c):7===n.length&&(o=n.substring(1,3),a=n.substring(3,5),c=n.substring(5,7)),o=parseInt(o,16),a=parseInt(a,16),c=parseInt(c,16)),t(o,a,c))}function st(n,t,e){var r,u,i=Math.min(n/=255,t/=255,e/=255),o=Math.max(n,t,e),a=o-i,c=(o+i)/2;return a?(u=.5>c?a/(o+i):a/(2-o-i),r=n==o?(t-e)/a+(e>t?6:0):t==o?(e-n)/a+2:(n-t)/a+4,r*=60):(r=0/0,u=c>0&&1>c?0:r),V(r,u,c)}function ft(n,t,e){n=ht(n),t=ht(t),e=ht(e);var r=et((.4124564*n+.3575761*t+.1804375*e)/ca),u=et((.2126729*n+.7151522*t+.072175*e)/la),i=et((.0193339*n+.119192*t+.9503041*e)/sa);return G(116*u-16,500*(r-u),200*(u-i))}function ht(n){return(n/=255)<=.04045?n/12.92:Math.pow((n+.055)/1.055,2.4)}function gt(n){var t=parseFloat(n);return"%"===n.charAt(n.length-1)?Math.round(2.55*t):t}function pt(n){return"function"==typeof n?n:function(){return n}}function dt(n){return n}function vt(n){return function(t,e,r){return 2===arguments.length&&"function"==typeof e&&(r=e,e=null),mt(t,e,n,r)}}function mt(n,t,e,r){function u(){var n,t=c.status;if(!t&&c.responseText||t>=200&&300>t||304===t){try{n=e.call(i,c)}catch(r){return o.error.call(i,r),void 0}o.load.call(i,n)}else o.error.call(i,c)}var i={},o=mo.dispatch("beforesend","progress","load","error"),a={},c=new XMLHttpRequest,l=null;return!_o.XDomainRequest||"withCredentials"in c||!/^(http(s)?:)?\/\//.test(n)||(c=new XDomainRequest),"onload"in c?c.onload=c.onerror=u:c.onreadystatechange=function(){c.readyState>3&&u()},c.onprogress=function(n){var t=mo.event;mo.event=n;try{o.progress.call(i,c)}finally{mo.event=t}},i.header=function(n,t){return n=(n+"").toLowerCase(),arguments.length<2?a[n]:(null==t?delete a[n]:a[n]=t+"",i)},i.mimeType=function(n){return arguments.length?(t=null==n?null:n+"",i):t},i.responseType=function(n){return arguments.length?(l=n,i):l},i.response=function(n){return e=n,i},["get","post"].forEach(function(n){i[n]=function(){return i.send.apply(i,[n].concat(Mo(arguments)))}}),i.send=function(e,r,u){if(2===arguments.length&&"function"==typeof r&&(u=r,r=null),c.open(e,n,!0),null==t||"accept"in a||(a.accept=t+",*/*"),c.setRequestHeader)for(var s in a)c.setRequestHeader(s,a[s]);return null!=t&&c.overrideMimeType&&c.overrideMimeType(t),null!=l&&(c.responseType=l),null!=u&&i.on("error",u).on("load",function(n){u(null,n)}),o.beforesend.call(i,c),c.send(null==r?null:r),i},i.abort=function(){return c.abort(),i},mo.rebind(i,o,"on"),null==r?i:i.get(yt(r))}function yt(n){return 1===n.length?function(t,e){n(null==t?e:null)}:n}function Mt(){var n=bt(),t=_t()-n;t>24?(isFinite(t)&&(clearTimeout(ma),ma=setTimeout(Mt,t)),va=0):(va=1,Ma(Mt))}function xt(n,t,e){var r=arguments.length;2>r&&(t=0),3>r&&(e=Date.now()),ya.callback=n,ya.time=e+t}function bt(){var n=Date.now();for(ya=pa;ya;)n>=ya.time&&(ya.flush=ya.callback(n-ya.time)),ya=ya.next;return n}function _t(){for(var n,t=pa,e=1/0;t;)t.flush?t=n?n.next=t.next:pa=t.next:(t.time<e&&(e=t.time),t=(n=t).next);return da=n,e}function wt(n,t){var e=Math.pow(10,3*Math.abs(8-t));return{scale:t>8?function(n){return n/e}:function(n){return n*e},symbol:n}}function St(n,t){return t-(n?Math.ceil(Math.log(n)/Math.LN10):1)}function Et(n){return n+""}function kt(){}function At(n,t,e){var r=e.s=n+t,u=r-n,i=r-u;e.t=n-i+(t-u)}function Nt(n,t){n&&za.hasOwnProperty(n.type)&&za[n.type](n,t)}function Tt(n,t,e){var r,u=-1,i=n.length-e;for(t.lineStart();++u<i;)r=n[u],t.point(r[0],r[1],r[2]);t.lineEnd()}function qt(n,t){var e=-1,r=n.length;for(t.polygonStart();++e<r;)Tt(n[e],t,1);t.polygonEnd()}function zt(){function n(n,t){n*=Go,t=t*Go/2+Bo/4;var e=n-r,o=Math.cos(t),a=Math.sin(t),c=i*a,l=u*o+c*Math.cos(e),s=c*Math.sin(e);Da.add(Math.atan2(s,l)),r=n,u=o,i=a}var t,e,r,u,i;ja.point=function(o,a){ja.point=n,r=(t=o)*Go,u=Math.cos(a=(e=a)*Go/2+Bo/4),i=Math.sin(a)},ja.lineEnd=function(){n(t,e)}}function Ct(n){var t=n[0],e=n[1],r=Math.cos(e);return[r*Math.cos(t),r*Math.sin(t),Math.sin(e)]}function Dt(n,t){return n[0]*t[0]+n[1]*t[1]+n[2]*t[2]}function jt(n,t){return[n[1]*t[2]-n[2]*t[1],n[2]*t[0]-n[0]*t[2],n[0]*t[1]-n[1]*t[0]]}function Lt(n,t){n[0]+=t[0],n[1]+=t[1],n[2]+=t[2]}function Ht(n,t){return[n[0]*t,n[1]*t,n[2]*t]}function Ft(n){var t=Math.sqrt(n[0]*n[0]+n[1]*n[1]+n[2]*n[2]);n[0]/=t,n[1]/=t,n[2]/=t}function Pt(n){return[Math.atan2(n[1],n[0]),O(n[2])]}function Ot(n,t){return Math.abs(n[0]-t[0])<Wo&&Math.abs(n[1]-t[1])<Wo}function Rt(n,t){n*=Go;var e=Math.cos(t*=Go);Yt(e*Math.cos(n),e*Math.sin(n),Math.sin(t))}function Yt(n,t,e){++La,Fa+=(n-Fa)/La,Pa+=(t-Pa)/La,Oa+=(e-Oa)/La}function It(){function n(n,u){n*=Go;var i=Math.cos(u*=Go),o=i*Math.cos(n),a=i*Math.sin(n),c=Math.sin(u),l=Math.atan2(Math.sqrt((l=e*c-r*a)*l+(l=r*o-t*c)*l+(l=t*a-e*o)*l),t*o+e*a+r*c);Ha+=l,Ra+=l*(t+(t=o)),Ya+=l*(e+(e=a)),Ia+=l*(r+(r=c)),Yt(t,e,r)}var t,e,r;Xa.point=function(u,i){u*=Go;var o=Math.cos(i*=Go);t=o*Math.cos(u),e=o*Math.sin(u),r=Math.sin(i),Xa.point=n,Yt(t,e,r)}}function Ut(){Xa.point=Rt}function Zt(){function n(n,t){n*=Go;var e=Math.cos(t*=Go),o=e*Math.cos(n),a=e*Math.sin(n),c=Math.sin(t),l=u*c-i*a,s=i*o-r*c,f=r*a-u*o,h=Math.sqrt(l*l+s*s+f*f),g=r*o+u*a+i*c,p=h&&-P(g)/h,d=Math.atan2(h,g);Ua+=p*l,Za+=p*s,Va+=p*f,Ha+=d,Ra+=d*(r+(r=o)),Ya+=d*(u+(u=a)),Ia+=d*(i+(i=c)),Yt(r,u,i)}var t,e,r,u,i;Xa.point=function(o,a){t=o,e=a,Xa.point=n,o*=Go;var c=Math.cos(a*=Go);r=c*Math.cos(o),u=c*Math.sin(o),i=Math.sin(a),Yt(r,u,i)},Xa.lineEnd=function(){n(t,e),Xa.lineEnd=Ut,Xa.point=Rt}}function Vt(){return!0}function Xt(n,t,e,r,u){var i=[],o=[];if(n.forEach(function(n){if(!((t=n.length-1)<=0)){var t,e=n[0],r=n[t];if(Ot(e,r)){u.lineStart();for(var a=0;t>a;++a)u.point((e=n[a])[0],e[1]);return u.lineEnd(),void 0}var c={point:e,points:n,other:null,visited:!1,entry:!0,subject:!0},l={point:e,points:[e],other:c,visited:!1,entry:!1,subject:!1};c.other=l,i.push(c),o.push(l),c={point:r,points:[r],other:null,visited:!1,entry:!1,subject:!0},l={point:r,points:[r],other:c,visited:!1,entry:!0,subject:!1},c.other=l,i.push(c),o.push(l)}}),o.sort(t),$t(i),$t(o),i.length){if(e)for(var a=1,c=!e(o[0].point),l=o.length;l>a;++a)o[a].entry=c=!c;for(var s,f,h,g=i[0];;){for(s=g;s.visited;)if((s=s.next)===g)return;f=s.points,u.lineStart();do{if(s.visited=s.other.visited=!0,s.entry){if(s.subject)for(var a=0;a<f.length;a++)u.point((h=f[a])[0],h[1]);else r(s.point,s.next.point,1,u);s=s.next}else{if(s.subject){f=s.prev.points;for(var a=f.length;--a>=0;)u.point((h=f[a])[0],h[1])}else r(s.point,s.prev.point,-1,u);s=s.prev}s=s.other,f=s.points}while(!s.visited);u.lineEnd()}}}function $t(n){if(t=n.length){for(var t,e,r=0,u=n[0];++r<t;)u.next=e=n[r],e.prev=u,u=e;u.next=e=n[0],e.prev=u}}function Bt(n,t,e,r){return function(u){function i(t,e){n(t,e)&&u.point(t,e)}function o(n,t){d.point(n,t)}function a(){v.point=o,d.lineStart()}function c(){v.point=i,d.lineEnd()}function l(n,t){y.point(n,t),p.push([n,t])}function s(){y.lineStart(),p=[]}function f(){l(p[0][0],p[0][1]),y.lineEnd();var n,t=y.clean(),e=m.buffer(),r=e.length;if(p.pop(),g.push(p),p=null,r){if(1&t){n=e[0];var i,r=n.length-1,o=-1;for(u.lineStart();++o<r;)u.point((i=n[o])[0],i[1]);return u.lineEnd(),void 0}r>1&&2&t&&e.push(e.pop().concat(e.shift())),h.push(e.filter(Wt))}}var h,g,p,d=t(u),v={point:i,lineStart:a,lineEnd:c,polygonStart:function(){v.point=l,v.lineStart=s,v.lineEnd=f,h=[],g=[],u.polygonStart()},polygonEnd:function(){v.point=i,v.lineStart=a,v.lineEnd=c,h=mo.merge(h),h.length?Xt(h,Gt,null,e,u):r(g)&&(u.lineStart(),e(null,null,1,u),u.lineEnd()),u.polygonEnd(),h=g=null},sphere:function(){u.polygonStart(),u.lineStart(),e(null,null,1,u),u.lineEnd(),u.polygonEnd()}},m=Jt(),y=t(m);return v}}function Wt(n){return n.length>1}function Jt(){var n,t=[];return{lineStart:function(){t.push(n=[])},point:function(t,e){n.push([t,e])},lineEnd:c,buffer:function(){var e=t;return t=[],n=null,e},rejoin:function(){t.length>1&&t.push(t.pop().concat(t.shift()))}}}function Gt(n,t){return((n=n.point)[0]<0?n[1]-Bo/2-Wo:Bo/2-n[1])-((t=t.point)[0]<0?t[1]-Bo/2-Wo:Bo/2-t[1])}function Kt(n,t){var e=n[0],r=n[1],u=[Math.sin(e),-Math.cos(e),0],i=0,o=!1,a=!1,c=0;Da.reset();for(var l=0,s=t.length;s>l;++l){var f=t[l],h=f.length;if(h){for(var g=f[0],p=g[0],d=g[1]/2+Bo/4,v=Math.sin(d),m=Math.cos(d),y=1;;){y===h&&(y=0),n=f[y];var M=n[0],x=n[1]/2+Bo/4,b=Math.sin(x),_=Math.cos(x),w=M-p,S=Math.abs(w)>Bo,E=v*b;if(Da.add(Math.atan2(E*Math.sin(w),m*_+E*Math.cos(w))),Math.abs(x)<Wo&&(a=!0),i+=S?w+(w>=0?2:-2)*Bo:w,S^p>=e^M>=e){var k=jt(Ct(g),Ct(n));Ft(k);var A=jt(u,k);Ft(A);var N=(S^w>=0?-1:1)*O(A[2]);r>N&&(c+=S^w>=0?1:-1)}if(!y++)break;p=M,v=b,m=_,g=n}Math.abs(i)>Wo&&(o=!0)}}return(!a&&!o&&0>Da||-Wo>i)^1&c}function Qt(n){var t,e=0/0,r=0/0,u=0/0;return{lineStart:function(){n.lineStart(),t=1},point:function(i,o){var a=i>0?Bo:-Bo,c=Math.abs(i-e);Math.abs(c-Bo)<Wo?(n.point(e,r=(r+o)/2>0?Bo/2:-Bo/2),n.point(u,r),n.lineEnd(),n.lineStart(),n.point(a,r),n.point(i,r),t=0):u!==a&&c>=Bo&&(Math.abs(e-u)<Wo&&(e-=u*Wo),Math.abs(i-a)<Wo&&(i-=a*Wo),r=ne(e,r,i,o),n.point(u,r),n.lineEnd(),n.lineStart(),n.point(a,r),t=0),n.point(e=i,r=o),u=a},lineEnd:function(){n.lineEnd(),e=r=0/0},clean:function(){return 2-t}}}function ne(n,t,e,r){var u,i,o=Math.sin(n-e);return Math.abs(o)>Wo?Math.atan((Math.sin(t)*(i=Math.cos(r))*Math.sin(e)-Math.sin(r)*(u=Math.cos(t))*Math.sin(n))/(u*i*o)):(t+r)/2}function te(n,t,e,r){var u;if(null==n)u=e*Bo/2,r.point(-Bo,u),r.point(0,u),r.point(Bo,u),r.point(Bo,0),r.point(Bo,-u),r.point(0,-u),r.point(-Bo,-u),r.point(-Bo,0),r.point(-Bo,u);else if(Math.abs(n[0]-t[0])>Wo){var i=(n[0]<t[0]?1:-1)*Bo;u=e*i/2,r.point(-i,u),r.point(0,u),r.point(i,u)}else r.point(t[0],t[1])}function ee(n){return Kt(Ba,n)}function re(n){function t(n,t){return Math.cos(n)*Math.cos(t)>o}function e(n){var e,i,o,c,s;return{lineStart:function(){c=o=!1,s=1},point:function(f,h){var g,p=[f,h],d=t(f,h),v=a?d?0:u(f,h):d?u(f+(0>f?Bo:-Bo),h):0;if(!e&&(c=o=d)&&n.lineStart(),d!==o&&(g=r(e,p),(Ot(e,g)||Ot(p,g))&&(p[0]+=Wo,p[1]+=Wo,d=t(p[0],p[1]))),d!==o)s=0,d?(n.lineStart(),g=r(p,e),n.point(g[0],g[1])):(g=r(e,p),n.point(g[0],g[1]),n.lineEnd()),e=g;else if(l&&e&&a^d){var m;v&i||!(m=r(p,e,!0))||(s=0,a?(n.lineStart(),n.point(m[0][0],m[0][1]),n.point(m[1][0],m[1][1]),n.lineEnd()):(n.point(m[1][0],m[1][1]),n.lineEnd(),n.lineStart(),n.point(m[0][0],m[0][1])))}!d||e&&Ot(e,p)||n.point(p[0],p[1]),e=p,o=d,i=v},lineEnd:function(){o&&n.lineEnd(),e=null},clean:function(){return s|(c&&o)<<1}}}function r(n,t,e){var r=Ct(n),u=Ct(t),i=[1,0,0],a=jt(r,u),c=Dt(a,a),l=a[0],s=c-l*l;if(!s)return!e&&n;var f=o*c/s,h=-o*l/s,g=jt(i,a),p=Ht(i,f),d=Ht(a,h);Lt(p,d);var v=g,m=Dt(p,v),y=Dt(v,v),M=m*m-y*(Dt(p,p)-1);if(!(0>M)){var x=Math.sqrt(M),b=Ht(v,(-m-x)/y);if(Lt(b,p),b=Pt(b),!e)return b;var _,w=n[0],S=t[0],E=n[1],k=t[1];w>S&&(_=w,w=S,S=_);var A=S-w,N=Math.abs(A-Bo)<Wo,T=N||Wo>A;if(!N&&E>k&&(_=E,E=k,k=_),T?N?E+k>0^b[1]<(Math.abs(b[0]-w)<Wo?E:k):E<=b[1]&&b[1]<=k:A>Bo^(w<=b[0]&&b[0]<=S)){var q=Ht(v,(-m+x)/y);return Lt(q,p),[b,Pt(q)]}}}function u(t,e){var r=a?n:Bo-n,u=0;return-r>t?u|=1:t>r&&(u|=2),-r>e?u|=4:e>r&&(u|=8),u}function i(n){return Kt(c,n)}var o=Math.cos(n),a=o>0,c=[n,0],l=Math.abs(o)>Wo,s=Te(n,6*Go);return Bt(t,e,s,i)}function ue(n,t,e,r){function u(r,u){return Math.abs(r[0]-n)<Wo?u>0?0:3:Math.abs(r[0]-e)<Wo?u>0?2:1:Math.abs(r[1]-t)<Wo?u>0?1:0:u>0?3:2}function i(n,t){return o(n.point,t.point)}function o(n,t){var e=u(n,1),r=u(t,1);return e!==r?e-r:0===e?t[1]-n[1]:1===e?n[0]-t[0]:2===e?n[1]-t[1]:t[0]-n[0]}function a(u,i){var o=i[0]-u[0],a=i[1]-u[1],c=[0,1];return Math.abs(o)<Wo&&Math.abs(a)<Wo?n<=u[0]&&u[0]<=e&&t<=u[1]&&u[1]<=r:ie(n-u[0],o,c)&&ie(u[0]-e,-o,c)&&ie(t-u[1],a,c)&&ie(u[1]-r,-a,c)?(c[1]<1&&(i[0]=u[0]+c[1]*o,i[1]=u[1]+c[1]*a),c[0]>0&&(u[0]+=c[0]*o,u[1]+=c[0]*a),!0):!1}return function(c){function l(i){var o=u(i,-1),a=s([0===o||3===o?n:e,o>1?r:t]);return a}function s(n){for(var t=0,e=M.length,r=n[1],u=0;e>u;++u)for(var i,o=1,a=M[u],c=a.length,l=a[0];c>o;++o)i=a[o],l[1]<=r?i[1]>r&&f(l,i,n)>0&&++t:i[1]<=r&&f(l,i,n)<0&&--t,l=i;return 0!==t}function f(n,t,e){return(t[0]-n[0])*(e[1]-n[1])-(e[0]-n[0])*(t[1]-n[1])}function h(i,a,c,l){var s=0,f=0;if(null==i||(s=u(i,c))!==(f=u(a,c))||o(i,a)<0^c>0){do l.point(0===s||3===s?n:e,s>1?r:t);while((s=(s+c+4)%4)!==f)}else l.point(a[0],a[1])}function g(u,i){return u>=n&&e>=u&&i>=t&&r>=i}function p(n,t){g(n,t)&&c.point(n,t)}function d(){q.point=m,M&&M.push(x=[]),A=!0,k=!1,S=E=0/0}function v(){y&&(m(b,_),w&&k&&T.rejoin(),y.push(T.buffer())),q.point=p,k&&c.lineEnd()}function m(n,t){n=Math.max(-Wa,Math.min(Wa,n)),t=Math.max(-Wa,Math.min(Wa,t));var e=g(n,t);if(M&&x.push([n,t]),A)b=n,_=t,w=e,A=!1,e&&(c.lineStart(),c.point(n,t));else if(e&&k)c.point(n,t);else{var r=[S,E],u=[n,t];a(r,u)?(k||(c.lineStart(),c.point(r[0],r[1])),c.point(u[0],u[1]),e||c.lineEnd()):e&&(c.lineStart(),c.point(n,t))}S=n,E=t,k=e}var y,M,x,b,_,w,S,E,k,A,N=c,T=Jt(),q={point:p,lineStart:d,lineEnd:v,polygonStart:function(){c=T,y=[],M=[]},polygonEnd:function(){c=N,(y=mo.merge(y)).length?(c.polygonStart(),Xt(y,i,l,h,c),c.polygonEnd()):s([n,t])&&(c.polygonStart(),c.lineStart(),h(null,null,1,c),c.lineEnd(),c.polygonEnd()),y=M=x=null}};return q}}function ie(n,t,e){if(Math.abs(t)<Wo)return 0>=n;var r=n/t;if(t>0){if(r>e[1])return!1;r>e[0]&&(e[0]=r)}else{if(r<e[0])return!1;r<e[1]&&(e[1]=r)}return!0}function oe(n,t){function e(e,r){return e=n(e,r),t(e[0],e[1])}return n.invert&&t.invert&&(e.invert=function(e,r){return e=t.invert(e,r),e&&n.invert(e[0],e[1])}),e}function ae(n){var t=0,e=Bo/3,r=_e(n),u=r(t,e);return u.parallels=function(n){return arguments.length?r(t=n[0]*Bo/180,e=n[1]*Bo/180):[180*(t/Bo),180*(e/Bo)]},u}function ce(n,t){function e(n,t){var e=Math.sqrt(i-2*u*Math.sin(t))/u;return[e*Math.sin(n*=u),o-e*Math.cos(n)]}var r=Math.sin(n),u=(r+Math.sin(t))/2,i=1+r*(2*u-r),o=Math.sqrt(i)/u;return e.invert=function(n,t){var e=o-t;return[Math.atan2(n,e)/u,O((i-(n*n+e*e)*u*u)/(2*u))]},e}function le(){function n(n,t){Ga+=u*n-r*t,r=n,u=t}var t,e,r,u;ec.point=function(i,o){ec.point=n,t=r=i,e=u=o},ec.lineEnd=function(){n(t,e)}}function se(n,t){Ka>n&&(Ka=n),n>nc&&(nc=n),Qa>t&&(Qa=t),t>tc&&(tc=t)}function fe(){function n(n,t){o.push("M",n,",",t,i)}function t(n,t){o.push("M",n,",",t),a.point=e}function e(n,t){o.push("L",n,",",t)}function r(){a.point=n}function u(){o.push("Z")}var i=he(4.5),o=[],a={point:n,lineStart:function(){a.point=t},lineEnd:r,polygonStart:function(){a.lineEnd=u},polygonEnd:function(){a.lineEnd=r,a.point=n},pointRadius:function(n){return i=he(n),a},result:function(){if(o.length){var n=o.join("");return o=[],n}}};return a}function he(n){return"m0,"+n+"a"+n+","+n+" 0 1,1 0,"+-2*n+"a"+n+","+n+" 0 1,1 0,"+2*n+"z"}function ge(n,t){Fa+=n,Pa+=t,++Oa}function pe(){function n(n,r){var u=n-t,i=r-e,o=Math.sqrt(u*u+i*i);Ra+=o*(t+n)/2,Ya+=o*(e+r)/2,Ia+=o,ge(t=n,e=r)}var t,e;uc.point=function(r,u){uc.point=n,ge(t=r,e=u)}}function de(){uc.point=ge}function ve(){function n(n,t){var e=n-r,i=t-u,o=Math.sqrt(e*e+i*i);Ra+=o*(r+n)/2,Ya+=o*(u+t)/2,Ia+=o,o=u*n-r*t,Ua+=o*(r+n),Za+=o*(u+t),Va+=3*o,ge(r=n,u=t)}var t,e,r,u;uc.point=function(i,o){uc.point=n,ge(t=r=i,e=u=o)},uc.lineEnd=function(){n(t,e)}}function me(n){function t(t,e){n.moveTo(t,e),n.arc(t,e,o,0,2*Bo)}function e(t,e){n.moveTo(t,e),a.point=r}function r(t,e){n.lineTo(t,e)}function u(){a.point=t}function i(){n.closePath()}var o=4.5,a={point:t,lineStart:function(){a.point=e},lineEnd:u,polygonStart:function(){a.lineEnd=i},polygonEnd:function(){a.lineEnd=u,a.point=t},pointRadius:function(n){return o=n,a},result:c};return a}function ye(n){function t(t){function r(e,r){e=n(e,r),t.point(e[0],e[1])}function u(){M=0/0,S.point=o,t.lineStart()}function o(r,u){var o=Ct([r,u]),a=n(r,u);e(M,x,y,b,_,w,M=a[0],x=a[1],y=r,b=o[0],_=o[1],w=o[2],i,t),t.point(M,x)}function a(){S.point=r,t.lineEnd()}function c(){u(),S.point=l,S.lineEnd=s}function l(n,t){o(f=n,h=t),g=M,p=x,d=b,v=_,m=w,S.point=o}function s(){e(M,x,y,b,_,w,g,p,f,d,v,m,i,t),S.lineEnd=a,a()}var f,h,g,p,d,v,m,y,M,x,b,_,w,S={point:r,lineStart:u,lineEnd:a,polygonStart:function(){t.polygonStart(),S.lineStart=c},polygonEnd:function(){t.polygonEnd(),S.lineStart=u}};return S}function e(t,i,o,a,c,l,s,f,h,g,p,d,v,m){var y=s-t,M=f-i,x=y*y+M*M;if(x>4*r&&v--){var b=a+g,_=c+p,w=l+d,S=Math.sqrt(b*b+_*_+w*w),E=Math.asin(w/=S),k=Math.abs(Math.abs(w)-1)<Wo?(o+h)/2:Math.atan2(_,b),A=n(k,E),N=A[0],T=A[1],q=N-t,z=T-i,C=M*q-y*z;(C*C/x>r||Math.abs((y*q+M*z)/x-.5)>.3||u>a*g+c*p+l*d)&&(e(t,i,o,a,c,l,N,T,k,b/=S,_/=S,w,v,m),m.point(N,T),e(N,T,k,b,_,w,s,f,h,g,p,d,v,m))}}var r=.5,u=Math.cos(30*Go),i=16;return t.precision=function(n){return arguments.length?(i=(r=n*n)>0&&16,t):Math.sqrt(r)},t}function Me(n){this.stream=n}function xe(n){var t=ye(function(t,e){return n([t*Ko,e*Ko])});return function(n){var e=new Me(n=t(n));return e.point=function(t,e){n.point(t*Go,e*Go)},e}}function be(n){return _e(function(){return n})()}function _e(n){function t(n){return n=a(n[0]*Go,n[1]*Go),[n[0]*h+c,l-n[1]*h]}function e(n){return n=a.invert((n[0]-c)/h,(l-n[1])/h),n&&[n[0]*Ko,n[1]*Ko]}function r(){a=oe(o=Ee(m,y,M),i);var n=i(d,v);return c=g-n[0]*h,l=p+n[1]*h,u()}function u(){return s&&(s.valid=!1,s=null),t}var i,o,a,c,l,s,f=ye(function(n,t){return n=i(n,t),[n[0]*h+c,l-n[1]*h]}),h=150,g=480,p=250,d=0,v=0,m=0,y=0,M=0,x=$a,b=dt,_=null,w=null;return t.stream=function(n){return s&&(s.valid=!1),s=we(o,x(f(b(n)))),s.valid=!0,s},t.clipAngle=function(n){return arguments.length?(x=null==n?(_=n,$a):re((_=+n)*Go),u()):_},t.clipExtent=function(n){return arguments.length?(w=n,b=n?ue(n[0][0],n[0][1],n[1][0],n[1][1]):dt,u()):w},t.scale=function(n){return arguments.length?(h=+n,r()):h},t.translate=function(n){return arguments.length?(g=+n[0],p=+n[1],r()):[g,p]},t.center=function(n){return arguments.length?(d=n[0]%360*Go,v=n[1]%360*Go,r()):[d*Ko,v*Ko]},t.rotate=function(n){return arguments.length?(m=n[0]%360*Go,y=n[1]%360*Go,M=n.length>2?n[2]%360*Go:0,r()):[m*Ko,y*Ko,M*Ko]},mo.rebind(t,f,"precision"),function(){return i=n.apply(this,arguments),t.invert=i.invert&&e,r()}}function we(n,t){var e=new Me(t);return e.point=function(e,r){r=n(e*Go,r*Go),e=r[0],t.point(e>Bo?e-2*Bo:-Bo>e?e+2*Bo:e,r[1])},e}function Se(n,t){return[n,t]}function Ee(n,t,e){return n?t||e?oe(Ae(n),Ne(t,e)):Ae(n):t||e?Ne(t,e):Se}function ke(n){return function(t,e){return t+=n,[t>Bo?t-2*Bo:-Bo>t?t+2*Bo:t,e]}}function Ae(n){var t=ke(n);return t.invert=ke(-n),t}function Ne(n,t){function e(n,t){var e=Math.cos(t),a=Math.cos(n)*e,c=Math.sin(n)*e,l=Math.sin(t),s=l*r+a*u;return[Math.atan2(c*i-s*o,a*r-l*u),O(s*i+c*o)]}var r=Math.cos(n),u=Math.sin(n),i=Math.cos(t),o=Math.sin(t);return e.invert=function(n,t){var e=Math.cos(t),a=Math.cos(n)*e,c=Math.sin(n)*e,l=Math.sin(t),s=l*i-c*o;return[Math.atan2(c*i+l*o,a*r+s*u),O(s*r-a*u)]},e}function Te(n,t){var e=Math.cos(n),r=Math.sin(n);return function(u,i,o,a){var c=o*t;null!=u?(u=qe(e,u),i=qe(e,i),(o>0?i>u:u>i)&&(u+=2*o*Bo)):(u=n+2*o*Bo,i=n-.5*c);for(var l,s=u;o>0?s>i:i>s;s-=c)a.point((l=Pt([e,-r*Math.cos(s),-r*Math.sin(s)]))[0],l[1])}}function qe(n,t){var e=Ct(t);e[0]-=n,Ft(e);var r=P(-e[1]);return((-e[2]<0?-r:r)+2*Math.PI-Wo)%(2*Math.PI)}function ze(n,t,e){var r=mo.range(n,t-Wo,e).concat(t);return function(n){return r.map(function(t){return[n,t]})}}function Ce(n,t,e){var r=mo.range(n,t-Wo,e).concat(t);return function(n){return r.map(function(t){return[t,n]})}}function De(n){return n.source}function je(n){return n.target}function Le(n,t,e,r){var u=Math.cos(t),i=Math.sin(t),o=Math.cos(r),a=Math.sin(r),c=u*Math.cos(n),l=u*Math.sin(n),s=o*Math.cos(e),f=o*Math.sin(e),h=2*Math.asin(Math.sqrt(U(r-t)+u*o*U(e-n))),g=1/Math.sin(h),p=h?function(n){var t=Math.sin(n*=h)*g,e=Math.sin(h-n)*g,r=e*c+t*s,u=e*l+t*f,o=e*i+t*a;return[Math.atan2(u,r)*Ko,Math.atan2(o,Math.sqrt(r*r+u*u))*Ko]}:function(){return[n*Ko,t*Ko]};return p.distance=h,p}function He(){function n(n,u){var i=Math.sin(u*=Go),o=Math.cos(u),a=Math.abs((n*=Go)-t),c=Math.cos(a);ic+=Math.atan2(Math.sqrt((a=o*Math.sin(a))*a+(a=r*i-e*o*c)*a),e*i+r*o*c),t=n,e=i,r=o}var t,e,r;oc.point=function(u,i){t=u*Go,e=Math.sin(i*=Go),r=Math.cos(i),oc.point=n},oc.lineEnd=function(){oc.point=oc.lineEnd=c}}function Fe(n,t){function e(t,e){var r=Math.cos(t),u=Math.cos(e),i=n(r*u);return[i*u*Math.sin(t),i*Math.sin(e)]}return e.invert=function(n,e){var r=Math.sqrt(n*n+e*e),u=t(r),i=Math.sin(u),o=Math.cos(u);return[Math.atan2(n*i,r*o),Math.asin(r&&e*i/r)]},e}function Pe(n,t){function e(n,t){var e=Math.abs(Math.abs(t)-Bo/2)<Wo?0:o/Math.pow(u(t),i);return[e*Math.sin(i*n),o-e*Math.cos(i*n)]}var r=Math.cos(n),u=function(n){return Math.tan(Bo/4+n/2)},i=n===t?Math.sin(n):Math.log(r/Math.cos(t))/Math.log(u(t)/u(n)),o=r*Math.pow(u(n),i)/i;return i?(e.invert=function(n,t){var e=o-t,r=F(i)*Math.sqrt(n*n+e*e);return[Math.atan2(n,e)/i,2*Math.atan(Math.pow(o/r,1/i))-Bo/2]},e):Re}function Oe(n,t){function e(n,t){var e=i-t;return[e*Math.sin(u*n),i-e*Math.cos(u*n)]}var r=Math.cos(n),u=n===t?Math.sin(n):(r-Math.cos(t))/(t-n),i=r/u+n;return Math.abs(u)<Wo?Se:(e.invert=function(n,t){var e=i-t;return[Math.atan2(n,e)/u,i-F(u)*Math.sqrt(n*n+e*e)]},e)}function Re(n,t){return[n,Math.log(Math.tan(Bo/4+t/2))]}function Ye(n){var t,e=be(n),r=e.scale,u=e.translate,i=e.clipExtent;return e.scale=function(){var n=r.apply(e,arguments);return n===e?t?e.clipExtent(null):e:n},e.translate=function(){var n=u.apply(e,arguments);return n===e?t?e.clipExtent(null):e:n},e.clipExtent=function(n){var o=i.apply(e,arguments);if(o===e){if(t=null==n){var a=Bo*r(),c=u();i([[c[0]-a,c[1]-a],[c[0]+a,c[1]+a]])}}else t&&(o=null);return o},e.clipExtent(null)}function Ie(n,t){var e=Math.cos(t)*Math.sin(n);return[Math.log((1+e)/(1-e))/2,Math.atan2(Math.tan(t),Math.cos(n))]}function Ue(n){function t(t){function o(){l.push("M",i(n(s),a))}for(var c,l=[],s=[],f=-1,h=t.length,g=pt(e),p=pt(r);++f<h;)u.call(this,c=t[f],f)?s.push([+g.call(this,c,f),+p.call(this,c,f)]):s.length&&(o(),s=[]);return s.length&&o(),l.length?l.join(""):null}var e=Ze,r=Ve,u=Vt,i=Xe,o=i.key,a=.7;return t.x=function(n){return arguments.length?(e=n,t):e},t.y=function(n){return arguments.length?(r=n,t):r},t.defined=function(n){return arguments.length?(u=n,t):u},t.interpolate=function(n){return arguments.length?(o="function"==typeof n?i=n:(i=hc.get(n)||Xe).key,t):o},t.tension=function(n){return arguments.length?(a=n,t):a},t}function Ze(n){return n[0]}function Ve(n){return n[1]}function Xe(n){return n.join("L")}function $e(n){return Xe(n)+"Z"}function Be(n){for(var t=0,e=n.length,r=n[0],u=[r[0],",",r[1]];++t<e;)u.push("H",(r[0]+(r=n[t])[0])/2,"V",r[1]);return e>1&&u.push("H",r[0]),u.join("")}function We(n){for(var t=0,e=n.length,r=n[0],u=[r[0],",",r[1]];++t<e;)u.push("V",(r=n[t])[1],"H",r[0]);return u.join("")}function Je(n){for(var t=0,e=n.length,r=n[0],u=[r[0],",",r[1]];++t<e;)u.push("H",(r=n[t])[0],"V",r[1]);return u.join("")}function Ge(n,t){return n.length<4?Xe(n):n[1]+nr(n.slice(1,n.length-1),tr(n,t))}function Ke(n,t){return n.length<3?Xe(n):n[0]+nr((n.push(n[0]),n),tr([n[n.length-2]].concat(n,[n[1]]),t))}function Qe(n,t){return n.length<3?Xe(n):n[0]+nr(n,tr(n,t))}function nr(n,t){if(t.length<1||n.length!=t.length&&n.length!=t.length+2)return Xe(n);var e=n.length!=t.length,r="",u=n[0],i=n[1],o=t[0],a=o,c=1;if(e&&(r+="Q"+(i[0]-2*o[0]/3)+","+(i[1]-2*o[1]/3)+","+i[0]+","+i[1],u=n[1],c=2),t.length>1){a=t[1],i=n[c],c++,r+="C"+(u[0]+o[0])+","+(u[1]+o[1])+","+(i[0]-a[0])+","+(i[1]-a[1])+","+i[0]+","+i[1];for(var l=2;l<t.length;l++,c++)i=n[c],a=t[l],r+="S"+(i[0]-a[0])+","+(i[1]-a[1])+","+i[0]+","+i[1]}if(e){var s=n[c];r+="Q"+(i[0]+2*a[0]/3)+","+(i[1]+2*a[1]/3)+","+s[0]+","+s[1]}return r}function tr(n,t){for(var e,r=[],u=(1-t)/2,i=n[0],o=n[1],a=1,c=n.length;++a<c;)e=i,i=o,o=n[a],r.push([u*(o[0]-e[0]),u*(o[1]-e[1])]);return r}function er(n){if(n.length<3)return Xe(n);var t=1,e=n.length,r=n[0],u=r[0],i=r[1],o=[u,u,u,(r=n[1])[0]],a=[i,i,i,r[1]],c=[u,",",i,"L",or(dc,o),",",or(dc,a)];for(n.push(n[e-1]);++t<=e;)r=n[t],o.shift(),o.push(r[0]),a.shift(),a.push(r[1]),ar(c,o,a);return n.pop(),c.push("L",r),c.join("")}function rr(n){if(n.length<4)return Xe(n);for(var t,e=[],r=-1,u=n.length,i=[0],o=[0];++r<3;)t=n[r],i.push(t[0]),o.push(t[1]);for(e.push(or(dc,i)+","+or(dc,o)),--r;++r<u;)t=n[r],i.shift(),i.push(t[0]),o.shift(),o.push(t[1]),ar(e,i,o);return e.join("")}function ur(n){for(var t,e,r=-1,u=n.length,i=u+4,o=[],a=[];++r<4;)e=n[r%u],o.push(e[0]),a.push(e[1]);for(t=[or(dc,o),",",or(dc,a)],--r;++r<i;)e=n[r%u],o.shift(),o.push(e[0]),a.shift(),a.push(e[1]),ar(t,o,a);return t.join("")}function ir(n,t){var e=n.length-1;if(e)for(var r,u,i=n[0][0],o=n[0][1],a=n[e][0]-i,c=n[e][1]-o,l=-1;++l<=e;)r=n[l],u=l/e,r[0]=t*r[0]+(1-t)*(i+u*a),r[1]=t*r[1]+(1-t)*(o+u*c);return er(n)}function or(n,t){return n[0]*t[0]+n[1]*t[1]+n[2]*t[2]+n[3]*t[3]}function ar(n,t,e){n.push("C",or(gc,t),",",or(gc,e),",",or(pc,t),",",or(pc,e),",",or(dc,t),",",or(dc,e))}function cr(n,t){return(t[1]-n[1])/(t[0]-n[0])}function lr(n){for(var t=0,e=n.length-1,r=[],u=n[0],i=n[1],o=r[0]=cr(u,i);++t<e;)r[t]=(o+(o=cr(u=i,i=n[t+1])))/2;
      return r[t]=o,r}function sr(n){for(var t,e,r,u,i=[],o=lr(n),a=-1,c=n.length-1;++a<c;)t=cr(n[a],n[a+1]),Math.abs(t)<1e-6?o[a]=o[a+1]=0:(e=o[a]/t,r=o[a+1]/t,u=e*e+r*r,u>9&&(u=3*t/Math.sqrt(u),o[a]=u*e,o[a+1]=u*r));for(a=-1;++a<=c;)u=(n[Math.min(c,a+1)][0]-n[Math.max(0,a-1)][0])/(6*(1+o[a]*o[a])),i.push([u||0,o[a]*u||0]);return i}function fr(n){return n.length<3?Xe(n):n[0]+nr(n,sr(n))}function hr(n,t,e,r){var u,i,o,a,c,l,s;return u=r[n],i=u[0],o=u[1],u=r[t],a=u[0],c=u[1],u=r[e],l=u[0],s=u[1],(s-o)*(a-i)-(c-o)*(l-i)>0}function gr(n,t,e){return(e[0]-t[0])*(n[1]-t[1])<(e[1]-t[1])*(n[0]-t[0])}function pr(n,t,e,r){var u=n[0],i=e[0],o=t[0]-u,a=r[0]-i,c=n[1],l=e[1],s=t[1]-c,f=r[1]-l,h=(a*(c-l)-f*(u-i))/(f*o-a*s);return[u+h*o,c+h*s]}function dr(n){var t=n[0],e=n[n.length-1];return!(t[0]-e[0]||t[1]-e[1])}function vr(n,t){var e={list:n.map(function(n,t){return{index:t,x:n[0],y:n[1]}}).sort(function(n,t){return n.y<t.y?-1:n.y>t.y?1:n.x<t.x?-1:n.x>t.x?1:0}),bottomSite:null},r={list:[],leftEnd:null,rightEnd:null,init:function(){r.leftEnd=r.createHalfEdge(null,"l"),r.rightEnd=r.createHalfEdge(null,"l"),r.leftEnd.r=r.rightEnd,r.rightEnd.l=r.leftEnd,r.list.unshift(r.leftEnd,r.rightEnd)},createHalfEdge:function(n,t){return{edge:n,side:t,vertex:null,l:null,r:null}},insert:function(n,t){t.l=n,t.r=n.r,n.r.l=t,n.r=t},leftBound:function(n){var t=r.leftEnd;do t=t.r;while(t!=r.rightEnd&&u.rightOf(t,n));return t=t.l},del:function(n){n.l.r=n.r,n.r.l=n.l,n.edge=null},right:function(n){return n.r},left:function(n){return n.l},leftRegion:function(n){return null==n.edge?e.bottomSite:n.edge.region[n.side]},rightRegion:function(n){return null==n.edge?e.bottomSite:n.edge.region[mc[n.side]]}},u={bisect:function(n,t){var e={region:{l:n,r:t},ep:{l:null,r:null}},r=t.x-n.x,u=t.y-n.y,i=r>0?r:-r,o=u>0?u:-u;return e.c=n.x*r+n.y*u+.5*(r*r+u*u),i>o?(e.a=1,e.b=u/r,e.c/=r):(e.b=1,e.a=r/u,e.c/=u),e},intersect:function(n,t){var e=n.edge,r=t.edge;if(!e||!r||e.region.r==r.region.r)return null;var u=e.a*r.b-e.b*r.a;if(Math.abs(u)<1e-10)return null;var i,o,a=(e.c*r.b-r.c*e.b)/u,c=(r.c*e.a-e.c*r.a)/u,l=e.region.r,s=r.region.r;l.y<s.y||l.y==s.y&&l.x<s.x?(i=n,o=e):(i=t,o=r);var f=a>=o.region.r.x;return f&&"l"===i.side||!f&&"r"===i.side?null:{x:a,y:c}},rightOf:function(n,t){var e=n.edge,r=e.region.r,u=t.x>r.x;if(u&&"l"===n.side)return 1;if(!u&&"r"===n.side)return 0;if(1===e.a){var i=t.y-r.y,o=t.x-r.x,a=0,c=0;if(!u&&e.b<0||u&&e.b>=0?c=a=i>=e.b*o:(c=t.x+t.y*e.b>e.c,e.b<0&&(c=!c),c||(a=1)),!a){var l=r.x-e.region.l.x;c=e.b*(o*o-i*i)<l*i*(1+2*o/l+e.b*e.b),e.b<0&&(c=!c)}}else{var s=e.c-e.a*t.x,f=t.y-s,h=t.x-r.x,g=s-r.y;c=f*f>h*h+g*g}return"l"===n.side?c:!c},endPoint:function(n,e,r){n.ep[e]=r,n.ep[mc[e]]&&t(n)},distance:function(n,t){var e=n.x-t.x,r=n.y-t.y;return Math.sqrt(e*e+r*r)}},i={list:[],insert:function(n,t,e){n.vertex=t,n.ystar=t.y+e;for(var r=0,u=i.list,o=u.length;o>r;r++){var a=u[r];if(!(n.ystar>a.ystar||n.ystar==a.ystar&&t.x>a.vertex.x))break}u.splice(r,0,n)},del:function(n){for(var t=0,e=i.list,r=e.length;r>t&&e[t]!=n;++t);e.splice(t,1)},empty:function(){return 0===i.list.length},nextEvent:function(n){for(var t=0,e=i.list,r=e.length;r>t;++t)if(e[t]==n)return e[t+1];return null},min:function(){var n=i.list[0];return{x:n.vertex.x,y:n.ystar}},extractMin:function(){return i.list.shift()}};r.init(),e.bottomSite=e.list.shift();for(var o,a,c,l,s,f,h,g,p,d,v,m,y,M=e.list.shift();;)if(i.empty()||(o=i.min()),M&&(i.empty()||M.y<o.y||M.y==o.y&&M.x<o.x))a=r.leftBound(M),c=r.right(a),h=r.rightRegion(a),m=u.bisect(h,M),f=r.createHalfEdge(m,"l"),r.insert(a,f),d=u.intersect(a,f),d&&(i.del(a),i.insert(a,d,u.distance(d,M))),a=f,f=r.createHalfEdge(m,"r"),r.insert(a,f),d=u.intersect(f,c),d&&i.insert(f,d,u.distance(d,M)),M=e.list.shift();else{if(i.empty())break;a=i.extractMin(),l=r.left(a),c=r.right(a),s=r.right(c),h=r.leftRegion(a),g=r.rightRegion(c),v=a.vertex,u.endPoint(a.edge,a.side,v),u.endPoint(c.edge,c.side,v),r.del(a),i.del(c),r.del(c),y="l",h.y>g.y&&(p=h,h=g,g=p,y="r"),m=u.bisect(h,g),f=r.createHalfEdge(m,y),r.insert(l,f),u.endPoint(m,mc[y],v),d=u.intersect(l,f),d&&(i.del(l),i.insert(l,d,u.distance(d,h))),d=u.intersect(f,s),d&&i.insert(f,d,u.distance(d,h))}for(a=r.right(r.leftEnd);a!=r.rightEnd;a=r.right(a))t(a.edge)}function mr(n){return n.x}function yr(n){return n.y}function Mr(){return{leaf:!0,nodes:[],point:null,x:null,y:null}}function xr(n,t,e,r,u,i){if(!n(t,e,r,u,i)){var o=.5*(e+u),a=.5*(r+i),c=t.nodes;c[0]&&xr(n,c[0],e,r,o,a),c[1]&&xr(n,c[1],o,r,u,a),c[2]&&xr(n,c[2],e,a,o,i),c[3]&&xr(n,c[3],o,a,u,i)}}function br(n,t){n=mo.rgb(n),t=mo.rgb(t);var e=n.r,r=n.g,u=n.b,i=t.r-e,o=t.g-r,a=t.b-u;return function(n){return"#"+ct(Math.round(e+i*n))+ct(Math.round(r+o*n))+ct(Math.round(u+a*n))}}function _r(n,t){var e,r={},u={};for(e in n)e in t?r[e]=Er(n[e],t[e]):u[e]=n[e];for(e in t)e in n||(u[e]=t[e]);return function(n){for(e in r)u[e]=r[e](n);return u}}function wr(n,t){return t-=n=+n,function(e){return n+t*e}}function Sr(n,t){var e,r,u,i,o,a=0,c=0,l=[],s=[];for(n+="",t+="",yc.lastIndex=0,r=0;e=yc.exec(t);++r)e.index&&l.push(t.substring(a,c=e.index)),s.push({i:l.length,x:e[0]}),l.push(null),a=yc.lastIndex;for(a<t.length&&l.push(t.substring(a)),r=0,i=s.length;(e=yc.exec(n))&&i>r;++r)if(o=s[r],o.x==e[0]){if(o.i)if(null==l[o.i+1])for(l[o.i-1]+=o.x,l.splice(o.i,1),u=r+1;i>u;++u)s[u].i--;else for(l[o.i-1]+=o.x+l[o.i+1],l.splice(o.i,2),u=r+1;i>u;++u)s[u].i-=2;else if(null==l[o.i+1])l[o.i]=o.x;else for(l[o.i]=o.x+l[o.i+1],l.splice(o.i+1,1),u=r+1;i>u;++u)s[u].i--;s.splice(r,1),i--,r--}else o.x=wr(parseFloat(e[0]),parseFloat(o.x));for(;i>r;)o=s.pop(),null==l[o.i+1]?l[o.i]=o.x:(l[o.i]=o.x+l[o.i+1],l.splice(o.i+1,1)),i--;return 1===l.length?null==l[0]?(o=s[0].x,function(n){return o(n)+""}):function(){return t}:function(n){for(r=0;i>r;++r)l[(o=s[r]).i]=o.x(n);return l.join("")}}function Er(n,t){for(var e,r=mo.interpolators.length;--r>=0&&!(e=mo.interpolators[r](n,t)););return e}function kr(n,t){var e,r=[],u=[],i=n.length,o=t.length,a=Math.min(n.length,t.length);for(e=0;a>e;++e)r.push(Er(n[e],t[e]));for(;i>e;++e)u[e]=n[e];for(;o>e;++e)u[e]=t[e];return function(n){for(e=0;a>e;++e)u[e]=r[e](n);return u}}function Ar(n){return function(t){return 0>=t?0:t>=1?1:n(t)}}function Nr(n){return function(t){return 1-n(1-t)}}function Tr(n){return function(t){return.5*(.5>t?n(2*t):2-n(2-2*t))}}function qr(n){return n*n}function zr(n){return n*n*n}function Cr(n){if(0>=n)return 0;if(n>=1)return 1;var t=n*n,e=t*n;return 4*(.5>n?e:3*(n-t)+e-.75)}function Dr(n){return function(t){return Math.pow(t,n)}}function jr(n){return 1-Math.cos(n*Bo/2)}function Lr(n){return Math.pow(2,10*(n-1))}function Hr(n){return 1-Math.sqrt(1-n*n)}function Fr(n,t){var e;return arguments.length<2&&(t=.45),arguments.length?e=t/(2*Bo)*Math.asin(1/n):(n=1,e=t/4),function(r){return 1+n*Math.pow(2,10*-r)*Math.sin(2*(r-e)*Bo/t)}}function Pr(n){return n||(n=1.70158),function(t){return t*t*((n+1)*t-n)}}function Or(n){return 1/2.75>n?7.5625*n*n:2/2.75>n?7.5625*(n-=1.5/2.75)*n+.75:2.5/2.75>n?7.5625*(n-=2.25/2.75)*n+.9375:7.5625*(n-=2.625/2.75)*n+.984375}function Rr(n,t){n=mo.hcl(n),t=mo.hcl(t);var e=n.h,r=n.c,u=n.l,i=t.h-e,o=t.c-r,a=t.l-u;return isNaN(o)&&(o=0,r=isNaN(r)?t.c:r),isNaN(i)?(i=0,e=isNaN(e)?t.h:e):i>180?i-=360:-180>i&&(i+=360),function(n){return J(e+i*n,r+o*n,u+a*n)+""}}function Yr(n,t){n=mo.hsl(n),t=mo.hsl(t);var e=n.h,r=n.s,u=n.l,i=t.h-e,o=t.s-r,a=t.l-u;return isNaN(o)&&(o=0,r=isNaN(r)?t.s:r),isNaN(i)?(i=0,e=isNaN(e)?t.h:e):i>180?i-=360:-180>i&&(i+=360),function(n){return $(e+i*n,r+o*n,u+a*n)+""}}function Ir(n,t){n=mo.lab(n),t=mo.lab(t);var e=n.l,r=n.a,u=n.b,i=t.l-e,o=t.a-r,a=t.b-u;return function(n){return Q(e+i*n,r+o*n,u+a*n)+""}}function Ur(n,t){return t-=n,function(e){return Math.round(n+t*e)}}function Zr(n){var t=[n.a,n.b],e=[n.c,n.d],r=Xr(t),u=Vr(t,e),i=Xr($r(e,t,-u))||0;t[0]*e[1]<e[0]*t[1]&&(t[0]*=-1,t[1]*=-1,r*=-1,u*=-1),this.rotate=(r?Math.atan2(t[1],t[0]):Math.atan2(-e[0],e[1]))*Ko,this.translate=[n.e,n.f],this.scale=[r,i],this.skew=i?Math.atan2(u,i)*Ko:0}function Vr(n,t){return n[0]*t[0]+n[1]*t[1]}function Xr(n){var t=Math.sqrt(Vr(n,n));return t&&(n[0]/=t,n[1]/=t),t}function $r(n,t,e){return n[0]+=e*t[0],n[1]+=e*t[1],n}function Br(n,t){var e,r=[],u=[],i=mo.transform(n),o=mo.transform(t),a=i.translate,c=o.translate,l=i.rotate,s=o.rotate,f=i.skew,h=o.skew,g=i.scale,p=o.scale;return a[0]!=c[0]||a[1]!=c[1]?(r.push("translate(",null,",",null,")"),u.push({i:1,x:wr(a[0],c[0])},{i:3,x:wr(a[1],c[1])})):c[0]||c[1]?r.push("translate("+c+")"):r.push(""),l!=s?(l-s>180?s+=360:s-l>180&&(l+=360),u.push({i:r.push(r.pop()+"rotate(",null,")")-2,x:wr(l,s)})):s&&r.push(r.pop()+"rotate("+s+")"),f!=h?u.push({i:r.push(r.pop()+"skewX(",null,")")-2,x:wr(f,h)}):h&&r.push(r.pop()+"skewX("+h+")"),g[0]!=p[0]||g[1]!=p[1]?(e=r.push(r.pop()+"scale(",null,",",null,")"),u.push({i:e-4,x:wr(g[0],p[0])},{i:e-2,x:wr(g[1],p[1])})):(1!=p[0]||1!=p[1])&&r.push(r.pop()+"scale("+p+")"),e=u.length,function(n){for(var t,i=-1;++i<e;)r[(t=u[i]).i]=t.x(n);return r.join("")}}function Wr(n,t){return t=t-(n=+n)?1/(t-n):0,function(e){return(e-n)*t}}function Jr(n,t){return t=t-(n=+n)?1/(t-n):0,function(e){return Math.max(0,Math.min(1,(e-n)*t))}}function Gr(n){for(var t=n.source,e=n.target,r=Qr(t,e),u=[t];t!==r;)t=t.parent,u.push(t);for(var i=u.length;e!==r;)u.splice(i,0,e),e=e.parent;return u}function Kr(n){for(var t=[],e=n.parent;null!=e;)t.push(n),n=e,e=e.parent;return t.push(n),t}function Qr(n,t){if(n===t)return n;for(var e=Kr(n),r=Kr(t),u=e.pop(),i=r.pop(),o=null;u===i;)o=u,u=e.pop(),i=r.pop();return o}function nu(n){n.fixed|=2}function tu(n){n.fixed&=-7}function eu(n){n.fixed|=4,n.px=n.x,n.py=n.y}function ru(n){n.fixed&=-5}function uu(n,t,e){var r=0,u=0;if(n.charge=0,!n.leaf)for(var i,o=n.nodes,a=o.length,c=-1;++c<a;)i=o[c],null!=i&&(uu(i,t,e),n.charge+=i.charge,r+=i.charge*i.cx,u+=i.charge*i.cy);if(n.point){n.leaf||(n.point.x+=Math.random()-.5,n.point.y+=Math.random()-.5);var l=t*e[n.point.index];n.charge+=n.pointCharge=l,r+=l*n.point.x,u+=l*n.point.y}n.cx=r/n.charge,n.cy=u/n.charge}function iu(n,t){return mo.rebind(n,t,"sort","children","value"),n.nodes=n,n.links=lu,n}function ou(n){return n.children}function au(n){return n.value}function cu(n,t){return t.value-n.value}function lu(n){return mo.merge(n.map(function(n){return(n.children||[]).map(function(t){return{source:n,target:t}})}))}function su(n){return n.x}function fu(n){return n.y}function hu(n,t,e){n.y0=t,n.y=e}function gu(n){return mo.range(n.length)}function pu(n){for(var t=-1,e=n[0].length,r=[];++t<e;)r[t]=0;return r}function du(n){for(var t,e=1,r=0,u=n[0][1],i=n.length;i>e;++e)(t=n[e][1])>u&&(r=e,u=t);return r}function vu(n){return n.reduce(mu,0)}function mu(n,t){return n+t[1]}function yu(n,t){return Mu(n,Math.ceil(Math.log(t.length)/Math.LN2+1))}function Mu(n,t){for(var e=-1,r=+n[0],u=(n[1]-r)/t,i=[];++e<=t;)i[e]=u*e+r;return i}function xu(n){return[mo.min(n),mo.max(n)]}function bu(n,t){return n.parent==t.parent?1:2}function _u(n){var t=n.children;return t&&t.length?t[0]:n._tree.thread}function wu(n){var t,e=n.children;return e&&(t=e.length)?e[t-1]:n._tree.thread}function Su(n,t){var e=n.children;if(e&&(u=e.length))for(var r,u,i=-1;++i<u;)t(r=Su(e[i],t),n)>0&&(n=r);return n}function Eu(n,t){return n.x-t.x}function ku(n,t){return t.x-n.x}function Au(n,t){return n.depth-t.depth}function Nu(n,t){function e(n,r){var u=n.children;if(u&&(o=u.length))for(var i,o,a=null,c=-1;++c<o;)i=u[c],e(i,a),a=i;t(n,r)}e(n,null)}function Tu(n){for(var t,e=0,r=0,u=n.children,i=u.length;--i>=0;)t=u[i]._tree,t.prelim+=e,t.mod+=e,e+=t.shift+(r+=t.change)}function qu(n,t,e){n=n._tree,t=t._tree;var r=e/(t.number-n.number);n.change+=r,t.change-=r,t.shift+=e,t.prelim+=e,t.mod+=e}function zu(n,t,e){return n._tree.ancestor.parent==t.parent?n._tree.ancestor:e}function Cu(n,t){return n.value-t.value}function Du(n,t){var e=n._pack_next;n._pack_next=t,t._pack_prev=n,t._pack_next=e,e._pack_prev=t}function ju(n,t){n._pack_next=t,t._pack_prev=n}function Lu(n,t){var e=t.x-n.x,r=t.y-n.y,u=n.r+t.r;return.999*u*u>e*e+r*r}function Hu(n){function t(n){s=Math.min(n.x-n.r,s),f=Math.max(n.x+n.r,f),h=Math.min(n.y-n.r,h),g=Math.max(n.y+n.r,g)}if((e=n.children)&&(l=e.length)){var e,r,u,i,o,a,c,l,s=1/0,f=-1/0,h=1/0,g=-1/0;if(e.forEach(Fu),r=e[0],r.x=-r.r,r.y=0,t(r),l>1&&(u=e[1],u.x=u.r,u.y=0,t(u),l>2))for(i=e[2],Ru(r,u,i),t(i),Du(r,i),r._pack_prev=i,Du(i,u),u=r._pack_next,o=3;l>o;o++){Ru(r,u,i=e[o]);var p=0,d=1,v=1;for(a=u._pack_next;a!==u;a=a._pack_next,d++)if(Lu(a,i)){p=1;break}if(1==p)for(c=r._pack_prev;c!==a._pack_prev&&!Lu(c,i);c=c._pack_prev,v++);p?(v>d||d==v&&u.r<r.r?ju(r,u=a):ju(r=c,u),o--):(Du(r,i),u=i,t(i))}var m=(s+f)/2,y=(h+g)/2,M=0;for(o=0;l>o;o++)i=e[o],i.x-=m,i.y-=y,M=Math.max(M,i.r+Math.sqrt(i.x*i.x+i.y*i.y));n.r=M,e.forEach(Pu)}}function Fu(n){n._pack_next=n._pack_prev=n}function Pu(n){delete n._pack_next,delete n._pack_prev}function Ou(n,t,e,r){var u=n.children;if(n.x=t+=r*n.x,n.y=e+=r*n.y,n.r*=r,u)for(var i=-1,o=u.length;++i<o;)Ou(u[i],t,e,r)}function Ru(n,t,e){var r=n.r+e.r,u=t.x-n.x,i=t.y-n.y;if(r&&(u||i)){var o=t.r+e.r,a=u*u+i*i;o*=o,r*=r;var c=.5+(r-o)/(2*a),l=Math.sqrt(Math.max(0,2*o*(r+a)-(r-=a)*r-o*o))/(2*a);e.x=n.x+c*u+l*i,e.y=n.y+c*i-l*u}else e.x=n.x+r,e.y=n.y}function Yu(n){return 1+mo.max(n,function(n){return n.y})}function Iu(n){return n.reduce(function(n,t){return n+t.x},0)/n.length}function Uu(n){var t=n.children;return t&&t.length?Uu(t[0]):n}function Zu(n){var t,e=n.children;return e&&(t=e.length)?Zu(e[t-1]):n}function Vu(n){return{x:n.x,y:n.y,dx:n.dx,dy:n.dy}}function Xu(n,t){var e=n.x+t[3],r=n.y+t[0],u=n.dx-t[1]-t[3],i=n.dy-t[0]-t[2];return 0>u&&(e+=u/2,u=0),0>i&&(r+=i/2,i=0),{x:e,y:r,dx:u,dy:i}}function $u(n){var t=n[0],e=n[n.length-1];return e>t?[t,e]:[e,t]}function Bu(n){return n.rangeExtent?n.rangeExtent():$u(n.range())}function Wu(n,t,e,r){var u=e(n[0],n[1]),i=r(t[0],t[1]);return function(n){return i(u(n))}}function Ju(n,t){var e,r=0,u=n.length-1,i=n[r],o=n[u];return i>o&&(e=r,r=u,u=e,e=i,i=o,o=e),n[r]=t.floor(i),n[u]=t.ceil(o),n}function Gu(n){return n?{floor:function(t){return Math.floor(t/n)*n},ceil:function(t){return Math.ceil(t/n)*n}}:Nc}function Ku(n,t,e,r){var u=[],i=[],o=0,a=Math.min(n.length,t.length)-1;for(n[a]<n[0]&&(n=n.slice().reverse(),t=t.slice().reverse());++o<=a;)u.push(e(n[o-1],n[o])),i.push(r(t[o-1],t[o]));return function(t){var e=mo.bisect(n,t,1,a)-1;return i[e](u[e](t))}}function Qu(n,t,e,r){function u(){var u=Math.min(n.length,t.length)>2?Ku:Wu,c=r?Jr:Wr;return o=u(n,t,c,e),a=u(t,n,c,Er),i}function i(n){return o(n)}var o,a;return i.invert=function(n){return a(n)},i.domain=function(t){return arguments.length?(n=t.map(Number),u()):n},i.range=function(n){return arguments.length?(t=n,u()):t},i.rangeRound=function(n){return i.range(n).interpolate(Ur)},i.clamp=function(n){return arguments.length?(r=n,u()):r},i.interpolate=function(n){return arguments.length?(e=n,u()):e},i.ticks=function(t){return ri(n,t)},i.tickFormat=function(t,e){return ui(n,t,e)},i.nice=function(t){return ti(n,t),u()},i.copy=function(){return Qu(n,t,e,r)},u()}function ni(n,t){return mo.rebind(n,t,"range","rangeRound","interpolate","clamp")}function ti(n,t){return Ju(n,Gu(ei(n,t)[2]))}function ei(n,t){null==t&&(t=10);var e=$u(n),r=e[1]-e[0],u=Math.pow(10,Math.floor(Math.log(r/t)/Math.LN10)),i=t/r*u;return.15>=i?u*=10:.35>=i?u*=5:.75>=i&&(u*=2),e[0]=Math.ceil(e[0]/u)*u,e[1]=Math.floor(e[1]/u)*u+.5*u,e[2]=u,e}function ri(n,t){return mo.range.apply(mo,ei(n,t))}function ui(n,t,e){var r=-Math.floor(Math.log(ei(n,t)[2])/Math.LN10+.01);return mo.format(e?e.replace(Ea,function(n,t,e,u,i,o,a,c,l,s){return[t,e,u,i,o,a,c,l||"."+(r-2*("%"===s)),s].join("")}):",."+r+"f")}function ii(n,t,e,r){function u(n){return(e?Math.log(0>n?0:n):-Math.log(n>0?0:-n))/Math.log(t)}function i(n){return e?Math.pow(t,n):-Math.pow(t,-n)}function o(t){return n(u(t))}return o.invert=function(t){return i(n.invert(t))},o.domain=function(t){return arguments.length?(e=t[0]>=0,n.domain((r=t.map(Number)).map(u)),o):r},o.base=function(e){return arguments.length?(t=+e,n.domain(r.map(u)),o):t},o.nice=function(){var t=Ju(r.map(u),e?Math:qc);return n.domain(t),r=t.map(i),o},o.ticks=function(){var n=$u(r),o=[],a=n[0],c=n[1],l=Math.floor(u(a)),s=Math.ceil(u(c)),f=t%1?2:t;if(isFinite(s-l)){if(e){for(;s>l;l++)for(var h=1;f>h;h++)o.push(i(l)*h);o.push(i(l))}else for(o.push(i(l));l++<s;)for(var h=f-1;h>0;h--)o.push(i(l)*h);for(l=0;o[l]<a;l++);for(s=o.length;o[s-1]>c;s--);o=o.slice(l,s)}return o},o.tickFormat=function(n,t){if(!arguments.length)return Tc;arguments.length<2?t=Tc:"function"!=typeof t&&(t=mo.format(t));var r,a=Math.max(.1,n/o.ticks().length),c=e?(r=1e-12,Math.ceil):(r=-1e-12,Math.floor);return function(n){return n/i(c(u(n)+r))<=a?t(n):""}},o.copy=function(){return ii(n.copy(),t,e,r)},ni(o,n)}function oi(n,t,e){function r(t){return n(u(t))}var u=ai(t),i=ai(1/t);return r.invert=function(t){return i(n.invert(t))},r.domain=function(t){return arguments.length?(n.domain((e=t.map(Number)).map(u)),r):e},r.ticks=function(n){return ri(e,n)},r.tickFormat=function(n,t){return ui(e,n,t)},r.nice=function(n){return r.domain(ti(e,n))},r.exponent=function(o){return arguments.length?(u=ai(t=o),i=ai(1/t),n.domain(e.map(u)),r):t},r.copy=function(){return oi(n.copy(),t,e)},ni(r,n)}function ai(n){return function(t){return 0>t?-Math.pow(-t,n):Math.pow(t,n)}}function ci(n,t){function e(t){return o[((i.get(t)||i.set(t,n.push(t)))-1)%o.length]}function r(t,e){return mo.range(n.length).map(function(n){return t+e*n})}var i,o,a;return e.domain=function(r){if(!arguments.length)return n;n=[],i=new u;for(var o,a=-1,c=r.length;++a<c;)i.has(o=r[a])||i.set(o,n.push(o));return e[t.t].apply(e,t.a)},e.range=function(n){return arguments.length?(o=n,a=0,t={t:"range",a:arguments},e):o},e.rangePoints=function(u,i){arguments.length<2&&(i=0);var c=u[0],l=u[1],s=(l-c)/(Math.max(1,n.length-1)+i);return o=r(n.length<2?(c+l)/2:c+s*i/2,s),a=0,t={t:"rangePoints",a:arguments},e},e.rangeBands=function(u,i,c){arguments.length<2&&(i=0),arguments.length<3&&(c=i);var l=u[1]<u[0],s=u[l-0],f=u[1-l],h=(f-s)/(n.length-i+2*c);return o=r(s+h*c,h),l&&o.reverse(),a=h*(1-i),t={t:"rangeBands",a:arguments},e},e.rangeRoundBands=function(u,i,c){arguments.length<2&&(i=0),arguments.length<3&&(c=i);var l=u[1]<u[0],s=u[l-0],f=u[1-l],h=Math.floor((f-s)/(n.length-i+2*c)),g=f-s-(n.length-i)*h;return o=r(s+Math.round(g/2),h),l&&o.reverse(),a=Math.round(h*(1-i)),t={t:"rangeRoundBands",a:arguments},e},e.rangeBand=function(){return a},e.rangeExtent=function(){return $u(t.a[0])},e.copy=function(){return ci(n,t)},e.domain(n)}function li(n,t){function e(){var e=0,i=t.length;for(u=[];++e<i;)u[e-1]=mo.quantile(n,e/i);return r}function r(n){return isNaN(n=+n)?void 0:t[mo.bisect(u,n)]}var u;return r.domain=function(t){return arguments.length?(n=t.filter(function(n){return!isNaN(n)}).sort(mo.ascending),e()):n},r.range=function(n){return arguments.length?(t=n,e()):t},r.quantiles=function(){return u},r.invertExtent=function(e){return e=t.indexOf(e),0>e?[0/0,0/0]:[e>0?u[e-1]:n[0],e<u.length?u[e]:n[n.length-1]]},r.copy=function(){return li(n,t)},e()}function si(n,t,e){function r(t){return e[Math.max(0,Math.min(o,Math.floor(i*(t-n))))]}function u(){return i=e.length/(t-n),o=e.length-1,r}var i,o;return r.domain=function(e){return arguments.length?(n=+e[0],t=+e[e.length-1],u()):[n,t]},r.range=function(n){return arguments.length?(e=n,u()):e},r.invertExtent=function(t){return t=e.indexOf(t),t=0>t?0/0:t/i+n,[t,t+1/i]},r.copy=function(){return si(n,t,e)},u()}function fi(n,t){function e(e){return e>=e?t[mo.bisect(n,e)]:void 0}return e.domain=function(t){return arguments.length?(n=t,e):n},e.range=function(n){return arguments.length?(t=n,e):t},e.invertExtent=function(e){return e=t.indexOf(e),[n[e-1],n[e]]},e.copy=function(){return fi(n,t)},e}function hi(n){function t(n){return+n}return t.invert=t,t.domain=t.range=function(e){return arguments.length?(n=e.map(t),t):n},t.ticks=function(t){return ri(n,t)},t.tickFormat=function(t,e){return ui(n,t,e)},t.copy=function(){return hi(n)},t}function gi(n){return n.innerRadius}function pi(n){return n.outerRadius}function di(n){return n.startAngle}function vi(n){return n.endAngle}function mi(n){for(var t,e,r,u=-1,i=n.length;++u<i;)t=n[u],e=t[0],r=t[1]+Lc,t[0]=e*Math.cos(r),t[1]=e*Math.sin(r);return n}function yi(n){function t(t){function c(){d.push("M",a(n(m),f),s,l(n(v.reverse()),f),"Z")}for(var h,g,p,d=[],v=[],m=[],y=-1,M=t.length,x=pt(e),b=pt(u),_=e===r?function(){return g}:pt(r),w=u===i?function(){return p}:pt(i);++y<M;)o.call(this,h=t[y],y)?(v.push([g=+x.call(this,h,y),p=+b.call(this,h,y)]),m.push([+_.call(this,h,y),+w.call(this,h,y)])):v.length&&(c(),v=[],m=[]);return v.length&&c(),d.length?d.join(""):null}var e=Ze,r=Ze,u=0,i=Ve,o=Vt,a=Xe,c=a.key,l=a,s="L",f=.7;return t.x=function(n){return arguments.length?(e=r=n,t):r},t.x0=function(n){return arguments.length?(e=n,t):e},t.x1=function(n){return arguments.length?(r=n,t):r},t.y=function(n){return arguments.length?(u=i=n,t):i},t.y0=function(n){return arguments.length?(u=n,t):u},t.y1=function(n){return arguments.length?(i=n,t):i},t.defined=function(n){return arguments.length?(o=n,t):o},t.interpolate=function(n){return arguments.length?(c="function"==typeof n?a=n:(a=hc.get(n)||Xe).key,l=a.reverse||a,s=a.closed?"M":"L",t):c},t.tension=function(n){return arguments.length?(f=n,t):f},t}function Mi(n){return n.radius}function xi(n){return[n.x,n.y]}function bi(n){return function(){var t=n.apply(this,arguments),e=t[0],r=t[1]+Lc;return[e*Math.cos(r),e*Math.sin(r)]}}function _i(){return 64}function wi(){return"circle"}function Si(n){var t=Math.sqrt(n/Bo);return"M0,"+t+"A"+t+","+t+" 0 1,1 0,"+-t+"A"+t+","+t+" 0 1,1 0,"+t+"Z"}function Ei(n,t){return Lo(n,Ic),n.id=t,n}function ki(n,t,e,r){var u=n.id;return N(n,"function"==typeof e?function(n,i,o){n.__transition__[u].tween.set(t,r(e.call(n,n.__data__,i,o)))}:(e=r(e),function(n){n.__transition__[u].tween.set(t,e)}))}function Ai(n){return null==n&&(n=""),function(){this.textContent=n}}function Ni(n,t,e,r){var i=n.__transition__||(n.__transition__={active:0,count:0}),o=i[e];if(!o){var a=r.time;o=i[e]={tween:new u,time:a,ease:r.ease,delay:r.delay,duration:r.duration},++i.count,mo.timer(function(r){function u(r){return i.active>e?l():(i.active=e,o.event&&o.event.start.call(n,s,t),o.tween.forEach(function(e,r){(r=r.call(n,s,t))&&p.push(r)}),c(r)?1:(xt(c,0,a),void 0))}function c(r){if(i.active!==e)return l();for(var u=(r-h)/g,a=f(u),c=p.length;c>0;)p[--c].call(n,a);return u>=1?(o.event&&o.event.end.call(n,s,t),l()):void 0}function l(){return--i.count?delete i[e]:delete n.__transition__,1}var s=n.__data__,f=o.ease,h=o.delay,g=o.duration,p=[];return r>=h?u(r):(xt(u,h,a),void 0)},0,a)}}function Ti(n,t){n.attr("transform",function(n){return"translate("+t(n)+",0)"})}function qi(n,t){n.attr("transform",function(n){return"translate(0,"+t(n)+")"})}function zi(){this._=new Date(arguments.length>1?Date.UTC.apply(this,arguments):arguments[0])}function Ci(n,t,e){function r(t){var e=n(t),r=i(e,1);return r-t>t-e?e:r}function u(e){return t(e=n(new Wc(e-1)),1),e}function i(n,e){return t(n=new Wc(+n),e),n}function o(n,r,i){var o=u(n),a=[];if(i>1)for(;r>o;)e(o)%i||a.push(new Date(+o)),t(o,1);else for(;r>o;)a.push(new Date(+o)),t(o,1);return a}function a(n,t,e){try{Wc=zi;var r=new zi;return r._=n,o(r,t,e)}finally{Wc=Date}}n.floor=n,n.round=r,n.ceil=u,n.offset=i,n.range=o;var c=n.utc=Di(n);return c.floor=c,c.round=Di(r),c.ceil=Di(u),c.offset=Di(i),c.range=a,n}function Di(n){return function(t,e){try{Wc=zi;var r=new zi;return r._=t,n(r,e)._}finally{Wc=Date}}}function ji(n){function t(t){for(var r,u,i,o=[],a=-1,c=0;++a<e;)37===n.charCodeAt(a)&&(o.push(n.substring(c,a)),null!=(u=pl[r=n.charAt(++a)])&&(r=n.charAt(++a)),(i=dl[r])&&(r=i(t,null==u?"e"===r?" ":"0":u)),o.push(r),c=a+1);return o.push(n.substring(c,a)),o.join("")}var e=n.length;return t.parse=function(t){var e={y:1900,m:0,d:1,H:0,M:0,S:0,L:0,Z:null},r=Li(e,n,t,0);if(r!=t.length)return null;"p"in e&&(e.H=e.H%12+12*e.p);var u=null!=e.Z&&Wc!==zi,i=new(u?zi:Wc);return"j"in e?i.setFullYear(e.y,0,e.j):"w"in e&&("W"in e||"U"in e)?(i.setFullYear(e.y,0,1),i.setFullYear(e.y,0,"W"in e?(e.w+6)%7+7*e.W-(i.getDay()+5)%7:e.w+7*e.U-(i.getDay()+6)%7)):i.setFullYear(e.y,e.m,e.d),i.setHours(e.H+Math.floor(e.Z/100),e.M+e.Z%100,e.S,e.L),u?i._:i},t.toString=function(){return n},t}function Li(n,t,e,r){for(var u,i,o,a=0,c=t.length,l=e.length;c>a;){if(r>=l)return-1;if(u=t.charCodeAt(a++),37===u){if(o=t.charAt(a++),i=vl[o in pl?t.charAt(a++):o],!i||(r=i(n,e,r))<0)return-1}else if(u!=e.charCodeAt(r++))return-1}return r}function Hi(n){return new RegExp("^(?:"+n.map(mo.requote).join("|")+")","i")}function Fi(n){for(var t=new u,e=-1,r=n.length;++e<r;)t.set(n[e].toLowerCase(),e);return t}function Pi(n,t,e){var r=0>n?"-":"",u=(r?-n:n)+"",i=u.length;return r+(e>i?new Array(e-i+1).join(t)+u:u)}function Oi(n,t,e){al.lastIndex=0;var r=al.exec(t.substring(e));return r?(n.w=cl.get(r[0].toLowerCase()),e+r[0].length):-1}function Ri(n,t,e){il.lastIndex=0;var r=il.exec(t.substring(e));return r?(n.w=ol.get(r[0].toLowerCase()),e+r[0].length):-1}function Yi(n,t,e){ml.lastIndex=0;var r=ml.exec(t.substring(e,e+1));return r?(n.w=+r[0],e+r[0].length):-1}function Ii(n,t,e){ml.lastIndex=0;var r=ml.exec(t.substring(e));return r?(n.U=+r[0],e+r[0].length):-1}function Ui(n,t,e){ml.lastIndex=0;var r=ml.exec(t.substring(e));return r?(n.W=+r[0],e+r[0].length):-1}function Zi(n,t,e){fl.lastIndex=0;var r=fl.exec(t.substring(e));return r?(n.m=hl.get(r[0].toLowerCase()),e+r[0].length):-1}function Vi(n,t,e){ll.lastIndex=0;var r=ll.exec(t.substring(e));return r?(n.m=sl.get(r[0].toLowerCase()),e+r[0].length):-1}function Xi(n,t,e){return Li(n,dl.c.toString(),t,e)}function $i(n,t,e){return Li(n,dl.x.toString(),t,e)}function Bi(n,t,e){return Li(n,dl.X.toString(),t,e)}function Wi(n,t,e){ml.lastIndex=0;var r=ml.exec(t.substring(e,e+4));return r?(n.y=+r[0],e+r[0].length):-1}function Ji(n,t,e){ml.lastIndex=0;var r=ml.exec(t.substring(e,e+2));return r?(n.y=Ki(+r[0]),e+r[0].length):-1}function Gi(n,t,e){return/^[+-]\d{4}$/.test(t=t.substring(e,e+5))?(n.Z=+t,e+5):-1}function Ki(n){return n+(n>68?1900:2e3)}function Qi(n,t,e){ml.lastIndex=0;var r=ml.exec(t.substring(e,e+2));return r?(n.m=r[0]-1,e+r[0].length):-1}function no(n,t,e){ml.lastIndex=0;var r=ml.exec(t.substring(e,e+2));return r?(n.d=+r[0],e+r[0].length):-1}function to(n,t,e){ml.lastIndex=0;var r=ml.exec(t.substring(e,e+3));return r?(n.j=+r[0],e+r[0].length):-1}function eo(n,t,e){ml.lastIndex=0;var r=ml.exec(t.substring(e,e+2));return r?(n.H=+r[0],e+r[0].length):-1}function ro(n,t,e){ml.lastIndex=0;var r=ml.exec(t.substring(e,e+2));return r?(n.M=+r[0],e+r[0].length):-1}function uo(n,t,e){ml.lastIndex=0;var r=ml.exec(t.substring(e,e+2));return r?(n.S=+r[0],e+r[0].length):-1}function io(n,t,e){ml.lastIndex=0;var r=ml.exec(t.substring(e,e+3));return r?(n.L=+r[0],e+r[0].length):-1}function oo(n,t,e){var r=yl.get(t.substring(e,e+=2).toLowerCase());return null==r?-1:(n.p=r,e)}function ao(n){var t=n.getTimezoneOffset(),e=t>0?"-":"+",r=~~(Math.abs(t)/60),u=Math.abs(t)%60;return e+Pi(r,"0",2)+Pi(u,"0",2)}function co(n,t,e){gl.lastIndex=0;var r=gl.exec(t.substring(e,e+1));return r?e+r[0].length:-1}function lo(n){function t(n){try{Wc=zi;var t=new Wc;return t._=n,e(t)}finally{Wc=Date}}var e=ji(n);return t.parse=function(n){try{Wc=zi;var t=e.parse(n);return t&&t._}finally{Wc=Date}},t.toString=e.toString,t}function so(n){return n.toISOString()}function fo(n,t,e){function r(t){return n(t)}function u(n,e){var r=n[1]-n[0],u=r/e,i=mo.bisect(xl,u);return i==xl.length?[t.year,ei(n.map(function(n){return n/31536e6}),e)[2]]:i?t[u/xl[i-1]<xl[i]/u?i-1:i]:[Sl,ei(n,e)[2]]}return r.invert=function(t){return ho(n.invert(t))},r.domain=function(t){return arguments.length?(n.domain(t),r):n.domain().map(ho)},r.nice=function(n,t){function e(e){return!isNaN(e)&&!n.range(e,ho(+e+1),t).length}var i=r.domain(),o=$u(i),a=null==n?u(o,10):"number"==typeof n&&u(o,n);return a&&(n=a[0],t=a[1]),r.domain(Ju(i,t>1?{floor:function(t){for(;e(t=n.floor(t));)t=ho(t-1);return t},ceil:function(t){for(;e(t=n.ceil(t));)t=ho(+t+1);return t}}:n))},r.ticks=function(n,t){var e=$u(r.domain()),i=null==n?u(e,10):"number"==typeof n?u(e,n):!n.range&&[{range:n},t];return i&&(n=i[0],t=i[1]),n.range(e[0],ho(+e[1]+1),t)},r.tickFormat=function(){return e},r.copy=function(){return fo(n.copy(),t,e)},ni(r,n)}function ho(n){return new Date(n)}function go(n){return function(t){for(var e=n.length-1,r=n[e];!r[1](t);)r=n[--e];return r[0](t)}}function po(n){return JSON.parse(n.responseText)}function vo(n){var t=xo.createRange();return t.selectNode(xo.body),t.createContextualFragment(n.responseText)}var mo={version:"3.3.3"};Date.now||(Date.now=function(){return+new Date});var yo=[].slice,Mo=function(n){return yo.call(n)},xo=document,bo=xo.documentElement,_o=window;try{Mo(bo.childNodes)[0].nodeType}catch(wo){Mo=function(n){for(var t=n.length,e=new Array(t);t--;)e[t]=n[t];return e}}try{xo.createElement("div").style.setProperty("opacity",0,"")}catch(So){var Eo=_o.Element.prototype,ko=Eo.setAttribute,Ao=Eo.setAttributeNS,No=_o.CSSStyleDeclaration.prototype,To=No.setProperty;Eo.setAttribute=function(n,t){ko.call(this,n,t+"")},Eo.setAttributeNS=function(n,t,e){Ao.call(this,n,t,e+"")},No.setProperty=function(n,t,e){To.call(this,n,t+"",e)}}mo.ascending=function(n,t){return t>n?-1:n>t?1:n>=t?0:0/0},mo.descending=function(n,t){return n>t?-1:t>n?1:t>=n?0:0/0},mo.min=function(n,t){var e,r,u=-1,i=n.length;if(1===arguments.length){for(;++u<i&&!(null!=(e=n[u])&&e>=e);)e=void 0;for(;++u<i;)null!=(r=n[u])&&e>r&&(e=r)}else{for(;++u<i&&!(null!=(e=t.call(n,n[u],u))&&e>=e);)e=void 0;for(;++u<i;)null!=(r=t.call(n,n[u],u))&&e>r&&(e=r)}return e},mo.max=function(n,t){var e,r,u=-1,i=n.length;if(1===arguments.length){for(;++u<i&&!(null!=(e=n[u])&&e>=e);)e=void 0;for(;++u<i;)null!=(r=n[u])&&r>e&&(e=r)}else{for(;++u<i&&!(null!=(e=t.call(n,n[u],u))&&e>=e);)e=void 0;for(;++u<i;)null!=(r=t.call(n,n[u],u))&&r>e&&(e=r)}return e},mo.extent=function(n,t){var e,r,u,i=-1,o=n.length;if(1===arguments.length){for(;++i<o&&!(null!=(e=u=n[i])&&e>=e);)e=u=void 0;for(;++i<o;)null!=(r=n[i])&&(e>r&&(e=r),r>u&&(u=r))}else{for(;++i<o&&!(null!=(e=u=t.call(n,n[i],i))&&e>=e);)e=void 0;for(;++i<o;)null!=(r=t.call(n,n[i],i))&&(e>r&&(e=r),r>u&&(u=r))}return[e,u]},mo.sum=function(n,t){var e,r=0,u=n.length,i=-1;if(1===arguments.length)for(;++i<u;)isNaN(e=+n[i])||(r+=e);else for(;++i<u;)isNaN(e=+t.call(n,n[i],i))||(r+=e);return r},mo.mean=function(t,e){var r,u=t.length,i=0,o=-1,a=0;if(1===arguments.length)for(;++o<u;)n(r=t[o])&&(i+=(r-i)/++a);else for(;++o<u;)n(r=e.call(t,t[o],o))&&(i+=(r-i)/++a);return a?i:void 0},mo.quantile=function(n,t){var e=(n.length-1)*t+1,r=Math.floor(e),u=+n[r-1],i=e-r;return i?u+i*(n[r]-u):u},mo.median=function(t,e){return arguments.length>1&&(t=t.map(e)),t=t.filter(n),t.length?mo.quantile(t.sort(mo.ascending),.5):void 0},mo.bisector=function(n){return{left:function(t,e,r,u){for(arguments.length<3&&(r=0),arguments.length<4&&(u=t.length);u>r;){var i=r+u>>>1;n.call(t,t[i],i)<e?r=i+1:u=i}return r},right:function(t,e,r,u){for(arguments.length<3&&(r=0),arguments.length<4&&(u=t.length);u>r;){var i=r+u>>>1;e<n.call(t,t[i],i)?u=i:r=i+1}return r}}};var qo=mo.bisector(function(n){return n});mo.bisectLeft=qo.left,mo.bisect=mo.bisectRight=qo.right,mo.shuffle=function(n){for(var t,e,r=n.length;r;)e=0|Math.random()*r--,t=n[r],n[r]=n[e],n[e]=t;return n},mo.permute=function(n,t){for(var e=t.length,r=new Array(e);e--;)r[e]=n[t[e]];return r},mo.pairs=function(n){for(var t,e=0,r=n.length-1,u=n[0],i=new Array(0>r?0:r);r>e;)i[e]=[t=u,u=n[++e]];return i},mo.zip=function(){if(!(u=arguments.length))return[];for(var n=-1,e=mo.min(arguments,t),r=new Array(e);++n<e;)for(var u,i=-1,o=r[n]=new Array(u);++i<u;)o[i]=arguments[i][n];return r},mo.transpose=function(n){return mo.zip.apply(mo,n)},mo.keys=function(n){var t=[];for(var e in n)t.push(e);return t},mo.values=function(n){var t=[];for(var e in n)t.push(n[e]);return t},mo.entries=function(n){var t=[];
      for(var e in n)t.push({key:e,value:n[e]});return t},mo.merge=function(n){return Array.prototype.concat.apply([],n)},mo.range=function(n,t,r){if(arguments.length<3&&(r=1,arguments.length<2&&(t=n,n=0)),1/0===(t-n)/r)throw new Error("infinite range");var u,i=[],o=e(Math.abs(r)),a=-1;if(n*=o,t*=o,r*=o,0>r)for(;(u=n+r*++a)>t;)i.push(u/o);else for(;(u=n+r*++a)<t;)i.push(u/o);return i},mo.map=function(n){var t=new u;if(n instanceof u)n.forEach(function(n,e){t.set(n,e)});else for(var e in n)t.set(e,n[e]);return t},r(u,{has:function(n){return zo+n in this},get:function(n){return this[zo+n]},set:function(n,t){return this[zo+n]=t},remove:function(n){return n=zo+n,n in this&&delete this[n]},keys:function(){var n=[];return this.forEach(function(t){n.push(t)}),n},values:function(){var n=[];return this.forEach(function(t,e){n.push(e)}),n},entries:function(){var n=[];return this.forEach(function(t,e){n.push({key:t,value:e})}),n},forEach:function(n){for(var t in this)t.charCodeAt(0)===Co&&n.call(this,t.substring(1),this[t])}});var zo="\0",Co=zo.charCodeAt(0);mo.nest=function(){function n(t,a,c){if(c>=o.length)return r?r.call(i,a):e?a.sort(e):a;for(var l,s,f,h,g=-1,p=a.length,d=o[c++],v=new u;++g<p;)(h=v.get(l=d(s=a[g])))?h.push(s):v.set(l,[s]);return t?(s=t(),f=function(e,r){s.set(e,n(t,r,c))}):(s={},f=function(e,r){s[e]=n(t,r,c)}),v.forEach(f),s}function t(n,e){if(e>=o.length)return n;var r=[],u=a[e++];return n.forEach(function(n,u){r.push({key:n,values:t(u,e)})}),u?r.sort(function(n,t){return u(n.key,t.key)}):r}var e,r,i={},o=[],a=[];return i.map=function(t,e){return n(e,t,0)},i.entries=function(e){return t(n(mo.map,e,0),0)},i.key=function(n){return o.push(n),i},i.sortKeys=function(n){return a[o.length-1]=n,i},i.sortValues=function(n){return e=n,i},i.rollup=function(n){return r=n,i},i},mo.set=function(n){var t=new i;if(n)for(var e=0,r=n.length;r>e;++e)t.add(n[e]);return t},r(i,{has:function(n){return zo+n in this},add:function(n){return this[zo+n]=!0,n},remove:function(n){return n=zo+n,n in this&&delete this[n]},values:function(){var n=[];return this.forEach(function(t){n.push(t)}),n},forEach:function(n){for(var t in this)t.charCodeAt(0)===Co&&n.call(this,t.substring(1))}}),mo.behavior={},mo.rebind=function(n,t){for(var e,r=1,u=arguments.length;++r<u;)n[e=arguments[r]]=o(n,t,t[e]);return n};var Do=["webkit","ms","moz","Moz","o","O"];mo.dispatch=function(){for(var n=new l,t=-1,e=arguments.length;++t<e;)n[arguments[t]]=s(n);return n},l.prototype.on=function(n,t){var e=n.indexOf("."),r="";if(e>=0&&(r=n.substring(e+1),n=n.substring(0,e)),n)return arguments.length<2?this[n].on(r):this[n].on(r,t);if(2===arguments.length){if(null==t)for(n in this)this.hasOwnProperty(n)&&this[n].on(r,null);return this}},mo.event=null,mo.requote=function(n){return n.replace(jo,"\\$&")};var jo=/[\\\^\$\*\+\?\|\[\]\(\)\.\{\}]/g,Lo={}.__proto__?function(n,t){n.__proto__=t}:function(n,t){for(var e in t)n[e]=t[e]},Ho=function(n,t){return t.querySelector(n)},Fo=function(n,t){return t.querySelectorAll(n)},Po=bo[a(bo,"matchesSelector")],Oo=function(n,t){return Po.call(n,t)};"function"==typeof Sizzle&&(Ho=function(n,t){return Sizzle(n,t)[0]||null},Fo=function(n,t){return Sizzle.uniqueSort(Sizzle(n,t))},Oo=Sizzle.matchesSelector),mo.selection=function(){return Uo};var Ro=mo.selection.prototype=[];Ro.select=function(n){var t,e,r,u,i=[];n=d(n);for(var o=-1,a=this.length;++o<a;){i.push(t=[]),t.parentNode=(r=this[o]).parentNode;for(var c=-1,l=r.length;++c<l;)(u=r[c])?(t.push(e=n.call(u,u.__data__,c,o)),e&&"__data__"in u&&(e.__data__=u.__data__)):t.push(null)}return p(i)},Ro.selectAll=function(n){var t,e,r=[];n=v(n);for(var u=-1,i=this.length;++u<i;)for(var o=this[u],a=-1,c=o.length;++a<c;)(e=o[a])&&(r.push(t=Mo(n.call(e,e.__data__,a,u))),t.parentNode=e);return p(r)};var Yo={svg:"http://www.w3.org/2000/svg",xhtml:"http://www.w3.org/1999/xhtml",xlink:"http://www.w3.org/1999/xlink",xml:"http://www.w3.org/XML/1998/namespace",xmlns:"http://www.w3.org/2000/xmlns/"};mo.ns={prefix:Yo,qualify:function(n){var t=n.indexOf(":"),e=n;return t>=0&&(e=n.substring(0,t),n=n.substring(t+1)),Yo.hasOwnProperty(e)?{space:Yo[e],local:n}:n}},Ro.attr=function(n,t){if(arguments.length<2){if("string"==typeof n){var e=this.node();return n=mo.ns.qualify(n),n.local?e.getAttributeNS(n.space,n.local):e.getAttribute(n)}for(t in n)this.each(m(t,n[t]));return this}return this.each(m(n,t))},Ro.classed=function(n,t){if(arguments.length<2){if("string"==typeof n){var e=this.node(),r=(n=n.trim().split(/^|\s+/g)).length,u=-1;if(t=e.classList){for(;++u<r;)if(!t.contains(n[u]))return!1}else for(t=e.getAttribute("class");++u<r;)if(!M(n[u]).test(t))return!1;return!0}for(t in n)this.each(x(t,n[t]));return this}return this.each(x(n,t))},Ro.style=function(n,t,e){var r=arguments.length;if(3>r){if("string"!=typeof n){2>r&&(t="");for(e in n)this.each(_(e,n[e],t));return this}if(2>r)return _o.getComputedStyle(this.node(),null).getPropertyValue(n);e=""}return this.each(_(n,t,e))},Ro.property=function(n,t){if(arguments.length<2){if("string"==typeof n)return this.node()[n];for(t in n)this.each(w(t,n[t]));return this}return this.each(w(n,t))},Ro.text=function(n){return arguments.length?this.each("function"==typeof n?function(){var t=n.apply(this,arguments);this.textContent=null==t?"":t}:null==n?function(){this.textContent=""}:function(){this.textContent=n}):this.node().textContent},Ro.html=function(n){return arguments.length?this.each("function"==typeof n?function(){var t=n.apply(this,arguments);this.innerHTML=null==t?"":t}:null==n?function(){this.innerHTML=""}:function(){this.innerHTML=n}):this.node().innerHTML},Ro.append=function(n){return n=S(n),this.select(function(){return this.appendChild(n.apply(this,arguments))})},Ro.insert=function(n,t){return n=S(n),t=d(t),this.select(function(){return this.insertBefore(n.apply(this,arguments),t.apply(this,arguments))})},Ro.remove=function(){return this.each(function(){var n=this.parentNode;n&&n.removeChild(this)})},Ro.data=function(n,t){function e(n,e){var r,i,o,a=n.length,f=e.length,h=Math.min(a,f),g=new Array(f),p=new Array(f),d=new Array(a);if(t){var v,m=new u,y=new u,M=[];for(r=-1;++r<a;)v=t.call(i=n[r],i.__data__,r),m.has(v)?d[r]=i:m.set(v,i),M.push(v);for(r=-1;++r<f;)v=t.call(e,o=e[r],r),(i=m.get(v))?(g[r]=i,i.__data__=o):y.has(v)||(p[r]=E(o)),y.set(v,o),m.remove(v);for(r=-1;++r<a;)m.has(M[r])&&(d[r]=n[r])}else{for(r=-1;++r<h;)i=n[r],o=e[r],i?(i.__data__=o,g[r]=i):p[r]=E(o);for(;f>r;++r)p[r]=E(e[r]);for(;a>r;++r)d[r]=n[r]}p.update=g,p.parentNode=g.parentNode=d.parentNode=n.parentNode,c.push(p),l.push(g),s.push(d)}var r,i,o=-1,a=this.length;if(!arguments.length){for(n=new Array(a=(r=this[0]).length);++o<a;)(i=r[o])&&(n[o]=i.__data__);return n}var c=T([]),l=p([]),s=p([]);if("function"==typeof n)for(;++o<a;)e(r=this[o],n.call(r,r.parentNode.__data__,o));else for(;++o<a;)e(r=this[o],n);return l.enter=function(){return c},l.exit=function(){return s},l},Ro.datum=function(n){return arguments.length?this.property("__data__",n):this.property("__data__")},Ro.filter=function(n){var t,e,r,u=[];"function"!=typeof n&&(n=k(n));for(var i=0,o=this.length;o>i;i++){u.push(t=[]),t.parentNode=(e=this[i]).parentNode;for(var a=0,c=e.length;c>a;a++)(r=e[a])&&n.call(r,r.__data__,a)&&t.push(r)}return p(u)},Ro.order=function(){for(var n=-1,t=this.length;++n<t;)for(var e,r=this[n],u=r.length-1,i=r[u];--u>=0;)(e=r[u])&&(i&&i!==e.nextSibling&&i.parentNode.insertBefore(e,i),i=e);return this},Ro.sort=function(n){n=A.apply(this,arguments);for(var t=-1,e=this.length;++t<e;)this[t].sort(n);return this.order()},Ro.each=function(n){return N(this,function(t,e,r){n.call(t,t.__data__,e,r)})},Ro.call=function(n){var t=Mo(arguments);return n.apply(t[0]=this,t),this},Ro.empty=function(){return!this.node()},Ro.node=function(){for(var n=0,t=this.length;t>n;n++)for(var e=this[n],r=0,u=e.length;u>r;r++){var i=e[r];if(i)return i}return null},Ro.size=function(){var n=0;return this.each(function(){++n}),n};var Io=[];mo.selection.enter=T,mo.selection.enter.prototype=Io,Io.append=Ro.append,Io.empty=Ro.empty,Io.node=Ro.node,Io.call=Ro.call,Io.size=Ro.size,Io.select=function(n){for(var t,e,r,u,i,o=[],a=-1,c=this.length;++a<c;){r=(u=this[a]).update,o.push(t=[]),t.parentNode=u.parentNode;for(var l=-1,s=u.length;++l<s;)(i=u[l])?(t.push(r[l]=e=n.call(u.parentNode,i.__data__,l,a)),e.__data__=i.__data__):t.push(null)}return p(o)},Io.insert=function(n,t){return arguments.length<2&&(t=q(this)),Ro.insert.call(this,n,t)},Ro.transition=function(){for(var n,t,e=Pc||++Uc,r=[],u=Oc||{time:Date.now(),ease:Cr,delay:0,duration:250},i=-1,o=this.length;++i<o;){r.push(n=[]);for(var a=this[i],c=-1,l=a.length;++c<l;)(t=a[c])&&Ni(t,c,e,u),n.push(t)}return Ei(r,e)},Ro.interrupt=function(){return this.each(z)},mo.select=function(n){var t=["string"==typeof n?Ho(n,xo):n];return t.parentNode=bo,p([t])},mo.selectAll=function(n){var t=Mo("string"==typeof n?Fo(n,xo):n);return t.parentNode=bo,p([t])};var Uo=mo.select(bo);Ro.on=function(n,t,e){var r=arguments.length;if(3>r){if("string"!=typeof n){2>r&&(t=!1);for(e in n)this.each(C(e,n[e],t));return this}if(2>r)return(r=this.node()["__on"+n])&&r._;e=!1}return this.each(C(n,t,e))};var Zo=mo.map({mouseenter:"mouseover",mouseleave:"mouseout"});Zo.forEach(function(n){"on"+n in xo&&Zo.remove(n)});var Vo=a(bo.style,"userSelect"),Xo=0;mo.mouse=function(n){return H(n,h())};var $o=/WebKit/.test(_o.navigator.userAgent)?-1:0;mo.touches=function(n,t){return arguments.length<2&&(t=h().touches),t?Mo(t).map(function(t){var e=H(n,t);return e.identifier=t.identifier,e}):[]},mo.behavior.drag=function(){function n(){this.on("mousedown.drag",o).on("touchstart.drag",a)}function t(){return mo.event.changedTouches[0].identifier}function e(n,t){return mo.touches(n).filter(function(n){return n.identifier===t})[0]}function r(n,t,e,r){return function(){function o(){if(!s)return a();var n=t(s,g),e=n[0]-d[0],r=n[1]-d[1];v|=e|r,d=n,f({type:"drag",x:n[0]+c[0],y:n[1]+c[1],dx:e,dy:r})}function a(){m.on(e+"."+p,null).on(r+"."+p,null),y(v&&mo.event.target===h),f({type:"dragend"})}var c,l=this,s=l.parentNode,f=u.of(l,arguments),h=mo.event.target,g=n(),p=null==g?"drag":"drag-"+g,d=t(s,g),v=0,m=mo.select(_o).on(e+"."+p,o).on(r+"."+p,a),y=L();i?(c=i.apply(l,arguments),c=[c.x-d[0],c.y-d[1]]):c=[0,0],f({type:"dragstart"})}}var u=g(n,"drag","dragstart","dragend"),i=null,o=r(c,mo.mouse,"mousemove","mouseup"),a=r(t,e,"touchmove","touchend");return n.origin=function(t){return arguments.length?(i=t,n):i},mo.rebind(n,u,"on")};var Bo=Math.PI,Wo=1e-6,Jo=Wo*Wo,Go=Bo/180,Ko=180/Bo,Qo=Math.SQRT2,na=2,ta=4;mo.interpolateZoom=function(n,t){function e(n){var t=n*y;if(m){var e=Y(d),o=i/(na*h)*(e*I(Qo*t+d)-R(d));return[r+o*l,u+o*s,i*e/Y(Qo*t+d)]}return[r+n*l,u+n*s,i*Math.exp(Qo*t)]}var r=n[0],u=n[1],i=n[2],o=t[0],a=t[1],c=t[2],l=o-r,s=a-u,f=l*l+s*s,h=Math.sqrt(f),g=(c*c-i*i+ta*f)/(2*i*na*h),p=(c*c-i*i-ta*f)/(2*c*na*h),d=Math.log(Math.sqrt(g*g+1)-g),v=Math.log(Math.sqrt(p*p+1)-p),m=v-d,y=(m||Math.log(c/i))/Qo;return e.duration=1e3*y,e},mo.behavior.zoom=function(){function n(n){n.on(A,l).on(ua+".zoom",h).on(N,p).on("dblclick.zoom",d).on(q,s)}function t(n){return[(n[0]-S.x)/S.k,(n[1]-S.y)/S.k]}function e(n){return[n[0]*S.k+S.x,n[1]*S.k+S.y]}function r(n){S.k=Math.max(k[0],Math.min(k[1],n))}function u(n,t){t=e(t),S.x+=n[0]-t[0],S.y+=n[1]-t[1]}function i(){b&&b.domain(x.range().map(function(n){return(n-S.x)/S.k}).map(x.invert)),w&&w.domain(_.range().map(function(n){return(n-S.y)/S.k}).map(_.invert))}function o(n){n({type:"zoomstart"})}function a(n){i(),n({type:"zoom",scale:S.k,translate:[S.x,S.y]})}function c(n){n({type:"zoomend"})}function l(){function n(){s=1,u(mo.mouse(r),h),a(i)}function e(){f.on(N,_o===r?p:null).on(T,null),g(s&&mo.event.target===l),c(i)}var r=this,i=C.of(r,arguments),l=mo.event.target,s=0,f=mo.select(_o).on(N,n).on(T,e),h=t(mo.mouse(r)),g=L();z.call(r),o(i)}function s(){function n(){var n=mo.touches(p);return g=S.k,n.forEach(function(n){n.identifier in v&&(v[n.identifier]=t(n))}),n}function e(){for(var t=mo.event.changedTouches,e=0,i=t.length;i>e;++e)v[t[e].identifier]=null;var o=n(),c=Date.now();if(1===o.length){if(500>c-M){var l=o[0],s=v[l.identifier];r(2*S.k),u(l,s),f(),a(d)}M=c}else if(o.length>1){var l=o[0],h=o[1],g=l[0]-h[0],p=l[1]-h[1];m=g*g+p*p}}function i(){for(var n,t,e,i,o=mo.touches(p),c=0,l=o.length;l>c;++c,i=null)if(e=o[c],i=v[e.identifier]){if(t)break;n=e,t=i}if(i){var s=(s=e[0]-n[0])*s+(s=e[1]-n[1])*s,f=m&&Math.sqrt(s/m);n=[(n[0]+e[0])/2,(n[1]+e[1])/2],t=[(t[0]+i[0])/2,(t[1]+i[1])/2],r(f*g)}M=null,u(n,t),a(d)}function h(){if(mo.event.touches.length){for(var t=mo.event.changedTouches,e=0,r=t.length;r>e;++e)delete v[t[e].identifier];for(var u in v)return void n()}_.on(x,null).on(b,null),w.on(A,l).on(q,s),E(),c(d)}var g,p=this,d=C.of(p,arguments),v={},m=0,y=mo.event.changedTouches[0].identifier,x="touchmove.zoom-"+y,b="touchend.zoom-"+y,_=mo.select(_o).on(x,i).on(b,h),w=mo.select(p).on(A,null).on(q,e),E=L();z.call(p),e(),o(d)}function h(){var n=C.of(this,arguments);y?clearTimeout(y):(z.call(this),o(n)),y=setTimeout(function(){y=null,c(n)},50),f();var e=m||mo.mouse(this);v||(v=t(e)),r(Math.pow(2,.002*ea())*S.k),u(e,v),a(n)}function p(){v=null}function d(){var n=C.of(this,arguments),e=mo.mouse(this),i=t(e),l=Math.log(S.k)/Math.LN2;o(n),r(Math.pow(2,mo.event.shiftKey?Math.ceil(l)-1:Math.floor(l)+1)),u(e,i),a(n),c(n)}var v,m,y,M,x,b,_,w,S={x:0,y:0,k:1},E=[960,500],k=ra,A="mousedown.zoom",N="mousemove.zoom",T="mouseup.zoom",q="touchstart.zoom",C=g(n,"zoomstart","zoom","zoomend");return n.event=function(n){n.each(function(){var n=C.of(this,arguments),t=S;Pc?mo.select(this).transition().each("start.zoom",function(){S=this.__chart__||{x:0,y:0,k:1},o(n)}).tween("zoom:zoom",function(){var e=E[0],r=E[1],u=e/2,i=r/2,o=mo.interpolateZoom([(u-S.x)/S.k,(i-S.y)/S.k,e/S.k],[(u-t.x)/t.k,(i-t.y)/t.k,e/t.k]);return function(t){var r=o(t),c=e/r[2];this.__chart__=S={x:u-r[0]*c,y:i-r[1]*c,k:c},a(n)}}).each("end.zoom",function(){c(n)}):(this.__chart__=S,o(n),a(n),c(n))})},n.translate=function(t){return arguments.length?(S={x:+t[0],y:+t[1],k:S.k},i(),n):[S.x,S.y]},n.scale=function(t){return arguments.length?(S={x:S.x,y:S.y,k:+t},i(),n):S.k},n.scaleExtent=function(t){return arguments.length?(k=null==t?ra:[+t[0],+t[1]],n):k},n.center=function(t){return arguments.length?(m=t&&[+t[0],+t[1]],n):m},n.size=function(t){return arguments.length?(E=t&&[+t[0],+t[1]],n):E},n.x=function(t){return arguments.length?(b=t,x=t.copy(),S={x:0,y:0,k:1},n):b},n.y=function(t){return arguments.length?(w=t,_=t.copy(),S={x:0,y:0,k:1},n):w},mo.rebind(n,C,"on")};var ea,ra=[0,1/0],ua="onwheel"in xo?(ea=function(){return-mo.event.deltaY*(mo.event.deltaMode?120:1)},"wheel"):"onmousewheel"in xo?(ea=function(){return mo.event.wheelDelta},"mousewheel"):(ea=function(){return-mo.event.detail},"MozMousePixelScroll");Z.prototype.toString=function(){return this.rgb()+""},mo.hsl=function(n,t,e){return 1===arguments.length?n instanceof X?V(n.h,n.s,n.l):lt(""+n,st,V):V(+n,+t,+e)};var ia=X.prototype=new Z;ia.brighter=function(n){return n=Math.pow(.7,arguments.length?n:1),V(this.h,this.s,this.l/n)},ia.darker=function(n){return n=Math.pow(.7,arguments.length?n:1),V(this.h,this.s,n*this.l)},ia.rgb=function(){return $(this.h,this.s,this.l)},mo.hcl=function(n,t,e){return 1===arguments.length?n instanceof W?B(n.h,n.c,n.l):n instanceof K?nt(n.l,n.a,n.b):nt((n=ft((n=mo.rgb(n)).r,n.g,n.b)).l,n.a,n.b):B(+n,+t,+e)};var oa=W.prototype=new Z;oa.brighter=function(n){return B(this.h,this.c,Math.min(100,this.l+aa*(arguments.length?n:1)))},oa.darker=function(n){return B(this.h,this.c,Math.max(0,this.l-aa*(arguments.length?n:1)))},oa.rgb=function(){return J(this.h,this.c,this.l).rgb()},mo.lab=function(n,t,e){return 1===arguments.length?n instanceof K?G(n.l,n.a,n.b):n instanceof W?J(n.l,n.c,n.h):ft((n=mo.rgb(n)).r,n.g,n.b):G(+n,+t,+e)};var aa=18,ca=.95047,la=1,sa=1.08883,fa=K.prototype=new Z;fa.brighter=function(n){return G(Math.min(100,this.l+aa*(arguments.length?n:1)),this.a,this.b)},fa.darker=function(n){return G(Math.max(0,this.l-aa*(arguments.length?n:1)),this.a,this.b)},fa.rgb=function(){return Q(this.l,this.a,this.b)},mo.rgb=function(n,t,e){return 1===arguments.length?n instanceof at?ot(n.r,n.g,n.b):lt(""+n,ot,$):ot(~~n,~~t,~~e)};var ha=at.prototype=new Z;ha.brighter=function(n){n=Math.pow(.7,arguments.length?n:1);var t=this.r,e=this.g,r=this.b,u=30;return t||e||r?(t&&u>t&&(t=u),e&&u>e&&(e=u),r&&u>r&&(r=u),ot(Math.min(255,~~(t/n)),Math.min(255,~~(e/n)),Math.min(255,~~(r/n)))):ot(u,u,u)},ha.darker=function(n){return n=Math.pow(.7,arguments.length?n:1),ot(~~(n*this.r),~~(n*this.g),~~(n*this.b))},ha.hsl=function(){return st(this.r,this.g,this.b)},ha.toString=function(){return"#"+ct(this.r)+ct(this.g)+ct(this.b)};var ga=mo.map({aliceblue:15792383,antiquewhite:16444375,aqua:65535,aquamarine:8388564,azure:15794175,beige:16119260,bisque:16770244,black:0,blanchedalmond:16772045,blue:255,blueviolet:9055202,brown:10824234,burlywood:14596231,cadetblue:6266528,chartreuse:8388352,chocolate:13789470,coral:16744272,cornflowerblue:6591981,cornsilk:16775388,crimson:14423100,cyan:65535,darkblue:139,darkcyan:35723,darkgoldenrod:12092939,darkgray:11119017,darkgreen:25600,darkgrey:11119017,darkkhaki:12433259,darkmagenta:9109643,darkolivegreen:5597999,darkorange:16747520,darkorchid:10040012,darkred:9109504,darksalmon:15308410,darkseagreen:9419919,darkslateblue:4734347,darkslategray:3100495,darkslategrey:3100495,darkturquoise:52945,darkviolet:9699539,deeppink:16716947,deepskyblue:49151,dimgray:6908265,dimgrey:6908265,dodgerblue:2003199,firebrick:11674146,floralwhite:16775920,forestgreen:2263842,fuchsia:16711935,gainsboro:14474460,ghostwhite:16316671,gold:16766720,goldenrod:14329120,gray:8421504,green:32768,greenyellow:11403055,grey:8421504,honeydew:15794160,hotpink:16738740,indianred:13458524,indigo:4915330,ivory:16777200,khaki:15787660,lavender:15132410,lavenderblush:16773365,lawngreen:8190976,lemonchiffon:16775885,lightblue:11393254,lightcoral:15761536,lightcyan:14745599,lightgoldenrodyellow:16448210,lightgray:13882323,lightgreen:9498256,lightgrey:13882323,lightpink:16758465,lightsalmon:16752762,lightseagreen:2142890,lightskyblue:8900346,lightslategray:7833753,lightslategrey:7833753,lightsteelblue:11584734,lightyellow:16777184,lime:65280,limegreen:3329330,linen:16445670,magenta:16711935,maroon:8388608,mediumaquamarine:6737322,mediumblue:205,mediumorchid:12211667,mediumpurple:9662683,mediumseagreen:3978097,mediumslateblue:8087790,mediumspringgreen:64154,mediumturquoise:4772300,mediumvioletred:13047173,midnightblue:1644912,mintcream:16121850,mistyrose:16770273,moccasin:16770229,navajowhite:16768685,navy:128,oldlace:16643558,olive:8421376,olivedrab:7048739,orange:16753920,orangered:16729344,orchid:14315734,palegoldenrod:15657130,palegreen:10025880,paleturquoise:11529966,palevioletred:14381203,papayawhip:16773077,peachpuff:16767673,peru:13468991,pink:16761035,plum:14524637,powderblue:11591910,purple:8388736,red:16711680,rosybrown:12357519,royalblue:4286945,saddlebrown:9127187,salmon:16416882,sandybrown:16032864,seagreen:3050327,seashell:16774638,sienna:10506797,silver:12632256,skyblue:8900331,slateblue:6970061,slategray:7372944,slategrey:7372944,snow:16775930,springgreen:65407,steelblue:4620980,tan:13808780,teal:32896,thistle:14204888,tomato:16737095,turquoise:4251856,violet:15631086,wheat:16113331,white:16777215,whitesmoke:16119285,yellow:16776960,yellowgreen:10145074});ga.forEach(function(n,t){ga.set(n,ut(t))}),mo.functor=pt,mo.xhr=vt(dt),mo.dsv=function(n,t){function e(n,e,i){arguments.length<3&&(i=e,e=null);var o=mo.xhr(n,t,i);return o.row=function(n){return arguments.length?o.response(null==(e=n)?r:u(n)):e},o.row(e)}function r(n){return e.parse(n.responseText)}function u(n){return function(t){return e.parse(t.responseText,n)}}function o(t){return t.map(a).join(n)}function a(n){return c.test(n)?'"'+n.replace(/\"/g,'""')+'"':n}var c=new RegExp('["'+n+"\n]"),l=n.charCodeAt(0);return e.parse=function(n,t){var r;return e.parseRows(n,function(n,e){if(r)return r(n,e-1);var u=new Function("d","return {"+n.map(function(n,t){return JSON.stringify(n)+": d["+t+"]"}).join(",")+"}");r=t?function(n,e){return t(u(n),e)}:u})},e.parseRows=function(n,t){function e(){if(s>=c)return o;if(u)return u=!1,i;var t=s;if(34===n.charCodeAt(t)){for(var e=t;e++<c;)if(34===n.charCodeAt(e)){if(34!==n.charCodeAt(e+1))break;++e}s=e+2;var r=n.charCodeAt(e+1);return 13===r?(u=!0,10===n.charCodeAt(e+2)&&++s):10===r&&(u=!0),n.substring(t+1,e).replace(/""/g,'"')}for(;c>s;){var r=n.charCodeAt(s++),a=1;if(10===r)u=!0;else if(13===r)u=!0,10===n.charCodeAt(s)&&(++s,++a);else if(r!==l)continue;return n.substring(t,s-a)}return n.substring(t)}for(var r,u,i={},o={},a=[],c=n.length,s=0,f=0;(r=e())!==o;){for(var h=[];r!==i&&r!==o;)h.push(r),r=e();(!t||(h=t(h,f++)))&&a.push(h)}return a},e.format=function(t){if(Array.isArray(t[0]))return e.formatRows(t);var r=new i,u=[];return t.forEach(function(n){for(var t in n)r.has(t)||u.push(r.add(t))}),[u.map(a).join(n)].concat(t.map(function(t){return u.map(function(n){return a(t[n])}).join(n)})).join("\n")},e.formatRows=function(n){return n.map(o).join("\n")},e},mo.csv=mo.dsv(",","text/csv"),mo.tsv=mo.dsv(" ","text/tab-separated-values");var pa,da,va,ma,ya,Ma=_o[a(_o,"requestAnimationFrame")]||function(n){setTimeout(n,17)};mo.timer=function(n,t,e){var r=arguments.length;2>r&&(t=0),3>r&&(e=Date.now());var u=e+t,i={callback:n,time:u,next:null};da?da.next=i:pa=i,da=i,va||(ma=clearTimeout(ma),va=1,Ma(Mt))},mo.timer.flush=function(){bt(),_t()};var xa=".",ba=",",_a=[3,3],wa="$",Sa=["y","z","a","f","p","n","\xb5","m","","k","M","G","T","P","E","Z","Y"].map(wt);mo.formatPrefix=function(n,t){var e=0;return n&&(0>n&&(n*=-1),t&&(n=mo.round(n,St(n,t))),e=1+Math.floor(1e-12+Math.log(n)/Math.LN10),e=Math.max(-24,Math.min(24,3*Math.floor((0>=e?e+1:e-1)/3)))),Sa[8+e/3]},mo.round=function(n,t){return t?Math.round(n*(t=Math.pow(10,t)))/t:Math.round(n)},mo.format=function(n){var t=Ea.exec(n),e=t[1]||" ",r=t[2]||">",u=t[3]||"",i=t[4]||"",o=t[5],a=+t[6],c=t[7],l=t[8],s=t[9],f=1,h="",g=!1;switch(l&&(l=+l.substring(1)),(o||"0"===e&&"="===r)&&(o=e="0",r="=",c&&(a-=Math.floor((a-1)/4))),s){case"n":c=!0,s="g";break;case"%":f=100,h="%",s="f";break;case"p":f=100,h="%",s="r";break;case"b":case"o":case"x":case"X":"#"===i&&(i="0"+s.toLowerCase());case"c":case"d":g=!0,l=0;break;case"s":f=-1,s="r"}"#"===i?i="":"$"===i&&(i=wa),"r"!=s||l||(s="g"),null!=l&&("g"==s?l=Math.max(1,Math.min(21,l)):("e"==s||"f"==s)&&(l=Math.max(0,Math.min(20,l)))),s=ka.get(s)||Et;var p=o&&c;return function(n){if(g&&n%1)return"";var t=0>n||0===n&&0>1/n?(n=-n,"-"):u;if(0>f){var d=mo.formatPrefix(n,l);n=d.scale(n),h=d.symbol}else n*=f;n=s(n,l);var v=n.lastIndexOf("."),m=0>v?n:n.substring(0,v),y=0>v?"":xa+n.substring(v+1);!o&&c&&(m=Aa(m));var M=i.length+m.length+y.length+(p?0:t.length),x=a>M?new Array(M=a-M+1).join(e):"";return p&&(m=Aa(x+m)),t+=i,n=m+y,("<"===r?t+n+x:">"===r?x+t+n:"^"===r?x.substring(0,M>>=1)+t+n+x.substring(M):t+(p?n:x+n))+h}};var Ea=/(?:([^{])?([<>=^]))?([+\- ])?([$#])?(0)?(\d+)?(,)?(\.-?\d+)?([a-z%])?/i,ka=mo.map({b:function(n){return n.toString(2)},c:function(n){return String.fromCharCode(n)},o:function(n){return n.toString(8)},x:function(n){return n.toString(16)},X:function(n){return n.toString(16).toUpperCase()},g:function(n,t){return n.toPrecision(t)},e:function(n,t){return n.toExponential(t)},f:function(n,t){return n.toFixed(t)},r:function(n,t){return(n=mo.round(n,St(n,t))).toFixed(Math.max(0,Math.min(20,St(n*(1+1e-15),t))))}}),Aa=dt;if(_a){var Na=_a.length;Aa=function(n){for(var t=n.length,e=[],r=0,u=_a[0];t>0&&u>0;)e.push(n.substring(t-=u,t+u)),u=_a[r=(r+1)%Na];return e.reverse().join(ba)}}mo.geo={},kt.prototype={s:0,t:0,add:function(n){At(n,this.t,Ta),At(Ta.s,this.s,this),this.s?this.t+=Ta.t:this.s=Ta.t},reset:function(){this.s=this.t=0},valueOf:function(){return this.s}};var Ta=new kt;mo.geo.stream=function(n,t){n&&qa.hasOwnProperty(n.type)?qa[n.type](n,t):Nt(n,t)};var qa={Feature:function(n,t){Nt(n.geometry,t)},FeatureCollection:function(n,t){for(var e=n.features,r=-1,u=e.length;++r<u;)Nt(e[r].geometry,t)}},za={Sphere:function(n,t){t.sphere()},Point:function(n,t){n=n.coordinates,t.point(n[0],n[1],n[2])},MultiPoint:function(n,t){for(var e=n.coordinates,r=-1,u=e.length;++r<u;)n=e[r],t.point(n[0],n[1],n[2])},LineString:function(n,t){Tt(n.coordinates,t,0)},MultiLineString:function(n,t){for(var e=n.coordinates,r=-1,u=e.length;++r<u;)Tt(e[r],t,0)},Polygon:function(n,t){qt(n.coordinates,t)},MultiPolygon:function(n,t){for(var e=n.coordinates,r=-1,u=e.length;++r<u;)qt(e[r],t)},GeometryCollection:function(n,t){for(var e=n.geometries,r=-1,u=e.length;++r<u;)Nt(e[r],t)}};mo.geo.area=function(n){return Ca=0,mo.geo.stream(n,ja),Ca};var Ca,Da=new kt,ja={sphere:function(){Ca+=4*Bo},point:c,lineStart:c,lineEnd:c,polygonStart:function(){Da.reset(),ja.lineStart=zt},polygonEnd:function(){var n=2*Da;Ca+=0>n?4*Bo+n:n,ja.lineStart=ja.lineEnd=ja.point=c}};mo.geo.bounds=function(){function n(n,t){M.push(x=[s=n,h=n]),f>t&&(f=t),t>g&&(g=t)}function t(t,e){var r=Ct([t*Go,e*Go]);if(m){var u=jt(m,r),i=[u[1],-u[0],0],o=jt(i,u);Ft(o),o=Pt(o);var c=t-p,l=c>0?1:-1,d=o[0]*Ko*l,v=Math.abs(c)>180;if(v^(d>l*p&&l*t>d)){var y=o[1]*Ko;y>g&&(g=y)}else if(d=(d+360)%360-180,v^(d>l*p&&l*t>d)){var y=-o[1]*Ko;f>y&&(f=y)}else f>e&&(f=e),e>g&&(g=e);v?p>t?a(s,t)>a(s,h)&&(h=t):a(t,h)>a(s,h)&&(s=t):h>=s?(s>t&&(s=t),t>h&&(h=t)):t>p?a(s,t)>a(s,h)&&(h=t):a(t,h)>a(s,h)&&(s=t)}else n(t,e);m=r,p=t}function e(){b.point=t}function r(){x[0]=s,x[1]=h,b.point=n,m=null}function u(n,e){if(m){var r=n-p;y+=Math.abs(r)>180?r+(r>0?360:-360):r}else d=n,v=e;ja.point(n,e),t(n,e)}function i(){ja.lineStart()}function o(){u(d,v),ja.lineEnd(),Math.abs(y)>Wo&&(s=-(h=180)),x[0]=s,x[1]=h,m=null}function a(n,t){return(t-=n)<0?t+360:t}function c(n,t){return n[0]-t[0]}function l(n,t){return t[0]<=t[1]?t[0]<=n&&n<=t[1]:n<t[0]||t[1]<n}var s,f,h,g,p,d,v,m,y,M,x,b={point:n,lineStart:e,lineEnd:r,polygonStart:function(){b.point=u,b.lineStart=i,b.lineEnd=o,y=0,ja.polygonStart()},polygonEnd:function(){ja.polygonEnd(),b.point=n,b.lineStart=e,b.lineEnd=r,0>Da?(s=-(h=180),f=-(g=90)):y>Wo?g=90:-Wo>y&&(f=-90),x[0]=s,x[1]=h}};return function(n){g=h=-(s=f=1/0),M=[],mo.geo.stream(n,b);var t=M.length;if(t){M.sort(c);for(var e,r=1,u=M[0],i=[u];t>r;++r)e=M[r],l(e[0],u)||l(e[1],u)?(a(u[0],e[1])>a(u[0],u[1])&&(u[1]=e[1]),a(e[0],u[1])>a(u[0],u[1])&&(u[0]=e[0])):i.push(u=e);for(var o,e,p=-1/0,t=i.length-1,r=0,u=i[t];t>=r;u=e,++r)e=i[r],(o=a(u[1],e[0]))>p&&(p=o,s=e[0],h=u[1])}return M=x=null,1/0===s||1/0===f?[[0/0,0/0],[0/0,0/0]]:[[s,f],[h,g]]}}(),mo.geo.centroid=function(n){La=Ha=Fa=Pa=Oa=Ra=Ya=Ia=Ua=Za=Va=0,mo.geo.stream(n,Xa);var t=Ua,e=Za,r=Va,u=t*t+e*e+r*r;return Jo>u&&(t=Ra,e=Ya,r=Ia,Wo>Ha&&(t=Fa,e=Pa,r=Oa),u=t*t+e*e+r*r,Jo>u)?[0/0,0/0]:[Math.atan2(e,t)*Ko,O(r/Math.sqrt(u))*Ko]};var La,Ha,Fa,Pa,Oa,Ra,Ya,Ia,Ua,Za,Va,Xa={sphere:c,point:Rt,lineStart:It,lineEnd:Ut,polygonStart:function(){Xa.lineStart=Zt},polygonEnd:function(){Xa.lineStart=It}},$a=Bt(Vt,Qt,te,ee),Ba=[-Bo,0],Wa=1e9;mo.geo.clipExtent=function(){var n,t,e,r,u,i,o={stream:function(n){return u&&(u.valid=!1),u=i(n),u.valid=!0,u},extent:function(a){return arguments.length?(i=ue(n=+a[0][0],t=+a[0][1],e=+a[1][0],r=+a[1][1]),u&&(u.valid=!1,u=null),o):[[n,t],[e,r]]}};return o.extent([[0,0],[960,500]])},(mo.geo.conicEqualArea=function(){return ae(ce)}).raw=ce,mo.geo.albers=function(){return mo.geo.conicEqualArea().rotate([96,0]).center([-.6,38.7]).parallels([29.5,45.5]).scale(1070)},mo.geo.albersUsa=function(){function n(n){var i=n[0],o=n[1];return t=null,e(i,o),t||(r(i,o),t)||u(i,o),t}var t,e,r,u,i=mo.geo.albers(),o=mo.geo.conicEqualArea().rotate([154,0]).center([-2,58.5]).parallels([55,65]),a=mo.geo.conicEqualArea().rotate([157,0]).center([-3,19.9]).parallels([8,18]),c={point:function(n,e){t=[n,e]}};return n.invert=function(n){var t=i.scale(),e=i.translate(),r=(n[0]-e[0])/t,u=(n[1]-e[1])/t;return(u>=.12&&.234>u&&r>=-.425&&-.214>r?o:u>=.166&&.234>u&&r>=-.214&&-.115>r?a:i).invert(n)},n.stream=function(n){var t=i.stream(n),e=o.stream(n),r=a.stream(n);return{point:function(n,u){t.point(n,u),e.point(n,u),r.point(n,u)},sphere:function(){t.sphere(),e.sphere(),r.sphere()},lineStart:function(){t.lineStart(),e.lineStart(),r.lineStart()},lineEnd:function(){t.lineEnd(),e.lineEnd(),r.lineEnd()},polygonStart:function(){t.polygonStart(),e.polygonStart(),r.polygonStart()},polygonEnd:function(){t.polygonEnd(),e.polygonEnd(),r.polygonEnd()}}},n.precision=function(t){return arguments.length?(i.precision(t),o.precision(t),a.precision(t),n):i.precision()},n.scale=function(t){return arguments.length?(i.scale(t),o.scale(.35*t),a.scale(t),n.translate(i.translate())):i.scale()},n.translate=function(t){if(!arguments.length)return i.translate();var l=i.scale(),s=+t[0],f=+t[1];return e=i.translate(t).clipExtent([[s-.455*l,f-.238*l],[s+.455*l,f+.238*l]]).stream(c).point,r=o.translate([s-.307*l,f+.201*l]).clipExtent([[s-.425*l+Wo,f+.12*l+Wo],[s-.214*l-Wo,f+.234*l-Wo]]).stream(c).point,u=a.translate([s-.205*l,f+.212*l]).clipExtent([[s-.214*l+Wo,f+.166*l+Wo],[s-.115*l-Wo,f+.234*l-Wo]]).stream(c).point,n},n.scale(1070)};var Ja,Ga,Ka,Qa,nc,tc,ec={point:c,lineStart:c,lineEnd:c,polygonStart:function(){Ga=0,ec.lineStart=le},polygonEnd:function(){ec.lineStart=ec.lineEnd=ec.point=c,Ja+=Math.abs(Ga/2)}},rc={point:se,lineStart:c,lineEnd:c,polygonStart:c,polygonEnd:c},uc={point:ge,lineStart:pe,lineEnd:de,polygonStart:function(){uc.lineStart=ve},polygonEnd:function(){uc.point=ge,uc.lineStart=pe,uc.lineEnd=de}};mo.geo.transform=function(n){return{stream:function(t){var e=new Me(t);for(var r in n)e[r]=n[r];return e}}},Me.prototype={point:function(n,t){this.stream.point(n,t)},sphere:function(){this.stream.sphere()},lineStart:function(){this.stream.lineStart()},lineEnd:function(){this.stream.lineEnd()},polygonStart:function(){this.stream.polygonStart()},polygonEnd:function(){this.stream.polygonEnd()}},mo.geo.path=function(){function n(n){return n&&("function"==typeof a&&i.pointRadius(+a.apply(this,arguments)),o&&o.valid||(o=u(i)),mo.geo.stream(n,o)),i.result()}function t(){return o=null,n}var e,r,u,i,o,a=4.5;return n.area=function(n){return Ja=0,mo.geo.stream(n,u(ec)),Ja},n.centroid=function(n){return Fa=Pa=Oa=Ra=Ya=Ia=Ua=Za=Va=0,mo.geo.stream(n,u(uc)),Va?[Ua/Va,Za/Va]:Ia?[Ra/Ia,Ya/Ia]:Oa?[Fa/Oa,Pa/Oa]:[0/0,0/0]},n.bounds=function(n){return nc=tc=-(Ka=Qa=1/0),mo.geo.stream(n,u(rc)),[[Ka,Qa],[nc,tc]]},n.projection=function(n){return arguments.length?(u=(e=n)?n.stream||xe(n):dt,t()):e},n.context=function(n){return arguments.length?(i=null==(r=n)?new fe:new me(n),"function"!=typeof a&&i.pointRadius(a),t()):r},n.pointRadius=function(t){return arguments.length?(a="function"==typeof t?t:(i.pointRadius(+t),+t),n):a},n.projection(mo.geo.albersUsa()).context(null)},mo.geo.projection=be,mo.geo.projectionMutator=_e,(mo.geo.equirectangular=function(){return be(Se)}).raw=Se.invert=Se,mo.geo.rotation=function(n){function t(t){return t=n(t[0]*Go,t[1]*Go),t[0]*=Ko,t[1]*=Ko,t}return n=Ee(n[0]%360*Go,n[1]*Go,n.length>2?n[2]*Go:0),t.invert=function(t){return t=n.invert(t[0]*Go,t[1]*Go),t[0]*=Ko,t[1]*=Ko,t},t},mo.geo.circle=function(){function n(){var n="function"==typeof r?r.apply(this,arguments):r,t=Ee(-n[0]*Go,-n[1]*Go,0).invert,u=[];return e(null,null,1,{point:function(n,e){u.push(n=t(n,e)),n[0]*=Ko,n[1]*=Ko}}),{type:"Polygon",coordinates:[u]}}var t,e,r=[0,0],u=6;return n.origin=function(t){return arguments.length?(r=t,n):r},n.angle=function(r){return arguments.length?(e=Te((t=+r)*Go,u*Go),n):t},n.precision=function(r){return arguments.length?(e=Te(t*Go,(u=+r)*Go),n):u},n.angle(90)},mo.geo.distance=function(n,t){var e,r=(t[0]-n[0])*Go,u=n[1]*Go,i=t[1]*Go,o=Math.sin(r),a=Math.cos(r),c=Math.sin(u),l=Math.cos(u),s=Math.sin(i),f=Math.cos(i);return Math.atan2(Math.sqrt((e=f*o)*e+(e=l*s-c*f*a)*e),c*s+l*f*a)},mo.geo.graticule=function(){function n(){return{type:"MultiLineString",coordinates:t()}}function t(){return mo.range(Math.ceil(i/v)*v,u,v).map(h).concat(mo.range(Math.ceil(l/m)*m,c,m).map(g)).concat(mo.range(Math.ceil(r/p)*p,e,p).filter(function(n){return Math.abs(n%v)>Wo
      }).map(s)).concat(mo.range(Math.ceil(a/d)*d,o,d).filter(function(n){return Math.abs(n%m)>Wo}).map(f))}var e,r,u,i,o,a,c,l,s,f,h,g,p=10,d=p,v=90,m=360,y=2.5;return n.lines=function(){return t().map(function(n){return{type:"LineString",coordinates:n}})},n.outline=function(){return{type:"Polygon",coordinates:[h(i).concat(g(c).slice(1),h(u).reverse().slice(1),g(l).reverse().slice(1))]}},n.extent=function(t){return arguments.length?n.majorExtent(t).minorExtent(t):n.minorExtent()},n.majorExtent=function(t){return arguments.length?(i=+t[0][0],u=+t[1][0],l=+t[0][1],c=+t[1][1],i>u&&(t=i,i=u,u=t),l>c&&(t=l,l=c,c=t),n.precision(y)):[[i,l],[u,c]]},n.minorExtent=function(t){return arguments.length?(r=+t[0][0],e=+t[1][0],a=+t[0][1],o=+t[1][1],r>e&&(t=r,r=e,e=t),a>o&&(t=a,a=o,o=t),n.precision(y)):[[r,a],[e,o]]},n.step=function(t){return arguments.length?n.majorStep(t).minorStep(t):n.minorStep()},n.majorStep=function(t){return arguments.length?(v=+t[0],m=+t[1],n):[v,m]},n.minorStep=function(t){return arguments.length?(p=+t[0],d=+t[1],n):[p,d]},n.precision=function(t){return arguments.length?(y=+t,s=ze(a,o,90),f=Ce(r,e,y),h=ze(l,c,90),g=Ce(i,u,y),n):y},n.majorExtent([[-180,-90+Wo],[180,90-Wo]]).minorExtent([[-180,-80-Wo],[180,80+Wo]])},mo.geo.greatArc=function(){function n(){return{type:"LineString",coordinates:[t||r.apply(this,arguments),e||u.apply(this,arguments)]}}var t,e,r=De,u=je;return n.distance=function(){return mo.geo.distance(t||r.apply(this,arguments),e||u.apply(this,arguments))},n.source=function(e){return arguments.length?(r=e,t="function"==typeof e?null:e,n):r},n.target=function(t){return arguments.length?(u=t,e="function"==typeof t?null:t,n):u},n.precision=function(){return arguments.length?n:0},n},mo.geo.interpolate=function(n,t){return Le(n[0]*Go,n[1]*Go,t[0]*Go,t[1]*Go)},mo.geo.length=function(n){return ic=0,mo.geo.stream(n,oc),ic};var ic,oc={sphere:c,point:c,lineStart:He,lineEnd:c,polygonStart:c,polygonEnd:c},ac=Fe(function(n){return Math.sqrt(2/(1+n))},function(n){return 2*Math.asin(n/2)});(mo.geo.azimuthalEqualArea=function(){return be(ac)}).raw=ac;var cc=Fe(function(n){var t=Math.acos(n);return t&&t/Math.sin(t)},dt);(mo.geo.azimuthalEquidistant=function(){return be(cc)}).raw=cc,(mo.geo.conicConformal=function(){return ae(Pe)}).raw=Pe,(mo.geo.conicEquidistant=function(){return ae(Oe)}).raw=Oe;var lc=Fe(function(n){return 1/n},Math.atan);(mo.geo.gnomonic=function(){return be(lc)}).raw=lc,Re.invert=function(n,t){return[n,2*Math.atan(Math.exp(t))-Bo/2]},(mo.geo.mercator=function(){return Ye(Re)}).raw=Re;var sc=Fe(function(){return 1},Math.asin);(mo.geo.orthographic=function(){return be(sc)}).raw=sc;var fc=Fe(function(n){return 1/(1+n)},function(n){return 2*Math.atan(n)});(mo.geo.stereographic=function(){return be(fc)}).raw=fc,Ie.invert=function(n,t){return[Math.atan2(R(n),Math.cos(t)),O(Math.sin(t)/Y(n))]},(mo.geo.transverseMercator=function(){return Ye(Ie)}).raw=Ie,mo.geom={},mo.svg={},mo.svg.line=function(){return Ue(dt)};var hc=mo.map({linear:Xe,"linear-closed":$e,step:Be,"step-before":We,"step-after":Je,basis:er,"basis-open":rr,"basis-closed":ur,bundle:ir,cardinal:Qe,"cardinal-open":Ge,"cardinal-closed":Ke,monotone:fr});hc.forEach(function(n,t){t.key=n,t.closed=/-closed$/.test(n)});var gc=[0,2/3,1/3,0],pc=[0,1/3,2/3,0],dc=[0,1/6,2/3,1/6];mo.geom.hull=function(n){function t(n){if(n.length<3)return[];var t,u,i,o,a,c,l,s,f,h,g,p,d=pt(e),v=pt(r),m=n.length,y=m-1,M=[],x=[],b=0;if(d===Ze&&r===Ve)t=n;else for(i=0,t=[];m>i;++i)t.push([+d.call(this,u=n[i],i),+v.call(this,u,i)]);for(i=1;m>i;++i)(t[i][1]<t[b][1]||t[i][1]==t[b][1]&&t[i][0]<t[b][0])&&(b=i);for(i=0;m>i;++i)i!==b&&(c=t[i][1]-t[b][1],a=t[i][0]-t[b][0],M.push({angle:Math.atan2(c,a),index:i}));for(M.sort(function(n,t){return n.angle-t.angle}),g=M[0].angle,h=M[0].index,f=0,i=1;y>i;++i){if(o=M[i].index,g==M[i].angle){if(a=t[h][0]-t[b][0],c=t[h][1]-t[b][1],l=t[o][0]-t[b][0],s=t[o][1]-t[b][1],a*a+c*c>=l*l+s*s){M[i].index=-1;continue}M[f].index=-1}g=M[i].angle,f=i,h=o}for(x.push(b),i=0,o=0;2>i;++o)M[o].index>-1&&(x.push(M[o].index),i++);for(p=x.length;y>o;++o)if(!(M[o].index<0)){for(;!hr(x[p-2],x[p-1],M[o].index,t);)--p;x[p++]=M[o].index}var _=[];for(i=p-1;i>=0;--i)_.push(n[x[i]]);return _}var e=Ze,r=Ve;return arguments.length?t(n):(t.x=function(n){return arguments.length?(e=n,t):e},t.y=function(n){return arguments.length?(r=n,t):r},t)},mo.geom.polygon=function(n){return Lo(n,vc),n};var vc=mo.geom.polygon.prototype=[];vc.area=function(){for(var n,t=-1,e=this.length,r=this[e-1],u=0;++t<e;)n=r,r=this[t],u+=n[1]*r[0]-n[0]*r[1];return.5*u},vc.centroid=function(n){var t,e,r=-1,u=this.length,i=0,o=0,a=this[u-1];for(arguments.length||(n=-1/(6*this.area()));++r<u;)t=a,a=this[r],e=t[0]*a[1]-a[0]*t[1],i+=(t[0]+a[0])*e,o+=(t[1]+a[1])*e;return[i*n,o*n]},vc.clip=function(n){for(var t,e,r,u,i,o,a=dr(n),c=-1,l=this.length-dr(this),s=this[l-1];++c<l;){for(t=n.slice(),n.length=0,u=this[c],i=t[(r=t.length-a)-1],e=-1;++e<r;)o=t[e],gr(o,s,u)?(gr(i,s,u)||n.push(pr(i,o,s,u)),n.push(o)):gr(i,s,u)&&n.push(pr(i,o,s,u)),i=o;a&&n.push(n[0]),s=u}return n},mo.geom.delaunay=function(n){var t=n.map(function(){return[]}),e=[];return vr(n,function(e){t[e.region.l.index].push(n[e.region.r.index])}),t.forEach(function(t,r){var u=n[r],i=u[0],o=u[1];t.forEach(function(n){n.angle=Math.atan2(n[0]-i,n[1]-o)}),t.sort(function(n,t){return n.angle-t.angle});for(var a=0,c=t.length-1;c>a;a++)e.push([u,t[a],t[a+1]])}),e},mo.geom.voronoi=function(n){function t(n){var t,i,o,a=n.map(function(){return[]}),c=pt(e),l=pt(r),s=n.length,f=1e6;if(c===Ze&&l===Ve)t=n;else for(t=new Array(s),o=0;s>o;++o)t[o]=[+c.call(this,i=n[o],o),+l.call(this,i,o)];if(vr(t,function(n){var t,e,r,u,i,o;1===n.a&&n.b>=0?(t=n.ep.r,e=n.ep.l):(t=n.ep.l,e=n.ep.r),1===n.a?(i=t?t.y:-f,r=n.c-n.b*i,o=e?e.y:f,u=n.c-n.b*o):(r=t?t.x:-f,i=n.c-n.a*r,u=e?e.x:f,o=n.c-n.a*u);var c=[r,i],l=[u,o];a[n.region.l.index].push(c,l),a[n.region.r.index].push(c,l)}),a=a.map(function(n,e){var r=t[e][0],u=t[e][1],i=n.map(function(n){return Math.atan2(n[0]-r,n[1]-u)}),o=mo.range(n.length).sort(function(n,t){return i[n]-i[t]});return o.filter(function(n,t){return!t||i[n]-i[o[t-1]]>Wo}).map(function(t){return n[t]})}),a.forEach(function(n,e){var r=n.length;if(!r)return n.push([-f,-f],[-f,f],[f,f],[f,-f]);if(!(r>2)){var u=t[e],i=n[0],o=n[1],a=u[0],c=u[1],l=i[0],s=i[1],h=o[0],g=o[1],p=Math.abs(h-l),d=g-s;if(Math.abs(d)<Wo){var v=s>c?-f:f;n.push([-f,v],[f,v])}else if(Wo>p){var m=l>a?-f:f;n.push([m,-f],[m,f])}else{var v=(l-a)*(g-s)>(h-l)*(s-c)?f:-f,y=Math.abs(d)-p;Math.abs(y)<Wo?n.push([0>d?v:-v,v]):(y>0&&(v*=-1),n.push([-f,v],[f,v]))}}}),u)for(o=0;s>o;++o)u.clip(a[o]);for(o=0;s>o;++o)a[o].point=n[o];return a}var e=Ze,r=Ve,u=null;return arguments.length?t(n):(t.x=function(n){return arguments.length?(e=n,t):e},t.y=function(n){return arguments.length?(r=n,t):r},t.clipExtent=function(n){if(!arguments.length)return u&&[u[0],u[2]];if(null==n)u=null;else{var e=+n[0][0],r=+n[0][1],i=+n[1][0],o=+n[1][1];u=mo.geom.polygon([[e,r],[e,o],[i,o],[i,r]])}return t},t.size=function(n){return arguments.length?t.clipExtent(n&&[[0,0],n]):u&&u[2]},t.links=function(n){var t,u,i,o=n.map(function(){return[]}),a=[],c=pt(e),l=pt(r),s=n.length;if(c===Ze&&l===Ve)t=n;else for(t=new Array(s),i=0;s>i;++i)t[i]=[+c.call(this,u=n[i],i),+l.call(this,u,i)];return vr(t,function(t){var e=t.region.l.index,r=t.region.r.index;o[e][r]||(o[e][r]=o[r][e]=!0,a.push({source:n[e],target:n[r]}))}),a},t.triangles=function(n){if(e===Ze&&r===Ve)return mo.geom.delaunay(n);for(var t,u=new Array(c),i=pt(e),o=pt(r),a=-1,c=n.length;++a<c;)(u[a]=[+i.call(this,t=n[a],a),+o.call(this,t,a)]).data=t;return mo.geom.delaunay(u).map(function(n){return n.map(function(n){return n.data})})},t)};var mc={l:"r",r:"l"};mo.geom.quadtree=function(n,t,e,r,u){function i(n){function i(n,t,e,r,u,i,o,a){if(!isNaN(e)&&!isNaN(r))if(n.leaf){var c=n.x,s=n.y;if(null!=c)if(Math.abs(c-e)+Math.abs(s-r)<.01)l(n,t,e,r,u,i,o,a);else{var f=n.point;n.x=n.y=n.point=null,l(n,f,c,s,u,i,o,a),l(n,t,e,r,u,i,o,a)}else n.x=e,n.y=r,n.point=t}else l(n,t,e,r,u,i,o,a)}function l(n,t,e,r,u,o,a,c){var l=.5*(u+a),s=.5*(o+c),f=e>=l,h=r>=s,g=(h<<1)+f;n.leaf=!1,n=n.nodes[g]||(n.nodes[g]=Mr()),f?u=l:a=l,h?o=s:c=s,i(n,t,e,r,u,o,a,c)}var s,f,h,g,p,d,v,m,y,M=pt(a),x=pt(c);if(null!=t)d=t,v=e,m=r,y=u;else if(m=y=-(d=v=1/0),f=[],h=[],p=n.length,o)for(g=0;p>g;++g)s=n[g],s.x<d&&(d=s.x),s.y<v&&(v=s.y),s.x>m&&(m=s.x),s.y>y&&(y=s.y),f.push(s.x),h.push(s.y);else for(g=0;p>g;++g){var b=+M(s=n[g],g),_=+x(s,g);d>b&&(d=b),v>_&&(v=_),b>m&&(m=b),_>y&&(y=_),f.push(b),h.push(_)}var w=m-d,S=y-v;w>S?y=v+w:m=d+S;var E=Mr();if(E.add=function(n){i(E,n,+M(n,++g),+x(n,g),d,v,m,y)},E.visit=function(n){xr(n,E,d,v,m,y)},g=-1,null==t){for(;++g<p;)i(E,n[g],f[g],h[g],d,v,m,y);--g}else n.forEach(E.add);return f=h=n=s=null,E}var o,a=Ze,c=Ve;return(o=arguments.length)?(a=mr,c=yr,3===o&&(u=e,r=t,e=t=0),i(n)):(i.x=function(n){return arguments.length?(a=n,i):a},i.y=function(n){return arguments.length?(c=n,i):c},i.extent=function(n){return arguments.length?(null==n?t=e=r=u=null:(t=+n[0][0],e=+n[0][1],r=+n[1][0],u=+n[1][1]),i):null==t?null:[[t,e],[r,u]]},i.size=function(n){return arguments.length?(null==n?t=e=r=u=null:(t=e=0,r=+n[0],u=+n[1]),i):null==t?null:[r-t,u-e]},i)},mo.interpolateRgb=br,mo.interpolateObject=_r,mo.interpolateNumber=wr,mo.interpolateString=Sr;var yc=/[-+]?(?:\d+\.?\d*|\.?\d+)(?:[eE][-+]?\d+)?/g;mo.interpolate=Er,mo.interpolators=[function(n,t){var e=typeof t;return("string"===e?ga.has(t)||/^(#|rgb\(|hsl\()/.test(t)?br:Sr:t instanceof Z?br:"object"===e?Array.isArray(t)?kr:_r:wr)(n,t)}],mo.interpolateArray=kr;var Mc=function(){return dt},xc=mo.map({linear:Mc,poly:Dr,quad:function(){return qr},cubic:function(){return zr},sin:function(){return jr},exp:function(){return Lr},circle:function(){return Hr},elastic:Fr,back:Pr,bounce:function(){return Or}}),bc=mo.map({"in":dt,out:Nr,"in-out":Tr,"out-in":function(n){return Tr(Nr(n))}});mo.ease=function(n){var t=n.indexOf("-"),e=t>=0?n.substring(0,t):n,r=t>=0?n.substring(t+1):"in";return e=xc.get(e)||Mc,r=bc.get(r)||dt,Ar(r(e.apply(null,Array.prototype.slice.call(arguments,1))))},mo.interpolateHcl=Rr,mo.interpolateHsl=Yr,mo.interpolateLab=Ir,mo.interpolateRound=Ur,mo.transform=function(n){var t=xo.createElementNS(mo.ns.prefix.svg,"g");return(mo.transform=function(n){if(null!=n){t.setAttribute("transform",n);var e=t.transform.baseVal.consolidate()}return new Zr(e?e.matrix:_c)})(n)},Zr.prototype.toString=function(){return"translate("+this.translate+")rotate("+this.rotate+")skewX("+this.skew+")scale("+this.scale+")"};var _c={a:1,b:0,c:0,d:1,e:0,f:0};mo.interpolateTransform=Br,mo.layout={},mo.layout.bundle=function(){return function(n){for(var t=[],e=-1,r=n.length;++e<r;)t.push(Gr(n[e]));return t}},mo.layout.chord=function(){function n(){var n,l,f,h,g,p={},d=[],v=mo.range(i),m=[];for(e=[],r=[],n=0,h=-1;++h<i;){for(l=0,g=-1;++g<i;)l+=u[h][g];d.push(l),m.push(mo.range(i)),n+=l}for(o&&v.sort(function(n,t){return o(d[n],d[t])}),a&&m.forEach(function(n,t){n.sort(function(n,e){return a(u[t][n],u[t][e])})}),n=(2*Bo-s*i)/n,l=0,h=-1;++h<i;){for(f=l,g=-1;++g<i;){var y=v[h],M=m[y][g],x=u[y][M],b=l,_=l+=x*n;p[y+"-"+M]={index:y,subindex:M,startAngle:b,endAngle:_,value:x}}r[y]={index:y,startAngle:f,endAngle:l,value:(l-f)/n},l+=s}for(h=-1;++h<i;)for(g=h-1;++g<i;){var w=p[h+"-"+g],S=p[g+"-"+h];(w.value||S.value)&&e.push(w.value<S.value?{source:S,target:w}:{source:w,target:S})}c&&t()}function t(){e.sort(function(n,t){return c((n.source.value+n.target.value)/2,(t.source.value+t.target.value)/2)})}var e,r,u,i,o,a,c,l={},s=0;return l.matrix=function(n){return arguments.length?(i=(u=n)&&u.length,e=r=null,l):u},l.padding=function(n){return arguments.length?(s=n,e=r=null,l):s},l.sortGroups=function(n){return arguments.length?(o=n,e=r=null,l):o},l.sortSubgroups=function(n){return arguments.length?(a=n,e=null,l):a},l.sortChords=function(n){return arguments.length?(c=n,e&&t(),l):c},l.chords=function(){return e||n(),e},l.groups=function(){return r||n(),r},l},mo.layout.force=function(){function n(n){return function(t,e,r,u){if(t.point!==n){var i=t.cx-n.x,o=t.cy-n.y,a=1/Math.sqrt(i*i+o*o);if(d>(u-e)*a){var c=t.charge*a*a;return n.px-=i*c,n.py-=o*c,!0}if(t.point&&isFinite(a)){var c=t.pointCharge*a*a;n.px-=i*c,n.py-=o*c}}return!t.charge}}function t(n){n.px=mo.event.x,n.py=mo.event.y,a.resume()}var e,r,u,i,o,a={},c=mo.dispatch("start","tick","end"),l=[1,1],s=.9,f=wc,h=Sc,g=-30,p=.1,d=.8,v=[],m=[];return a.tick=function(){if((r*=.99)<.005)return c.end({type:"end",alpha:r=0}),!0;var t,e,a,f,h,d,y,M,x,b=v.length,_=m.length;for(e=0;_>e;++e)a=m[e],f=a.source,h=a.target,M=h.x-f.x,x=h.y-f.y,(d=M*M+x*x)&&(d=r*i[e]*((d=Math.sqrt(d))-u[e])/d,M*=d,x*=d,h.x-=M*(y=f.weight/(h.weight+f.weight)),h.y-=x*y,f.x+=M*(y=1-y),f.y+=x*y);if((y=r*p)&&(M=l[0]/2,x=l[1]/2,e=-1,y))for(;++e<b;)a=v[e],a.x+=(M-a.x)*y,a.y+=(x-a.y)*y;if(g)for(uu(t=mo.geom.quadtree(v),r,o),e=-1;++e<b;)(a=v[e]).fixed||t.visit(n(a));for(e=-1;++e<b;)a=v[e],a.fixed?(a.x=a.px,a.y=a.py):(a.x-=(a.px-(a.px=a.x))*s,a.y-=(a.py-(a.py=a.y))*s);c.tick({type:"tick",alpha:r})},a.nodes=function(n){return arguments.length?(v=n,a):v},a.links=function(n){return arguments.length?(m=n,a):m},a.size=function(n){return arguments.length?(l=n,a):l},a.linkDistance=function(n){return arguments.length?(f="function"==typeof n?n:+n,a):f},a.distance=a.linkDistance,a.linkStrength=function(n){return arguments.length?(h="function"==typeof n?n:+n,a):h},a.friction=function(n){return arguments.length?(s=+n,a):s},a.charge=function(n){return arguments.length?(g="function"==typeof n?n:+n,a):g},a.gravity=function(n){return arguments.length?(p=+n,a):p},a.theta=function(n){return arguments.length?(d=+n,a):d},a.alpha=function(n){return arguments.length?(n=+n,r?r=n>0?n:0:n>0&&(c.start({type:"start",alpha:r=n}),mo.timer(a.tick)),a):r},a.start=function(){function n(n,r){for(var u,i=t(e),o=-1,a=i.length;++o<a;)if(!isNaN(u=i[o][n]))return u;return Math.random()*r}function t(){if(!c){for(c=[],r=0;p>r;++r)c[r]=[];for(r=0;d>r;++r){var n=m[r];c[n.source.index].push(n.target),c[n.target.index].push(n.source)}}return c[e]}var e,r,c,s,p=v.length,d=m.length,y=l[0],M=l[1];for(e=0;p>e;++e)(s=v[e]).index=e,s.weight=0;for(e=0;d>e;++e)s=m[e],"number"==typeof s.source&&(s.source=v[s.source]),"number"==typeof s.target&&(s.target=v[s.target]),++s.source.weight,++s.target.weight;for(e=0;p>e;++e)s=v[e],isNaN(s.x)&&(s.x=n("x",y)),isNaN(s.y)&&(s.y=n("y",M)),isNaN(s.px)&&(s.px=s.x),isNaN(s.py)&&(s.py=s.y);if(u=[],"function"==typeof f)for(e=0;d>e;++e)u[e]=+f.call(this,m[e],e);else for(e=0;d>e;++e)u[e]=f;if(i=[],"function"==typeof h)for(e=0;d>e;++e)i[e]=+h.call(this,m[e],e);else for(e=0;d>e;++e)i[e]=h;if(o=[],"function"==typeof g)for(e=0;p>e;++e)o[e]=+g.call(this,v[e],e);else for(e=0;p>e;++e)o[e]=g;return a.resume()},a.resume=function(){return a.alpha(.1)},a.stop=function(){return a.alpha(0)},a.drag=function(){return e||(e=mo.behavior.drag().origin(dt).on("dragstart.force",nu).on("drag.force",t).on("dragend.force",tu)),arguments.length?(this.on("mouseover.force",eu).on("mouseout.force",ru).call(e),void 0):e},mo.rebind(a,c,"on")};var wc=20,Sc=1;mo.layout.hierarchy=function(){function n(t,o,a){var c=u.call(e,t,o);if(t.depth=o,a.push(t),c&&(l=c.length)){for(var l,s,f=-1,h=t.children=[],g=0,p=o+1;++f<l;)s=n(c[f],p,a),s.parent=t,h.push(s),g+=s.value;r&&h.sort(r),i&&(t.value=g)}else i&&(t.value=+i.call(e,t,o)||0);return t}function t(n,r){var u=n.children,o=0;if(u&&(a=u.length))for(var a,c=-1,l=r+1;++c<a;)o+=t(u[c],l);else i&&(o=+i.call(e,n,r)||0);return i&&(n.value=o),o}function e(t){var e=[];return n(t,0,e),e}var r=cu,u=ou,i=au;return e.sort=function(n){return arguments.length?(r=n,e):r},e.children=function(n){return arguments.length?(u=n,e):u},e.value=function(n){return arguments.length?(i=n,e):i},e.revalue=function(n){return t(n,0),n},e},mo.layout.partition=function(){function n(t,e,r,u){var i=t.children;if(t.x=e,t.y=t.depth*u,t.dx=r,t.dy=u,i&&(o=i.length)){var o,a,c,l=-1;for(r=t.value?r/t.value:0;++l<o;)n(a=i[l],e,c=a.value*r,u),e+=c}}function t(n){var e=n.children,r=0;if(e&&(u=e.length))for(var u,i=-1;++i<u;)r=Math.max(r,t(e[i]));return 1+r}function e(e,i){var o=r.call(this,e,i);return n(o[0],0,u[0],u[1]/t(o[0])),o}var r=mo.layout.hierarchy(),u=[1,1];return e.size=function(n){return arguments.length?(u=n,e):u},iu(e,r)},mo.layout.pie=function(){function n(i){var o=i.map(function(e,r){return+t.call(n,e,r)}),a=+("function"==typeof r?r.apply(this,arguments):r),c=(("function"==typeof u?u.apply(this,arguments):u)-a)/mo.sum(o),l=mo.range(i.length);null!=e&&l.sort(e===Ec?function(n,t){return o[t]-o[n]}:function(n,t){return e(i[n],i[t])});var s=[];return l.forEach(function(n){var t;s[n]={data:i[n],value:t=o[n],startAngle:a,endAngle:a+=t*c}}),s}var t=Number,e=Ec,r=0,u=2*Bo;return n.value=function(e){return arguments.length?(t=e,n):t},n.sort=function(t){return arguments.length?(e=t,n):e},n.startAngle=function(t){return arguments.length?(r=t,n):r},n.endAngle=function(t){return arguments.length?(u=t,n):u},n};var Ec={};mo.layout.stack=function(){function n(a,c){var l=a.map(function(e,r){return t.call(n,e,r)}),s=l.map(function(t){return t.map(function(t,e){return[i.call(n,t,e),o.call(n,t,e)]})}),f=e.call(n,s,c);l=mo.permute(l,f),s=mo.permute(s,f);var h,g,p,d=r.call(n,s,c),v=l.length,m=l[0].length;for(g=0;m>g;++g)for(u.call(n,l[0][g],p=d[g],s[0][g][1]),h=1;v>h;++h)u.call(n,l[h][g],p+=s[h-1][g][1],s[h][g][1]);return a}var t=dt,e=gu,r=pu,u=hu,i=su,o=fu;return n.values=function(e){return arguments.length?(t=e,n):t},n.order=function(t){return arguments.length?(e="function"==typeof t?t:kc.get(t)||gu,n):e},n.offset=function(t){return arguments.length?(r="function"==typeof t?t:Ac.get(t)||pu,n):r},n.x=function(t){return arguments.length?(i=t,n):i},n.y=function(t){return arguments.length?(o=t,n):o},n.out=function(t){return arguments.length?(u=t,n):u},n};var kc=mo.map({"inside-out":function(n){var t,e,r=n.length,u=n.map(du),i=n.map(vu),o=mo.range(r).sort(function(n,t){return u[n]-u[t]}),a=0,c=0,l=[],s=[];for(t=0;r>t;++t)e=o[t],c>a?(a+=i[e],l.push(e)):(c+=i[e],s.push(e));return s.reverse().concat(l)},reverse:function(n){return mo.range(n.length).reverse()},"default":gu}),Ac=mo.map({silhouette:function(n){var t,e,r,u=n.length,i=n[0].length,o=[],a=0,c=[];for(e=0;i>e;++e){for(t=0,r=0;u>t;t++)r+=n[t][e][1];r>a&&(a=r),o.push(r)}for(e=0;i>e;++e)c[e]=(a-o[e])/2;return c},wiggle:function(n){var t,e,r,u,i,o,a,c,l,s=n.length,f=n[0],h=f.length,g=[];for(g[0]=c=l=0,e=1;h>e;++e){for(t=0,u=0;s>t;++t)u+=n[t][e][1];for(t=0,i=0,a=f[e][0]-f[e-1][0];s>t;++t){for(r=0,o=(n[t][e][1]-n[t][e-1][1])/(2*a);t>r;++r)o+=(n[r][e][1]-n[r][e-1][1])/a;i+=o*n[t][e][1]}g[e]=c-=u?i/u*a:0,l>c&&(l=c)}for(e=0;h>e;++e)g[e]-=l;return g},expand:function(n){var t,e,r,u=n.length,i=n[0].length,o=1/u,a=[];for(e=0;i>e;++e){for(t=0,r=0;u>t;t++)r+=n[t][e][1];if(r)for(t=0;u>t;t++)n[t][e][1]/=r;else for(t=0;u>t;t++)n[t][e][1]=o}for(e=0;i>e;++e)a[e]=0;return a},zero:pu});mo.layout.histogram=function(){function n(n,i){for(var o,a,c=[],l=n.map(e,this),s=r.call(this,l,i),f=u.call(this,s,l,i),i=-1,h=l.length,g=f.length-1,p=t?1:1/h;++i<g;)o=c[i]=[],o.dx=f[i+1]-(o.x=f[i]),o.y=0;if(g>0)for(i=-1;++i<h;)a=l[i],a>=s[0]&&a<=s[1]&&(o=c[mo.bisect(f,a,1,g)-1],o.y+=p,o.push(n[i]));return c}var t=!0,e=Number,r=xu,u=yu;return n.value=function(t){return arguments.length?(e=t,n):e},n.range=function(t){return arguments.length?(r=pt(t),n):r},n.bins=function(t){return arguments.length?(u="number"==typeof t?function(n){return Mu(n,t)}:pt(t),n):u},n.frequency=function(e){return arguments.length?(t=!!e,n):t},n},mo.layout.tree=function(){function n(n,i){function o(n,t){var r=n.children,u=n._tree;if(r&&(i=r.length)){for(var i,a,l,s=r[0],f=s,h=-1;++h<i;)l=r[h],o(l,a),f=c(l,a,f),a=l;Tu(n);var g=.5*(s._tree.prelim+l._tree.prelim);t?(u.prelim=t._tree.prelim+e(n,t),u.mod=u.prelim-g):u.prelim=g}else t&&(u.prelim=t._tree.prelim+e(n,t))}function a(n,t){n.x=n._tree.prelim+t;var e=n.children;if(e&&(r=e.length)){var r,u=-1;for(t+=n._tree.mod;++u<r;)a(e[u],t)}}function c(n,t,r){if(t){for(var u,i=n,o=n,a=t,c=n.parent.children[0],l=i._tree.mod,s=o._tree.mod,f=a._tree.mod,h=c._tree.mod;a=wu(a),i=_u(i),a&&i;)c=_u(c),o=wu(o),o._tree.ancestor=n,u=a._tree.prelim+f-i._tree.prelim-l+e(a,i),u>0&&(qu(zu(a,n,r),n,u),l+=u,s+=u),f+=a._tree.mod,l+=i._tree.mod,h+=c._tree.mod,s+=o._tree.mod;a&&!wu(o)&&(o._tree.thread=a,o._tree.mod+=f-s),i&&!_u(c)&&(c._tree.thread=i,c._tree.mod+=l-h,r=n)}return r}var l=t.call(this,n,i),s=l[0];Nu(s,function(n,t){n._tree={ancestor:n,prelim:0,mod:0,change:0,shift:0,number:t?t._tree.number+1:0}}),o(s),a(s,-s._tree.prelim);var f=Su(s,ku),h=Su(s,Eu),g=Su(s,Au),p=f.x-e(f,h)/2,d=h.x+e(h,f)/2,v=g.depth||1;return Nu(s,u?function(n){n.x*=r[0],n.y=n.depth*r[1],delete n._tree}:function(n){n.x=(n.x-p)/(d-p)*r[0],n.y=n.depth/v*r[1],delete n._tree}),l}var t=mo.layout.hierarchy().sort(null).value(null),e=bu,r=[1,1],u=!1;return n.separation=function(t){return arguments.length?(e=t,n):e},n.size=function(t){return arguments.length?(u=null==(r=t),n):u?null:r},n.nodeSize=function(t){return arguments.length?(u=null!=(r=t),n):u?r:null},iu(n,t)},mo.layout.pack=function(){function n(n,i){var o=e.call(this,n,i),a=o[0],c=u[0],l=u[1],s=null==t?Math.sqrt:"function"==typeof t?t:function(){return t};if(a.x=a.y=0,Nu(a,function(n){n.r=+s(n.value)}),Nu(a,Hu),r){var f=r*(t?1:Math.max(2*a.r/c,2*a.r/l))/2;Nu(a,function(n){n.r+=f}),Nu(a,Hu),Nu(a,function(n){n.r-=f})}return Ou(a,c/2,l/2,t?1:1/Math.max(2*a.r/c,2*a.r/l)),o}var t,e=mo.layout.hierarchy().sort(Cu),r=0,u=[1,1];return n.size=function(t){return arguments.length?(u=t,n):u},n.radius=function(e){return arguments.length?(t=null==e||"function"==typeof e?e:+e,n):t},n.padding=function(t){return arguments.length?(r=+t,n):r},iu(n,e)},mo.layout.cluster=function(){function n(n,i){var o,a=t.call(this,n,i),c=a[0],l=0;Nu(c,function(n){var t=n.children;t&&t.length?(n.x=Iu(t),n.y=Yu(t)):(n.x=o?l+=e(n,o):0,n.y=0,o=n)});var s=Uu(c),f=Zu(c),h=s.x-e(s,f)/2,g=f.x+e(f,s)/2;return Nu(c,u?function(n){n.x=(n.x-c.x)*r[0],n.y=(c.y-n.y)*r[1]}:function(n){n.x=(n.x-h)/(g-h)*r[0],n.y=(1-(c.y?n.y/c.y:1))*r[1]}),a}var t=mo.layout.hierarchy().sort(null).value(null),e=bu,r=[1,1],u=!1;return n.separation=function(t){return arguments.length?(e=t,n):e},n.size=function(t){return arguments.length?(u=null==(r=t),n):u?null:r},n.nodeSize=function(t){return arguments.length?(u=null!=(r=t),n):u?r:null},iu(n,t)},mo.layout.treemap=function(){function n(n,t){for(var e,r,u=-1,i=n.length;++u<i;)r=(e=n[u]).value*(0>t?0:t),e.area=isNaN(r)||0>=r?0:r}function t(e){var i=e.children;if(i&&i.length){var o,a,c,l=f(e),s=[],h=i.slice(),p=1/0,d="slice"===g?l.dx:"dice"===g?l.dy:"slice-dice"===g?1&e.depth?l.dy:l.dx:Math.min(l.dx,l.dy);for(n(h,l.dx*l.dy/e.value),s.area=0;(c=h.length)>0;)s.push(o=h[c-1]),s.area+=o.area,"squarify"!==g||(a=r(s,d))<=p?(h.pop(),p=a):(s.area-=s.pop().area,u(s,d,l,!1),d=Math.min(l.dx,l.dy),s.length=s.area=0,p=1/0);s.length&&(u(s,d,l,!0),s.length=s.area=0),i.forEach(t)}}function e(t){var r=t.children;if(r&&r.length){var i,o=f(t),a=r.slice(),c=[];for(n(a,o.dx*o.dy/t.value),c.area=0;i=a.pop();)c.push(i),c.area+=i.area,null!=i.z&&(u(c,i.z?o.dx:o.dy,o,!a.length),c.length=c.area=0);r.forEach(e)}}function r(n,t){for(var e,r=n.area,u=0,i=1/0,o=-1,a=n.length;++o<a;)(e=n[o].area)&&(i>e&&(i=e),e>u&&(u=e));return r*=r,t*=t,r?Math.max(t*u*p/r,r/(t*i*p)):1/0}function u(n,t,e,r){var u,i=-1,o=n.length,a=e.x,l=e.y,s=t?c(n.area/t):0;if(t==e.dx){for((r||s>e.dy)&&(s=e.dy);++i<o;)u=n[i],u.x=a,u.y=l,u.dy=s,a+=u.dx=Math.min(e.x+e.dx-a,s?c(u.area/s):0);u.z=!0,u.dx+=e.x+e.dx-a,e.y+=s,e.dy-=s}else{for((r||s>e.dx)&&(s=e.dx);++i<o;)u=n[i],u.x=a,u.y=l,u.dx=s,l+=u.dy=Math.min(e.y+e.dy-l,s?c(u.area/s):0);u.z=!1,u.dy+=e.y+e.dy-l,e.x+=s,e.dx-=s}}function i(r){var u=o||a(r),i=u[0];return i.x=0,i.y=0,i.dx=l[0],i.dy=l[1],o&&a.revalue(i),n([i],i.dx*i.dy/i.value),(o?e:t)(i),h&&(o=u),u}var o,a=mo.layout.hierarchy(),c=Math.round,l=[1,1],s=null,f=Vu,h=!1,g="squarify",p=.5*(1+Math.sqrt(5));return i.size=function(n){return arguments.length?(l=n,i):l},i.padding=function(n){function t(t){var e=n.call(i,t,t.depth);return null==e?Vu(t):Xu(t,"number"==typeof e?[e,e,e,e]:e)}function e(t){return Xu(t,n)}if(!arguments.length)return s;var r;return f=null==(s=n)?Vu:"function"==(r=typeof n)?t:"number"===r?(n=[n,n,n,n],e):e,i},i.round=function(n){return arguments.length?(c=n?Math.round:Number,i):c!=Number},i.sticky=function(n){return arguments.length?(h=n,o=null,i):h},i.ratio=function(n){return arguments.length?(p=n,i):p},i.mode=function(n){return arguments.length?(g=n+"",i):g},iu(i,a)},mo.random={normal:function(n,t){var e=arguments.length;return 2>e&&(t=1),1>e&&(n=0),function(){var e,r,u;do e=2*Math.random()-1,r=2*Math.random()-1,u=e*e+r*r;while(!u||u>1);return n+t*e*Math.sqrt(-2*Math.log(u)/u)}},logNormal:function(){var n=mo.random.normal.apply(mo,arguments);return function(){return Math.exp(n())}},irwinHall:function(n){return function(){for(var t=0,e=0;n>e;e++)t+=Math.random();return t/n}}},mo.scale={};var Nc={floor:dt,ceil:dt};mo.scale.linear=function(){return Qu([0,1],[0,1],Er,!1)},mo.scale.log=function(){return ii(mo.scale.linear().domain([0,1]),10,!0,[1,10])};var Tc=mo.format(".0e"),qc={floor:function(n){return-Math.ceil(-n)},ceil:function(n){return-Math.floor(-n)}};mo.scale.pow=function(){return oi(mo.scale.linear(),1,[0,1])},mo.scale.sqrt=function(){return mo.scale.pow().exponent(.5)},mo.scale.ordinal=function(){return ci([],{t:"range",a:[[]]})},mo.scale.category10=function(){return mo.scale.ordinal().range(zc)},mo.scale.category20=function(){return mo.scale.ordinal().range(Cc)},mo.scale.category20b=function(){return mo.scale.ordinal().range(Dc)},mo.scale.category20c=function(){return mo.scale.ordinal().range(jc)};var zc=[2062260,16744206,2924588,14034728,9725885,9197131,14907330,8355711,12369186,1556175].map(it),Cc=[2062260,11454440,16744206,16759672,2924588,10018698,14034728,16750742,9725885,12955861,9197131,12885140,14907330,16234194,8355711,13092807,12369186,14408589,1556175,10410725].map(it),Dc=[3750777,5395619,7040719,10264286,6519097,9216594,11915115,13556636,9202993,12426809,15186514,15190932,8666169,11356490,14049643,15177372,8077683,10834324,13528509,14589654].map(it),jc=[3244733,7057110,10406625,13032431,15095053,16616764,16625259,16634018,3253076,7652470,10607003,13101504,7695281,10394312,12369372,14342891,6513507,9868950,12434877,14277081].map(it);mo.scale.quantile=function(){return li([],[])},mo.scale.quantize=function(){return si(0,1,[0,1])},mo.scale.threshold=function(){return fi([.5],[0,1])},mo.scale.identity=function(){return hi([0,1])},mo.svg.arc=function(){function n(){var n=t.apply(this,arguments),i=e.apply(this,arguments),o=r.apply(this,arguments)+Lc,a=u.apply(this,arguments)+Lc,c=(o>a&&(c=o,o=a,a=c),a-o),l=Bo>c?"0":"1",s=Math.cos(o),f=Math.sin(o),h=Math.cos(a),g=Math.sin(a);return c>=Hc?n?"M0,"+i+"A"+i+","+i+" 0 1,1 0,"+-i+"A"+i+","+i+" 0 1,1 0,"+i+"M0,"+n+"A"+n+","+n+" 0 1,0 0,"+-n+"A"+n+","+n+" 0 1,0 0,"+n+"Z":"M0,"+i+"A"+i+","+i+" 0 1,1 0,"+-i+"A"+i+","+i+" 0 1,1 0,"+i+"Z":n?"M"+i*s+","+i*f+"A"+i+","+i+" 0 "+l+",1 "+i*h+","+i*g+"L"+n*h+","+n*g+"A"+n+","+n+" 0 "+l+",0 "+n*s+","+n*f+"Z":"M"+i*s+","+i*f+"A"+i+","+i+" 0 "+l+",1 "+i*h+","+i*g+"L0,0"+"Z"}var t=gi,e=pi,r=di,u=vi;return n.innerRadius=function(e){return arguments.length?(t=pt(e),n):t},n.outerRadius=function(t){return arguments.length?(e=pt(t),n):e},n.startAngle=function(t){return arguments.length?(r=pt(t),n):r},n.endAngle=function(t){return arguments.length?(u=pt(t),n):u},n.centroid=function(){var n=(t.apply(this,arguments)+e.apply(this,arguments))/2,i=(r.apply(this,arguments)+u.apply(this,arguments))/2+Lc;return[Math.cos(i)*n,Math.sin(i)*n]},n};var Lc=-Bo/2,Hc=2*Bo-1e-6;mo.svg.line.radial=function(){var n=Ue(mi);return n.radius=n.x,delete n.x,n.angle=n.y,delete n.y,n},We.reverse=Je,Je.reverse=We,mo.svg.area=function(){return yi(dt)},mo.svg.area.radial=function(){var n=yi(mi);return n.radius=n.x,delete n.x,n.innerRadius=n.x0,delete n.x0,n.outerRadius=n.x1,delete n.x1,n.angle=n.y,delete n.y,n.startAngle=n.y0,delete n.y0,n.endAngle=n.y1,delete n.y1,n},mo.svg.chord=function(){function n(n,a){var c=t(this,i,n,a),l=t(this,o,n,a);return"M"+c.p0+r(c.r,c.p1,c.a1-c.a0)+(e(c,l)?u(c.r,c.p1,c.r,c.p0):u(c.r,c.p1,l.r,l.p0)+r(l.r,l.p1,l.a1-l.a0)+u(l.r,l.p1,c.r,c.p0))+"Z"}function t(n,t,e,r){var u=t.call(n,e,r),i=a.call(n,u,r),o=c.call(n,u,r)+Lc,s=l.call(n,u,r)+Lc;return{r:i,a0:o,a1:s,p0:[i*Math.cos(o),i*Math.sin(o)],p1:[i*Math.cos(s),i*Math.sin(s)]}}function e(n,t){return n.a0==t.a0&&n.a1==t.a1}function r(n,t,e){return"A"+n+","+n+" 0 "+ +(e>Bo)+",1 "+t}function u(n,t,e,r){return"Q 0,0 "+r}var i=De,o=je,a=Mi,c=di,l=vi;return n.radius=function(t){return arguments.length?(a=pt(t),n):a},n.source=function(t){return arguments.length?(i=pt(t),n):i},n.target=function(t){return arguments.length?(o=pt(t),n):o},n.startAngle=function(t){return arguments.length?(c=pt(t),n):c},n.endAngle=function(t){return arguments.length?(l=pt(t),n):l},n},mo.svg.diagonal=function(){function n(n,u){var i=t.call(this,n,u),o=e.call(this,n,u),a=(i.y+o.y)/2,c=[i,{x:i.x,y:a},{x:o.x,y:a},o];return c=c.map(r),"M"+c[0]+"C"+c[1]+" "+c[2]+" "+c[3]}var t=De,e=je,r=xi;return n.source=function(e){return arguments.length?(t=pt(e),n):t},n.target=function(t){return arguments.length?(e=pt(t),n):e},n.projection=function(t){return arguments.length?(r=t,n):r},n},mo.svg.diagonal.radial=function(){var n=mo.svg.diagonal(),t=xi,e=n.projection;return n.projection=function(n){return arguments.length?e(bi(t=n)):t},n},mo.svg.symbol=function(){function n(n,r){return(Fc.get(t.call(this,n,r))||Si)(e.call(this,n,r))}var t=wi,e=_i;return n.type=function(e){return arguments.length?(t=pt(e),n):t},n.size=function(t){return arguments.length?(e=pt(t),n):e},n};var Fc=mo.map({circle:Si,cross:function(n){var t=Math.sqrt(n/5)/2;return"M"+-3*t+","+-t+"H"+-t+"V"+-3*t+"H"+t+"V"+-t+"H"+3*t+"V"+t+"H"+t+"V"+3*t+"H"+-t+"V"+t+"H"+-3*t+"Z"},diamond:function(n){var t=Math.sqrt(n/(2*Yc)),e=t*Yc;return"M0,"+-t+"L"+e+",0"+" 0,"+t+" "+-e+",0"+"Z"},square:function(n){var t=Math.sqrt(n)/2;return"M"+-t+","+-t+"L"+t+","+-t+" "+t+","+t+" "+-t+","+t+"Z"},"triangle-down":function(n){var t=Math.sqrt(n/Rc),e=t*Rc/2;return"M0,"+e+"L"+t+","+-e+" "+-t+","+-e+"Z"},"triangle-up":function(n){var t=Math.sqrt(n/Rc),e=t*Rc/2;return"M0,"+-e+"L"+t+","+e+" "+-t+","+e+"Z"}});mo.svg.symbolTypes=Fc.keys();var Pc,Oc,Rc=Math.sqrt(3),Yc=Math.tan(30*Go),Ic=[],Uc=0;Ic.call=Ro.call,Ic.empty=Ro.empty,Ic.node=Ro.node,Ic.size=Ro.size,mo.transition=function(n){return arguments.length?Pc?n.transition():n:Uo.transition()},mo.transition.prototype=Ic,Ic.select=function(n){var t,e,r,u=this.id,i=[];n=d(n);for(var o=-1,a=this.length;++o<a;){i.push(t=[]);for(var c=this[o],l=-1,s=c.length;++l<s;)(r=c[l])&&(e=n.call(r,r.__data__,l,o))?("__data__"in r&&(e.__data__=r.__data__),Ni(e,l,u,r.__transition__[u]),t.push(e)):t.push(null)}return Ei(i,u)},Ic.selectAll=function(n){var t,e,r,u,i,o=this.id,a=[];n=v(n);for(var c=-1,l=this.length;++c<l;)for(var s=this[c],f=-1,h=s.length;++f<h;)if(r=s[f]){i=r.__transition__[o],e=n.call(r,r.__data__,f,c),a.push(t=[]);for(var g=-1,p=e.length;++g<p;)(u=e[g])&&Ni(u,g,o,i),t.push(u)}return Ei(a,o)},Ic.filter=function(n){var t,e,r,u=[];"function"!=typeof n&&(n=k(n));for(var i=0,o=this.length;o>i;i++){u.push(t=[]);for(var e=this[i],a=0,c=e.length;c>a;a++)(r=e[a])&&n.call(r,r.__data__,a)&&t.push(r)}return Ei(u,this.id)},Ic.tween=function(n,t){var e=this.id;return arguments.length<2?this.node().__transition__[e].tween.get(n):N(this,null==t?function(t){t.__transition__[e].tween.remove(n)}:function(r){r.__transition__[e].tween.set(n,t)})},Ic.attr=function(n,t){function e(){this.removeAttribute(a)}function r(){this.removeAttributeNS(a.space,a.local)}function u(n){return null==n?e:(n+="",function(){var t,e=this.getAttribute(a);return e!==n&&(t=o(e,n),function(n){this.setAttribute(a,t(n))})})}function i(n){return null==n?r:(n+="",function(){var t,e=this.getAttributeNS(a.space,a.local);return e!==n&&(t=o(e,n),function(n){this.setAttributeNS(a.space,a.local,t(n))
      })})}if(arguments.length<2){for(t in n)this.attr(t,n[t]);return this}var o="transform"==n?Br:Er,a=mo.ns.qualify(n);return ki(this,"attr."+n,t,a.local?i:u)},Ic.attrTween=function(n,t){function e(n,e){var r=t.call(this,n,e,this.getAttribute(u));return r&&function(n){this.setAttribute(u,r(n))}}function r(n,e){var r=t.call(this,n,e,this.getAttributeNS(u.space,u.local));return r&&function(n){this.setAttributeNS(u.space,u.local,r(n))}}var u=mo.ns.qualify(n);return this.tween("attr."+n,u.local?r:e)},Ic.style=function(n,t,e){function r(){this.style.removeProperty(n)}function u(t){return null==t?r:(t+="",function(){var r,u=_o.getComputedStyle(this,null).getPropertyValue(n);return u!==t&&(r=Er(u,t),function(t){this.style.setProperty(n,r(t),e)})})}var i=arguments.length;if(3>i){if("string"!=typeof n){2>i&&(t="");for(e in n)this.style(e,n[e],t);return this}e=""}return ki(this,"style."+n,t,u)},Ic.styleTween=function(n,t,e){function r(r,u){var i=t.call(this,r,u,_o.getComputedStyle(this,null).getPropertyValue(n));return i&&function(t){this.style.setProperty(n,i(t),e)}}return arguments.length<3&&(e=""),this.tween("style."+n,r)},Ic.text=function(n){return ki(this,"text",n,Ai)},Ic.remove=function(){return this.each("end.transition",function(){var n;this.__transition__.count<2&&(n=this.parentNode)&&n.removeChild(this)})},Ic.ease=function(n){var t=this.id;return arguments.length<1?this.node().__transition__[t].ease:("function"!=typeof n&&(n=mo.ease.apply(mo,arguments)),N(this,function(e){e.__transition__[t].ease=n}))},Ic.delay=function(n){var t=this.id;return N(this,"function"==typeof n?function(e,r,u){e.__transition__[t].delay=+n.call(e,e.__data__,r,u)}:(n=+n,function(e){e.__transition__[t].delay=n}))},Ic.duration=function(n){var t=this.id;return N(this,"function"==typeof n?function(e,r,u){e.__transition__[t].duration=Math.max(1,n.call(e,e.__data__,r,u))}:(n=Math.max(1,n),function(e){e.__transition__[t].duration=n}))},Ic.each=function(n,t){var e=this.id;if(arguments.length<2){var r=Oc,u=Pc;Pc=e,N(this,function(t,r,u){Oc=t.__transition__[e],n.call(t,t.__data__,r,u)}),Oc=r,Pc=u}else N(this,function(r){var u=r.__transition__[e];(u.event||(u.event=mo.dispatch("start","end"))).on(n,t)});return this},Ic.transition=function(){for(var n,t,e,r,u=this.id,i=++Uc,o=[],a=0,c=this.length;c>a;a++){o.push(n=[]);for(var t=this[a],l=0,s=t.length;s>l;l++)(e=t[l])&&(r=Object.create(e.__transition__[u]),r.delay+=r.duration,Ni(e,l,i,r)),n.push(e)}return Ei(o,i)},mo.svg.axis=function(){function n(n){n.each(function(){var n,l=mo.select(this),s=null==c?e.ticks?e.ticks.apply(e,a):e.domain():c,f=null==t?e.tickFormat?e.tickFormat.apply(e,a):dt:t,h=l.selectAll(".tick").data(s,dt),g=h.enter().insert("g",".domain").attr("class","tick").style("opacity",1e-6),p=mo.transition(h.exit()).style("opacity",1e-6).remove(),d=mo.transition(h).style("opacity",1),v=Bu(e),m=l.selectAll(".domain").data([0]),y=(m.enter().append("path").attr("class","domain"),mo.transition(m)),M=e.copy(),x=this.__chart__||M;this.__chart__=M,g.append("line"),g.append("text");var b=g.select("line"),_=d.select("line"),w=h.select("text").text(f),S=g.select("text"),E=d.select("text");switch(r){case"bottom":n=Ti,b.attr("y2",u),S.attr("y",Math.max(u,0)+o),_.attr("x2",0).attr("y2",u),E.attr("x",0).attr("y",Math.max(u,0)+o),w.attr("dy",".71em").style("text-anchor","middle"),y.attr("d","M"+v[0]+","+i+"V0H"+v[1]+"V"+i);break;case"top":n=Ti,b.attr("y2",-u),S.attr("y",-(Math.max(u,0)+o)),_.attr("x2",0).attr("y2",-u),E.attr("x",0).attr("y",-(Math.max(u,0)+o)),w.attr("dy","0em").style("text-anchor","middle"),y.attr("d","M"+v[0]+","+-i+"V0H"+v[1]+"V"+-i);break;case"left":n=qi,b.attr("x2",-u),S.attr("x",-(Math.max(u,0)+o)),_.attr("x2",-u).attr("y2",0),E.attr("x",-(Math.max(u,0)+o)).attr("y",0),w.attr("dy",".32em").style("text-anchor","end"),y.attr("d","M"+-i+","+v[0]+"H0V"+v[1]+"H"+-i);break;case"right":n=qi,b.attr("x2",u),S.attr("x",Math.max(u,0)+o),_.attr("x2",u).attr("y2",0),E.attr("x",Math.max(u,0)+o).attr("y",0),w.attr("dy",".32em").style("text-anchor","start"),y.attr("d","M"+i+","+v[0]+"H0V"+v[1]+"H"+i)}if(e.rangeBand){var k=M.rangeBand()/2,A=function(n){return M(n)+k};g.call(n,A),d.call(n,A)}else g.call(n,x),d.call(n,M),p.call(n,M)})}var t,e=mo.scale.linear(),r=Zc,u=6,i=6,o=3,a=[10],c=null;return n.scale=function(t){return arguments.length?(e=t,n):e},n.orient=function(t){return arguments.length?(r=t in Vc?t+"":Zc,n):r},n.ticks=function(){return arguments.length?(a=arguments,n):a},n.tickValues=function(t){return arguments.length?(c=t,n):c},n.tickFormat=function(e){return arguments.length?(t=e,n):t},n.tickSize=function(t){var e=arguments.length;return e?(u=+t,i=+arguments[e-1],n):u},n.innerTickSize=function(t){return arguments.length?(u=+t,n):u},n.outerTickSize=function(t){return arguments.length?(i=+t,n):i},n.tickPadding=function(t){return arguments.length?(o=+t,n):o},n.tickSubdivide=function(){return arguments.length&&n},n};var Zc="bottom",Vc={top:1,right:1,bottom:1,left:1};mo.svg.brush=function(){function n(i){i.each(function(){var i=mo.select(this).style("pointer-events","all").style("-webkit-tap-highlight-color","rgba(0,0,0,0)").on("mousedown.brush",u).on("touchstart.brush",u),o=i.selectAll(".background").data([0]);o.enter().append("rect").attr("class","background").style("visibility","hidden").style("cursor","crosshair"),i.selectAll(".extent").data([0]).enter().append("rect").attr("class","extent").style("cursor","move");var a=i.selectAll(".resize").data(v,dt);a.exit().remove(),a.enter().append("g").attr("class",function(n){return"resize "+n}).style("cursor",function(n){return Xc[n]}).append("rect").attr("x",function(n){return/[ew]$/.test(n)?-3:null}).attr("y",function(n){return/^[ns]/.test(n)?-3:null}).attr("width",6).attr("height",6).style("visibility","hidden"),a.style("display",n.empty()?"none":null);var s,f=mo.transition(i),h=mo.transition(o);c&&(s=Bu(c),h.attr("x",s[0]).attr("width",s[1]-s[0]),e(f)),l&&(s=Bu(l),h.attr("y",s[0]).attr("height",s[1]-s[0]),r(f)),t(f)})}function t(n){n.selectAll(".resize").attr("transform",function(n){return"translate("+s[+/e$/.test(n)]+","+h[+/^s/.test(n)]+")"})}function e(n){n.select(".extent").attr("x",s[0]),n.selectAll(".extent,.n>rect,.s>rect").attr("width",s[1]-s[0])}function r(n){n.select(".extent").attr("y",h[0]),n.selectAll(".extent,.e>rect,.w>rect").attr("height",h[1]-h[0])}function u(){function u(){32==mo.event.keyCode&&(N||(M=null,q[0]-=s[1],q[1]-=h[1],N=2),f())}function g(){32==mo.event.keyCode&&2==N&&(q[0]+=s[1],q[1]+=h[1],N=0,f())}function v(){var n=mo.mouse(b),u=!1;x&&(n[0]+=x[0],n[1]+=x[1]),N||(mo.event.altKey?(M||(M=[(s[0]+s[1])/2,(h[0]+h[1])/2]),q[0]=s[+(n[0]<M[0])],q[1]=h[+(n[1]<M[1])]):M=null),k&&m(n,c,0)&&(e(S),u=!0),A&&m(n,l,1)&&(r(S),u=!0),u&&(t(S),w({type:"brush",mode:N?"move":"resize"}))}function m(n,t,e){var r,u,a=Bu(t),c=a[0],l=a[1],f=q[e],g=e?h:s,v=g[1]-g[0];return N&&(c-=f,l-=v+f),r=(e?d:p)?Math.max(c,Math.min(l,n[e])):n[e],N?u=(r+=f)+v:(M&&(f=Math.max(c,Math.min(l,2*M[e]-r))),r>f?(u=r,r=f):u=f),g[0]!=r||g[1]!=u?(e?o=null:i=null,g[0]=r,g[1]=u,!0):void 0}function y(){v(),S.style("pointer-events","all").selectAll(".resize").style("display",n.empty()?"none":null),mo.select("body").style("cursor",null),z.on("mousemove.brush",null).on("mouseup.brush",null).on("touchmove.brush",null).on("touchend.brush",null).on("keydown.brush",null).on("keyup.brush",null),T(),w({type:"brushend"})}var M,x,b=this,_=mo.select(mo.event.target),w=a.of(b,arguments),S=mo.select(b),E=_.datum(),k=!/^(n|s)$/.test(E)&&c,A=!/^(e|w)$/.test(E)&&l,N=_.classed("extent"),T=L(),q=mo.mouse(b),z=mo.select(_o).on("keydown.brush",u).on("keyup.brush",g);if(mo.event.changedTouches?z.on("touchmove.brush",v).on("touchend.brush",y):z.on("mousemove.brush",v).on("mouseup.brush",y),S.interrupt().selectAll("*").interrupt(),N)q[0]=s[0]-q[0],q[1]=h[0]-q[1];else if(E){var C=+/w$/.test(E),D=+/^n/.test(E);x=[s[1-C]-q[0],h[1-D]-q[1]],q[0]=s[C],q[1]=h[D]}else mo.event.altKey&&(M=q.slice());S.style("pointer-events","none").selectAll(".resize").style("display",null),mo.select("body").style("cursor",_.style("cursor")),w({type:"brushstart"}),v()}var i,o,a=g(n,"brushstart","brush","brushend"),c=null,l=null,s=[0,0],h=[0,0],p=!0,d=!0,v=$c[0];return n.event=function(n){n.each(function(){var n=a.of(this,arguments),t={x:s,y:h,i:i,j:o},e=this.__chart__||t;this.__chart__=t,Pc?mo.select(this).transition().each("start.brush",function(){i=e.i,o=e.j,s=e.x,h=e.y,n({type:"brushstart"})}).tween("brush:brush",function(){var e=kr(s,t.x),r=kr(h,t.y);return i=o=null,function(u){s=t.x=e(u),h=t.y=r(u),n({type:"brush",mode:"resize"})}}).each("end.brush",function(){i=t.i,o=t.j,n({type:"brush",mode:"resize"}),n({type:"brushend"})}):(n({type:"brushstart"}),n({type:"brush",mode:"resize"}),n({type:"brushend"}))})},n.x=function(t){return arguments.length?(c=t,v=$c[!c<<1|!l],n):c},n.y=function(t){return arguments.length?(l=t,v=$c[!c<<1|!l],n):l},n.clamp=function(t){return arguments.length?(c&&l?(p=!!t[0],d=!!t[1]):c?p=!!t:l&&(d=!!t),n):c&&l?[p,d]:c?p:l?d:null},n.extent=function(t){var e,r,u,a,f;return arguments.length?(c&&(e=t[0],r=t[1],l&&(e=e[0],r=r[0]),i=[e,r],c.invert&&(e=c(e),r=c(r)),e>r&&(f=e,e=r,r=f),(e!=s[0]||r!=s[1])&&(s=[e,r])),l&&(u=t[0],a=t[1],c&&(u=u[1],a=a[1]),o=[u,a],l.invert&&(u=l(u),a=l(a)),u>a&&(f=u,u=a,a=f),(u!=h[0]||a!=h[1])&&(h=[u,a])),n):(c&&(i?(e=i[0],r=i[1]):(e=s[0],r=s[1],c.invert&&(e=c.invert(e),r=c.invert(r)),e>r&&(f=e,e=r,r=f))),l&&(o?(u=o[0],a=o[1]):(u=h[0],a=h[1],l.invert&&(u=l.invert(u),a=l.invert(a)),u>a&&(f=u,u=a,a=f))),c&&l?[[e,u],[r,a]]:c?[e,r]:l&&[u,a])},n.clear=function(){return n.empty()||(s=[0,0],h=[0,0],i=o=null),n},n.empty=function(){return!!c&&s[0]==s[1]||!!l&&h[0]==h[1]},mo.rebind(n,a,"on")};var Xc={n:"ns-resize",e:"ew-resize",s:"ns-resize",w:"ew-resize",nw:"nwse-resize",ne:"nesw-resize",se:"nwse-resize",sw:"nesw-resize"},$c=[["n","e","s","w","nw","ne","se","sw"],["e","w"],["n","s"],[]],Bc=mo.time={},Wc=Date,Jc=["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];zi.prototype={getDate:function(){return this._.getUTCDate()},getDay:function(){return this._.getUTCDay()},getFullYear:function(){return this._.getUTCFullYear()},getHours:function(){return this._.getUTCHours()},getMilliseconds:function(){return this._.getUTCMilliseconds()},getMinutes:function(){return this._.getUTCMinutes()},getMonth:function(){return this._.getUTCMonth()},getSeconds:function(){return this._.getUTCSeconds()},getTime:function(){return this._.getTime()},getTimezoneOffset:function(){return 0},valueOf:function(){return this._.valueOf()},setDate:function(){Gc.setUTCDate.apply(this._,arguments)},setDay:function(){Gc.setUTCDay.apply(this._,arguments)},setFullYear:function(){Gc.setUTCFullYear.apply(this._,arguments)},setHours:function(){Gc.setUTCHours.apply(this._,arguments)},setMilliseconds:function(){Gc.setUTCMilliseconds.apply(this._,arguments)},setMinutes:function(){Gc.setUTCMinutes.apply(this._,arguments)},setMonth:function(){Gc.setUTCMonth.apply(this._,arguments)},setSeconds:function(){Gc.setUTCSeconds.apply(this._,arguments)},setTime:function(){Gc.setTime.apply(this._,arguments)}};var Gc=Date.prototype,Kc="%a %b %e %X %Y",Qc="%m/%d/%Y",nl="%H:%M:%S",tl=["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"],el=["Sun","Mon","Tue","Wed","Thu","Fri","Sat"],rl=["January","February","March","April","May","June","July","August","September","October","November","December"],ul=["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];Bc.year=Ci(function(n){return n=Bc.day(n),n.setMonth(0,1),n},function(n,t){n.setFullYear(n.getFullYear()+t)},function(n){return n.getFullYear()}),Bc.years=Bc.year.range,Bc.years.utc=Bc.year.utc.range,Bc.day=Ci(function(n){var t=new Wc(2e3,0);return t.setFullYear(n.getFullYear(),n.getMonth(),n.getDate()),t},function(n,t){n.setDate(n.getDate()+t)},function(n){return n.getDate()-1}),Bc.days=Bc.day.range,Bc.days.utc=Bc.day.utc.range,Bc.dayOfYear=function(n){var t=Bc.year(n);return Math.floor((n-t-6e4*(n.getTimezoneOffset()-t.getTimezoneOffset()))/864e5)},Jc.forEach(function(n,t){n=n.toLowerCase(),t=7-t;var e=Bc[n]=Ci(function(n){return(n=Bc.day(n)).setDate(n.getDate()-(n.getDay()+t)%7),n},function(n,t){n.setDate(n.getDate()+7*Math.floor(t))},function(n){var e=Bc.year(n).getDay();return Math.floor((Bc.dayOfYear(n)+(e+t)%7)/7)-(e!==t)});Bc[n+"s"]=e.range,Bc[n+"s"].utc=e.utc.range,Bc[n+"OfYear"]=function(n){var e=Bc.year(n).getDay();return Math.floor((Bc.dayOfYear(n)+(e+t)%7)/7)}}),Bc.week=Bc.sunday,Bc.weeks=Bc.sunday.range,Bc.weeks.utc=Bc.sunday.utc.range,Bc.weekOfYear=Bc.sundayOfYear,Bc.format=ji;var il=Hi(tl),ol=Fi(tl),al=Hi(el),cl=Fi(el),ll=Hi(rl),sl=Fi(rl),fl=Hi(ul),hl=Fi(ul),gl=/^%/,pl={"-":"",_:" ",0:"0"},dl={a:function(n){return el[n.getDay()]},A:function(n){return tl[n.getDay()]},b:function(n){return ul[n.getMonth()]},B:function(n){return rl[n.getMonth()]},c:ji(Kc),d:function(n,t){return Pi(n.getDate(),t,2)},e:function(n,t){return Pi(n.getDate(),t,2)},H:function(n,t){return Pi(n.getHours(),t,2)},I:function(n,t){return Pi(n.getHours()%12||12,t,2)},j:function(n,t){return Pi(1+Bc.dayOfYear(n),t,3)},L:function(n,t){return Pi(n.getMilliseconds(),t,3)},m:function(n,t){return Pi(n.getMonth()+1,t,2)},M:function(n,t){return Pi(n.getMinutes(),t,2)},p:function(n){return n.getHours()>=12?"PM":"AM"},S:function(n,t){return Pi(n.getSeconds(),t,2)},U:function(n,t){return Pi(Bc.sundayOfYear(n),t,2)},w:function(n){return n.getDay()},W:function(n,t){return Pi(Bc.mondayOfYear(n),t,2)},x:ji(Qc),X:ji(nl),y:function(n,t){return Pi(n.getFullYear()%100,t,2)},Y:function(n,t){return Pi(n.getFullYear()%1e4,t,4)},Z:ao,"%":function(){return"%"}},vl={a:Oi,A:Ri,b:Zi,B:Vi,c:Xi,d:no,e:no,H:eo,I:eo,j:to,L:io,m:Qi,M:ro,p:oo,S:uo,U:Ii,w:Yi,W:Ui,x:$i,X:Bi,y:Ji,Y:Wi,Z:Gi,"%":co},ml=/^\s*\d+/,yl=mo.map({am:0,pm:1});ji.utc=lo;var Ml=lo("%Y-%m-%dT%H:%M:%S.%LZ");ji.iso=Date.prototype.toISOString&&+new Date("2000-01-01T00:00:00.000Z")?so:Ml,so.parse=function(n){var t=new Date(n);return isNaN(t)?null:t},so.toString=Ml.toString,Bc.second=Ci(function(n){return new Wc(1e3*Math.floor(n/1e3))},function(n,t){n.setTime(n.getTime()+1e3*Math.floor(t))},function(n){return n.getSeconds()}),Bc.seconds=Bc.second.range,Bc.seconds.utc=Bc.second.utc.range,Bc.minute=Ci(function(n){return new Wc(6e4*Math.floor(n/6e4))},function(n,t){n.setTime(n.getTime()+6e4*Math.floor(t))},function(n){return n.getMinutes()}),Bc.minutes=Bc.minute.range,Bc.minutes.utc=Bc.minute.utc.range,Bc.hour=Ci(function(n){var t=n.getTimezoneOffset()/60;return new Wc(36e5*(Math.floor(n/36e5-t)+t))},function(n,t){n.setTime(n.getTime()+36e5*Math.floor(t))},function(n){return n.getHours()}),Bc.hours=Bc.hour.range,Bc.hours.utc=Bc.hour.utc.range,Bc.month=Ci(function(n){return n=Bc.day(n),n.setDate(1),n},function(n,t){n.setMonth(n.getMonth()+t)},function(n){return n.getMonth()}),Bc.months=Bc.month.range,Bc.months.utc=Bc.month.utc.range;var xl=[1e3,5e3,15e3,3e4,6e4,3e5,9e5,18e5,36e5,108e5,216e5,432e5,864e5,1728e5,6048e5,2592e6,7776e6,31536e6],bl=[[Bc.second,1],[Bc.second,5],[Bc.second,15],[Bc.second,30],[Bc.minute,1],[Bc.minute,5],[Bc.minute,15],[Bc.minute,30],[Bc.hour,1],[Bc.hour,3],[Bc.hour,6],[Bc.hour,12],[Bc.day,1],[Bc.day,2],[Bc.week,1],[Bc.month,1],[Bc.month,3],[Bc.year,1]],_l=[[ji("%Y"),Vt],[ji("%B"),function(n){return n.getMonth()}],[ji("%b %d"),function(n){return 1!=n.getDate()}],[ji("%a %d"),function(n){return n.getDay()&&1!=n.getDate()}],[ji("%I %p"),function(n){return n.getHours()}],[ji("%I:%M"),function(n){return n.getMinutes()}],[ji(":%S"),function(n){return n.getSeconds()}],[ji(".%L"),function(n){return n.getMilliseconds()}]],wl=go(_l);bl.year=Bc.year,Bc.scale=function(){return fo(mo.scale.linear(),bl,wl)};var Sl={range:function(n,t,e){return mo.range(+n,+t,e).map(ho)}},El=bl.map(function(n){return[n[0].utc,n[1]]}),kl=[[lo("%Y"),Vt],[lo("%B"),function(n){return n.getUTCMonth()}],[lo("%b %d"),function(n){return 1!=n.getUTCDate()}],[lo("%a %d"),function(n){return n.getUTCDay()&&1!=n.getUTCDate()}],[lo("%I %p"),function(n){return n.getUTCHours()}],[lo("%I:%M"),function(n){return n.getUTCMinutes()}],[lo(":%S"),function(n){return n.getUTCSeconds()}],[lo(".%L"),function(n){return n.getUTCMilliseconds()}]],Al=go(kl);return El.year=Bc.year.utc,Bc.scale.utc=function(){return fo(mo.scale.linear(),El,Al)},mo.text=vt(function(n){return n.responseText}),mo.json=function(n,t){return mt(n,"application/json",po,t)},mo.html=function(n,t){return mt(n,"text/html",vo,t)},mo.xml=vt(function(n){return n.responseXML}),mo}();
      // end d3 source
      return d3;
    }]);

}());


// angular.module('d3', [])
// .factory('d3', [function(){
//     var d3;
//     d3 = function() {
//         var d3 = {
//             version: "3.3.9"
//         };
//         if (!Date.now) Date.now = function() {
//             return +new Date();
//         };
//         var d3_arraySlice = [].slice, d3_array = function(list) {
//             return d3_arraySlice.call(list);
//         };
//         var d3_document = document, d3_documentElement = d3_document.documentElement, d3_window = window;
//         try {
//             d3_array(d3_documentElement.childNodes)[0].nodeType;
//         } catch (e) {
//             d3_array = function(list) {
//                 var i = list.length, array = new Array(i);
//                 while (i--) array[i] = list[i];
//                 return array;
//             };
//         }
//         try {
//             d3_document.createElement("div").style.setProperty("opacity", 0, "");
//         } catch (error) {
//             var d3_element_prototype = d3_window.Element.prototype, d3_element_setAttribute = d3_element_prototype.setAttribute, d3_element_setAttributeNS = d3_element_prototype.setAttributeNS, d3_style_prototype = d3_window.CSSStyleDeclaration.prototype, d3_style_setProperty = d3_style_prototype.setProperty;
//             d3_element_prototype.setAttribute = function(name, value) {
//                 d3_element_setAttribute.call(this, name, value + "");
//             };
//             d3_element_prototype.setAttributeNS = function(space, local, value) {
//                 d3_element_setAttributeNS.call(this, space, local, value + "");
//             };
//             d3_style_prototype.setProperty = function(name, value, priority) {
//                 d3_style_setProperty.call(this, name, value + "", priority);
//             };
//         }
//         d3.ascending = function(a, b) {
//             return a < b ? -1 : a > b ? 1 : a >= b ? 0 : NaN;
//         };
//         d3.descending = function(a, b) {
//             return b < a ? -1 : b > a ? 1 : b >= a ? 0 : NaN;
//         };
//         d3.min = function(array, f) {
//             var i = -1, n = array.length, a, b;
//             if (arguments.length === 1) {
//                 while (++i < n && !((a = array[i]) != null && a <= a)) a = undefined;
//                 while (++i < n) if ((b = array[i]) != null && a > b) a = b;
//             } else {
//                 while (++i < n && !((a = f.call(array, array[i], i)) != null && a <= a)) a = undefined;
//                 while (++i < n) if ((b = f.call(array, array[i], i)) != null && a > b) a = b;
//             }
//             return a;
//         };
//         d3.max = function(array, f) {
//             var i = -1, n = array.length, a, b;
//             if (arguments.length === 1) {
//                 while (++i < n && !((a = array[i]) != null && a <= a)) a = undefined;
//                 while (++i < n) if ((b = array[i]) != null && b > a) a = b;
//             } else {
//                 while (++i < n && !((a = f.call(array, array[i], i)) != null && a <= a)) a = undefined;
//                 while (++i < n) if ((b = f.call(array, array[i], i)) != null && b > a) a = b;
//             }
//             return a;
//         };
//         d3.extent = function(array, f) {
//             var i = -1, n = array.length, a, b, c;
//             if (arguments.length === 1) {
//                 while (++i < n && !((a = c = array[i]) != null && a <= a)) a = c = undefined;
//                 while (++i < n) if ((b = array[i]) != null) {
//                     if (a > b) a = b;
//                     if (c < b) c = b;
//                 }
//             } else {
//                 while (++i < n && !((a = c = f.call(array, array[i], i)) != null && a <= a)) a = undefined;
//                 while (++i < n) if ((b = f.call(array, array[i], i)) != null) {
//                     if (a > b) a = b;
//                     if (c < b) c = b;
//                 }
//             }
//             return [ a, c ];
//         };
//         d3.sum = function(array, f) {
//             var s = 0, n = array.length, a, i = -1;
//             if (arguments.length === 1) {
//                 while (++i < n) if (!isNaN(a = +array[i])) s += a;
//             } else {
//                 while (++i < n) if (!isNaN(a = +f.call(array, array[i], i))) s += a;
//             }
//             return s;
//         };
//         function d3_number(x) {
//             return x != null && !isNaN(x);
//         }
//         d3.mean = function(array, f) {
//             var n = array.length, a, m = 0, i = -1, j = 0;
//             if (arguments.length === 1) {
//                 while (++i < n) if (d3_number(a = array[i])) m += (a - m) / ++j;
//             } else {
//                 while (++i < n) if (d3_number(a = f.call(array, array[i], i))) m += (a - m) / ++j;
//             }
//             return j ? m : undefined;
//         };
//         d3.quantile = function(values, p) {
//             var H = (values.length - 1) * p + 1, h = Math.floor(H), v = +values[h - 1], e = H - h;
//             return e ? v + e * (values[h] - v) : v;
//         };
//         d3.median = function(array, f) {
//             if (arguments.length > 1) array = array.map(f);
//             array = array.filter(d3_number);
//             return array.length ? d3.quantile(array.sort(d3.ascending), .5) : undefined;
//         };
//         d3.bisector = function(f) {
//             return {
//                 left: function(a, x, lo, hi) {
//                     if (arguments.length < 3) lo = 0;
//                     if (arguments.length < 4) hi = a.length;
//                     while (lo < hi) {
//                         var mid = lo + hi >>> 1;
//                         if (f.call(a, a[mid], mid) < x) lo = mid + 1; else hi = mid;
//                     }
//                     return lo;
//                 },
//                     right: function(a, x, lo, hi) {
//                         if (arguments.length < 3) lo = 0;
//                         if (arguments.length < 4) hi = a.length;
//                         while (lo < hi) {
//                             var mid = lo + hi >>> 1;
//                             if (x < f.call(a, a[mid], mid)) hi = mid; else lo = mid + 1;
//                         }
//                         return lo;
//                     }
//             };
//         };
//         var d3_bisector = d3.bisector(function(d) {
//             return d;
//         });
//         d3.bisectLeft = d3_bisector.left;
//         d3.bisect = d3.bisectRight = d3_bisector.right;
//         d3.shuffle = function(array) {
//             var m = array.length, t, i;
//             while (m) {
//                 i = Math.random() * m-- | 0;
//                 t = array[m], array[m] = array[i], array[i] = t;
//             }
//             return array;
//         };
//         d3.permute = function(array, indexes) {
//             var i = indexes.length, permutes = new Array(i);
//             while (i--) permutes[i] = array[indexes[i]];
//             return permutes;
//         };
//         d3.pairs = function(array) {
//             var i = 0, n = array.length - 1, p0, p1 = array[0], pairs = new Array(n < 0 ? 0 : n);
//             while (i < n) pairs[i] = [ p0 = p1, p1 = array[++i] ];
//             return pairs;
//         };
//         d3.zip = function() {
//             if (!(n = arguments.length)) return [];
//             for (var i = -1, m = d3.min(arguments, d3_zipLength), zips = new Array(m); ++i < m; ) {
//                 for (var j = -1, n, zip = zips[i] = new Array(n); ++j < n; ) {
//                     zip[j] = arguments[j][i];
//                 }
//             }
//             return zips;
//         };
//         function d3_zipLength(d) {
//             return d.length;
//         }
//         d3.transpose = function(matrix) {
//             return d3.zip.apply(d3, matrix);
//         };
//         d3.keys = function(map) {
//             var keys = [];
//             for (var key in map) keys.push(key);
//             return keys;
//         };
//         d3.values = function(map) {
//             var values = [];
//             for (var key in map) values.push(map[key]);
//             return values;
//         };
//         d3.entries = function(map) {
//             var entries = [];
//             for (var key in map) entries.push({
//                 key: key,
//                 value: map[key]
//             });
//             return entries;
//         };
//         d3.merge = function(arrays) {
//             var n = arrays.length, m, i = -1, j = 0, merged, array;
//             while (++i < n) j += arrays[i].length;
//             merged = new Array(j);
//             while (--n >= 0) {
//                 array = arrays[n];
//                 m = array.length;
//                 while (--m >= 0) {
//                     merged[--j] = array[m];
//                 }
//             }
//             return merged;
//         };
//         var abs = Math.abs;
//         d3.range = function(start, stop, step) {
//             if (arguments.length < 3) {
//                 step = 1;
//                 if (arguments.length < 2) {
//                     stop = start;
//                     start = 0;
//                 }
//             }
//             if ((stop - start) / step === Infinity) throw new Error("infinite range");
//             var range = [], k = d3_range_integerScale(abs(step)), i = -1, j;
//             start *= k, stop *= k, step *= k;
//             if (step < 0) while ((j = start + step * ++i) > stop) range.push(j / k); else while ((j = start + step * ++i) < stop) range.push(j / k);
//             return range;
//         };
//         function d3_range_integerScale(x) {
//             var k = 1;
//             while (x * k % 1) k *= 10;
//             return k;
//         }
//         function d3_class(ctor, properties) {
//             try {
//                 for (var key in properties) {
//                     Object.defineProperty(ctor.prototype, key, {
//                         value: properties[key],
//                         enumerable: false
//                     });
//                 }
//             } catch (e) {
//                 ctor.prototype = properties;
//             }
//         }
//         d3.map = function(object) {
//             var map = new d3_Map();
//             if (object instanceof d3_Map) object.forEach(function(key, value) {
//                 map.set(key, value);
//             }); else for (var key in object) map.set(key, object[key]);
//             return map;
//         };
//         function d3_Map() {}
//         d3_class(d3_Map, {
//             has: function(key) {
//                 return d3_map_prefix + key in this;
//             },
//             get: function(key) {
//                 return this[d3_map_prefix + key];
//             },
//             set: function(key, value) {
//                 return this[d3_map_prefix + key] = value;
//             },
//             remove: function(key) {
//                 key = d3_map_prefix + key;
//                 return key in this && delete this[key];
//             },
//             keys: function() {
//                 var keys = [];
//                 this.forEach(function(key) {
//                     keys.push(key);
//                 });
//                 return keys;
//             },
//             values: function() {
//                 var values = [];
//                 this.forEach(function(key, value) {
//                     values.push(value);
//                 });
//                 return values;
//             },
//             entries: function() {
//                 var entries = [];
//                 this.forEach(function(key, value) {
//                     entries.push({
//                         key: key,
//                         value: value
//                     });
//                 });
//                 return entries;
//             },
//             forEach: function(f) {
//                 for (var key in this) {
//                     if (key.charCodeAt(0) === d3_map_prefixCode) {
//                         f.call(this, key.substring(1), this[key]);
//                     }
//                 }
//             }
//         });
//         var d3_map_prefix = "\x00", d3_map_prefixCode = d3_map_prefix.charCodeAt(0);
//         d3.nest = function() {
//             var nest = {}, keys = [], sortKeys = [], sortValues, rollup;
//             function map(mapType, array, depth) {
//                 if (depth >= keys.length) return rollup ? rollup.call(nest, array) : sortValues ? array.sort(sortValues) : array;
//                 var i = -1, n = array.length, key = keys[depth++], keyValue, object, setter, valuesByKey = new d3_Map(), values;
//                 while (++i < n) {
//                     if (values = valuesByKey.get(keyValue = key(object = array[i]))) {
//                         values.push(object);
//                     } else {
//                         valuesByKey.set(keyValue, [ object ]);
//                     }
//                 }
//                 if (mapType) {
//                     object = mapType();
//                     setter = function(keyValue, values) {
//                         object.set(keyValue, map(mapType, values, depth));
//                     };
//                 } else {
//                     object = {};
//                     setter = function(keyValue, values) {
//                         object[keyValue] = map(mapType, values, depth);
//                     };
//                 }
//                 valuesByKey.forEach(setter);
//                 return object;
//             }
//             function entries(map, depth) {
//                 if (depth >= keys.length) return map;
//                 var array = [], sortKey = sortKeys[depth++];
//                 map.forEach(function(key, keyMap) {
//                     array.push({
//                         key: key,
//                         values: entries(keyMap, depth)
//                     });
//                 });
//                 return sortKey ? array.sort(function(a, b) {
//                     return sortKey(a.key, b.key);
//                 }) : array;
//             }
//             nest.map = function(array, mapType) {
//                 return map(mapType, array, 0);
//             };
//             nest.entries = function(array) {
//                 return entries(map(d3.map, array, 0), 0);
//             };
//             nest.key = function(d) {
//                 keys.push(d);
//                 return nest;
//             };
//             nest.sortKeys = function(order) {
//                 sortKeys[keys.length - 1] = order;
//                 return nest;
//             };
//             nest.sortValues = function(order) {
//                 sortValues = order;
//                 return nest;
//             };
//             nest.rollup = function(f) {
//                 rollup = f;
//                 return nest;
//             };
//             return nest;
//         };
//         d3.set = function(array) {
//             var set = new d3_Set();
//             if (array) for (var i = 0, n = array.length; i < n; ++i) set.add(array[i]);
//             return set;
//         };
//         function d3_Set() {}
//         d3_class(d3_Set, {
//             has: function(value) {
//                 return d3_map_prefix + value in this;
//             },
//             add: function(value) {
//                 this[d3_map_prefix + value] = true;
//                 return value;
//             },
//             remove: function(value) {
//                 value = d3_map_prefix + value;
//                 return value in this && delete this[value];
//             },
//             values: function() {
//                 var values = [];
//                 this.forEach(function(value) {
//                     values.push(value);
//                 });
//                 return values;
//             },
//             forEach: function(f) {
//                 for (var value in this) {
//                     if (value.charCodeAt(0) === d3_map_prefixCode) {
//                         f.call(this, value.substring(1));
//                     }
//                 }
//             }
//         });
//         d3.behavior = {};
//         d3.rebind = function(target, source) {
//             var i = 1, n = arguments.length, method;
//             while (++i < n) target[method = arguments[i]] = d3_rebind(target, source, source[method]);
//             return target;
//         };
//         function d3_rebind(target, source, method) {
//             return function() {
//                 var value = method.apply(source, arguments);
//                 return value === source ? target : value;
//             };
//         }
//         function d3_vendorSymbol(object, name) {
//             if (name in object) return name;
//             name = name.charAt(0).toUpperCase() + name.substring(1);
//             for (var i = 0, n = d3_vendorPrefixes.length; i < n; ++i) {
//                 var prefixName = d3_vendorPrefixes[i] + name;
//                 if (prefixName in object) return prefixName;
//             }
//         }
//         var d3_vendorPrefixes = [ "webkit", "ms", "moz", "Moz", "o", "O" ];
//         function d3_noop() {}
//         d3.dispatch = function() {
//             var dispatch = new d3_dispatch(), i = -1, n = arguments.length;
//             while (++i < n) dispatch[arguments[i]] = d3_dispatch_event(dispatch);
//             return dispatch;
//         };
//         function d3_dispatch() {}
//         d3_dispatch.prototype.on = function(type, listener) {
//             var i = type.indexOf("."), name = "";
//             if (i >= 0) {
//                 name = type.substring(i + 1);
//                 type = type.substring(0, i);
//             }
//             if (type) return arguments.length < 2 ? this[type].on(name) : this[type].on(name, listener);
//             if (arguments.length === 2) {
//                 if (listener == null) for (type in this) {
//                     if (this.hasOwnProperty(type)) this[type].on(name, null);
//                 }
//                 return this;
//             }
//         };
//         function d3_dispatch_event(dispatch) {
//             var listeners = [], listenerByName = new d3_Map();
//             function event() {
//                 var z = listeners, i = -1, n = z.length, l;
//                 while (++i < n) if (l = z[i].on) l.apply(this, arguments);
//                 return dispatch;
//             }
//             event.on = function(name, listener) {
//                 var l = listenerByName.get(name), i;
//                 if (arguments.length < 2) return l && l.on;
//                 if (l) {
//                     l.on = null;
//                     listeners = listeners.slice(0, i = listeners.indexOf(l)).concat(listeners.slice(i + 1));
//                     listenerByName.remove(name);
//                 }
//                 if (listener) listeners.push(listenerByName.set(name, {
//                     on: listener
//                 }));
//                 return dispatch;
//             };
//             return event;
//         }
//         d3.event = null;
//         function d3_eventPreventDefault() {
//             d3.event.preventDefault();
//         }
//         function d3_eventSource() {
//             var e = d3.event, s;
//             while (s = e.sourceEvent) e = s;
//             return e;
//         }
//         function d3_eventDispatch(target) {
//             var dispatch = new d3_dispatch(), i = 0, n = arguments.length;
//             while (++i < n) dispatch[arguments[i]] = d3_dispatch_event(dispatch);
//             dispatch.of = function(thiz, argumentz) {
//                 return function(e1) {
//                     try {
//                         var e0 = e1.sourceEvent = d3.event;
//                         e1.target = target;
//                         d3.event = e1;
//                         dispatch[e1.type].apply(thiz, argumentz);
//                     } finally {
//                         d3.event = e0;
//                     }
//                 };
//             };
//             return dispatch;
//         }
//         d3.requote = function(s) {
//             return s.replace(d3_requote_re, "\\$&");
//         };
//         var d3_requote_re = /[\\\^\$\*\+\?\|\[\]\(\)\.\{\}]/g;
//         var d3_subclass = {}.__proto__ ? function(object, prototype) {
//             object.__proto__ = prototype;
//         } : function(object, prototype) {
//             for (var property in prototype) object[property] = prototype[property];
//         };
//         function d3_selection(groups) {
//             d3_subclass(groups, d3_selectionPrototype);
//             return groups;
//         }
//         var d3_select = function(s, n) {
//             return n.querySelector(s);
//         }, d3_selectAll = function(s, n) {
//             return n.querySelectorAll(s);
//         }, d3_selectMatcher = d3_documentElement[d3_vendorSymbol(d3_documentElement, "matchesSelector")], d3_selectMatches = function(n, s) {
//             return d3_selectMatcher.call(n, s);
//         };
//         if (typeof Sizzle === "function") {
//             d3_select = function(s, n) {
//                 return Sizzle(s, n)[0] || null;
//             };
//             d3_selectAll = function(s, n) {
//                 return Sizzle.uniqueSort(Sizzle(s, n));
//             };
//             d3_selectMatches = Sizzle.matchesSelector;
//         }
//         d3.selection = function() {
//             return d3_selectionRoot;
//         };
//         var d3_selectionPrototype = d3.selection.prototype = [];
//         d3_selectionPrototype.select = function(selector) {
//             var subgroups = [], subgroup, subnode, group, node;
//             selector = d3_selection_selector(selector);
//             for (var j = -1, m = this.length; ++j < m; ) {
//                 subgroups.push(subgroup = []);
//                 subgroup.parentNode = (group = this[j]).parentNode;
//                 for (var i = -1, n = group.length; ++i < n; ) {
//                     if (node = group[i]) {
//                         subgroup.push(subnode = selector.call(node, node.__data__, i, j));
//                         if (subnode && "__data__" in node) subnode.__data__ = node.__data__;
//                     } else {
//                         subgroup.push(null);
//                     }
//                 }
//             }
//             return d3_selection(subgroups);
//         };
//         function d3_selection_selector(selector) {
//             return typeof selector === "function" ? selector : function() {
//                 return d3_select(selector, this);
//             };
//         }
//         d3_selectionPrototype.selectAll = function(selector) {
//             var subgroups = [], subgroup, node;
//             selector = d3_selection_selectorAll(selector);
//             for (var j = -1, m = this.length; ++j < m; ) {
//                 for (var group = this[j], i = -1, n = group.length; ++i < n; ) {
//                     if (node = group[i]) {
//                         subgroups.push(subgroup = d3_array(selector.call(node, node.__data__, i, j)));
//                         subgroup.parentNode = node;
//                     }
//                 }
//             }
//             return d3_selection(subgroups);
//         };
//         function d3_selection_selectorAll(selector) {
//             return typeof selector === "function" ? selector : function() {
//                 return d3_selectAll(selector, this);
//             };
//         }
//         var d3_nsPrefix = {
//             svg: "http://www.w3.org/2000/svg",
//             xhtml: "http://www.w3.org/1999/xhtml",
//             xlink: "http://www.w3.org/1999/xlink",
//             xml: "http://www.w3.org/XML/1998/namespace",
//             xmlns: "http://www.w3.org/2000/xmlns/"
//         };
//         d3.ns = {
//             prefix: d3_nsPrefix,
//             qualify: function(name) {
//                 var i = name.indexOf(":"), prefix = name;
//                 if (i >= 0) {
//                     prefix = name.substring(0, i);
//                     name = name.substring(i + 1);
//                 }
//                 return d3_nsPrefix.hasOwnProperty(prefix) ? {
//                     space: d3_nsPrefix[prefix],
//                         local: name
//                 } : name;
//             }
//         };
//         d3_selectionPrototype.attr = function(name, value) {
//             if (arguments.length < 2) {
//                 if (typeof name === "string") {
//                     var node = this.node();
//                     name = d3.ns.qualify(name);
//                     return name.local ? node.getAttributeNS(name.space, name.local) : node.getAttribute(name);
//                 }
//                 for (value in name) this.each(d3_selection_attr(value, name[value]));
//                 return this;
//             }
//             return this.each(d3_selection_attr(name, value));
//         };
//         function d3_selection_attr(name, value) {
//             name = d3.ns.qualify(name);
//             function attrNull() {
//                 this.removeAttribute(name);
//             }
//             function attrNullNS() {
//                 this.removeAttributeNS(name.space, name.local);
//             }
//             function attrConstant() {
//                 this.setAttribute(name, value);
//             }
//             function attrConstantNS() {
//                 this.setAttributeNS(name.space, name.local, value);
//             }
//             function attrFunction() {
//                 var x = value.apply(this, arguments);
//                 if (x == null) this.removeAttribute(name); else this.setAttribute(name, x);
//             }
//             function attrFunctionNS() {
//                 var x = value.apply(this, arguments);
//                 if (x == null) this.removeAttributeNS(name.space, name.local); else this.setAttributeNS(name.space, name.local, x);
//             }
//             return value == null ? name.local ? attrNullNS : attrNull : typeof value === "function" ? name.local ? attrFunctionNS : attrFunction : name.local ? attrConstantNS : attrConstant;
//         }
//         function d3_collapse(s) {
//             return s.trim().replace(/\s+/g, " ");
//         }
//         d3_selectionPrototype.classed = function(name, value) {
//             if (arguments.length < 2) {
//                 if (typeof name === "string") {
//                     var node = this.node(), n = (name = name.trim().split(/^|\s+/g)).length, i = -1;
//                     if (value = node.classList) {
//                         while (++i < n) if (!value.contains(name[i])) return false;
//                     } else {
//                         value = node.getAttribute("class");
//                         while (++i < n) if (!d3_selection_classedRe(name[i]).test(value)) return false;
//                     }
//                     return true;
//                 }
//                 for (value in name) this.each(d3_selection_classed(value, name[value]));
//                 return this;
//             }
//             return this.each(d3_selection_classed(name, value));
//         };
//         function d3_selection_classedRe(name) {
//             return new RegExp("(?:^|\\s+)" + d3.requote(name) + "(?:\\s+|$)", "g");
//         }
//         function d3_selection_classed(name, value) {
//             name = name.trim().split(/\s+/).map(d3_selection_classedName);
//             var n = name.length;
//             function classedConstant() {
//                 var i = -1;
//                 while (++i < n) name[i](this, value);
//             }
//             function classedFunction() {
//                 var i = -1, x = value.apply(this, arguments);
//                 while (++i < n) name[i](this, x);
//             }
//             return typeof value === "function" ? classedFunction : classedConstant;
//         }
//         function d3_selection_classedName(name) {
//             var re = d3_selection_classedRe(name);
//             return function(node, value) {
//                 if (c = node.classList) return value ? c.add(name) : c.remove(name);
//                 var c = node.getAttribute("class") || "";
//                 if (value) {
//                     re.lastIndex = 0;
//                     if (!re.test(c)) node.setAttribute("class", d3_collapse(c + " " + name));
//                 } else {
//                     node.setAttribute("class", d3_collapse(c.replace(re, " ")));
//                 }
//             };
//         }
//         d3_selectionPrototype.style = function(name, value, priority) {
//             var n = arguments.length;
//             if (n < 3) {
//                 if (typeof name !== "string") {
//                     if (n < 2) value = "";
//                     for (priority in name) this.each(d3_selection_style(priority, name[priority], value));
//                     return this;
//                 }
//                 if (n < 2) return d3_window.getComputedStyle(this.node(), null).getPropertyValue(name);
//                 priority = "";
//             }
//             return this.each(d3_selection_style(name, value, priority));
//         };
//         function d3_selection_style(name, value, priority) {
//             function styleNull() {
//                 this.style.removeProperty(name);
//             }
//             function styleConstant() {
//                 this.style.setProperty(name, value, priority);
//             }
//             function styleFunction() {
//                 var x = value.apply(this, arguments);
//                 if (x == null) this.style.removeProperty(name); else this.style.setProperty(name, x, priority);
//             }
//             return value == null ? styleNull : typeof value === "function" ? styleFunction : styleConstant;
//         }
//         d3_selectionPrototype.property = function(name, value) {
//             if (arguments.length < 2) {
//                 if (typeof name === "string") return this.node()[name];
//                 for (value in name) this.each(d3_selection_property(value, name[value]));
//                 return this;
//             }
//             return this.each(d3_selection_property(name, value));
//         };
//         function d3_selection_property(name, value) {
//             function propertyNull() {
//                 delete this[name];
//             }
//             function propertyConstant() {
//                 this[name] = value;
//             }
//             function propertyFunction() {
//                 var x = value.apply(this, arguments);
//                 if (x == null) delete this[name]; else this[name] = x;
//             }
//             return value == null ? propertyNull : typeof value === "function" ? propertyFunction : propertyConstant;
//         }
//         d3_selectionPrototype.text = function(value) {
//             return arguments.length ? this.each(typeof value === "function" ? function() {
//                 var v = value.apply(this, arguments);
//                 this.textContent = v == null ? "" : v;
//             } : value == null ? function() {
//                 this.textContent = "";
//             } : function() {
//                 this.textContent = value;
//             }) : this.node().textContent;
//         };
//         d3_selectionPrototype.html = function(value) {
//             return arguments.length ? this.each(typeof value === "function" ? function() {
//                 var v = value.apply(this, arguments);
//                 this.innerHTML = v == null ? "" : v;
//             } : value == null ? function() {
//                 this.innerHTML = "";
//             } : function() {
//                 this.innerHTML = value;
//             }) : this.node().innerHTML;
//         };
//         d3_selectionPrototype.append = function(name) {
//             name = d3_selection_creator(name);
//             return this.select(function() {
//                 return this.appendChild(name.apply(this, arguments));
//             });
//         };
//         function d3_selection_creator(name) {
//             return typeof name === "function" ? name : (name = d3.ns.qualify(name)).local ? function() {
//                 return this.ownerDocument.createElementNS(name.space, name.local);
//             } : function() {
//                 return this.ownerDocument.createElementNS(this.namespaceURI, name);
//             };
//         }
//         d3_selectionPrototype.insert = function(name, before) {
//             name = d3_selection_creator(name);
//             before = d3_selection_selector(before);
//             return this.select(function() {
//                 return this.insertBefore(name.apply(this, arguments), before.apply(this, arguments) || null);
//             });
//         };
//         d3_selectionPrototype.remove = function() {
//             return this.each(function() {
//                 var parent = this.parentNode;
//                 if (parent) parent.removeChild(this);
//             });
//         };
//         d3_selectionPrototype.data = function(value, key) {
//             var i = -1, n = this.length, group, node;
//             if (!arguments.length) {
//                 value = new Array(n = (group = this[0]).length);
//                 while (++i < n) {
//                     if (node = group[i]) {
//                         value[i] = node.__data__;
//                     }
//                 }
//                 return value;
//             }
//             function bind(group, groupData) {
//                 var i, n = group.length, m = groupData.length, n0 = Math.min(n, m), updateNodes = new Array(m), enterNodes = new Array(m), exitNodes = new Array(n), node, nodeData;
//                 if (key) {
//                     var nodeByKeyValue = new d3_Map(), dataByKeyValue = new d3_Map(), keyValues = [], keyValue;
//                     for (i = -1; ++i < n; ) {
//                         keyValue = key.call(node = group[i], node.__data__, i);
//                         if (nodeByKeyValue.has(keyValue)) {
//                             exitNodes[i] = node;
//                         } else {
//                             nodeByKeyValue.set(keyValue, node);
//                         }
//                         keyValues.push(keyValue);
//                     }
//                     for (i = -1; ++i < m; ) {
//                         keyValue = key.call(groupData, nodeData = groupData[i], i);
//                         if (node = nodeByKeyValue.get(keyValue)) {
//                             updateNodes[i] = node;
//                             node.__data__ = nodeData;
//                         } else if (!dataByKeyValue.has(keyValue)) {
//                             enterNodes[i] = d3_selection_dataNode(nodeData);
//                         }
//                         dataByKeyValue.set(keyValue, nodeData);
//                         nodeByKeyValue.remove(keyValue);
//                     }
//                     for (i = -1; ++i < n; ) {
//                         if (nodeByKeyValue.has(keyValues[i])) {
//                             exitNodes[i] = group[i];
//                         }
//                     }
//                 } else {
//                     for (i = -1; ++i < n0; ) {
//                         node = group[i];
//                         nodeData = groupData[i];
//                         if (node) {
//                             node.__data__ = nodeData;
//                             updateNodes[i] = node;
//                         } else {
//                             enterNodes[i] = d3_selection_dataNode(nodeData);
//                         }
//                     }
//                     for (;i < m; ++i) {
//                         enterNodes[i] = d3_selection_dataNode(groupData[i]);
//                     }
//                     for (;i < n; ++i) {
//                         exitNodes[i] = group[i];
//                     }
//                 }
//                 enterNodes.update = updateNodes;
//                 enterNodes.parentNode = updateNodes.parentNode = exitNodes.parentNode = group.parentNode;
//                 enter.push(enterNodes);
//                 update.push(updateNodes);
//                 exit.push(exitNodes);
//             }
//             var enter = d3_selection_enter([]), update = d3_selection([]), exit = d3_selection([]);
//             if (typeof value === "function") {
//                 while (++i < n) {
//                     bind(group = this[i], value.call(group, group.parentNode.__data__, i));
//                 }
//             } else {
//                 while (++i < n) {
//                     bind(group = this[i], value);
//                 }
//             }
//             update.enter = function() {
//                 return enter;
//             };
//             update.exit = function() {
//                 return exit;
//             };
//             return update;
//         };
//         function d3_selection_dataNode(data) {
//             return {
//                 __data__: data
//             };
//         }
//         d3_selectionPrototype.datum = function(value) {
//             return arguments.length ? this.property("__data__", value) : this.property("__data__");
//         };
//         d3_selectionPrototype.filter = function(filter) {
//             var subgroups = [], subgroup, group, node;
//             if (typeof filter !== "function") filter = d3_selection_filter(filter);
//             for (var j = 0, m = this.length; j < m; j++) {
//                 subgroups.push(subgroup = []);
//                 subgroup.parentNode = (group = this[j]).parentNode;
//                 for (var i = 0, n = group.length; i < n; i++) {
//                     if ((node = group[i]) && filter.call(node, node.__data__, i)) {
//                         subgroup.push(node);
//                     }
//                 }
//             }
//             return d3_selection(subgroups);
//         };
//         function d3_selection_filter(selector) {
//             return function() {
//                 return d3_selectMatches(this, selector);
//             };
//         }
//         d3_selectionPrototype.order = function() {
//             for (var j = -1, m = this.length; ++j < m; ) {
//                 for (var group = this[j], i = group.length - 1, next = group[i], node; --i >= 0; ) {
//                     if (node = group[i]) {
//                         if (next && next !== node.nextSibling) next.parentNode.insertBefore(node, next);
//                         next = node;
//                     }
//                 }
//             }
//             return this;
//         };
//         d3_selectionPrototype.sort = function(comparator) {
//             comparator = d3_selection_sortComparator.apply(this, arguments);
//             for (var j = -1, m = this.length; ++j < m; ) this[j].sort(comparator);
//             return this.order();
//         };
//         function d3_selection_sortComparator(comparator) {
//             if (!arguments.length) comparator = d3.ascending;
//             return function(a, b) {
//                 return a && b ? comparator(a.__data__, b.__data__) : !a - !b;
//             };
//         }
//         d3_selectionPrototype.each = function(callback) {
//             return d3_selection_each(this, function(node, i, j) {
//                 callback.call(node, node.__data__, i, j);
//             });
//         };
//         function d3_selection_each(groups, callback) {
//             for (var j = 0, m = groups.length; j < m; j++) {
//                 for (var group = groups[j], i = 0, n = group.length, node; i < n; i++) {
//                     if (node = group[i]) callback(node, i, j);
//                 }
//             }
//             return groups;
//         }
//         d3_selectionPrototype.call = function(callback) {
//             var args = d3_array(arguments);
//             callback.apply(args[0] = this, args);
//             return this;
//         };
//         d3_selectionPrototype.empty = function() {
//             return !this.node();
//         };
//         d3_selectionPrototype.node = function() {
//             for (var j = 0, m = this.length; j < m; j++) {
//                 for (var group = this[j], i = 0, n = group.length; i < n; i++) {
//                     var node = group[i];
//                     if (node) return node;
//                 }
//             }
//             return null;
//         };
//         d3_selectionPrototype.size = function() {
//             var n = 0;
//             this.each(function() {
//                 ++n;
//             });
//             return n;
//         };
//         function d3_selection_enter(selection) {
//             d3_subclass(selection, d3_selection_enterPrototype);
//             return selection;
//         }
//         var d3_selection_enterPrototype = [];
//         d3.selection.enter = d3_selection_enter;
//         d3.selection.enter.prototype = d3_selection_enterPrototype;
//         d3_selection_enterPrototype.append = d3_selectionPrototype.append;
//         d3_selection_enterPrototype.empty = d3_selectionPrototype.empty;
//         d3_selection_enterPrototype.node = d3_selectionPrototype.node;
//         d3_selection_enterPrototype.call = d3_selectionPrototype.call;
//         d3_selection_enterPrototype.size = d3_selectionPrototype.size;
//         d3_selection_enterPrototype.select = function(selector) {
//             var subgroups = [], subgroup, subnode, upgroup, group, node;
//             for (var j = -1, m = this.length; ++j < m; ) {
//                 upgroup = (group = this[j]).update;
//                 subgroups.push(subgroup = []);
//                 subgroup.parentNode = group.parentNode;
//                 for (var i = -1, n = group.length; ++i < n; ) {
//                     if (node = group[i]) {
//                         subgroup.push(upgroup[i] = subnode = selector.call(group.parentNode, node.__data__, i, j));
//                         subnode.__data__ = node.__data__;
//                     } else {
//                         subgroup.push(null);
//                     }
//                 }
//             }
//             return d3_selection(subgroups);
//         };
//         d3_selection_enterPrototype.insert = function(name, before) {
//             if (arguments.length < 2) before = d3_selection_enterInsertBefore(this);
//             return d3_selectionPrototype.insert.call(this, name, before);
//         };
//         function d3_selection_enterInsertBefore(enter) {
//             var i0, j0;
//             return function(d, i, j) {
//                 var group = enter[j].update, n = group.length, node;
//                 if (j != j0) j0 = j, i0 = 0;
//                 if (i >= i0) i0 = i + 1;
//                 while (!(node = group[i0]) && ++i0 < n) ;
//                 return node;
//             };
//         }
//         d3_selectionPrototype.transition = function() {
//             var id = d3_transitionInheritId || ++d3_transitionId, subgroups = [], subgroup, node, transition = d3_transitionInherit || {
//                 time: Date.now(),
//                     ease: d3_ease_cubicInOut,
//                     delay: 0,
//                     duration: 250
//             };
//             for (var j = -1, m = this.length; ++j < m; ) {
//                 subgroups.push(subgroup = []);
//                 for (var group = this[j], i = -1, n = group.length; ++i < n; ) {
//                     if (node = group[i]) d3_transitionNode(node, i, id, transition);
//                     subgroup.push(node);
//                 }
//             }
//             return d3_transition(subgroups, id);
//         };
//         d3_selectionPrototype.interrupt = function() {
//             return this.each(d3_selection_interrupt);
//         };
//         function d3_selection_interrupt() {
//             var lock = this.__transition__;
//             if (lock) ++lock.active;
//         }
//         d3.select = function(node) {
//             var group = [ typeof node === "string" ? d3_select(node, d3_document) : node ];
//             group.parentNode = d3_documentElement;
//             return d3_selection([ group ]);
//         };
//         d3.selectAll = function(nodes) {
//             var group = d3_array(typeof nodes === "string" ? d3_selectAll(nodes, d3_document) : nodes);
//             group.parentNode = d3_documentElement;
//             return d3_selection([ group ]);
//         };
//         var d3_selectionRoot = d3.select(d3_documentElement);
//         d3_selectionPrototype.on = function(type, listener, capture) {
//             var n = arguments.length;
//             if (n < 3) {
//                 if (typeof type !== "string") {
//                     if (n < 2) listener = false;
//                     for (capture in type) this.each(d3_selection_on(capture, type[capture], listener));
//                     return this;
//                 }
//                 if (n < 2) return (n = this.node()["__on" + type]) && n._;
//                 capture = false;
//             }
//             return this.each(d3_selection_on(type, listener, capture));
//         };
//         function d3_selection_on(type, listener, capture) {
//             var name = "__on" + type, i = type.indexOf("."), wrap = d3_selection_onListener;
//             if (i > 0) type = type.substring(0, i);
//             var filter = d3_selection_onFilters.get(type);
//             if (filter) type = filter, wrap = d3_selection_onFilter;
//             function onRemove() {
//                 var l = this[name];
//                 if (l) {
//                     this.removeEventListener(type, l, l.$);
//                     delete this[name];
//                 }
//             }
//             function onAdd() {
//                 var l = wrap(listener, d3_array(arguments));
//                 onRemove.call(this);
//                 this.addEventListener(type, this[name] = l, l.$ = capture);
//                 l._ = listener;
//             }
//             function removeAll() {
//                 var re = new RegExp("^__on([^.]+)" + d3.requote(type) + "$"), match;
//                 for (var name in this) {
//                     if (match = name.match(re)) {
//                         var l = this[name];
//                         this.removeEventListener(match[1], l, l.$);
//                         delete this[name];
//                     }
//                 }
//             }
//             return i ? listener ? onAdd : onRemove : listener ? d3_noop : removeAll;
//         }
//         var d3_selection_onFilters = d3.map({
//             mouseenter: "mouseover",
//             mouseleave: "mouseout"
//         });
//         d3_selection_onFilters.forEach(function(k) {
//             if ("on" + k in d3_document) d3_selection_onFilters.remove(k);
//         });
//         function d3_selection_onListener(listener, argumentz) {
//             return function(e) {
//                 var o = d3.event;
//                 d3.event = e;
//                 argumentz[0] = this.__data__;
//                 try {
//                     listener.apply(this, argumentz);
//                 } finally {
//                     d3.event = o;
//                 }
//             };
//         }
//         function d3_selection_onFilter(listener, argumentz) {
//             var l = d3_selection_onListener(listener, argumentz);
//             return function(e) {
//                 var target = this, related = e.relatedTarget;
//                 if (!related || related !== target && !(related.compareDocumentPosition(target) & 8)) {
//                     l.call(target, e);
//                 }
//             };
//         }
//         var d3_event_dragSelect = "onselectstart" in d3_document ? null : d3_vendorSymbol(d3_documentElement.style, "userSelect"), d3_event_dragId = 0;
//         function d3_event_dragSuppress() {
//             var name = ".dragsuppress-" + ++d3_event_dragId, click = "click" + name, w = d3.select(d3_window).on("touchmove" + name, d3_eventPreventDefault).on("dragstart" + name, d3_eventPreventDefault).on("selectstart" + name, d3_eventPreventDefault);
//             if (d3_event_dragSelect) {
//                 var style = d3_documentElement.style, select = style[d3_event_dragSelect];
//                 style[d3_event_dragSelect] = "none";
//             }
//             return function(suppressClick) {
//                 w.on(name, null);
//                 if (d3_event_dragSelect) style[d3_event_dragSelect] = select;
//                 if (suppressClick) {
//                     function off() {
//                         w.on(click, null);
//                     }
//                     w.on(click, function() {
//                         d3_eventPreventDefault();
//                         off();
//                     }, true);
//                     setTimeout(off, 0);
//                 }
//             };
//         }
//         d3.mouse = function(container) {
//             return d3_mousePoint(container, d3_eventSource());
//         };
//         var d3_mouse_bug44083 = /WebKit/.test(d3_window.navigator.userAgent) ? -1 : 0;
//         function d3_mousePoint(container, e) {
//             if (e.changedTouches) e = e.changedTouches[0];
//             var svg = container.ownerSVGElement || container;
//             if (svg.createSVGPoint) {
//                 var point = svg.createSVGPoint();
//                 if (d3_mouse_bug44083 < 0 && (d3_window.scrollX || d3_window.scrollY)) {
//                     svg = d3.select("body").append("svg").style({
//                         position: "absolute",
//                         top: 0,
//                         left: 0,
//                         margin: 0,
//                         padding: 0,
//                         border: "none"
//                     }, "important");
//                     var ctm = svg[0][0].getScreenCTM();
//                     d3_mouse_bug44083 = !(ctm.f || ctm.e);
//                     svg.remove();
//                 }
//                 if (d3_mouse_bug44083) point.x = e.pageX, point.y = e.pageY; else point.x = e.clientX,
//                    point.y = e.clientY;
//                 point = point.matrixTransform(container.getScreenCTM().inverse());
//                 return [ point.x, point.y ];
//             }
//             var rect = container.getBoundingClientRect();
//             return [ e.clientX - rect.left - container.clientLeft, e.clientY - rect.top - container.clientTop ];
//         }
//         d3.touches = function(container, touches) {
//             if (arguments.length < 2) touches = d3_eventSource().touches;
//             return touches ? d3_array(touches).map(function(touch) {
//                 var point = d3_mousePoint(container, touch);
//                 point.identifier = touch.identifier;
//                 return point;
//             }) : [];
//         };
//         d3.behavior.drag = function() {
//             var event = d3_eventDispatch(drag, "drag", "dragstart", "dragend"), origin = null, mousedown = dragstart(d3_noop, d3.mouse, "mousemove", "mouseup"), touchstart = dragstart(touchid, touchposition, "touchmove", "touchend");
//             function drag() {
//                 this.on("mousedown.drag", mousedown).on("touchstart.drag", touchstart);
//             }
//             function touchid() {
//                 return d3.event.changedTouches[0].identifier;
//             }
//             function touchposition(parent, id) {
//                 return d3.touches(parent).filter(function(p) {
//                     return p.identifier === id;
//                 })[0];
//             }
//             function dragstart(id, position, move, end) {
//                 return function() {
//                     var target = this, parent = target.parentNode, event_ = event.of(target, arguments), eventTarget = d3.event.target, eventId = id(), drag = eventId == null ? "drag" : "drag-" + eventId, origin_ = position(parent, eventId), dragged = 0, offset, w = d3.select(d3_window).on(move + "." + drag, moved).on(end + "." + drag, ended), dragRestore = d3_event_dragSuppress();
//                     if (origin) {
//                         offset = origin.apply(target, arguments);
//                         offset = [ offset.x - origin_[0], offset.y - origin_[1] ];
//                     } else {
//                         offset = [ 0, 0 ];
//                     }
//                     event_({
//                         type: "dragstart"
//                     });
//                     function moved() {
//                         var p = position(parent, eventId), dx = p[0] - origin_[0], dy = p[1] - origin_[1];
//                         dragged |= dx | dy;
//                         origin_ = p;
//                         event_({
//                             type: "drag",
//                             x: p[0] + offset[0],
//                             y: p[1] + offset[1],
//                             dx: dx,
//                             dy: dy
//                         });
//                     }
//                     function ended() {
//                         w.on(move + "." + drag, null).on(end + "." + drag, null);
//                         dragRestore(dragged && d3.event.target === eventTarget);
//                         event_({
//                             type: "dragend"
//                         });
//                     }
//                 };
//             }
//             drag.origin = function(x) {
//                 if (!arguments.length) return origin;
//                 origin = x;
//                 return drag;
//             };
//             return d3.rebind(drag, event, "on");
//         };
//         var π = Math.PI, τ = 2 * π, halfπ = π / 2, ε = 1e-6, ε2 = ε * ε, d3_radians = π / 180, d3_degrees = 180 / π;
//         function d3_sgn(x) {
//             return x > 0 ? 1 : x < 0 ? -1 : 0;
//         }
//         function d3_acos(x) {
//             return x > 1 ? 0 : x < -1 ? π : Math.acos(x);
//         }
//         function d3_asin(x) {
//             return x > 1 ? halfπ : x < -1 ? -halfπ : Math.asin(x);
//         }
//         function d3_sinh(x) {
//             return ((x = Math.exp(x)) - 1 / x) / 2;
//         }
//         function d3_cosh(x) {
//             return ((x = Math.exp(x)) + 1 / x) / 2;
//         }
//         function d3_tanh(x) {
//             return ((x = Math.exp(2 * x)) - 1) / (x + 1);
//         }
//         function d3_haversin(x) {
//             return (x = Math.sin(x / 2)) * x;
//         }
//         var ρ = Math.SQRT2, ρ2 = 2, ρ4 = 4;
//         d3.interpolateZoom = function(p0, p1) {
//             var ux0 = p0[0], uy0 = p0[1], w0 = p0[2], ux1 = p1[0], uy1 = p1[1], w1 = p1[2];
//             var dx = ux1 - ux0, dy = uy1 - uy0, d2 = dx * dx + dy * dy, d1 = Math.sqrt(d2), b0 = (w1 * w1 - w0 * w0 + ρ4 * d2) / (2 * w0 * ρ2 * d1), b1 = (w1 * w1 - w0 * w0 - ρ4 * d2) / (2 * w1 * ρ2 * d1), r0 = Math.log(Math.sqrt(b0 * b0 + 1) - b0), r1 = Math.log(Math.sqrt(b1 * b1 + 1) - b1), dr = r1 - r0, S = (dr || Math.log(w1 / w0)) / ρ;
//             function interpolate(t) {
//                 var s = t * S;
//                 if (dr) {
//                     var coshr0 = d3_cosh(r0), u = w0 / (ρ2 * d1) * (coshr0 * d3_tanh(ρ * s + r0) - d3_sinh(r0));
//                     return [ ux0 + u * dx, uy0 + u * dy, w0 * coshr0 / d3_cosh(ρ * s + r0) ];
//                 }
//                 return [ ux0 + t * dx, uy0 + t * dy, w0 * Math.exp(ρ * s) ];
//             }
//             interpolate.duration = S * 1e3;
//             return interpolate;
//         };
//         d3.behavior.zoom = function() {
//             var view = {
//                 x: 0,
//                 y: 0,
//                 k: 1
//             }, translate0, center, size = [ 960, 500 ], scaleExtent = d3_behavior_zoomInfinity, mousedown = "mousedown.zoom", mousemove = "mousemove.zoom", mouseup = "mouseup.zoom", mousewheelTimer, touchstart = "touchstart.zoom", touchtime, event = d3_eventDispatch(zoom, "zoomstart", "zoom", "zoomend"), x0, x1, y0, y1;
//             function zoom(g) {
//                 g.on(mousedown, mousedowned).on(d3_behavior_zoomWheel + ".zoom", mousewheeled).on(mousemove, mousewheelreset).on("dblclick.zoom", dblclicked).on(touchstart, touchstarted);
//             }
//             zoom.event = function(g) {
//                 g.each(function() {
//                     var event_ = event.of(this, arguments), view1 = view;
//                     if (d3_transitionInheritId) {
//                         d3.select(this).transition().each("start.zoom", function() {
//                             view = this.__chart__ || {
//                                 x: 0,
//                             y: 0,
//                             k: 1
//                             };
//                             zoomstarted(event_);
//                         }).tween("zoom:zoom", function() {
//                             var dx = size[0], dy = size[1], cx = dx / 2, cy = dy / 2, i = d3.interpolateZoom([ (cx - view.x) / view.k, (cy - view.y) / view.k, dx / view.k ], [ (cx - view1.x) / view1.k, (cy - view1.y) / view1.k, dx / view1.k ]);
//                             return function(t) {
//                                 var l = i(t), k = dx / l[2];
//                                 this.__chart__ = view = {
//                                     x: cx - l[0] * k,
//                             y: cy - l[1] * k,
//                             k: k
//                                 };
//                                 zoomed(event_);
//                             };
//                         }).each("end.zoom", function() {
//                             zoomended(event_);
//                         });
//                     } else {
//                         this.__chart__ = view;
//                         zoomstarted(event_);
//                         zoomed(event_);
//                         zoomended(event_);
//                     }
//                 });
//             };
//             zoom.translate = function(_) {
//                 if (!arguments.length) return [ view.x, view.y ];
//                 view = {
//                     x: +_[0],
//                     y: +_[1],
//                     k: view.k
//                 };
//                 rescale();
//                 return zoom;
//             };
//             zoom.scale = function(_) {
//                 if (!arguments.length) return view.k;
//                 view = {
//                     x: view.x,
//                     y: view.y,
//                     k: +_
//                 };
//                 rescale();
//                 return zoom;
//             };
//             zoom.scaleExtent = function(_) {
//                 if (!arguments.length) return scaleExtent;
//                 scaleExtent = _ == null ? d3_behavior_zoomInfinity : [ +_[0], +_[1] ];
//                 return zoom;
//             };
//             zoom.center = function(_) {
//                 if (!arguments.length) return center;
//                 center = _ && [ +_[0], +_[1] ];
//                 return zoom;
//             };
//             zoom.size = function(_) {
//                 if (!arguments.length) return size;
//                 size = _ && [ +_[0], +_[1] ];
//                 return zoom;
//             };
//             zoom.x = function(z) {
//                 if (!arguments.length) return x1;
//                 x1 = z;
//                 x0 = z.copy();
//                 view = {
//                     x: 0,
//                     y: 0,
//                     k: 1
//                 };
//                 return zoom;
//             };
//             zoom.y = function(z) {
//                 if (!arguments.length) return y1;
//                 y1 = z;
//                 y0 = z.copy();
//                 view = {
//                     x: 0,
//                     y: 0,
//                     k: 1
//                 };
//                 return zoom;
//             };
//             function location(p) {
//                 return [ (p[0] - view.x) / view.k, (p[1] - view.y) / view.k ];
//             }
//             function point(l) {
//                 return [ l[0] * view.k + view.x, l[1] * view.k + view.y ];
//             }
//             function scaleTo(s) {
//                 view.k = Math.max(scaleExtent[0], Math.min(scaleExtent[1], s));
//             }
//             function translateTo(p, l) {
//                 l = point(l);
//                 view.x += p[0] - l[0];
//                 view.y += p[1] - l[1];
//             }
//             function rescale() {
//                 if (x1) x1.domain(x0.range().map(function(x) {
//                     return (x - view.x) / view.k;
//                 }).map(x0.invert));
//                 if (y1) y1.domain(y0.range().map(function(y) {
//                     return (y - view.y) / view.k;
//                 }).map(y0.invert));
//             }
//             function zoomstarted(event) {
//                 event({
//                     type: "zoomstart"
//                 });
//             }
//             function zoomed(event) {
//                 rescale();
//                 event({
//                     type: "zoom",
//                     scale: view.k,
//                     translate: [ view.x, view.y ]
//                 });
//             }
//             function zoomended(event) {
//                 event({
//                     type: "zoomend"
//                 });
//             }
//             function mousedowned() {
//                 var target = this, event_ = event.of(target, arguments), eventTarget = d3.event.target, dragged = 0, w = d3.select(d3_window).on(mousemove, moved).on(mouseup, ended), l = location(d3.mouse(target)), dragRestore = d3_event_dragSuppress();
//                 d3_selection_interrupt.call(target);
//                 zoomstarted(event_);
//                 function moved() {
//                     dragged = 1;
//                     translateTo(d3.mouse(target), l);
//                     zoomed(event_);
//                 }
//                 function ended() {
//                     w.on(mousemove, d3_window === target ? mousewheelreset : null).on(mouseup, null);
//                     dragRestore(dragged && d3.event.target === eventTarget);
//                     zoomended(event_);
//                 }
//             }
//             function touchstarted() {
//                 var target = this, event_ = event.of(target, arguments), locations0 = {}, distance0 = 0, scale0, eventId = d3.event.changedTouches[0].identifier, touchmove = "touchmove.zoom-" + eventId, touchend = "touchend.zoom-" + eventId, w = d3.select(d3_window).on(touchmove, moved).on(touchend, ended), t = d3.select(target).on(mousedown, null).on(touchstart, started), dragRestore = d3_event_dragSuppress();
//                 d3_selection_interrupt.call(target);
//                 started();
//                 zoomstarted(event_);
//                 function relocate() {
//                     var touches = d3.touches(target);
//                     scale0 = view.k;
//                     touches.forEach(function(t) {
//                         if (t.identifier in locations0) locations0[t.identifier] = location(t);
//                     });
//                     return touches;
//                 }
//                 function started() {
//                     var changed = d3.event.changedTouches;
//                     for (var i = 0, n = changed.length; i < n; ++i) {
//                         locations0[changed[i].identifier] = null;
//                     }
//                     var touches = relocate(), now = Date.now();
//                     if (touches.length === 1) {
//                         if (now - touchtime < 500) {
//                             var p = touches[0], l = locations0[p.identifier];
//                             scaleTo(view.k * 2);
//                             translateTo(p, l);
//                             d3_eventPreventDefault();
//                             zoomed(event_);
//                         }
//                         touchtime = now;
//                     } else if (touches.length > 1) {
//                         var p = touches[0], q = touches[1], dx = p[0] - q[0], dy = p[1] - q[1];
//                         distance0 = dx * dx + dy * dy;
//                     }
//                 }
//                 function moved() {
//                     var touches = d3.touches(target), p0, l0, p1, l1;
//                     for (var i = 0, n = touches.length; i < n; ++i, l1 = null) {
//                         p1 = touches[i];
//                         if (l1 = locations0[p1.identifier]) {
//                             if (l0) break;
//                             p0 = p1, l0 = l1;
//                         }
//                     }
//                     if (l1) {
//                         var distance1 = (distance1 = p1[0] - p0[0]) * distance1 + (distance1 = p1[1] - p0[1]) * distance1, scale1 = distance0 && Math.sqrt(distance1 / distance0);
//                         p0 = [ (p0[0] + p1[0]) / 2, (p0[1] + p1[1]) / 2 ];
//                         l0 = [ (l0[0] + l1[0]) / 2, (l0[1] + l1[1]) / 2 ];
//                         scaleTo(scale1 * scale0);
//                     }
//                     touchtime = null;
//                     translateTo(p0, l0);
//                     zoomed(event_);
//                 }
//                 function ended() {
//                     if (d3.event.touches.length) {
//                         var changed = d3.event.changedTouches;
//                         for (var i = 0, n = changed.length; i < n; ++i) {
//                             delete locations0[changed[i].identifier];
//                         }
//                         for (var identifier in locations0) {
//                             return void relocate();
//                         }
//                     }
//                     w.on(touchmove, null).on(touchend, null);
//                     t.on(mousedown, mousedowned).on(touchstart, touchstarted);
//                     dragRestore();
//                     zoomended(event_);
//                 }
//             }
//             function mousewheeled() {
//                 var event_ = event.of(this, arguments);
//                 if (mousewheelTimer) clearTimeout(mousewheelTimer); else d3_selection_interrupt.call(this),
//                    zoomstarted(event_);
//                 mousewheelTimer = setTimeout(function() {
//                     mousewheelTimer = null;
//                     zoomended(event_);
//                 }, 50);
//                 d3_eventPreventDefault();
//                 var point = center || d3.mouse(this);
//                 if (!translate0) translate0 = location(point);
//                 scaleTo(Math.pow(2, d3_behavior_zoomDelta() * .002) * view.k);
//                 translateTo(point, translate0);
//                 zoomed(event_);
//             }
//             function mousewheelreset() {
//                 translate0 = null;
//             }
//             function dblclicked() {
//                 var event_ = event.of(this, arguments), p = d3.mouse(this), l = location(p), k = Math.log(view.k) / Math.LN2;
//                 zoomstarted(event_);
//                 scaleTo(Math.pow(2, d3.event.shiftKey ? Math.ceil(k) - 1 : Math.floor(k) + 1));
//                 translateTo(p, l);
//                 zoomed(event_);
//                 zoomended(event_);
//             }
//             return d3.rebind(zoom, event, "on");
//         };
//         var d3_behavior_zoomInfinity = [ 0, Infinity ];
//         var d3_behavior_zoomDelta, d3_behavior_zoomWheel = "onwheel" in d3_document ? (d3_behavior_zoomDelta = function() {
//             return -d3.event.deltaY * (d3.event.deltaMode ? 120 : 1);
//         }, "wheel") : "onmousewheel" in d3_document ? (d3_behavior_zoomDelta = function() {
//             return d3.event.wheelDelta;
//         }, "mousewheel") : (d3_behavior_zoomDelta = function() {
//             return -d3.event.detail;
//         }, "MozMousePixelScroll");
//         function d3_Color() {}
//         d3_Color.prototype.toString = function() {
//             return this.rgb() + "";
//         };
//         d3.hsl = function(h, s, l) {
//             return arguments.length === 1 ? h instanceof d3_Hsl ? d3_hsl(h.h, h.s, h.l) : d3_rgb_parse("" + h, d3_rgb_hsl, d3_hsl) : d3_hsl(+h, +s, +l);
//         };
//         function d3_hsl(h, s, l) {
//             return new d3_Hsl(h, s, l);
//         }
//         function d3_Hsl(h, s, l) {
//             this.h = h;
//             this.s = s;
//             this.l = l;
//         }
//         var d3_hslPrototype = d3_Hsl.prototype = new d3_Color();
//         d3_hslPrototype.brighter = function(k) {
//             k = Math.pow(.7, arguments.length ? k : 1);
//             return d3_hsl(this.h, this.s, this.l / k);
//         };
//         d3_hslPrototype.darker = function(k) {
//             k = Math.pow(.7, arguments.length ? k : 1);
//             return d3_hsl(this.h, this.s, k * this.l);
//         };
//         d3_hslPrototype.rgb = function() {
//             return d3_hsl_rgb(this.h, this.s, this.l);
//         };
//         function d3_hsl_rgb(h, s, l) {
//             var m1, m2;
//             h = isNaN(h) ? 0 : (h %= 360) < 0 ? h + 360 : h;
//             s = isNaN(s) ? 0 : s < 0 ? 0 : s > 1 ? 1 : s;
//             l = l < 0 ? 0 : l > 1 ? 1 : l;
//             m2 = l <= .5 ? l * (1 + s) : l + s - l * s;
//             m1 = 2 * l - m2;
//             function v(h) {
//                 if (h > 360) h -= 360; else if (h < 0) h += 360;
//                 if (h < 60) return m1 + (m2 - m1) * h / 60;
//                 if (h < 180) return m2;
//                 if (h < 240) return m1 + (m2 - m1) * (240 - h) / 60;
//                 return m1;
//             }
//             function vv(h) {
//                 return Math.round(v(h) * 255);
//             }
//             return d3_rgb(vv(h + 120), vv(h), vv(h - 120));
//         }
//         d3.hcl = function(h, c, l) {
//             return arguments.length === 1 ? h instanceof d3_Hcl ? d3_hcl(h.h, h.c, h.l) : h instanceof d3_Lab ? d3_lab_hcl(h.l, h.a, h.b) : d3_lab_hcl((h = d3_rgb_lab((h = d3.rgb(h)).r, h.g, h.b)).l, h.a, h.b) : d3_hcl(+h, +c, +l);
//         };
//         function d3_hcl(h, c, l) {
//             return new d3_Hcl(h, c, l);
//         }
//         function d3_Hcl(h, c, l) {
//             this.h = h;
//             this.c = c;
//             this.l = l;
//         }
//         var d3_hclPrototype = d3_Hcl.prototype = new d3_Color();
//         d3_hclPrototype.brighter = function(k) {
//             return d3_hcl(this.h, this.c, Math.min(100, this.l + d3_lab_K * (arguments.length ? k : 1)));
//         };
//         d3_hclPrototype.darker = function(k) {
//             return d3_hcl(this.h, this.c, Math.max(0, this.l - d3_lab_K * (arguments.length ? k : 1)));
//         };
//         d3_hclPrototype.rgb = function() {
//             return d3_hcl_lab(this.h, this.c, this.l).rgb();
//         };
//         function d3_hcl_lab(h, c, l) {
//             if (isNaN(h)) h = 0;
//             if (isNaN(c)) c = 0;
//             return d3_lab(l, Math.cos(h *= d3_radians) * c, Math.sin(h) * c);
//         }
//         d3.lab = function(l, a, b) {
//             return arguments.length === 1 ? l instanceof d3_Lab ? d3_lab(l.l, l.a, l.b) : l instanceof d3_Hcl ? d3_hcl_lab(l.l, l.c, l.h) : d3_rgb_lab((l = d3.rgb(l)).r, l.g, l.b) : d3_lab(+l, +a, +b);
//         };
//         function d3_lab(l, a, b) {
//             return new d3_Lab(l, a, b);
//         }
//         function d3_Lab(l, a, b) {
//             this.l = l;
//             this.a = a;
//             this.b = b;
//         }
//         var d3_lab_K = 18;
//         var d3_lab_X = .95047, d3_lab_Y = 1, d3_lab_Z = 1.08883;
//         var d3_labPrototype = d3_Lab.prototype = new d3_Color();
//         d3_labPrototype.brighter = function(k) {
//             return d3_lab(Math.min(100, this.l + d3_lab_K * (arguments.length ? k : 1)), this.a, this.b);
//         };
//         d3_labPrototype.darker = function(k) {
//             return d3_lab(Math.max(0, this.l - d3_lab_K * (arguments.length ? k : 1)), this.a, this.b);
//         };
//         d3_labPrototype.rgb = function() {
//             return d3_lab_rgb(this.l, this.a, this.b);
//         };
//         function d3_lab_rgb(l, a, b) {
//             var y = (l + 16) / 116, x = y + a / 500, z = y - b / 200;
//             x = d3_lab_xyz(x) * d3_lab_X;
//             y = d3_lab_xyz(y) * d3_lab_Y;
//             z = d3_lab_xyz(z) * d3_lab_Z;
//             return d3_rgb(d3_xyz_rgb(3.2404542 * x - 1.5371385 * y - .4985314 * z), d3_xyz_rgb(-.969266 * x + 1.8760108 * y + .041556 * z), d3_xyz_rgb(.0556434 * x - .2040259 * y + 1.0572252 * z));
//         }
//         function d3_lab_hcl(l, a, b) {
//             return l > 0 ? d3_hcl(Math.atan2(b, a) * d3_degrees, Math.sqrt(a * a + b * b), l) : d3_hcl(NaN, NaN, l);
//         }
//         function d3_lab_xyz(x) {
//             return x > .206893034 ? x * x * x : (x - 4 / 29) / 7.787037;
//         }
//         function d3_xyz_lab(x) {
//             return x > .008856 ? Math.pow(x, 1 / 3) : 7.787037 * x + 4 / 29;
//         }
//         function d3_xyz_rgb(r) {
//             return Math.round(255 * (r <= .00304 ? 12.92 * r : 1.055 * Math.pow(r, 1 / 2.4) - .055));
//         }
//         d3.rgb = function(r, g, b) {
//             return arguments.length === 1 ? r instanceof d3_Rgb ? d3_rgb(r.r, r.g, r.b) : d3_rgb_parse("" + r, d3_rgb, d3_hsl_rgb) : d3_rgb(~~r, ~~g, ~~b);
//         };
//         function d3_rgbNumber(value) {
//             return d3_rgb(value >> 16, value >> 8 & 255, value & 255);
//         }
//         function d3_rgbString(value) {
//             return d3_rgbNumber(value) + "";
//         }
//         function d3_rgb(r, g, b) {
//             return new d3_Rgb(r, g, b);
//         }
//         function d3_Rgb(r, g, b) {
//             this.r = r;
//             this.g = g;
//             this.b = b;
//         }
//         var d3_rgbPrototype = d3_Rgb.prototype = new d3_Color();
//         d3_rgbPrototype.brighter = function(k) {
//             k = Math.pow(.7, arguments.length ? k : 1);
//             var r = this.r, g = this.g, b = this.b, i = 30;
//             if (!r && !g && !b) return d3_rgb(i, i, i);
//             if (r && r < i) r = i;
//             if (g && g < i) g = i;
//             if (b && b < i) b = i;
//             return d3_rgb(Math.min(255, ~~(r / k)), Math.min(255, ~~(g / k)), Math.min(255, ~~(b / k)));
//         };
//         d3_rgbPrototype.darker = function(k) {
//             k = Math.pow(.7, arguments.length ? k : 1);
//             return d3_rgb(~~(k * this.r), ~~(k * this.g), ~~(k * this.b));
//         };
//         d3_rgbPrototype.hsl = function() {
//             return d3_rgb_hsl(this.r, this.g, this.b);
//         };
//         d3_rgbPrototype.toString = function() {
//             return "#" + d3_rgb_hex(this.r) + d3_rgb_hex(this.g) + d3_rgb_hex(this.b);
//         };
//         function d3_rgb_hex(v) {
//             return v < 16 ? "0" + Math.max(0, v).toString(16) : Math.min(255, v).toString(16);
//         }
//         function d3_rgb_parse(format, rgb, hsl) {
//             var r = 0, g = 0, b = 0, m1, m2, name;
//             m1 = /([a-z]+)\((.*)\)/i.exec(format);
//             if (m1) {
//                 m2 = m1[2].split(",");
//                 switch (m1[1]) {
//                 case "hsl":
//                     {
//                         return hsl(parseFloat(m2[0]), parseFloat(m2[1]) / 100, parseFloat(m2[2]) / 100);
//                     }
//
//                 case "rgb":
//                     {
//                         return rgb(d3_rgb_parseNumber(m2[0]), d3_rgb_parseNumber(m2[1]), d3_rgb_parseNumber(m2[2]));
//                     }
//                 }
//             }
//             if (name = d3_rgb_names.get(format)) return rgb(name.r, name.g, name.b);
//             if (format != null && format.charAt(0) === "#") {
//                 if (format.length === 4) {
//                     r = format.charAt(1);
//                     r += r;
//                     g = format.charAt(2);
//                     g += g;
//                     b = format.charAt(3);
//                     b += b;
//                 } else if (format.length === 7) {
//                     r = format.substring(1, 3);
//                     g = format.substring(3, 5);
//                     b = format.substring(5, 7);
//                 }
//                 r = parseInt(r, 16);
//                 g = parseInt(g, 16);
//                 b = parseInt(b, 16);
//             }
//             return rgb(r, g, b);
//         }
//         function d3_rgb_hsl(r, g, b) {
//             var min = Math.min(r /= 255, g /= 255, b /= 255), max = Math.max(r, g, b), d = max - min, h, s, l = (max + min) / 2;
//             if (d) {
//                 s = l < .5 ? d / (max + min) : d / (2 - max - min);
//                 if (r == max) h = (g - b) / d + (g < b ? 6 : 0); else if (g == max) h = (b - r) / d + 2; else h = (r - g) / d + 4;
//                 h *= 60;
//             } else {
//                 h = NaN;
//                 s = l > 0 && l < 1 ? 0 : h;
//             }
//             return d3_hsl(h, s, l);
//         }
//         function d3_rgb_lab(r, g, b) {
//             r = d3_rgb_xyz(r);
//             g = d3_rgb_xyz(g);
//             b = d3_rgb_xyz(b);
//             var x = d3_xyz_lab((.4124564 * r + .3575761 * g + .1804375 * b) / d3_lab_X), y = d3_xyz_lab((.2126729 * r + .7151522 * g + .072175 * b) / d3_lab_Y), z = d3_xyz_lab((.0193339 * r + .119192 * g + .9503041 * b) / d3_lab_Z);
//             return d3_lab(116 * y - 16, 500 * (x - y), 200 * (y - z));
//         }
//         function d3_rgb_xyz(r) {
//             return (r /= 255) <= .04045 ? r / 12.92 : Math.pow((r + .055) / 1.055, 2.4);
//         }
//         function d3_rgb_parseNumber(c) {
//             var f = parseFloat(c);
//             return c.charAt(c.length - 1) === "%" ? Math.round(f * 2.55) : f;
//         }
//         var d3_rgb_names = d3.map({
//             aliceblue: 15792383,
//             antiquewhite: 16444375,
//             aqua: 65535,
//             aquamarine: 8388564,
//             azure: 15794175,
//             beige: 16119260,
//             bisque: 16770244,
//             black: 0,
//             blanchedalmond: 16772045,
//             blue: 255,
//             blueviolet: 9055202,
//             brown: 10824234,
//             burlywood: 14596231,
//             cadetblue: 6266528,
//             chartreuse: 8388352,
//             chocolate: 13789470,
//             coral: 16744272,
//             cornflowerblue: 6591981,
//             cornsilk: 16775388,
//             crimson: 14423100,
//             cyan: 65535,
//             darkblue: 139,
//             darkcyan: 35723,
//             darkgoldenrod: 12092939,
//             darkgray: 11119017,
//             darkgreen: 25600,
//             darkgrey: 11119017,
//             darkkhaki: 12433259,
//             darkmagenta: 9109643,
//             darkolivegreen: 5597999,
//             darkorange: 16747520,
//             darkorchid: 10040012,
//             darkred: 9109504,
//             darksalmon: 15308410,
//             darkseagreen: 9419919,
//             darkslateblue: 4734347,
//             darkslategray: 3100495,
//             darkslategrey: 3100495,
//             darkturquoise: 52945,
//             darkviolet: 9699539,
//             deeppink: 16716947,
//             deepskyblue: 49151,
//             dimgray: 6908265,
//             dimgrey: 6908265,
//             dodgerblue: 2003199,
//             firebrick: 11674146,
//             floralwhite: 16775920,
//             forestgreen: 2263842,
//             fuchsia: 16711935,
//             gainsboro: 14474460,
//             ghostwhite: 16316671,
//             gold: 16766720,
//             goldenrod: 14329120,
//             gray: 8421504,
//             green: 32768,
//             greenyellow: 11403055,
//             grey: 8421504,
//             honeydew: 15794160,
//             hotpink: 16738740,
//             indianred: 13458524,
//             indigo: 4915330,
//             ivory: 16777200,
//             khaki: 15787660,
//             lavender: 15132410,
//             lavenderblush: 16773365,
//             lawngreen: 8190976,
//             lemonchiffon: 16775885,
//             lightblue: 11393254,
//             lightcoral: 15761536,
//             lightcyan: 14745599,
//             lightgoldenrodyellow: 16448210,
//             lightgray: 13882323,
//             lightgreen: 9498256,
//             lightgrey: 13882323,
//             lightpink: 16758465,
//             lightsalmon: 16752762,
//             lightseagreen: 2142890,
//             lightskyblue: 8900346,
//             lightslategray: 7833753,
//             lightslategrey: 7833753,
//             lightsteelblue: 11584734,
//             lightyellow: 16777184,
//             lime: 65280,
//             limegreen: 3329330,
//             linen: 16445670,
//             magenta: 16711935,
//             maroon: 8388608,
//             mediumaquamarine: 6737322,
//             mediumblue: 205,
//             mediumorchid: 12211667,
//             mediumpurple: 9662683,
//             mediumseagreen: 3978097,
//             mediumslateblue: 8087790,
//             mediumspringgreen: 64154,
//             mediumturquoise: 4772300,
//             mediumvioletred: 13047173,
//             midnightblue: 1644912,
//             mintcream: 16121850,
//             mistyrose: 16770273,
//             moccasin: 16770229,
//             navajowhite: 16768685,
//             navy: 128,
//             oldlace: 16643558,
//             olive: 8421376,
//             olivedrab: 7048739,
//             orange: 16753920,
//             orangered: 16729344,
//             orchid: 14315734,
//             palegoldenrod: 15657130,
//             palegreen: 10025880,
//             paleturquoise: 11529966,
//             palevioletred: 14381203,
//             papayawhip: 16773077,
//             peachpuff: 16767673,
//             peru: 13468991,
//             pink: 16761035,
//             plum: 14524637,
//             powderblue: 11591910,
//             purple: 8388736,
//             red: 16711680,
//             rosybrown: 12357519,
//             royalblue: 4286945,
//             saddlebrown: 9127187,
//             salmon: 16416882,
//             sandybrown: 16032864,
//             seagreen: 3050327,
//             seashell: 16774638,
//             sienna: 10506797,
//             silver: 12632256,
//             skyblue: 8900331,
//             slateblue: 6970061,
//             slategray: 7372944,
//             slategrey: 7372944,
//             snow: 16775930,
//             springgreen: 65407,
//             steelblue: 4620980,
//             tan: 13808780,
//             teal: 32896,
//             thistle: 14204888,
//             tomato: 16737095,
//             turquoise: 4251856,
//             violet: 15631086,
//             wheat: 16113331,
//             white: 16777215,
//             whitesmoke: 16119285,
//             yellow: 16776960,
//             yellowgreen: 10145074
//         });
//         d3_rgb_names.forEach(function(key, value) {
//             d3_rgb_names.set(key, d3_rgbNumber(value));
//         });
//         function d3_functor(v) {
//             return typeof v === "function" ? v : function() {
//                 return v;
//             };
//         }
//         d3.functor = d3_functor;
//         function d3_identity(d) {
//             return d;
//         }
//         d3.xhr = d3_xhrType(d3_identity);
//         function d3_xhrType(response) {
//             return function(url, mimeType, callback) {
//                 if (arguments.length === 2 && typeof mimeType === "function") callback = mimeType,
//                    mimeType = null;
//                 return d3_xhr(url, mimeType, response, callback);
//             };
//         }
//         function d3_xhr(url, mimeType, response, callback) {
//             var xhr = {}, dispatch = d3.dispatch("beforesend", "progress", "load", "error"), headers = {}, request = new XMLHttpRequest(), responseType = null;
//             if (d3_window.XDomainRequest && !("withCredentials" in request) && /^(http(s)?:)?\/\//.test(url)) request = new XDomainRequest();
//             "onload" in request ? request.onload = request.onerror = respond : request.onreadystatechange = function() {
//                 request.readyState > 3 && respond();
//             };
//             function respond() {
//                 var status = request.status, result;
//                 if (!status && request.responseText || status >= 200 && status < 300 || status === 304) {
//                     try {
//                         result = response.call(xhr, request);
//                     } catch (e) {
//                         dispatch.error.call(xhr, e);
//                         return;
//                     }
//                     dispatch.load.call(xhr, result);
//                 } else {
//                     dispatch.error.call(xhr, request);
//                 }
//             }
//             request.onprogress = function(event) {
//                 var o = d3.event;
//                 d3.event = event;
//                 try {
//                     dispatch.progress.call(xhr, request);
//                 } finally {
//                     d3.event = o;
//                 }
//             };
//             xhr.header = function(name, value) {
//                 name = (name + "").toLowerCase();
//                 if (arguments.length < 2) return headers[name];
//                 if (value == null) delete headers[name]; else headers[name] = value + "";
//                 return xhr;
//             };
//             xhr.mimeType = function(value) {
//                 if (!arguments.length) return mimeType;
//                 mimeType = value == null ? null : value + "";
//                 return xhr;
//             };
//             xhr.responseType = function(value) {
//                 if (!arguments.length) return responseType;
//                 responseType = value;
//                 return xhr;
//             };
//             xhr.response = function(value) {
//                 response = value;
//                 return xhr;
//             };
//             [ "get", "post" ].forEach(function(method) {
//                 xhr[method] = function() {
//                     return xhr.send.apply(xhr, [ method ].concat(d3_array(arguments)));
//                 };
//             });
//             xhr.send = function(method, data, callback) {
//                 if (arguments.length === 2 && typeof data === "function") callback = data, data = null;
//                 request.open(method, url, true);
//                 if (mimeType != null && !("accept" in headers)) headers["accept"] = mimeType + ",*/*";
//                 if (request.setRequestHeader) for (var name in headers) request.setRequestHeader(name, headers[name]);
//                 if (mimeType != null && request.overrideMimeType) request.overrideMimeType(mimeType);
//                 if (responseType != null) request.responseType = responseType;
//                 if (callback != null) xhr.on("error", callback).on("load", function(request) {
//                     callback(null, request);
//                 });
//                 dispatch.beforesend.call(xhr, request);
//                 request.send(data == null ? null : data);
//                 return xhr;
//             };
//             xhr.abort = function() {
//                 request.abort();
//                 return xhr;
//             };
//             d3.rebind(xhr, dispatch, "on");
//             return callback == null ? xhr : xhr.get(d3_xhr_fixCallback(callback));
//         }
//         function d3_xhr_fixCallback(callback) {
//             return callback.length === 1 ? function(error, request) {
//                 callback(error == null ? request : null);
//             } : callback;
//         }
//         d3.dsv = function(delimiter, mimeType) {
//             var reFormat = new RegExp('["' + delimiter + "\n]"), delimiterCode = delimiter.charCodeAt(0);
//             function dsv(url, row, callback) {
//                 if (arguments.length < 3) callback = row, row = null;
//                 var xhr = d3.xhr(url, mimeType, callback);
//                 xhr.row = function(_) {
//                     return arguments.length ? xhr.response((row = _) == null ? response : typedResponse(_)) : row;
//                 };
//                 return xhr.row(row);
//             }
//             function response(request) {
//                 return dsv.parse(request.responseText);
//             }
//             function typedResponse(f) {
//                 return function(request) {
//                     return dsv.parse(request.responseText, f);
//                 };
//             }
//             dsv.parse = function(text, f) {
//                 var o;
//                 return dsv.parseRows(text, function(row, i) {
//                     if (o) return o(row, i - 1);
//                     var a = new Function("d", "return {" + row.map(function(name, i) {
//                         return JSON.stringify(name) + ": d[" + i + "]";
//                     }).join(",") + "}");
//                     o = f ? function(row, i) {
//                         return f(a(row), i);
//                     } : a;
//                 });
//             };
//             dsv.parseRows = function(text, f) {
//                 var EOL = {}, EOF = {}, rows = [], N = text.length, I = 0, n = 0, t, eol;
//                 function token() {
//                     if (I >= N) return EOF;
//                     if (eol) return eol = false, EOL;
//                     var j = I;
//                     if (text.charCodeAt(j) === 34) {
//                         var i = j;
//                         while (i++ < N) {
//                             if (text.charCodeAt(i) === 34) {
//                                 if (text.charCodeAt(i + 1) !== 34) break;
//                                 ++i;
//                             }
//                         }
//                         I = i + 2;
//                         var c = text.charCodeAt(i + 1);
//                         if (c === 13) {
//                             eol = true;
//                             if (text.charCodeAt(i + 2) === 10) ++I;
//                         } else if (c === 10) {
//                             eol = true;
//                         }
//                         return text.substring(j + 1, i).replace(/""/g, '"');
//                     }
//                     while (I < N) {
//                         var c = text.charCodeAt(I++), k = 1;
//                         if (c === 10) eol = true; else if (c === 13) {
//                             eol = true;
//                             if (text.charCodeAt(I) === 10) ++I, ++k;
//                         } else if (c !== delimiterCode) continue;
//                         return text.substring(j, I - k);
//                     }
//                     return text.substring(j);
//                 }
//                 while ((t = token()) !== EOF) {
//                     var a = [];
//                     while (t !== EOL && t !== EOF) {
//                         a.push(t);
//                         t = token();
//                     }
//                     if (f && !(a = f(a, n++))) continue;
//                     rows.push(a);
//                 }
//                 return rows;
//             };
//             dsv.format = function(rows) {
//                 if (Array.isArray(rows[0])) return dsv.formatRows(rows);
//                 var fieldSet = new d3_Set(), fields = [];
//                 rows.forEach(function(row) {
//                     for (var field in row) {
//                         if (!fieldSet.has(field)) {
//                             fields.push(fieldSet.add(field));
//                         }
//                     }
//                 });
//                 return [ fields.map(formatValue).join(delimiter) ].concat(rows.map(function(row) {
//                     return fields.map(function(field) {
//                         return formatValue(row[field]);
//                     }).join(delimiter);
//                 })).join("\n");
//             };
//             dsv.formatRows = function(rows) {
//                 return rows.map(formatRow).join("\n");
//             };
//             function formatRow(row) {
//                 return row.map(formatValue).join(delimiter);
//             }
//             function formatValue(text) {
//                 return reFormat.test(text) ? '"' + text.replace(/\"/g, '""') + '"' : text;
//             }
//             return dsv;
//         };
//         d3.csv = d3.dsv(",", "text/csv");
//         d3.tsv = d3.dsv("	", "text/tab-separated-values");
//         var d3_timer_queueHead, d3_timer_queueTail, d3_timer_interval, d3_timer_timeout, d3_timer_active, d3_timer_frame = d3_window[d3_vendorSymbol(d3_window, "requestAnimationFrame")] || function(callback) {
//             setTimeout(callback, 17);
//         };
//         d3.timer = function(callback, delay, then) {
//             var n = arguments.length;
//             if (n < 2) delay = 0;
//             if (n < 3) then = Date.now();
//             var time = then + delay, timer = {
//                 c: callback,
//                 t: time,
//                 f: false,
//                 n: null
//             };
//             if (d3_timer_queueTail) d3_timer_queueTail.n = timer; else d3_timer_queueHead = timer;
//             d3_timer_queueTail = timer;
//             if (!d3_timer_interval) {
//                 d3_timer_timeout = clearTimeout(d3_timer_timeout);
//                 d3_timer_interval = 1;
//                 d3_timer_frame(d3_timer_step);
//             }
//         };
//         function d3_timer_step() {
//             var now = d3_timer_mark(), delay = d3_timer_sweep() - now;
//             if (delay > 24) {
//                 if (isFinite(delay)) {
//                     clearTimeout(d3_timer_timeout);
//                     d3_timer_timeout = setTimeout(d3_timer_step, delay);
//                 }
//                 d3_timer_interval = 0;
//             } else {
//                 d3_timer_interval = 1;
//                 d3_timer_frame(d3_timer_step);
//             }
//         }
//         d3.timer.flush = function() {
//             d3_timer_mark();
//             d3_timer_sweep();
//         };
//         function d3_timer_mark() {
//             var now = Date.now();
//             d3_timer_active = d3_timer_queueHead;
//             while (d3_timer_active) {
//                 if (now >= d3_timer_active.t) d3_timer_active.f = d3_timer_active.c(now - d3_timer_active.t);
//                 d3_timer_active = d3_timer_active.n;
//             }
//             return now;
//         }
//         function d3_timer_sweep() {
//             var t0, t1 = d3_timer_queueHead, time = Infinity;
//             while (t1) {
//                 if (t1.f) {
//                     t1 = t0 ? t0.n = t1.n : d3_timer_queueHead = t1.n;
//                 } else {
//                     if (t1.t < time) time = t1.t;
//                     t1 = (t0 = t1).n;
//                 }
//             }
//             d3_timer_queueTail = t0;
//             return time;
//         }
//         var d3_format_decimalPoint = ".", d3_format_thousandsSeparator = ",", d3_format_grouping = [ 3, 3 ], d3_format_currencySymbol = "$";
//         var d3_formatPrefixes = [ "y", "z", "a", "f", "p", "n", "µ", "m", "", "k", "M", "G", "T", "P", "E", "Z", "Y" ].map(d3_formatPrefix);
//         d3.formatPrefix = function(value, precision) {
//             var i = 0;
//             if (value) {
//                 if (value < 0) value *= -1;
//                 if (precision) value = d3.round(value, d3_format_precision(value, precision));
//                 i = 1 + Math.floor(1e-12 + Math.log(value) / Math.LN10);
//                 i = Math.max(-24, Math.min(24, Math.floor((i <= 0 ? i + 1 : i - 1) / 3) * 3));
//             }
//             return d3_formatPrefixes[8 + i / 3];
//         };
//         function d3_formatPrefix(d, i) {
//             var k = Math.pow(10, abs(8 - i) * 3);
//             return {
//                 scale: i > 8 ? function(d) {
//                     return d / k;
//                 } : function(d) {
//                     return d * k;
//                 },
//                     symbol: d
//             };
//         }
//         d3.round = function(x, n) {
//             return n ? Math.round(x * (n = Math.pow(10, n))) / n : Math.round(x);
//         };
//         d3.format = function(specifier) {
//             var match = d3_format_re.exec(specifier), fill = match[1] || " ", align = match[2] || ">", sign = match[3] || "", symbol = match[4] || "", zfill = match[5], width = +match[6], comma = match[7], precision = match[8], type = match[9], scale = 1, suffix = "", integer = false;
//             if (precision) precision = +precision.substring(1);
//             if (zfill || fill === "0" && align === "=") {
//                 zfill = fill = "0";
//                 align = "=";
//                 if (comma) width -= Math.floor((width - 1) / 4);
//             }
//             switch (type) {
//             case "n":
//                 comma = true;
//                 type = "g";
//                 break;
//
//             case "%":
//                 scale = 100;
//                 suffix = "%";
//                 type = "f";
//                 break;
//
//             case "p":
//                 scale = 100;
//                 suffix = "%";
//                 type = "r";
//                 break;
//
//             case "b":
//             case "o":
//             case "x":
//             case "X":
//                 if (symbol === "#") symbol = "0" + type.toLowerCase();
//
//             case "c":
//             case "d":
//                 integer = true;
//                 precision = 0;
//                 break;
//
//             case "s":
//                 scale = -1;
//                 type = "r";
//                 break;
//             }
//             if (symbol === "#") symbol = ""; else if (symbol === "$") symbol = d3_format_currencySymbol;
//             if (type == "r" && !precision) type = "g";
//             if (precision != null) {
//                 if (type == "g") precision = Math.max(1, Math.min(21, precision)); else if (type == "e" || type == "f") precision = Math.max(0, Math.min(20, precision));
//             }
//             type = d3_format_types.get(type) || d3_format_typeDefault;
//             var zcomma = zfill && comma;
//             return function(value) {
//                 if (integer && value % 1) return "";
//                 var negative = value < 0 || value === 0 && 1 / value < 0 ? (value = -value, "-") : sign;
//                 if (scale < 0) {
//                     var prefix = d3.formatPrefix(value, precision);
//                     value = prefix.scale(value);
//                     suffix = prefix.symbol;
//                 } else {
//                     value *= scale;
//                 }
//                 value = type(value, precision);
//                 var i = value.lastIndexOf("."), before = i < 0 ? value : value.substring(0, i), after = i < 0 ? "" : d3_format_decimalPoint + value.substring(i + 1);
//                 if (!zfill && comma) before = d3_format_group(before);
//                 var length = symbol.length + before.length + after.length + (zcomma ? 0 : negative.length), padding = length < width ? new Array(length = width - length + 1).join(fill) : "";
//                 if (zcomma) before = d3_format_group(padding + before);
//                 negative += symbol;
//                 value = before + after;
//                 return (align === "<" ? negative + value + padding : align === ">" ? padding + negative + value : align === "^" ? padding.substring(0, length >>= 1) + negative + value + padding.substring(length) : negative + (zcomma ? value : padding + value)) + suffix;
//             };
//         };
//         var d3_format_re = /(?:([^{])?([<>=^]))?([+\- ])?([$#])?(0)?(\d+)?(,)?(\.-?\d+)?([a-z%])?/i;
//             var d3_format_types = d3.map({
//                 b: function(x) {
//                     return x.toString(2);
//                 },
//                 c: function(x) {
//                     return String.fromCharCode(x);
//                 },
//                 o: function(x) {
//                     return x.toString(8);
//                 },
//                 x: function(x) {
//                     return x.toString(16);
//                 },
//                 X: function(x) {
//                     return x.toString(16).toUpperCase();
//                 },
//                 g: function(x, p) {
//                     return x.toPrecision(p);
//                 },
//                 e: function(x, p) {
//                     return x.toExponential(p);
//                 },
//                 f: function(x, p) {
//                     return x.toFixed(p);
//                 },
//                 r: function(x, p) {
//                     return (x = d3.round(x, d3_format_precision(x, p))).toFixed(Math.max(0, Math.min(20, d3_format_precision(x * (1 + 1e-15), p))));
//                 }
//             });
//             function d3_format_precision(x, p) {
//                 return p - (x ? Math.ceil(Math.log(x) / Math.LN10) : 1);
//             }
//             function d3_format_typeDefault(x) {
//                 return x + "";
//             }
//             var d3_format_group = d3_identity;
//             if (d3_format_grouping) {
//                 var d3_format_groupingLength = d3_format_grouping.length;
//                 d3_format_group = function(value) {
//                     var i = value.length, t = [], j = 0, g = d3_format_grouping[0];
//                     while (i > 0 && g > 0) {
//                         t.push(value.substring(i -= g, i + g));
//                         g = d3_format_grouping[j = (j + 1) % d3_format_groupingLength];
//                     }
//                     return t.reverse().join(d3_format_thousandsSeparator);
//                 };
//             }
//             d3.geo = {};
//             function d3_adder() {}
//             d3_adder.prototype = {
//                 s: 0,
//                 t: 0,
//                 add: function(y) {
//                     d3_adderSum(y, this.t, d3_adderTemp);
//                     d3_adderSum(d3_adderTemp.s, this.s, this);
//                     if (this.s) this.t += d3_adderTemp.t; else this.s = d3_adderTemp.t;
//                 },
//                 reset: function() {
//                     this.s = this.t = 0;
//                 },
//                 valueOf: function() {
//                     return this.s;
//                 }
//             };
//             var d3_adderTemp = new d3_adder();
//             function d3_adderSum(a, b, o) {
//                 var x = o.s = a + b, bv = x - a, av = x - bv;
//                 o.t = a - av + (b - bv);
//             }
//             d3.geo.stream = function(object, listener) {
//                 if (object && d3_geo_streamObjectType.hasOwnProperty(object.type)) {
//                     d3_geo_streamObjectType[object.type](object, listener);
//                 } else {
//                     d3_geo_streamGeometry(object, listener);
//                 }
//             };
//             function d3_geo_streamGeometry(geometry, listener) {
//                 if (geometry && d3_geo_streamGeometryType.hasOwnProperty(geometry.type)) {
//                     d3_geo_streamGeometryType[geometry.type](geometry, listener);
//                 }
//             }
//             var d3_geo_streamObjectType = {
//                 Feature: function(feature, listener) {
//                     d3_geo_streamGeometry(feature.geometry, listener);
//                 },
//                 FeatureCollection: function(object, listener) {
//                     var features = object.features, i = -1, n = features.length;
//                     while (++i < n) d3_geo_streamGeometry(features[i].geometry, listener);
//                 }
//             };
//             var d3_geo_streamGeometryType = {
//                 Sphere: function(object, listener) {
//                     listener.sphere();
//                 },
//                 Point: function(object, listener) {
//                     object = object.coordinates;
//                     listener.point(object[0], object[1], object[2]);
//                 },
//                 MultiPoint: function(object, listener) {
//                     var coordinates = object.coordinates, i = -1, n = coordinates.length;
//                     while (++i < n) object = coordinates[i], listener.point(object[0], object[1], object[2]);
//                 },
//                 LineString: function(object, listener) {
//                     d3_geo_streamLine(object.coordinates, listener, 0);
//                 },
//                 MultiLineString: function(object, listener) {
//                     var coordinates = object.coordinates, i = -1, n = coordinates.length;
//                     while (++i < n) d3_geo_streamLine(coordinates[i], listener, 0);
//                 },
//                 Polygon: function(object, listener) {
//                     d3_geo_streamPolygon(object.coordinates, listener);
//                 },
//                 MultiPolygon: function(object, listener) {
//                     var coordinates = object.coordinates, i = -1, n = coordinates.length;
//                     while (++i < n) d3_geo_streamPolygon(coordinates[i], listener);
//                 },
//                 GeometryCollection: function(object, listener) {
//                     var geometries = object.geometries, i = -1, n = geometries.length;
//                     while (++i < n) d3_geo_streamGeometry(geometries[i], listener);
//                 }
//             };
//             function d3_geo_streamLine(coordinates, listener, closed) {
//                 var i = -1, n = coordinates.length - closed, coordinate;
//                 listener.lineStart();
//                 while (++i < n) coordinate = coordinates[i], listener.point(coordinate[0], coordinate[1], coordinate[2]);
//                 listener.lineEnd();
//             }
//             function d3_geo_streamPolygon(coordinates, listener) {
//                 var i = -1, n = coordinates.length;
//                 listener.polygonStart();
//                 while (++i < n) d3_geo_streamLine(coordinates[i], listener, 1);
//                 listener.polygonEnd();
//             }
//             d3.geo.area = function(object) {
//                 d3_geo_areaSum = 0;
//                 d3.geo.stream(object, d3_geo_area);
//                 return d3_geo_areaSum;
//             };
//             var d3_geo_areaSum, d3_geo_areaRingSum = new d3_adder();
//             var d3_geo_area = {
//                 sphere: function() {
//                     d3_geo_areaSum += 4 * π;
//                 },
//                 point: d3_noop,
//                 lineStart: d3_noop,
//                 lineEnd: d3_noop,
//                 polygonStart: function() {
//                     d3_geo_areaRingSum.reset();
//                     d3_geo_area.lineStart = d3_geo_areaRingStart;
//                 },
//                 polygonEnd: function() {
//                     var area = 2 * d3_geo_areaRingSum;
//                     d3_geo_areaSum += area < 0 ? 4 * π + area : area;
//                     d3_geo_area.lineStart = d3_geo_area.lineEnd = d3_geo_area.point = d3_noop;
//                 }
//             };
//             function d3_geo_areaRingStart() {
//                 var λ00, φ00, λ0, cosφ0, sinφ0;
//                 d3_geo_area.point = function(λ, φ) {
//                     d3_geo_area.point = nextPoint;
//                     λ0 = (λ00 = λ) * d3_radians, cosφ0 = Math.cos(φ = (φ00 = φ) * d3_radians / 2 + π / 4),
//                         sinφ0 = Math.sin(φ);
//                 };
//                 function nextPoint(λ, φ) {
//                     λ *= d3_radians;
//                     φ = φ * d3_radians / 2 + π / 4;
//                     var dλ = λ - λ0, cosφ = Math.cos(φ), sinφ = Math.sin(φ), k = sinφ0 * sinφ, u = cosφ0 * cosφ + k * Math.cos(dλ), v = k * Math.sin(dλ);
//                     d3_geo_areaRingSum.add(Math.atan2(v, u));
//                     λ0 = λ, cosφ0 = cosφ, sinφ0 = sinφ;
//                 }
//                 d3_geo_area.lineEnd = function() {
//                     nextPoint(λ00, φ00);
//                 };
//             }
//             function d3_geo_cartesian(spherical) {
//                 var λ = spherical[0], φ = spherical[1], cosφ = Math.cos(φ);
//                 return [ cosφ * Math.cos(λ), cosφ * Math.sin(λ), Math.sin(φ) ];
//             }
//             function d3_geo_cartesianDot(a, b) {
//                 return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
//             }
//             function d3_geo_cartesianCross(a, b) {
//                 return [ a[1] * b[2] - a[2] * b[1], a[2] * b[0] - a[0] * b[2], a[0] * b[1] - a[1] * b[0] ];
//             }
//             function d3_geo_cartesianAdd(a, b) {
//                 a[0] += b[0];
//                 a[1] += b[1];
//                 a[2] += b[2];
//             }
//             function d3_geo_cartesianScale(vector, k) {
//                 return [ vector[0] * k, vector[1] * k, vector[2] * k ];
//             }
//             function d3_geo_cartesianNormalize(d) {
//                 var l = Math.sqrt(d[0] * d[0] + d[1] * d[1] + d[2] * d[2]);
//                 d[0] /= l;
//                 d[1] /= l;
//                 d[2] /= l;
//             }
//             function d3_geo_spherical(cartesian) {
//                 return [ Math.atan2(cartesian[1], cartesian[0]), d3_asin(cartesian[2]) ];
//             }
//             function d3_geo_sphericalEqual(a, b) {
//                 return abs(a[0] - b[0]) < ε && abs(a[1] - b[1]) < ε;
//             }
//             d3.geo.bounds = function() {
//                 var λ0, φ0, λ1, φ1, λ_, λ__, φ__, p0, dλSum, ranges, range;
//                 var bound = {
//                     point: point,
//                     lineStart: lineStart,
//                     lineEnd: lineEnd,
//                     polygonStart: function() {
//                         bound.point = ringPoint;
//                         bound.lineStart = ringStart;
//                         bound.lineEnd = ringEnd;
//                         dλSum = 0;
//                         d3_geo_area.polygonStart();
//                     },
//                     polygonEnd: function() {
//                         d3_geo_area.polygonEnd();
//                         bound.point = point;
//                         bound.lineStart = lineStart;
//                         bound.lineEnd = lineEnd;
//                         if (d3_geo_areaRingSum < 0) λ0 = -(λ1 = 180), φ0 = -(φ1 = 90); else if (dλSum > ε) φ1 = 90; else if (dλSum < -ε) φ0 = -90;
//                         range[0] = λ0, range[1] = λ1;
//                     }
//                 };
//                 function point(λ, φ) {
//                     ranges.push(range = [ λ0 = λ, λ1 = λ ]);
//                     if (φ < φ0) φ0 = φ;
//                     if (φ > φ1) φ1 = φ;
//                 }
//                 function linePoint(λ, φ) {
//                     var p = d3_geo_cartesian([ λ * d3_radians, φ * d3_radians ]);
//                     if (p0) {
//                         var normal = d3_geo_cartesianCross(p0, p), equatorial = [ normal[1], -normal[0], 0 ], inflection = d3_geo_cartesianCross(equatorial, normal);
//                         d3_geo_cartesianNormalize(inflection);
//                         inflection = d3_geo_spherical(inflection);
//                         var dλ = λ - λ_, s = dλ > 0 ? 1 : -1, λi = inflection[0] * d3_degrees * s, antimeridian = abs(dλ) > 180;
//                         if (antimeridian ^ (s * λ_ < λi && λi < s * λ)) {
//                             var φi = inflection[1] * d3_degrees;
//                             if (φi > φ1) φ1 = φi;
//                         } else if (λi = (λi + 360) % 360 - 180, antimeridian ^ (s * λ_ < λi && λi < s * λ)) {
//                             var φi = -inflection[1] * d3_degrees;
//                             if (φi < φ0) φ0 = φi;
//                         } else {
//                             if (φ < φ0) φ0 = φ;
//                             if (φ > φ1) φ1 = φ;
//                         }
//                         if (antimeridian) {
//                             if (λ < λ_) {
//                                 if (angle(λ0, λ) > angle(λ0, λ1)) λ1 = λ;
//                             } else {
//                                 if (angle(λ, λ1) > angle(λ0, λ1)) λ0 = λ;
//                             }
//                         } else {
//                             if (λ1 >= λ0) {
//                                 if (λ < λ0) λ0 = λ;
//                                 if (λ > λ1) λ1 = λ;
//                             } else {
//                                 if (λ > λ_) {
//                                     if (angle(λ0, λ) > angle(λ0, λ1)) λ1 = λ;
//                                 } else {
//                                     if (angle(λ, λ1) > angle(λ0, λ1)) λ0 = λ;
//                                 }
//                             }
//                         }
//                     } else {
//                         point(λ, φ);
//                     }
//                     p0 = p, λ_ = λ;
//                 }
//                 function lineStart() {
//                     bound.point = linePoint;
//                 }
//                 function lineEnd() {
//                     range[0] = λ0, range[1] = λ1;
//                     bound.point = point;
//                     p0 = null;
//                 }
//                 function ringPoint(λ, φ) {
//                     if (p0) {
//                         var dλ = λ - λ_;
//                         dλSum += abs(dλ) > 180 ? dλ + (dλ > 0 ? 360 : -360) : dλ;
//                     } else λ__ = λ, φ__ = φ;
//                     d3_geo_area.point(λ, φ);
//                     linePoint(λ, φ);
//                 }
//                 function ringStart() {
//                     d3_geo_area.lineStart();
//                 }
//                 function ringEnd() {
//                     ringPoint(λ__, φ__);
//                     d3_geo_area.lineEnd();
//                     if (abs(dλSum) > ε) λ0 = -(λ1 = 180);
//                     range[0] = λ0, range[1] = λ1;
//                     p0 = null;
//                 }
//                 function angle(λ0, λ1) {
//                     return (λ1 -= λ0) < 0 ? λ1 + 360 : λ1;
//                 }
//                 function compareRanges(a, b) {
//                     return a[0] - b[0];
//                 }
//                 function withinRange(x, range) {
//                     return range[0] <= range[1] ? range[0] <= x && x <= range[1] : x < range[0] || range[1] < x;
//                 }
//                 return function(feature) {
//                     φ1 = λ1 = -(λ0 = φ0 = Infinity);
//                     ranges = [];
//                     d3.geo.stream(feature, bound);
//                     var n = ranges.length;
//                     if (n) {
//                         ranges.sort(compareRanges);
//                         for (var i = 1, a = ranges[0], b, merged = [ a ]; i < n; ++i) {
//                             b = ranges[i];
//                             if (withinRange(b[0], a) || withinRange(b[1], a)) {
//                                 if (angle(a[0], b[1]) > angle(a[0], a[1])) a[1] = b[1];
//                                 if (angle(b[0], a[1]) > angle(a[0], a[1])) a[0] = b[0];
//                             } else {
//                                 merged.push(a = b);
//                             }
//                         }
//                         var best = -Infinity, dλ;
//                         for (var n = merged.length - 1, i = 0, a = merged[n], b; i <= n; a = b, ++i) {
//                             b = merged[i];
//                             if ((dλ = angle(a[1], b[0])) > best) best = dλ, λ0 = b[0], λ1 = a[1];
//                         }
//                     }
//                     ranges = range = null;
//                     return λ0 === Infinity || φ0 === Infinity ? [ [ NaN, NaN ], [ NaN, NaN ] ] : [ [ λ0, φ0 ], [ λ1, φ1 ] ];
//                 };
//             }();
//             d3.geo.centroid = function(object) {
//                 d3_geo_centroidW0 = d3_geo_centroidW1 = d3_geo_centroidX0 = d3_geo_centroidY0 = d3_geo_centroidZ0 = d3_geo_centroidX1 = d3_geo_centroidY1 = d3_geo_centroidZ1 = d3_geo_centroidX2 = d3_geo_centroidY2 = d3_geo_centroidZ2 = 0;
//                 d3.geo.stream(object, d3_geo_centroid);
//                 var x = d3_geo_centroidX2, y = d3_geo_centroidY2, z = d3_geo_centroidZ2, m = x * x + y * y + z * z;
//                 if (m < ε2) {
//                     x = d3_geo_centroidX1, y = d3_geo_centroidY1, z = d3_geo_centroidZ1;
//                     if (d3_geo_centroidW1 < ε) x = d3_geo_centroidX0, y = d3_geo_centroidY0, z = d3_geo_centroidZ0;
//                     m = x * x + y * y + z * z;
//                     if (m < ε2) return [ NaN, NaN ];
//                 }
//                 return [ Math.atan2(y, x) * d3_degrees, d3_asin(z / Math.sqrt(m)) * d3_degrees ];
//             };
//             var d3_geo_centroidW0, d3_geo_centroidW1, d3_geo_centroidX0, d3_geo_centroidY0, d3_geo_centroidZ0, d3_geo_centroidX1, d3_geo_centroidY1, d3_geo_centroidZ1, d3_geo_centroidX2, d3_geo_centroidY2, d3_geo_centroidZ2;
//             var d3_geo_centroid = {
//                 sphere: d3_noop,
//                 point: d3_geo_centroidPoint,
//                 lineStart: d3_geo_centroidLineStart,
//                 lineEnd: d3_geo_centroidLineEnd,
//                 polygonStart: function() {
//                     d3_geo_centroid.lineStart = d3_geo_centroidRingStart;
//                 },
//                 polygonEnd: function() {
//                     d3_geo_centroid.lineStart = d3_geo_centroidLineStart;
//                 }
//             };
//             function d3_geo_centroidPoint(λ, φ) {
//                 λ *= d3_radians;
//                 var cosφ = Math.cos(φ *= d3_radians);
//                 d3_geo_centroidPointXYZ(cosφ * Math.cos(λ), cosφ * Math.sin(λ), Math.sin(φ));
//             }
//             function d3_geo_centroidPointXYZ(x, y, z) {
//                 ++d3_geo_centroidW0;
//                 d3_geo_centroidX0 += (x - d3_geo_centroidX0) / d3_geo_centroidW0;
//                 d3_geo_centroidY0 += (y - d3_geo_centroidY0) / d3_geo_centroidW0;
//                 d3_geo_centroidZ0 += (z - d3_geo_centroidZ0) / d3_geo_centroidW0;
//             }
//             function d3_geo_centroidLineStart() {
//                 var x0, y0, z0;
//                 d3_geo_centroid.point = function(λ, φ) {
//                     λ *= d3_radians;
//                     var cosφ = Math.cos(φ *= d3_radians);
//                     x0 = cosφ * Math.cos(λ);
//                     y0 = cosφ * Math.sin(λ);
//                     z0 = Math.sin(φ);
//                     d3_geo_centroid.point = nextPoint;
//                     d3_geo_centroidPointXYZ(x0, y0, z0);
//                 };
//                 function nextPoint(λ, φ) {
//                     λ *= d3_radians;
//                     var cosφ = Math.cos(φ *= d3_radians), x = cosφ * Math.cos(λ), y = cosφ * Math.sin(λ), z = Math.sin(φ), w = Math.atan2(Math.sqrt((w = y0 * z - z0 * y) * w + (w = z0 * x - x0 * z) * w + (w = x0 * y - y0 * x) * w), x0 * x + y0 * y + z0 * z);
//                     d3_geo_centroidW1 += w;
//                     d3_geo_centroidX1 += w * (x0 + (x0 = x));
//                     d3_geo_centroidY1 += w * (y0 + (y0 = y));
//                     d3_geo_centroidZ1 += w * (z0 + (z0 = z));
//                     d3_geo_centroidPointXYZ(x0, y0, z0);
//                 }
//             }
//             function d3_geo_centroidLineEnd() {
//                 d3_geo_centroid.point = d3_geo_centroidPoint;
//             }
//             function d3_geo_centroidRingStart() {
//                 var λ00, φ00, x0, y0, z0;
//                 d3_geo_centroid.point = function(λ, φ) {
//                     λ00 = λ, φ00 = φ;
//                     d3_geo_centroid.point = nextPoint;
//                     λ *= d3_radians;
//                     var cosφ = Math.cos(φ *= d3_radians);
//                     x0 = cosφ * Math.cos(λ);
//                     y0 = cosφ * Math.sin(λ);
//                     z0 = Math.sin(φ);
//                     d3_geo_centroidPointXYZ(x0, y0, z0);
//                 };
//                 d3_geo_centroid.lineEnd = function() {
//                     nextPoint(λ00, φ00);
//                     d3_geo_centroid.lineEnd = d3_geo_centroidLineEnd;
//                     d3_geo_centroid.point = d3_geo_centroidPoint;
//                 };
//                 function nextPoint(λ, φ) {
//                     λ *= d3_radians;
//                     var cosφ = Math.cos(φ *= d3_radians), x = cosφ * Math.cos(λ), y = cosφ * Math.sin(λ), z = Math.sin(φ), cx = y0 * z - z0 * y, cy = z0 * x - x0 * z, cz = x0 * y - y0 * x, m = Math.sqrt(cx * cx + cy * cy + cz * cz), u = x0 * x + y0 * y + z0 * z, v = m && -d3_acos(u) / m, w = Math.atan2(m, u);
//                     d3_geo_centroidX2 += v * cx;
//                     d3_geo_centroidY2 += v * cy;
//                     d3_geo_centroidZ2 += v * cz;
//                     d3_geo_centroidW1 += w;
//                     d3_geo_centroidX1 += w * (x0 + (x0 = x));
//                     d3_geo_centroidY1 += w * (y0 + (y0 = y));
//                     d3_geo_centroidZ1 += w * (z0 + (z0 = z));
//                     d3_geo_centroidPointXYZ(x0, y0, z0);
//                 }
//             }
//             function d3_true() {
//                 return true;
//             }
//             function d3_geo_clipPolygon(segments, compare, clipStartInside, interpolate, listener) {
//                 var subject = [], clip = [];
//                 segments.forEach(function(segment) {
//                     if ((n = segment.length - 1) <= 0) return;
//                     var n, p0 = segment[0], p1 = segment[n];
//                     if (d3_geo_sphericalEqual(p0, p1)) {
//                         listener.lineStart();
//                         for (var i = 0; i < n; ++i) listener.point((p0 = segment[i])[0], p0[1]);
//                         listener.lineEnd();
//                         return;
//                     }
//                     var a = new d3_geo_clipPolygonIntersection(p0, segment, null, true), b = new d3_geo_clipPolygonIntersection(p0, null, a, false);
//                     a.o = b;
//                     subject.push(a);
//                     clip.push(b);
//                     a = new d3_geo_clipPolygonIntersection(p1, segment, null, false);
//                     b = new d3_geo_clipPolygonIntersection(p1, null, a, true);
//                     a.o = b;
//                     subject.push(a);
//                     clip.push(b);
//                 });
//                 clip.sort(compare);
//                 d3_geo_clipPolygonLinkCircular(subject);
//                 d3_geo_clipPolygonLinkCircular(clip);
//                 if (!subject.length) return;
//                 for (var i = 0, entry = clipStartInside, n = clip.length; i < n; ++i) {
//                     clip[i].e = entry = !entry;
//                 }
//                 var start = subject[0], points, point;
//                 while (1) {
//                     var current = start, isSubject = true;
//                     while (current.v) if ((current = current.n) === start) return;
//                     points = current.z;
//                     listener.lineStart();
//                     do {
//                         current.v = current.o.v = true;
//                         if (current.e) {
//                             if (isSubject) {
//                                 for (var i = 0, n = points.length; i < n; ++i) listener.point((point = points[i])[0], point[1]);
//                             } else {
//                                 interpolate(current.x, current.n.x, 1, listener);
//                             }
//                             current = current.n;
//                         } else {
//                             if (isSubject) {
//                                 points = current.p.z;
//                                 for (var i = points.length - 1; i >= 0; --i) listener.point((point = points[i])[0], point[1]);
//                             } else {
//                                 interpolate(current.x, current.p.x, -1, listener);
//                             }
//                             current = current.p;
//                         }
//                         current = current.o;
//                         points = current.z;
//                         isSubject = !isSubject;
//                     } while (!current.v);
//                     listener.lineEnd();
//                 }
//             }
//             function d3_geo_clipPolygonLinkCircular(array) {
//                 if (!(n = array.length)) return;
//                 var n, i = 0, a = array[0], b;
//                 while (++i < n) {
//                     a.n = b = array[i];
//                     b.p = a;
//                     a = b;
//                 }
//                 a.n = b = array[0];
//                 b.p = a;
//             }
//             function d3_geo_clipPolygonIntersection(point, points, other, entry) {
//                 this.x = point;
//                 this.z = points;
//                 this.o = other;
//                 this.e = entry;
//                 this.v = false;
//                 this.n = this.p = null;
//             }
//             function d3_geo_clip(pointVisible, clipLine, interpolate, clipStart) {
//                 return function(rotate, listener) {
//                     var line = clipLine(listener), rotatedClipStart = rotate.invert(clipStart[0], clipStart[1]);
//                     var clip = {
//                         point: point,
//                         lineStart: lineStart,
//                         lineEnd: lineEnd,
//                         polygonStart: function() {
//                             clip.point = pointRing;
//                             clip.lineStart = ringStart;
//                             clip.lineEnd = ringEnd;
//                             segments = [];
//                             polygon = [];
//                             listener.polygonStart();
//                         },
//                         polygonEnd: function() {
//                             clip.point = point;
//                             clip.lineStart = lineStart;
//                             clip.lineEnd = lineEnd;
//                             segments = d3.merge(segments);
//                             var clipStartInside = d3_geo_pointInPolygon(rotatedClipStart, polygon);
//                             if (segments.length) {
//                                 d3_geo_clipPolygon(segments, d3_geo_clipSort, clipStartInside, interpolate, listener);
//                             } else if (clipStartInside) {
//                                 listener.lineStart();
//                                 interpolate(null, null, 1, listener);
//                                 listener.lineEnd();
//                             }
//                             listener.polygonEnd();
//                             segments = polygon = null;
//                         },
//                         sphere: function() {
//                             listener.polygonStart();
//                             listener.lineStart();
//                             interpolate(null, null, 1, listener);
//                             listener.lineEnd();
//                             listener.polygonEnd();
//                         }
//                     };
//                     function point(λ, φ) {
//                         var point = rotate(λ, φ);
//                         if (pointVisible(λ = point[0], φ = point[1])) listener.point(λ, φ);
//                     }
//                     function pointLine(λ, φ) {
//                         var point = rotate(λ, φ);
//                         line.point(point[0], point[1]);
//                     }
//                     function lineStart() {
//                         clip.point = pointLine;
//                         line.lineStart();
//                     }
//                     function lineEnd() {
//                         clip.point = point;
//                         line.lineEnd();
//                     }
//                     var segments;
//                     var buffer = d3_geo_clipBufferListener(), ringListener = clipLine(buffer), polygon, ring;
//                     function pointRing(λ, φ) {
//                         ring.push([ λ, φ ]);
//                         var point = rotate(λ, φ);
//                         ringListener.point(point[0], point[1]);
//                     }
//                     function ringStart() {
//                         ringListener.lineStart();
//                         ring = [];
//                     }
//                     function ringEnd() {
//                         pointRing(ring[0][0], ring[0][1]);
//                         ringListener.lineEnd();
//                         var clean = ringListener.clean(), ringSegments = buffer.buffer(), segment, n = ringSegments.length;
//                         ring.pop();
//                         polygon.push(ring);
//                         ring = null;
//                         if (!n) return;
//                         if (clean & 1) {
//                             segment = ringSegments[0];
//                             var n = segment.length - 1, i = -1, point;
//                             listener.lineStart();
//                             while (++i < n) listener.point((point = segment[i])[0], point[1]);
//                             listener.lineEnd();
//                             return;
//                         }
//                         if (n > 1 && clean & 2) ringSegments.push(ringSegments.pop().concat(ringSegments.shift()));
//                         segments.push(ringSegments.filter(d3_geo_clipSegmentLength1));
//                     }
//                     return clip;
//                 };
//             }
//             function d3_geo_clipSegmentLength1(segment) {
//                 return segment.length > 1;
//             }
//             function d3_geo_clipBufferListener() {
//                 var lines = [], line;
//                 return {
//                     lineStart: function() {
//                         lines.push(line = []);
//                     },
//                         point: function(λ, φ) {
//                             line.push([ λ, φ ]);
//                         },
//                         lineEnd: d3_noop,
//                         buffer: function() {
//                             var buffer = lines;
//                             lines = [];
//                             line = null;
//                             return buffer;
//                         },
//                         rejoin: function() {
//                             if (lines.length > 1) lines.push(lines.pop().concat(lines.shift()));
//                         }
//                 };
//             }
//             function d3_geo_clipSort(a, b) {
//                 return ((a = a.x)[0] < 0 ? a[1] - halfπ - ε : halfπ - a[1]) - ((b = b.x)[0] < 0 ? b[1] - halfπ - ε : halfπ - b[1]);
//             }
//             function d3_geo_pointInPolygon(point, polygon) {
//                 var meridian = point[0], parallel = point[1], meridianNormal = [ Math.sin(meridian), -Math.cos(meridian), 0 ], polarAngle = 0, winding = 0;
//                 d3_geo_areaRingSum.reset();
//                 for (var i = 0, n = polygon.length; i < n; ++i) {
//                     var ring = polygon[i], m = ring.length;
//                     if (!m) continue;
//                     var point0 = ring[0], λ0 = point0[0], φ0 = point0[1] / 2 + π / 4, sinφ0 = Math.sin(φ0), cosφ0 = Math.cos(φ0), j = 1;
//                     while (true) {
//                         if (j === m) j = 0;
//                         point = ring[j];
//                         var λ = point[0], φ = point[1] / 2 + π / 4, sinφ = Math.sin(φ), cosφ = Math.cos(φ), dλ = λ - λ0, antimeridian = abs(dλ) > π, k = sinφ0 * sinφ;
//                         d3_geo_areaRingSum.add(Math.atan2(k * Math.sin(dλ), cosφ0 * cosφ + k * Math.cos(dλ)));
//                         polarAngle += antimeridian ? dλ + (dλ >= 0 ? τ : -τ) : dλ;
//                         if (antimeridian ^ λ0 >= meridian ^ λ >= meridian) {
//                             var arc = d3_geo_cartesianCross(d3_geo_cartesian(point0), d3_geo_cartesian(point));
//                             d3_geo_cartesianNormalize(arc);
//                             var intersection = d3_geo_cartesianCross(meridianNormal, arc);
//                             d3_geo_cartesianNormalize(intersection);
//                             var φarc = (antimeridian ^ dλ >= 0 ? -1 : 1) * d3_asin(intersection[2]);
//                             if (parallel > φarc || parallel === φarc && (arc[0] || arc[1])) {
//                                 winding += antimeridian ^ dλ >= 0 ? 1 : -1;
//                             }
//                         }
//                         if (!j++) break;
//                         λ0 = λ, sinφ0 = sinφ, cosφ0 = cosφ, point0 = point;
//                     }
//                 }
//                 return (polarAngle < -ε || polarAngle < ε && d3_geo_areaRingSum < 0) ^ winding & 1;
//             }
//             var d3_geo_clipAntimeridian = d3_geo_clip(d3_true, d3_geo_clipAntimeridianLine, d3_geo_clipAntimeridianInterpolate, [ -π, -π / 2 ]);
//             function d3_geo_clipAntimeridianLine(listener) {
//                 var λ0 = NaN, φ0 = NaN, sλ0 = NaN, clean;
//                 return {
//                     lineStart: function() {
//                         listener.lineStart();
//                         clean = 1;
//                     },
//                         point: function(λ1, φ1) {
//                             var sλ1 = λ1 > 0 ? π : -π, dλ = abs(λ1 - λ0);
//                             if (abs(dλ - π) < ε) {
//                                 listener.point(λ0, φ0 = (φ0 + φ1) / 2 > 0 ? halfπ : -halfπ);
//                                 listener.point(sλ0, φ0);
//                                 listener.lineEnd();
//                                 listener.lineStart();
//                                 listener.point(sλ1, φ0);
//                                 listener.point(λ1, φ0);
//                                 clean = 0;
//                             } else if (sλ0 !== sλ1 && dλ >= π) {
//                                 if (abs(λ0 - sλ0) < ε) λ0 -= sλ0 * ε;
//                                 if (abs(λ1 - sλ1) < ε) λ1 -= sλ1 * ε;
//                                 φ0 = d3_geo_clipAntimeridianIntersect(λ0, φ0, λ1, φ1);
//                                 listener.point(sλ0, φ0);
//                                 listener.lineEnd();
//                                 listener.lineStart();
//                                 listener.point(sλ1, φ0);
//                                 clean = 0;
//                             }
//                             listener.point(λ0 = λ1, φ0 = φ1);
//                             sλ0 = sλ1;
//                         },
//                         lineEnd: function() {
//                             listener.lineEnd();
//                             λ0 = φ0 = NaN;
//                         },
//                         clean: function() {
//                             return 2 - clean;
//                         }
//                 };
//             }
//             function d3_geo_clipAntimeridianIntersect(λ0, φ0, λ1, φ1) {
//                 var cosφ0, cosφ1, sinλ0_λ1 = Math.sin(λ0 - λ1);
//                 return abs(sinλ0_λ1) > ε ? Math.atan((Math.sin(φ0) * (cosφ1 = Math.cos(φ1)) * Math.sin(λ1) - Math.sin(φ1) * (cosφ0 = Math.cos(φ0)) * Math.sin(λ0)) / (cosφ0 * cosφ1 * sinλ0_λ1)) : (φ0 + φ1) / 2;
//             }
//             function d3_geo_clipAntimeridianInterpolate(from, to, direction, listener) {
//                 var φ;
//                 if (from == null) {
//                     φ = direction * halfπ;
//                     listener.point(-π, φ);
//                     listener.point(0, φ);
//                     listener.point(π, φ);
//                     listener.point(π, 0);
//                     listener.point(π, -φ);
//                     listener.point(0, -φ);
//                     listener.point(-π, -φ);
//                     listener.point(-π, 0);
//                     listener.point(-π, φ);
//                 } else if (abs(from[0] - to[0]) > ε) {
//                     var s = from[0] < to[0] ? π : -π;
//                     φ = direction * s / 2;
//                     listener.point(-s, φ);
//                     listener.point(0, φ);
//                     listener.point(s, φ);
//                 } else {
//                     listener.point(to[0], to[1]);
//                 }
//             }
//             function d3_geo_clipCircle(radius) {
//                 var cr = Math.cos(radius), smallRadius = cr > 0, notHemisphere = abs(cr) > ε, interpolate = d3_geo_circleInterpolate(radius, 6 * d3_radians);
//                 return d3_geo_clip(visible, clipLine, interpolate, smallRadius ? [ 0, -radius ] : [ -π, radius - π ]);
//                 function visible(λ, φ) {
//                     return Math.cos(λ) * Math.cos(φ) > cr;
//                 }
//                 function clipLine(listener) {
//                     var point0, c0, v0, v00, clean;
//                     return {
//                         lineStart: function() {
//                             v00 = v0 = false;
//                             clean = 1;
//                         },
//                             point: function(λ, φ) {
//                                 var point1 = [ λ, φ ], point2, v = visible(λ, φ), c = smallRadius ? v ? 0 : code(λ, φ) : v ? code(λ + (λ < 0 ? π : -π), φ) : 0;
//                                 if (!point0 && (v00 = v0 = v)) listener.lineStart();
//                                 if (v !== v0) {
//                                     point2 = intersect(point0, point1);
//                                     if (d3_geo_sphericalEqual(point0, point2) || d3_geo_sphericalEqual(point1, point2)) {
//                                         point1[0] += ε;
//                                         point1[1] += ε;
//                                         v = visible(point1[0], point1[1]);
//                                     }
//                                 }
//                                 if (v !== v0) {
//                                     clean = 0;
//                                     if (v) {
//                                         listener.lineStart();
//                                         point2 = intersect(point1, point0);
//                                         listener.point(point2[0], point2[1]);
//                                     } else {
//                                         point2 = intersect(point0, point1);
//                                         listener.point(point2[0], point2[1]);
//                                         listener.lineEnd();
//                                     }
//                                     point0 = point2;
//                                 } else if (notHemisphere && point0 && smallRadius ^ v) {
//                                     var t;
//                                     if (!(c & c0) && (t = intersect(point1, point0, true))) {
//                                         clean = 0;
//                                         if (smallRadius) {
//                                             listener.lineStart();
//                                             listener.point(t[0][0], t[0][1]);
//                                             listener.point(t[1][0], t[1][1]);
//                                             listener.lineEnd();
//                                         } else {
//                                             listener.point(t[1][0], t[1][1]);
//                                             listener.lineEnd();
//                                             listener.lineStart();
//                                             listener.point(t[0][0], t[0][1]);
//                                         }
//                                     }
//                                 }
//                                 if (v && (!point0 || !d3_geo_sphericalEqual(point0, point1))) {
//                                     listener.point(point1[0], point1[1]);
//                                 }
//                                 point0 = point1, v0 = v, c0 = c;
//                             },
//                             lineEnd: function() {
//                                 if (v0) listener.lineEnd();
//                                 point0 = null;
//                             },
//                             clean: function() {
//                                 return clean | (v00 && v0) << 1;
//                             }
//                     };
//                 }
//                 function intersect(a, b, two) {
//                     var pa = d3_geo_cartesian(a), pb = d3_geo_cartesian(b);
//                     var n1 = [ 1, 0, 0 ], n2 = d3_geo_cartesianCross(pa, pb), n2n2 = d3_geo_cartesianDot(n2, n2), n1n2 = n2[0], determinant = n2n2 - n1n2 * n1n2;
//                     if (!determinant) return !two && a;
//                     var c1 = cr * n2n2 / determinant, c2 = -cr * n1n2 / determinant, n1xn2 = d3_geo_cartesianCross(n1, n2), A = d3_geo_cartesianScale(n1, c1), B = d3_geo_cartesianScale(n2, c2);
//                     d3_geo_cartesianAdd(A, B);
//                     var u = n1xn2, w = d3_geo_cartesianDot(A, u), uu = d3_geo_cartesianDot(u, u), t2 = w * w - uu * (d3_geo_cartesianDot(A, A) - 1);
//                     if (t2 < 0) return;
//                     var t = Math.sqrt(t2), q = d3_geo_cartesianScale(u, (-w - t) / uu);
//                     d3_geo_cartesianAdd(q, A);
//                     q = d3_geo_spherical(q);
//                     if (!two) return q;
//                     var λ0 = a[0], λ1 = b[0], φ0 = a[1], φ1 = b[1], z;
//                     if (λ1 < λ0) z = λ0, λ0 = λ1, λ1 = z;
//                     var δλ = λ1 - λ0, polar = abs(δλ - π) < ε, meridian = polar || δλ < ε;
//                     if (!polar && φ1 < φ0) z = φ0, φ0 = φ1, φ1 = z;
//                     if (meridian ? polar ? φ0 + φ1 > 0 ^ q[1] < (abs(q[0] - λ0) < ε ? φ0 : φ1) : φ0 <= q[1] && q[1] <= φ1 : δλ > π ^ (λ0 <= q[0] && q[0] <= λ1)) {
//                         var q1 = d3_geo_cartesianScale(u, (-w + t) / uu);
//                         d3_geo_cartesianAdd(q1, A);
//                         return [ q, d3_geo_spherical(q1) ];
//                     }
//                 }
//                 function code(λ, φ) {
//                     var r = smallRadius ? radius : π - radius, code = 0;
//                     if (λ < -r) code |= 1; else if (λ > r) code |= 2;
//                     if (φ < -r) code |= 4; else if (φ > r) code |= 8;
//                     return code;
//                 }
//             }
//             function d3_geom_clipLine(x0, y0, x1, y1) {
//                 return function(line) {
//                     var a = line.a, b = line.b, ax = a.x, ay = a.y, bx = b.x, by = b.y, t0 = 0, t1 = 1, dx = bx - ax, dy = by - ay, r;
//                     r = x0 - ax;
//                     if (!dx && r > 0) return;
//                     r /= dx;
//                     if (dx < 0) {
//                         if (r < t0) return;
//                         if (r < t1) t1 = r;
//                     } else if (dx > 0) {
//                         if (r > t1) return;
//                         if (r > t0) t0 = r;
//                     }
//                     r = x1 - ax;
//                     if (!dx && r < 0) return;
//                     r /= dx;
//                     if (dx < 0) {
//                         if (r > t1) return;
//                         if (r > t0) t0 = r;
//                     } else if (dx > 0) {
//                         if (r < t0) return;
//                         if (r < t1) t1 = r;
//                     }
//                     r = y0 - ay;
//                     if (!dy && r > 0) return;
//                     r /= dy;
//                     if (dy < 0) {
//                         if (r < t0) return;
//                         if (r < t1) t1 = r;
//                     } else if (dy > 0) {
//                         if (r > t1) return;
//                         if (r > t0) t0 = r;
//                     }
//                     r = y1 - ay;
//                     if (!dy && r < 0) return;
//                     r /= dy;
//                     if (dy < 0) {
//                         if (r > t1) return;
//                         if (r > t0) t0 = r;
//                     } else if (dy > 0) {
//                         if (r < t0) return;
//                         if (r < t1) t1 = r;
//                     }
//                     if (t0 > 0) line.a = {
//                         x: ax + t0 * dx,
//                             y: ay + t0 * dy
//                     };
//                     if (t1 < 1) line.b = {
//                         x: ax + t1 * dx,
//                             y: ay + t1 * dy
//                     };
//                     return line;
//                 };
//             }
//             var d3_geo_clipExtentMAX = 1e9;
//             d3.geo.clipExtent = function() {
//                 var x0, y0, x1, y1, stream, clip, clipExtent = {
//                     stream: function(output) {
//                         if (stream) stream.valid = false;
//                         stream = clip(output);
//                         stream.valid = true;
//                         return stream;
//                     },
//                     extent: function(_) {
//                         if (!arguments.length) return [ [ x0, y0 ], [ x1, y1 ] ];
//                         clip = d3_geo_clipExtent(x0 = +_[0][0], y0 = +_[0][1], x1 = +_[1][0], y1 = +_[1][1]);
//                         if (stream) stream.valid = false, stream = null;
//                         return clipExtent;
//                     }
//                 };
//                 return clipExtent.extent([ [ 0, 0 ], [ 960, 500 ] ]);
//             };
//             function d3_geo_clipExtent(x0, y0, x1, y1) {
//                 return function(listener) {
//                     var listener_ = listener, bufferListener = d3_geo_clipBufferListener(), clipLine = d3_geom_clipLine(x0, y0, x1, y1), segments, polygon, ring;
//                     var clip = {
//                         point: point,
//                         lineStart: lineStart,
//                         lineEnd: lineEnd,
//                         polygonStart: function() {
//                             listener = bufferListener;
//                             segments = [];
//                             polygon = [];
//                             clean = true;
//                         },
//                         polygonEnd: function() {
//                             listener = listener_;
//                             segments = d3.merge(segments);
//                             var clipStartInside = insidePolygon([ x0, y1 ]), inside = clean && clipStartInside, visible = segments.length;
//                             if (inside || visible) {
//                                 listener.polygonStart();
//                                 if (inside) {
//                                     listener.lineStart();
//                                     interpolate(null, null, 1, listener);
//                                     listener.lineEnd();
//                                 }
//                                 if (visible) {
//                                     d3_geo_clipPolygon(segments, compare, clipStartInside, interpolate, listener);
//                                 }
//                                 listener.polygonEnd();
//                             }
//                             segments = polygon = ring = null;
//                         }
//                     };
//                     function insidePolygon(p) {
//                         var wn = 0, n = polygon.length, y = p[1];
//                         for (var i = 0; i < n; ++i) {
//                             for (var j = 1, v = polygon[i], m = v.length, a = v[0], b; j < m; ++j) {
//                                 b = v[j];
//                                 if (a[1] <= y) {
//                                     if (b[1] > y && isLeft(a, b, p) > 0) ++wn;
//                                 } else {
//                                     if (b[1] <= y && isLeft(a, b, p) < 0) --wn;
//                                 }
//                                 a = b;
//                             }
//                         }
//                         return wn !== 0;
//                     }
//                     function isLeft(a, b, c) {
//                         return (b[0] - a[0]) * (c[1] - a[1]) - (c[0] - a[0]) * (b[1] - a[1]);
//                     }
//                     function interpolate(from, to, direction, listener) {
//                         var a = 0, a1 = 0;
//                         if (from == null || (a = corner(from, direction)) !== (a1 = corner(to, direction)) || comparePoints(from, to) < 0 ^ direction > 0) {
//                             do {
//                                 listener.point(a === 0 || a === 3 ? x0 : x1, a > 1 ? y1 : y0);
//                             } while ((a = (a + direction + 4) % 4) !== a1);
//                         } else {
//                             listener.point(to[0], to[1]);
//                         }
//                     }
//                     function pointVisible(x, y) {
//                         return x0 <= x && x <= x1 && y0 <= y && y <= y1;
//                     }
//                     function point(x, y) {
//                         if (pointVisible(x, y)) listener.point(x, y);
//                     }
//                     var x__, y__, v__, x_, y_, v_, first, clean;
//                     function lineStart() {
//                         clip.point = linePoint;
//                         if (polygon) polygon.push(ring = []);
//                         first = true;
//                         v_ = false;
//                         x_ = y_ = NaN;
//                     }
//                     function lineEnd() {
//                         if (segments) {
//                             linePoint(x__, y__);
//                             if (v__ && v_) bufferListener.rejoin();
//                             segments.push(bufferListener.buffer());
//                         }
//                         clip.point = point;
//                         if (v_) listener.lineEnd();
//                     }
//                     function linePoint(x, y) {
//                         x = Math.max(-d3_geo_clipExtentMAX, Math.min(d3_geo_clipExtentMAX, x));
//                         y = Math.max(-d3_geo_clipExtentMAX, Math.min(d3_geo_clipExtentMAX, y));
//                         var v = pointVisible(x, y);
//                         if (polygon) ring.push([ x, y ]);
//                         if (first) {
//                             x__ = x, y__ = y, v__ = v;
//                             first = false;
//                             if (v) {
//                                 listener.lineStart();
//                                 listener.point(x, y);
//                             }
//                         } else {
//                             if (v && v_) listener.point(x, y); else {
//                                 var l = {
//                                     a: {
//                                         x: x_,
//                                         y: y_
//                                     },
//                                     b: {
//                                         x: x,
//                                         y: y
//                                     }
//                                 };
//                                 if (clipLine(l)) {
//                                     if (!v_) {
//                                         listener.lineStart();
//                                         listener.point(l.a.x, l.a.y);
//                                     }
//                                     listener.point(l.b.x, l.b.y);
//                                     if (!v) listener.lineEnd();
//                                     clean = false;
//                                 } else if (v) {
//                                     listener.lineStart();
//                                     listener.point(x, y);
//                                     clean = false;
//                                 }
//                             }
//                         }
//                         x_ = x, y_ = y, v_ = v;
//                     }
//                     return clip;
//                 };
//                 function corner(p, direction) {
//                     return abs(p[0] - x0) < ε ? direction > 0 ? 0 : 3 : abs(p[0] - x1) < ε ? direction > 0 ? 2 : 1 : abs(p[1] - y0) < ε ? direction > 0 ? 1 : 0 : direction > 0 ? 3 : 2;
//                 }
//                 function compare(a, b) {
//                     return comparePoints(a.x, b.x);
//                 }
//                 function comparePoints(a, b) {
//                     var ca = corner(a, 1), cb = corner(b, 1);
//                     return ca !== cb ? ca - cb : ca === 0 ? b[1] - a[1] : ca === 1 ? a[0] - b[0] : ca === 2 ? a[1] - b[1] : b[0] - a[0];
//                 }
//             }
//             function d3_geo_compose(a, b) {
//                 function compose(x, y) {
//                     return x = a(x, y), b(x[0], x[1]);
//                 }
//                 if (a.invert && b.invert) compose.invert = function(x, y) {
//                     return x = b.invert(x, y), x && a.invert(x[0], x[1]);
//                 };
//                 return compose;
//             }
//             function d3_geo_conic(projectAt) {
//                 var φ0 = 0, φ1 = π / 3, m = d3_geo_projectionMutator(projectAt), p = m(φ0, φ1);
//                 p.parallels = function(_) {
//                     if (!arguments.length) return [ φ0 / π * 180, φ1 / π * 180 ];
//                     return m(φ0 = _[0] * π / 180, φ1 = _[1] * π / 180);
//                 };
//                 return p;
//             }
//             function d3_geo_conicEqualArea(φ0, φ1) {
//                 var sinφ0 = Math.sin(φ0), n = (sinφ0 + Math.sin(φ1)) / 2, C = 1 + sinφ0 * (2 * n - sinφ0), ρ0 = Math.sqrt(C) / n;
//                 function forward(λ, φ) {
//                     var ρ = Math.sqrt(C - 2 * n * Math.sin(φ)) / n;
//                     return [ ρ * Math.sin(λ *= n), ρ0 - ρ * Math.cos(λ) ];
//                 }
//                 forward.invert = function(x, y) {
//                     var ρ0_y = ρ0 - y;
//                     return [ Math.atan2(x, ρ0_y) / n, d3_asin((C - (x * x + ρ0_y * ρ0_y) * n * n) / (2 * n)) ];
//                 };
//                 return forward;
//             }
//             (d3.geo.conicEqualArea = function() {
//                 return d3_geo_conic(d3_geo_conicEqualArea);
//             }).raw = d3_geo_conicEqualArea;
//             d3.geo.albers = function() {
//                 return d3.geo.conicEqualArea().rotate([ 96, 0 ]).center([ -.6, 38.7 ]).parallels([ 29.5, 45.5 ]).scale(1070);
//             };
//             d3.geo.albersUsa = function() {
//                 var lower48 = d3.geo.albers();
//                 var alaska = d3.geo.conicEqualArea().rotate([ 154, 0 ]).center([ -2, 58.5 ]).parallels([ 55, 65 ]);
//                 var hawaii = d3.geo.conicEqualArea().rotate([ 157, 0 ]).center([ -3, 19.9 ]).parallels([ 8, 18 ]);
//                 var point, pointStream = {
//                     point: function(x, y) {
//                         point = [ x, y ];
//                     }
//                 }, lower48Point, alaskaPoint, hawaiiPoint;
//                 function albersUsa(coordinates) {
//                     var x = coordinates[0], y = coordinates[1];
//                     point = null;
//                     (lower48Point(x, y), point) || (alaskaPoint(x, y), point) || hawaiiPoint(x, y);
//                     return point;
//                 }
//                 albersUsa.invert = function(coordinates) {
//                     var k = lower48.scale(), t = lower48.translate(), x = (coordinates[0] - t[0]) / k, y = (coordinates[1] - t[1]) / k;
//                     return (y >= .12 && y < .234 && x >= -.425 && x < -.214 ? alaska : y >= .166 && y < .234 && x >= -.214 && x < -.115 ? hawaii : lower48).invert(coordinates);
//                 };
//                 albersUsa.stream = function(stream) {
//                     var lower48Stream = lower48.stream(stream), alaskaStream = alaska.stream(stream), hawaiiStream = hawaii.stream(stream);
//                     return {
//                         point: function(x, y) {
//                             lower48Stream.point(x, y);
//                             alaskaStream.point(x, y);
//                             hawaiiStream.point(x, y);
//                         },
//                             sphere: function() {
//                                 lower48Stream.sphere();
//                                 alaskaStream.sphere();
//                                 hawaiiStream.sphere();
//                             },
//                             lineStart: function() {
//                                 lower48Stream.lineStart();
//                                 alaskaStream.lineStart();
//                                 hawaiiStream.lineStart();
//                             },
//                             lineEnd: function() {
//                                 lower48Stream.lineEnd();
//                                 alaskaStream.lineEnd();
//                                 hawaiiStream.lineEnd();
//                             },
//                             polygonStart: function() {
//                                 lower48Stream.polygonStart();
//                                 alaskaStream.polygonStart();
//                                 hawaiiStream.polygonStart();
//                             },
//                             polygonEnd: function() {
//                                 lower48Stream.polygonEnd();
//                                 alaskaStream.polygonEnd();
//                                 hawaiiStream.polygonEnd();
//                             }
//                     };
//                 };
//                 albersUsa.precision = function(_) {
//                     if (!arguments.length) return lower48.precision();
//                     lower48.precision(_);
//                     alaska.precision(_);
//                     hawaii.precision(_);
//                     return albersUsa;
//                 };
//                 albersUsa.scale = function(_) {
//                     if (!arguments.length) return lower48.scale();
//                     lower48.scale(_);
//                     alaska.scale(_ * .35);
//                     hawaii.scale(_);
//                     return albersUsa.translate(lower48.translate());
//                 };
//                 albersUsa.translate = function(_) {
//                     if (!arguments.length) return lower48.translate();
//                     var k = lower48.scale(), x = +_[0], y = +_[1];
//                     lower48Point = lower48.translate(_).clipExtent([ [ x - .455 * k, y - .238 * k ], [ x + .455 * k, y + .238 * k ] ]).stream(pointStream).point;
//                     alaskaPoint = alaska.translate([ x - .307 * k, y + .201 * k ]).clipExtent([ [ x - .425 * k + ε, y + .12 * k + ε ], [ x - .214 * k - ε, y + .234 * k - ε ] ]).stream(pointStream).point;
//                     hawaiiPoint = hawaii.translate([ x - .205 * k, y + .212 * k ]).clipExtent([ [ x - .214 * k + ε, y + .166 * k + ε ], [ x - .115 * k - ε, y + .234 * k - ε ] ]).stream(pointStream).point;
//                     return albersUsa;
//                 };
//                 return albersUsa.scale(1070);
//             };
//             var d3_geo_pathAreaSum, d3_geo_pathAreaPolygon, d3_geo_pathArea = {
//                 point: d3_noop,
//                 lineStart: d3_noop,
//                 lineEnd: d3_noop,
//                 polygonStart: function() {
//                     d3_geo_pathAreaPolygon = 0;
//                     d3_geo_pathArea.lineStart = d3_geo_pathAreaRingStart;
//                 },
//                 polygonEnd: function() {
//                     d3_geo_pathArea.lineStart = d3_geo_pathArea.lineEnd = d3_geo_pathArea.point = d3_noop;
//                     d3_geo_pathAreaSum += abs(d3_geo_pathAreaPolygon / 2);
//                 }
//             };
//             function d3_geo_pathAreaRingStart() {
//                 var x00, y00, x0, y0;
//                 d3_geo_pathArea.point = function(x, y) {
//                     d3_geo_pathArea.point = nextPoint;
//                     x00 = x0 = x, y00 = y0 = y;
//                 };
//                 function nextPoint(x, y) {
//                     d3_geo_pathAreaPolygon += y0 * x - x0 * y;
//                     x0 = x, y0 = y;
//                 }
//                 d3_geo_pathArea.lineEnd = function() {
//                     nextPoint(x00, y00);
//                 };
//             }
//             var d3_geo_pathBoundsX0, d3_geo_pathBoundsY0, d3_geo_pathBoundsX1, d3_geo_pathBoundsY1;
//             var d3_geo_pathBounds = {
//                 point: d3_geo_pathBoundsPoint,
//                 lineStart: d3_noop,
//                 lineEnd: d3_noop,
//                 polygonStart: d3_noop,
//                 polygonEnd: d3_noop
//             };
//             function d3_geo_pathBoundsPoint(x, y) {
//                 if (x < d3_geo_pathBoundsX0) d3_geo_pathBoundsX0 = x;
//                 if (x > d3_geo_pathBoundsX1) d3_geo_pathBoundsX1 = x;
//                 if (y < d3_geo_pathBoundsY0) d3_geo_pathBoundsY0 = y;
//                 if (y > d3_geo_pathBoundsY1) d3_geo_pathBoundsY1 = y;
//             }
//             function d3_geo_pathBuffer() {
//                 var pointCircle = d3_geo_pathBufferCircle(4.5), buffer = [];
//                 var stream = {
//                     point: point,
//                     lineStart: function() {
//                         stream.point = pointLineStart;
//                     },
//                     lineEnd: lineEnd,
//                     polygonStart: function() {
//                         stream.lineEnd = lineEndPolygon;
//                     },
//                     polygonEnd: function() {
//                         stream.lineEnd = lineEnd;
//                         stream.point = point;
//                     },
//                     pointRadius: function(_) {
//                         pointCircle = d3_geo_pathBufferCircle(_);
//                         return stream;
//                     },
//                     result: function() {
//                         if (buffer.length) {
//                             var result = buffer.join("");
//                             buffer = [];
//                             return result;
//                         }
//                     }
//                 };
//                 function point(x, y) {
//                     buffer.push("M", x, ",", y, pointCircle);
//                 }
//                 function pointLineStart(x, y) {
//                     buffer.push("M", x, ",", y);
//                     stream.point = pointLine;
//                 }
//                 function pointLine(x, y) {
//                     buffer.push("L", x, ",", y);
//                 }
//                 function lineEnd() {
//                     stream.point = point;
//                 }
//                 function lineEndPolygon() {
//                     buffer.push("Z");
//                 }
//                 return stream;
//             }
//             function d3_geo_pathBufferCircle(radius) {
//                 return "m0," + radius + "a" + radius + "," + radius + " 0 1,1 0," + -2 * radius + "a" + radius + "," + radius + " 0 1,1 0," + 2 * radius + "z";
//             }
//             var d3_geo_pathCentroid = {
//                 point: d3_geo_pathCentroidPoint,
//                 lineStart: d3_geo_pathCentroidLineStart,
//                 lineEnd: d3_geo_pathCentroidLineEnd,
//                 polygonStart: function() {
//                     d3_geo_pathCentroid.lineStart = d3_geo_pathCentroidRingStart;
//                 },
//                 polygonEnd: function() {
//                     d3_geo_pathCentroid.point = d3_geo_pathCentroidPoint;
//                     d3_geo_pathCentroid.lineStart = d3_geo_pathCentroidLineStart;
//                     d3_geo_pathCentroid.lineEnd = d3_geo_pathCentroidLineEnd;
//                 }
//             };
//             function d3_geo_pathCentroidPoint(x, y) {
//                 d3_geo_centroidX0 += x;
//                 d3_geo_centroidY0 += y;
//                 ++d3_geo_centroidZ0;
//             }
//             function d3_geo_pathCentroidLineStart() {
//                 var x0, y0;
//                 d3_geo_pathCentroid.point = function(x, y) {
//                     d3_geo_pathCentroid.point = nextPoint;
//                     d3_geo_pathCentroidPoint(x0 = x, y0 = y);
//                 };
//                 function nextPoint(x, y) {
//                     var dx = x - x0, dy = y - y0, z = Math.sqrt(dx * dx + dy * dy);
//                     d3_geo_centroidX1 += z * (x0 + x) / 2;
//                     d3_geo_centroidY1 += z * (y0 + y) / 2;
//                     d3_geo_centroidZ1 += z;
//                     d3_geo_pathCentroidPoint(x0 = x, y0 = y);
//                 }
//             }
//             function d3_geo_pathCentroidLineEnd() {
//                 d3_geo_pathCentroid.point = d3_geo_pathCentroidPoint;
//             }
//             function d3_geo_pathCentroidRingStart() {
//                 var x00, y00, x0, y0;
//                 d3_geo_pathCentroid.point = function(x, y) {
//                     d3_geo_pathCentroid.point = nextPoint;
//                     d3_geo_pathCentroidPoint(x00 = x0 = x, y00 = y0 = y);
//                 };
//                 function nextPoint(x, y) {
//                     var dx = x - x0, dy = y - y0, z = Math.sqrt(dx * dx + dy * dy);
//                     d3_geo_centroidX1 += z * (x0 + x) / 2;
//                     d3_geo_centroidY1 += z * (y0 + y) / 2;
//                     d3_geo_centroidZ1 += z;
//                     z = y0 * x - x0 * y;
//                     d3_geo_centroidX2 += z * (x0 + x);
//                     d3_geo_centroidY2 += z * (y0 + y);
//                     d3_geo_centroidZ2 += z * 3;
//                     d3_geo_pathCentroidPoint(x0 = x, y0 = y);
//                 }
//                 d3_geo_pathCentroid.lineEnd = function() {
//                     nextPoint(x00, y00);
//                 };
//             }
//             function d3_geo_pathContext(context) {
//                 var pointRadius = 4.5;
//                 var stream = {
//                     point: point,
//                     lineStart: function() {
//                         stream.point = pointLineStart;
//                     },
//                     lineEnd: lineEnd,
//                     polygonStart: function() {
//                         stream.lineEnd = lineEndPolygon;
//                     },
//                     polygonEnd: function() {
//                         stream.lineEnd = lineEnd;
//                         stream.point = point;
//                     },
//                     pointRadius: function(_) {
//                         pointRadius = _;
//                         return stream;
//                     },
//                     result: d3_noop
//                 };
//                 function point(x, y) {
//                     context.moveTo(x, y);
//                     context.arc(x, y, pointRadius, 0, τ);
//                 }
//                 function pointLineStart(x, y) {
//                     context.moveTo(x, y);
//                     stream.point = pointLine;
//                 }
//                 function pointLine(x, y) {
//                     context.lineTo(x, y);
//                 }
//                 function lineEnd() {
//                     stream.point = point;
//                 }
//                 function lineEndPolygon() {
//                     context.closePath();
//                 }
//                 return stream;
//             }
//             function d3_geo_resample(project) {
//                 var δ2 = .5, cosMinDistance = Math.cos(30 * d3_radians), maxDepth = 16;
//                 function resample(stream) {
//                     return (maxDepth ? resampleRecursive : resampleNone)(stream);
//                 }
//                 function resampleNone(stream) {
//                     return d3_geo_transformPoint(stream, function(x, y) {
//                         x = project(x, y);
//                         stream.point(x[0], x[1]);
//                     });
//                 }
//                 function resampleRecursive(stream) {
//                     var λ00, φ00, x00, y00, a00, b00, c00, λ0, x0, y0, a0, b0, c0;
//                     var resample = {
//                         point: point,
//                         lineStart: lineStart,
//                         lineEnd: lineEnd,
//                         polygonStart: function() {
//                             stream.polygonStart();
//                             resample.lineStart = ringStart;
//                         },
//                         polygonEnd: function() {
//                             stream.polygonEnd();
//                             resample.lineStart = lineStart;
//                         }
//                     };
//                     function point(x, y) {
//                         x = project(x, y);
//                         stream.point(x[0], x[1]);
//                     }
//                     function lineStart() {
//                         x0 = NaN;
//                         resample.point = linePoint;
//                         stream.lineStart();
//                     }
//                     function linePoint(λ, φ) {
//                         var c = d3_geo_cartesian([ λ, φ ]), p = project(λ, φ);
//                         resampleLineTo(x0, y0, λ0, a0, b0, c0, x0 = p[0], y0 = p[1], λ0 = λ, a0 = c[0], b0 = c[1], c0 = c[2], maxDepth, stream);
//                         stream.point(x0, y0);
//                     }
//                     function lineEnd() {
//                         resample.point = point;
//                         stream.lineEnd();
//                     }
//                     function ringStart() {
//                         lineStart();
//                         resample.point = ringPoint;
//                         resample.lineEnd = ringEnd;
//                     }
//                     function ringPoint(λ, φ) {
//                         linePoint(λ00 = λ, φ00 = φ), x00 = x0, y00 = y0, a00 = a0, b00 = b0, c00 = c0;
//                         resample.point = linePoint;
//                     }
//                     function ringEnd() {
//                         resampleLineTo(x0, y0, λ0, a0, b0, c0, x00, y00, λ00, a00, b00, c00, maxDepth, stream);
//                         resample.lineEnd = lineEnd;
//                         lineEnd();
//                     }
//                     return resample;
//                 }
//                 function resampleLineTo(x0, y0, λ0, a0, b0, c0, x1, y1, λ1, a1, b1, c1, depth, stream) {
//                     var dx = x1 - x0, dy = y1 - y0, d2 = dx * dx + dy * dy;
//                     if (d2 > 4 * δ2 && depth--) {
//                         var a = a0 + a1, b = b0 + b1, c = c0 + c1, m = Math.sqrt(a * a + b * b + c * c), φ2 = Math.asin(c /= m), λ2 = abs(abs(c) - 1) < ε ? (λ0 + λ1) / 2 : Math.atan2(b, a), p = project(λ2, φ2), x2 = p[0], y2 = p[1], dx2 = x2 - x0, dy2 = y2 - y0, dz = dy * dx2 - dx * dy2;
//                         if (dz * dz / d2 > δ2 || abs((dx * dx2 + dy * dy2) / d2 - .5) > .3 || a0 * a1 + b0 * b1 + c0 * c1 < cosMinDistance) {
//                             resampleLineTo(x0, y0, λ0, a0, b0, c0, x2, y2, λ2, a /= m, b /= m, c, depth, stream);
//                             stream.point(x2, y2);
//                             resampleLineTo(x2, y2, λ2, a, b, c, x1, y1, λ1, a1, b1, c1, depth, stream);
//                         }
//                     }
//                 }
//                 resample.precision = function(_) {
//                     if (!arguments.length) return Math.sqrt(δ2);
//                     maxDepth = (δ2 = _ * _) > 0 && 16;
//                     return resample;
//                 };
//                 return resample;
//             }
//             d3.geo.path = function() {
//                 var pointRadius = 4.5, projection, context, projectStream, contextStream, cacheStream;
//                 function path(object) {
//                     if (object) {
//                         if (typeof pointRadius === "function") contextStream.pointRadius(+pointRadius.apply(this, arguments));
//                         if (!cacheStream || !cacheStream.valid) cacheStream = projectStream(contextStream);
//                         d3.geo.stream(object, cacheStream);
//                     }
//                     return contextStream.result();
//                 }
//                 path.area = function(object) {
//                     d3_geo_pathAreaSum = 0;
//                     d3.geo.stream(object, projectStream(d3_geo_pathArea));
//                     return d3_geo_pathAreaSum;
//                 };
//                 path.centroid = function(object) {
//                     d3_geo_centroidX0 = d3_geo_centroidY0 = d3_geo_centroidZ0 = d3_geo_centroidX1 = d3_geo_centroidY1 = d3_geo_centroidZ1 = d3_geo_centroidX2 = d3_geo_centroidY2 = d3_geo_centroidZ2 = 0;
//                     d3.geo.stream(object, projectStream(d3_geo_pathCentroid));
//                     return d3_geo_centroidZ2 ? [ d3_geo_centroidX2 / d3_geo_centroidZ2, d3_geo_centroidY2 / d3_geo_centroidZ2 ] : d3_geo_centroidZ1 ? [ d3_geo_centroidX1 / d3_geo_centroidZ1, d3_geo_centroidY1 / d3_geo_centroidZ1 ] : d3_geo_centroidZ0 ? [ d3_geo_centroidX0 / d3_geo_centroidZ0, d3_geo_centroidY0 / d3_geo_centroidZ0 ] : [ NaN, NaN ];
//                 };
//                 path.bounds = function(object) {
//                     d3_geo_pathBoundsX1 = d3_geo_pathBoundsY1 = -(d3_geo_pathBoundsX0 = d3_geo_pathBoundsY0 = Infinity);
//                     d3.geo.stream(object, projectStream(d3_geo_pathBounds));
//                     return [ [ d3_geo_pathBoundsX0, d3_geo_pathBoundsY0 ], [ d3_geo_pathBoundsX1, d3_geo_pathBoundsY1 ] ];
//                 };
//                 path.projection = function(_) {
//                     if (!arguments.length) return projection;
//                     projectStream = (projection = _) ? _.stream || d3_geo_pathProjectStream(_) : d3_identity;
//                     return reset();
//                 };
//                 path.context = function(_) {
//                     if (!arguments.length) return context;
//                     contextStream = (context = _) == null ? new d3_geo_pathBuffer() : new d3_geo_pathContext(_);
//                     if (typeof pointRadius !== "function") contextStream.pointRadius(pointRadius);
//                     return reset();
//                 };
//                 path.pointRadius = function(_) {
//                     if (!arguments.length) return pointRadius;
//                     pointRadius = typeof _ === "function" ? _ : (contextStream.pointRadius(+_), +_);
//                     return path;
//                 };
//                 function reset() {
//                     cacheStream = null;
//                     return path;
//                 }
//                 return path.projection(d3.geo.albersUsa()).context(null);
//             };
//             function d3_geo_pathProjectStream(project) {
//                 var resample = d3_geo_resample(function(x, y) {
//                     return project([ x * d3_degrees, y * d3_degrees ]);
//                 });
//                 return function(stream) {
//                     return d3_geo_projectionRadians(resample(stream));
//                 };
//             }
//             d3.geo.transform = function(methods) {
//                 return {
//                     stream: function(stream) {
//                         var transform = new d3_geo_transform(stream);
//                         for (var k in methods) transform[k] = methods[k];
//                         return transform;
//                     }
//                 };
//             };
//             function d3_geo_transform(stream) {
//                 this.stream = stream;
//             }
//             d3_geo_transform.prototype = {
//                 point: function(x, y) {
//                     this.stream.point(x, y);
//                 },
//                 sphere: function() {
//                     this.stream.sphere();
//                 },
//                 lineStart: function() {
//                     this.stream.lineStart();
//                 },
//                 lineEnd: function() {
//                     this.stream.lineEnd();
//                 },
//                 polygonStart: function() {
//                     this.stream.polygonStart();
//                 },
//                 polygonEnd: function() {
//                     this.stream.polygonEnd();
//                 }
//             };
//             function d3_geo_transformPoint(stream, point) {
//                 return {
//                     point: point,
//                         sphere: function() {
//                             stream.sphere();
//                         },
//                         lineStart: function() {
//                             stream.lineStart();
//                         },
//                         lineEnd: function() {
//                             stream.lineEnd();
//                         },
//                         polygonStart: function() {
//                             stream.polygonStart();
//                         },
//                         polygonEnd: function() {
//                             stream.polygonEnd();
//                         }
//                 };
//             }
//             d3.geo.projection = d3_geo_projection;
//             d3.geo.projectionMutator = d3_geo_projectionMutator;
//             function d3_geo_projection(project) {
//                 return d3_geo_projectionMutator(function() {
//                     return project;
//                 })();
//             }
//             function d3_geo_projectionMutator(projectAt) {
//                 var project, rotate, projectRotate, projectResample = d3_geo_resample(function(x, y) {
//                     x = project(x, y);
//                     return [ x[0] * k + δx, δy - x[1] * k ];
//                 }), k = 150, x = 480, y = 250, λ = 0, φ = 0, δλ = 0, δφ = 0, δγ = 0, δx, δy, preclip = d3_geo_clipAntimeridian, postclip = d3_identity, clipAngle = null, clipExtent = null, stream;
//                 function projection(point) {
//                     point = projectRotate(point[0] * d3_radians, point[1] * d3_radians);
//                     return [ point[0] * k + δx, δy - point[1] * k ];
//                 }
//                 function invert(point) {
//                     point = projectRotate.invert((point[0] - δx) / k, (δy - point[1]) / k);
//                     return point && [ point[0] * d3_degrees, point[1] * d3_degrees ];
//                 }
//                 projection.stream = function(output) {
//                     if (stream) stream.valid = false;
//                     stream = d3_geo_projectionRadians(preclip(rotate, projectResample(postclip(output))));
//                     stream.valid = true;
//                     return stream;
//                 };
//                 projection.clipAngle = function(_) {
//                     if (!arguments.length) return clipAngle;
//                     preclip = _ == null ? (clipAngle = _, d3_geo_clipAntimeridian) : d3_geo_clipCircle((clipAngle = +_) * d3_radians);
//                     return invalidate();
//                 };
//                 projection.clipExtent = function(_) {
//                     if (!arguments.length) return clipExtent;
//                     clipExtent = _;
//                     postclip = _ ? d3_geo_clipExtent(_[0][0], _[0][1], _[1][0], _[1][1]) : d3_identity;
//                     return invalidate();
//                 };
//                 projection.scale = function(_) {
//                     if (!arguments.length) return k;
//                     k = +_;
//                     return reset();
//                 };
//                 projection.translate = function(_) {
//                     if (!arguments.length) return [ x, y ];
//                     x = +_[0];
//                     y = +_[1];
//                     return reset();
//                 };
//                 projection.center = function(_) {
//                     if (!arguments.length) return [ λ * d3_degrees, φ * d3_degrees ];
//                     λ = _[0] % 360 * d3_radians;
//                     φ = _[1] % 360 * d3_radians;
//                     return reset();
//                 };
//                 projection.rotate = function(_) {
//                     if (!arguments.length) return [ δλ * d3_degrees, δφ * d3_degrees, δγ * d3_degrees ];
//                     δλ = _[0] % 360 * d3_radians;
//                     δφ = _[1] % 360 * d3_radians;
//                     δγ = _.length > 2 ? _[2] % 360 * d3_radians : 0;
//                     return reset();
//                 };
//                 d3.rebind(projection, projectResample, "precision");
//                 function reset() {
//                     projectRotate = d3_geo_compose(rotate = d3_geo_rotation(δλ, δφ, δγ), project);
//                     var center = project(λ, φ);
//                     δx = x - center[0] * k;
//                     δy = y + center[1] * k;
//                     return invalidate();
//                 }
//                 function invalidate() {
//                     if (stream) stream.valid = false, stream = null;
//                     return projection;
//                 }
//                 return function() {
//                     project = projectAt.apply(this, arguments);
//                     projection.invert = project.invert && invert;
//                     return reset();
//                 };
//             }
//             function d3_geo_projectionRadians(stream) {
//                 return d3_geo_transformPoint(stream, function(x, y) {
//                     stream.point(x * d3_radians, y * d3_radians);
//                 });
//             }
//             function d3_geo_equirectangular(λ, φ) {
//                 return [ λ, φ ];
//             }
//             (d3.geo.equirectangular = function() {
//                 return d3_geo_projection(d3_geo_equirectangular);
//             }).raw = d3_geo_equirectangular.invert = d3_geo_equirectangular;
//             d3.geo.rotation = function(rotate) {
//                 rotate = d3_geo_rotation(rotate[0] % 360 * d3_radians, rotate[1] * d3_radians, rotate.length > 2 ? rotate[2] * d3_radians : 0);
//                 function forward(coordinates) {
//                     coordinates = rotate(coordinates[0] * d3_radians, coordinates[1] * d3_radians);
//                     return coordinates[0] *= d3_degrees, coordinates[1] *= d3_degrees, coordinates;
//                 }
//                 forward.invert = function(coordinates) {
//                     coordinates = rotate.invert(coordinates[0] * d3_radians, coordinates[1] * d3_radians);
//                     return coordinates[0] *= d3_degrees, coordinates[1] *= d3_degrees, coordinates;
//                 };
//                 return forward;
//             };
//             function d3_geo_identityRotation(λ, φ) {
//                 return [ λ > π ? λ - τ : λ < -π ? λ + τ : λ, φ ];
//             }
//             d3_geo_identityRotation.invert = d3_geo_equirectangular;
//             function d3_geo_rotation(δλ, δφ, δγ) {
//                 return δλ ? δφ || δγ ? d3_geo_compose(d3_geo_rotationλ(δλ), d3_geo_rotationφγ(δφ, δγ)) : d3_geo_rotationλ(δλ) : δφ || δγ ? d3_geo_rotationφγ(δφ, δγ) : d3_geo_identityRotation;
//             }
//             function d3_geo_forwardRotationλ(δλ) {
//                 return function(λ, φ) {
//                     return λ += δλ, [ λ > π ? λ - τ : λ < -π ? λ + τ : λ, φ ];
//                 };
//             }
//             function d3_geo_rotationλ(δλ) {
//                 var rotation = d3_geo_forwardRotationλ(δλ);
//                 rotation.invert = d3_geo_forwardRotationλ(-δλ);
//                 return rotation;
//             }
//             function d3_geo_rotationφγ(δφ, δγ) {
//                 var cosδφ = Math.cos(δφ), sinδφ = Math.sin(δφ), cosδγ = Math.cos(δγ), sinδγ = Math.sin(δγ);
//                 function rotation(λ, φ) {
//                     var cosφ = Math.cos(φ), x = Math.cos(λ) * cosφ, y = Math.sin(λ) * cosφ, z = Math.sin(φ), k = z * cosδφ + x * sinδφ;
//                     return [ Math.atan2(y * cosδγ - k * sinδγ, x * cosδφ - z * sinδφ), d3_asin(k * cosδγ + y * sinδγ) ];
//                 }
//                 rotation.invert = function(λ, φ) {
//                     var cosφ = Math.cos(φ), x = Math.cos(λ) * cosφ, y = Math.sin(λ) * cosφ, z = Math.sin(φ), k = z * cosδγ - y * sinδγ;
//                     return [ Math.atan2(y * cosδγ + z * sinδγ, x * cosδφ + k * sinδφ), d3_asin(k * cosδφ - x * sinδφ) ];
//                 };
//                 return rotation;
//             }
//             d3.geo.circle = function() {
//                 var origin = [ 0, 0 ], angle, precision = 6, interpolate;
//                 function circle() {
//                     var center = typeof origin === "function" ? origin.apply(this, arguments) : origin, rotate = d3_geo_rotation(-center[0] * d3_radians, -center[1] * d3_radians, 0).invert, ring = [];
//                     interpolate(null, null, 1, {
//                         point: function(x, y) {
//                             ring.push(x = rotate(x, y));
//                             x[0] *= d3_degrees, x[1] *= d3_degrees;
//                         }
//                     });
//                     return {
//                         type: "Polygon",
//                             coordinates: [ ring ]
//                     };
//                 }
//                 circle.origin = function(x) {
//                     if (!arguments.length) return origin;
//                     origin = x;
//                     return circle;
//                 };
//                 circle.angle = function(x) {
//                     if (!arguments.length) return angle;
//                     interpolate = d3_geo_circleInterpolate((angle = +x) * d3_radians, precision * d3_radians);
//                     return circle;
//                 };
//                 circle.precision = function(_) {
//                     if (!arguments.length) return precision;
//                     interpolate = d3_geo_circleInterpolate(angle * d3_radians, (precision = +_) * d3_radians);
//                     return circle;
//                 };
//                 return circle.angle(90);
//             };
//             function d3_geo_circleInterpolate(radius, precision) {
//                 var cr = Math.cos(radius), sr = Math.sin(radius);
//                 return function(from, to, direction, listener) {
//                     var step = direction * precision;
//                     if (from != null) {
//                         from = d3_geo_circleAngle(cr, from);
//                         to = d3_geo_circleAngle(cr, to);
//                         if (direction > 0 ? from < to : from > to) from += direction * τ;
//                     } else {
//                         from = radius + direction * τ;
//                         to = radius - .5 * step;
//                     }
//                     for (var point, t = from; direction > 0 ? t > to : t < to; t -= step) {
//                         listener.point((point = d3_geo_spherical([ cr, -sr * Math.cos(t), -sr * Math.sin(t) ]))[0], point[1]);
//                     }
//                 };
//             }
//             function d3_geo_circleAngle(cr, point) {
//                 var a = d3_geo_cartesian(point);
//                 a[0] -= cr;
//                 d3_geo_cartesianNormalize(a);
//                 var angle = d3_acos(-a[1]);
//                 return ((-a[2] < 0 ? -angle : angle) + 2 * Math.PI - ε) % (2 * Math.PI);
//             }
//             d3.geo.distance = function(a, b) {
//                 var Δλ = (b[0] - a[0]) * d3_radians, φ0 = a[1] * d3_radians, φ1 = b[1] * d3_radians, sinΔλ = Math.sin(Δλ), cosΔλ = Math.cos(Δλ), sinφ0 = Math.sin(φ0), cosφ0 = Math.cos(φ0), sinφ1 = Math.sin(φ1), cosφ1 = Math.cos(φ1), t;
//                 return Math.atan2(Math.sqrt((t = cosφ1 * sinΔλ) * t + (t = cosφ0 * sinφ1 - sinφ0 * cosφ1 * cosΔλ) * t), sinφ0 * sinφ1 + cosφ0 * cosφ1 * cosΔλ);
//             };
//             d3.geo.graticule = function() {
//                 var x1, x0, X1, X0, y1, y0, Y1, Y0, dx = 10, dy = dx, DX = 90, DY = 360, x, y, X, Y, precision = 2.5;
//                 function graticule() {
//                     return {
//                         type: "MultiLineString",
//                             coordinates: lines()
//                     };
//                 }
//                 function lines() {
//                     return d3.range(Math.ceil(X0 / DX) * DX, X1, DX).map(X).concat(d3.range(Math.ceil(Y0 / DY) * DY, Y1, DY).map(Y)).concat(d3.range(Math.ceil(x0 / dx) * dx, x1, dx).filter(function(x) {
//                         return abs(x % DX) > ε;
//                     }).map(x)).concat(d3.range(Math.ceil(y0 / dy) * dy, y1, dy).filter(function(y) {
//                         return abs(y % DY) > ε;
//                     }).map(y));
//                 }
//                 graticule.lines = function() {
//                     return lines().map(function(coordinates) {
//                         return {
//                             type: "LineString",
//                            coordinates: coordinates
//                         };
//                     });
//                 };
//                 graticule.outline = function() {
//                     return {
//                         type: "Polygon",
//                             coordinates: [ X(X0).concat(Y(Y1).slice(1), X(X1).reverse().slice(1), Y(Y0).reverse().slice(1)) ]
//                     };
//                 };
//                 graticule.extent = function(_) {
//                     if (!arguments.length) return graticule.minorExtent();
//                     return graticule.majorExtent(_).minorExtent(_);
//                 };
//                 graticule.majorExtent = function(_) {
//                     if (!arguments.length) return [ [ X0, Y0 ], [ X1, Y1 ] ];
//                     X0 = +_[0][0], X1 = +_[1][0];
//                     Y0 = +_[0][1], Y1 = +_[1][1];
//                     if (X0 > X1) _ = X0, X0 = X1, X1 = _;
//                     if (Y0 > Y1) _ = Y0, Y0 = Y1, Y1 = _;
//                     return graticule.precision(precision);
//                 };
//                 graticule.minorExtent = function(_) {
//                     if (!arguments.length) return [ [ x0, y0 ], [ x1, y1 ] ];
//                     x0 = +_[0][0], x1 = +_[1][0];
//                     y0 = +_[0][1], y1 = +_[1][1];
//                     if (x0 > x1) _ = x0, x0 = x1, x1 = _;
//                     if (y0 > y1) _ = y0, y0 = y1, y1 = _;
//                     return graticule.precision(precision);
//                 };
//                 graticule.step = function(_) {
//                     if (!arguments.length) return graticule.minorStep();
//                     return graticule.majorStep(_).minorStep(_);
//                 };
//                 graticule.majorStep = function(_) {
//                     if (!arguments.length) return [ DX, DY ];
//                     DX = +_[0], DY = +_[1];
//                     return graticule;
//                 };
//                 graticule.minorStep = function(_) {
//                     if (!arguments.length) return [ dx, dy ];
//                     dx = +_[0], dy = +_[1];
//                     return graticule;
//                 };
//                 graticule.precision = function(_) {
//                     if (!arguments.length) return precision;
//                     precision = +_;
//                     x = d3_geo_graticuleX(y0, y1, 90);
//                     y = d3_geo_graticuleY(x0, x1, precision);
//                     X = d3_geo_graticuleX(Y0, Y1, 90);
//                     Y = d3_geo_graticuleY(X0, X1, precision);
//                     return graticule;
//                 };
//                 return graticule.majorExtent([ [ -180, -90 + ε ], [ 180, 90 - ε ] ]).minorExtent([ [ -180, -80 - ε ], [ 180, 80 + ε ] ]);
//             };
//             function d3_geo_graticuleX(y0, y1, dy) {
//                 var y = d3.range(y0, y1 - ε, dy).concat(y1);
//                 return function(x) {
//                     return y.map(function(y) {
//                         return [ x, y ];
//                     });
//                 };
//             }
//             function d3_geo_graticuleY(x0, x1, dx) {
//                 var x = d3.range(x0, x1 - ε, dx).concat(x1);
//                 return function(y) {
//                     return x.map(function(x) {
//                         return [ x, y ];
//                     });
//                 };
//             }
//             function d3_source(d) {
//                 return d.source;
//             }
//             function d3_target(d) {
//                 return d.target;
//             }
//             d3.geo.greatArc = function() {
//                 var source = d3_source, source_, target = d3_target, target_;
//                 function greatArc() {
//                     return {
//                         type: "LineString",
//                             coordinates: [ source_ || source.apply(this, arguments), target_ || target.apply(this, arguments) ]
//                     };
//                 }
//                 greatArc.distance = function() {
//                     return d3.geo.distance(source_ || source.apply(this, arguments), target_ || target.apply(this, arguments));
//                 };
//                 greatArc.source = function(_) {
//                     if (!arguments.length) return source;
//                     source = _, source_ = typeof _ === "function" ? null : _;
//                     return greatArc;
//                 };
//                 greatArc.target = function(_) {
//                     if (!arguments.length) return target;
//                     target = _, target_ = typeof _ === "function" ? null : _;
//                     return greatArc;
//                 };
//                 greatArc.precision = function() {
//                     return arguments.length ? greatArc : 0;
//                 };
//                 return greatArc;
//             };
//             d3.geo.interpolate = function(source, target) {
//                 return d3_geo_interpolate(source[0] * d3_radians, source[1] * d3_radians, target[0] * d3_radians, target[1] * d3_radians);
//             };
//             function d3_geo_interpolate(x0, y0, x1, y1) {
//                 var cy0 = Math.cos(y0), sy0 = Math.sin(y0), cy1 = Math.cos(y1), sy1 = Math.sin(y1), kx0 = cy0 * Math.cos(x0), ky0 = cy0 * Math.sin(x0), kx1 = cy1 * Math.cos(x1), ky1 = cy1 * Math.sin(x1), d = 2 * Math.asin(Math.sqrt(d3_haversin(y1 - y0) + cy0 * cy1 * d3_haversin(x1 - x0))), k = 1 / Math.sin(d);
//                 var interpolate = d ? function(t) {
//                     var B = Math.sin(t *= d) * k, A = Math.sin(d - t) * k, x = A * kx0 + B * kx1, y = A * ky0 + B * ky1, z = A * sy0 + B * sy1;
//                     return [ Math.atan2(y, x) * d3_degrees, Math.atan2(z, Math.sqrt(x * x + y * y)) * d3_degrees ];
//                 } : function() {
//                     return [ x0 * d3_degrees, y0 * d3_degrees ];
//                 };
//                 interpolate.distance = d;
//                 return interpolate;
//             }
//             d3.geo.length = function(object) {
//                 d3_geo_lengthSum = 0;
//                 d3.geo.stream(object, d3_geo_length);
//                 return d3_geo_lengthSum;
//             };
//             var d3_geo_lengthSum;
//             var d3_geo_length = {
//                 sphere: d3_noop,
//                 point: d3_noop,
//                 lineStart: d3_geo_lengthLineStart,
//                 lineEnd: d3_noop,
//                 polygonStart: d3_noop,
//                 polygonEnd: d3_noop
//             };
//             function d3_geo_lengthLineStart() {
//                 var λ0, sinφ0, cosφ0;
//                 d3_geo_length.point = function(λ, φ) {
//                     λ0 = λ * d3_radians, sinφ0 = Math.sin(φ *= d3_radians), cosφ0 = Math.cos(φ);
//                     d3_geo_length.point = nextPoint;
//                 };
//                 d3_geo_length.lineEnd = function() {
//                     d3_geo_length.point = d3_geo_length.lineEnd = d3_noop;
//                 };
//                 function nextPoint(λ, φ) {
//                     var sinφ = Math.sin(φ *= d3_radians), cosφ = Math.cos(φ), t = abs((λ *= d3_radians) - λ0), cosΔλ = Math.cos(t);
//                     d3_geo_lengthSum += Math.atan2(Math.sqrt((t = cosφ * Math.sin(t)) * t + (t = cosφ0 * sinφ - sinφ0 * cosφ * cosΔλ) * t), sinφ0 * sinφ + cosφ0 * cosφ * cosΔλ);
//                     λ0 = λ, sinφ0 = sinφ, cosφ0 = cosφ;
//                 }
//             }
//             function d3_geo_azimuthal(scale, angle) {
//                 function azimuthal(λ, φ) {
//                     var cosλ = Math.cos(λ), cosφ = Math.cos(φ), k = scale(cosλ * cosφ);
//                     return [ k * cosφ * Math.sin(λ), k * Math.sin(φ) ];
//                 }
//                 azimuthal.invert = function(x, y) {
//                     var ρ = Math.sqrt(x * x + y * y), c = angle(ρ), sinc = Math.sin(c), cosc = Math.cos(c);
//                     return [ Math.atan2(x * sinc, ρ * cosc), Math.asin(ρ && y * sinc / ρ) ];
//                 };
//                 return azimuthal;
//             }
//             var d3_geo_azimuthalEqualArea = d3_geo_azimuthal(function(cosλcosφ) {
//                 return Math.sqrt(2 / (1 + cosλcosφ));
//             }, function(ρ) {
//                 return 2 * Math.asin(ρ / 2);
//             });
//             (d3.geo.azimuthalEqualArea = function() {
//                 return d3_geo_projection(d3_geo_azimuthalEqualArea);
//             }).raw = d3_geo_azimuthalEqualArea;
//             var d3_geo_azimuthalEquidistant = d3_geo_azimuthal(function(cosλcosφ) {
//                 var c = Math.acos(cosλcosφ);
//                 return c && c / Math.sin(c);
//             }, d3_identity);
//             (d3.geo.azimuthalEquidistant = function() {
//                 return d3_geo_projection(d3_geo_azimuthalEquidistant);
//             }).raw = d3_geo_azimuthalEquidistant;
//             function d3_geo_conicConformal(φ0, φ1) {
//                 var cosφ0 = Math.cos(φ0), t = function(φ) {
//                     return Math.tan(π / 4 + φ / 2);
//                 }, n = φ0 === φ1 ? Math.sin(φ0) : Math.log(cosφ0 / Math.cos(φ1)) / Math.log(t(φ1) / t(φ0)), F = cosφ0 * Math.pow(t(φ0), n) / n;
//                 if (!n) return d3_geo_mercator;
//                 function forward(λ, φ) {
//                     var ρ = abs(abs(φ) - halfπ) < ε ? 0 : F / Math.pow(t(φ), n);
//                     return [ ρ * Math.sin(n * λ), F - ρ * Math.cos(n * λ) ];
//                 }
//                 forward.invert = function(x, y) {
//                     var ρ0_y = F - y, ρ = d3_sgn(n) * Math.sqrt(x * x + ρ0_y * ρ0_y);
//                     return [ Math.atan2(x, ρ0_y) / n, 2 * Math.atan(Math.pow(F / ρ, 1 / n)) - halfπ ];
//                 };
//                 return forward;
//             }
//             (d3.geo.conicConformal = function() {
//                 return d3_geo_conic(d3_geo_conicConformal);
//             }).raw = d3_geo_conicConformal;
//             function d3_geo_conicEquidistant(φ0, φ1) {
//                 var cosφ0 = Math.cos(φ0), n = φ0 === φ1 ? Math.sin(φ0) : (cosφ0 - Math.cos(φ1)) / (φ1 - φ0), G = cosφ0 / n + φ0;
//                 if (abs(n) < ε) return d3_geo_equirectangular;
//                 function forward(λ, φ) {
//                     var ρ = G - φ;
//                     return [ ρ * Math.sin(n * λ), G - ρ * Math.cos(n * λ) ];
//                 }
//                 forward.invert = function(x, y) {
//                     var ρ0_y = G - y;
//                     return [ Math.atan2(x, ρ0_y) / n, G - d3_sgn(n) * Math.sqrt(x * x + ρ0_y * ρ0_y) ];
//                 };
//                 return forward;
//             }
//             (d3.geo.conicEquidistant = function() {
//                 return d3_geo_conic(d3_geo_conicEquidistant);
//             }).raw = d3_geo_conicEquidistant;
//             var d3_geo_gnomonic = d3_geo_azimuthal(function(cosλcosφ) {
//                 return 1 / cosλcosφ;
//             }, Math.atan);
//             (d3.geo.gnomonic = function() {
//                 return d3_geo_projection(d3_geo_gnomonic);
//             }).raw = d3_geo_gnomonic;
//             function d3_geo_mercator(λ, φ) {
//                 return [ λ, Math.log(Math.tan(π / 4 + φ / 2)) ];
//             }
//             d3_geo_mercator.invert = function(x, y) {
//                 return [ x, 2 * Math.atan(Math.exp(y)) - halfπ ];
//             };
//             function d3_geo_mercatorProjection(project) {
//                 var m = d3_geo_projection(project), scale = m.scale, translate = m.translate, clipExtent = m.clipExtent, clipAuto;
//                 m.scale = function() {
//                     var v = scale.apply(m, arguments);
//                     return v === m ? clipAuto ? m.clipExtent(null) : m : v;
//                 };
//                 m.translate = function() {
//                     var v = translate.apply(m, arguments);
//                     return v === m ? clipAuto ? m.clipExtent(null) : m : v;
//                 };
//                 m.clipExtent = function(_) {
//                     var v = clipExtent.apply(m, arguments);
//                     if (v === m) {
//                         if (clipAuto = _ == null) {
//                             var k = π * scale(), t = translate();
//                             clipExtent([ [ t[0] - k, t[1] - k ], [ t[0] + k, t[1] + k ] ]);
//                         }
//                     } else if (clipAuto) {
//                         v = null;
//                     }
//                     return v;
//                 };
//                 return m.clipExtent(null);
//             }
//             (d3.geo.mercator = function() {
//                 return d3_geo_mercatorProjection(d3_geo_mercator);
//             }).raw = d3_geo_mercator;
//             var d3_geo_orthographic = d3_geo_azimuthal(function() {
//                 return 1;
//             }, Math.asin);
//             (d3.geo.orthographic = function() {
//                 return d3_geo_projection(d3_geo_orthographic);
//             }).raw = d3_geo_orthographic;
//             var d3_geo_stereographic = d3_geo_azimuthal(function(cosλcosφ) {
//                 return 1 / (1 + cosλcosφ);
//             }, function(ρ) {
//                 return 2 * Math.atan(ρ);
//             });
//             (d3.geo.stereographic = function() {
//                 return d3_geo_projection(d3_geo_stereographic);
//             }).raw = d3_geo_stereographic;
//             function d3_geo_transverseMercator(λ, φ) {
//                 var B = Math.cos(φ) * Math.sin(λ);
//                 return [ Math.log((1 + B) / (1 - B)) / 2, Math.atan2(Math.tan(φ), Math.cos(λ)) ];
//             }
//             d3_geo_transverseMercator.invert = function(x, y) {
//                 return [ Math.atan2(d3_sinh(x), Math.cos(y)), d3_asin(Math.sin(y) / d3_cosh(x)) ];
//             };
//             (d3.geo.transverseMercator = function() {
//                 return d3_geo_mercatorProjection(d3_geo_transverseMercator);
//             }).raw = d3_geo_transverseMercator;
//             d3.geom = {};
//             function d3_geom_pointX(d) {
//                 return d[0];
//             }
//             function d3_geom_pointY(d) {
//                 return d[1];
//             }
//             d3.geom.hull = function(vertices) {
//                 var x = d3_geom_pointX, y = d3_geom_pointY;
//                 if (arguments.length) return hull(vertices);
//                 function hull(data) {
//                     if (data.length < 3) return [];
//                     var fx = d3_functor(x), fy = d3_functor(y), n = data.length, vertices, plen = n - 1, points = [], stack = [], d, i, j, h = 0, x1, y1, x2, y2, u, v, a, sp;
//                     if (fx === d3_geom_pointX && y === d3_geom_pointY) vertices = data; else for (i = 0,
//                         vertices = []; i < n; ++i) {
//                             vertices.push([ +fx.call(this, d = data[i], i), +fy.call(this, d, i) ]);
//                         }
//                     for (i = 1; i < n; ++i) {
//                         if (vertices[i][1] < vertices[h][1] || vertices[i][1] == vertices[h][1] && vertices[i][0] < vertices[h][0]) h = i;
//                     }
//                     for (i = 0; i < n; ++i) {
//                         if (i === h) continue;
//                         y1 = vertices[i][1] - vertices[h][1];
//                         x1 = vertices[i][0] - vertices[h][0];
//                         points.push({
//                             angle: Math.atan2(y1, x1),
//                             index: i
//                         });
//                     }
//                     points.sort(function(a, b) {
//                         return a.angle - b.angle;
//                     });
//                     a = points[0].angle;
//                     v = points[0].index;
//                     u = 0;
//                     for (i = 1; i < plen; ++i) {
//                         j = points[i].index;
//                         if (a == points[i].angle) {
//                             x1 = vertices[v][0] - vertices[h][0];
//                             y1 = vertices[v][1] - vertices[h][1];
//                             x2 = vertices[j][0] - vertices[h][0];
//                             y2 = vertices[j][1] - vertices[h][1];
//                             if (x1 * x1 + y1 * y1 >= x2 * x2 + y2 * y2) {
//                                 points[i].index = -1;
//                                 continue;
//                             } else {
//                                 points[u].index = -1;
//                             }
//                         }
//                         a = points[i].angle;
//                         u = i;
//                         v = j;
//                     }
//                     stack.push(h);
//                     for (i = 0, j = 0; i < 2; ++j) {
//                         if (points[j].index > -1) {
//                             stack.push(points[j].index);
//                             i++;
//                         }
//                     }
//                     sp = stack.length;
//                     for (;j < plen; ++j) {
//                         if (points[j].index < 0) continue;
//                         while (!d3_geom_hullCCW(stack[sp - 2], stack[sp - 1], points[j].index, vertices)) {
//                             --sp;
//                         }
//                         stack[sp++] = points[j].index;
//                     }
//                     var poly = [];
//                     for (i = sp - 1; i >= 0; --i) poly.push(data[stack[i]]);
//                     return poly;
//                 }
//                 hull.x = function(_) {
//                     return arguments.length ? (x = _, hull) : x;
//                 };
//                 hull.y = function(_) {
//                     return arguments.length ? (y = _, hull) : y;
//                 };
//                 return hull;
//             };
//             function d3_geom_hullCCW(i1, i2, i3, v) {
//                 var t, a, b, c, d, e, f;
//                 t = v[i1];
//                 a = t[0];
//                 b = t[1];
//                 t = v[i2];
//                 c = t[0];
//                 d = t[1];
//                 t = v[i3];
//                 e = t[0];
//                 f = t[1];
//                 return (f - b) * (c - a) - (d - b) * (e - a) > 0;
//             }
//             d3.geom.polygon = function(coordinates) {
//                 d3_subclass(coordinates, d3_geom_polygonPrototype);
//                 return coordinates;
//             };
//             var d3_geom_polygonPrototype = d3.geom.polygon.prototype = [];
//             d3_geom_polygonPrototype.area = function() {
//                 var i = -1, n = this.length, a, b = this[n - 1], area = 0;
//                 while (++i < n) {
//                     a = b;
//                     b = this[i];
//                     area += a[1] * b[0] - a[0] * b[1];
//                 }
//                 return area * .5;
//             };
//             d3_geom_polygonPrototype.centroid = function(k) {
//                 var i = -1, n = this.length, x = 0, y = 0, a, b = this[n - 1], c;
//                 if (!arguments.length) k = -1 / (6 * this.area());
//                 while (++i < n) {
//                     a = b;
//                     b = this[i];
//                     c = a[0] * b[1] - b[0] * a[1];
//                     x += (a[0] + b[0]) * c;
//                     y += (a[1] + b[1]) * c;
//                 }
//                 return [ x * k, y * k ];
//             };
//             d3_geom_polygonPrototype.clip = function(subject) {
//                 var input, closed = d3_geom_polygonClosed(subject), i = -1, n = this.length - d3_geom_polygonClosed(this), j, m, a = this[n - 1], b, c, d;
//                 while (++i < n) {
//                     input = subject.slice();
//                     subject.length = 0;
//                     b = this[i];
//                     c = input[(m = input.length - closed) - 1];
//                     j = -1;
//                     while (++j < m) {
//                         d = input[j];
//                         if (d3_geom_polygonInside(d, a, b)) {
//                             if (!d3_geom_polygonInside(c, a, b)) {
//                                 subject.push(d3_geom_polygonIntersect(c, d, a, b));
//                             }
//                             subject.push(d);
//                         } else if (d3_geom_polygonInside(c, a, b)) {
//                             subject.push(d3_geom_polygonIntersect(c, d, a, b));
//                         }
//                         c = d;
//                     }
//                     if (closed) subject.push(subject[0]);
//                     a = b;
//                 }
//                 return subject;
//             };
//             function d3_geom_polygonInside(p, a, b) {
//                 return (b[0] - a[0]) * (p[1] - a[1]) < (b[1] - a[1]) * (p[0] - a[0]);
//             }
//             function d3_geom_polygonIntersect(c, d, a, b) {
//                 var x1 = c[0], x3 = a[0], x21 = d[0] - x1, x43 = b[0] - x3, y1 = c[1], y3 = a[1], y21 = d[1] - y1, y43 = b[1] - y3, ua = (x43 * (y1 - y3) - y43 * (x1 - x3)) / (y43 * x21 - x43 * y21);
//                 return [ x1 + ua * x21, y1 + ua * y21 ];
//             }
//             function d3_geom_polygonClosed(coordinates) {
//                 var a = coordinates[0], b = coordinates[coordinates.length - 1];
//                 return !(a[0] - b[0] || a[1] - b[1]);
//             }
//             var d3_geom_voronoiEdges, d3_geom_voronoiCells, d3_geom_voronoiBeaches, d3_geom_voronoiBeachPool = [], d3_geom_voronoiFirstCircle, d3_geom_voronoiCircles, d3_geom_voronoiCirclePool = [];
//             function d3_geom_voronoiBeach() {
//                 d3_geom_voronoiRedBlackNode(this);
//                 this.edge = this.site = this.circle = null;
//             }
//             function d3_geom_voronoiCreateBeach(site) {
//                 var beach = d3_geom_voronoiBeachPool.pop() || new d3_geom_voronoiBeach();
//                 beach.site = site;
//                 return beach;
//             }
//             function d3_geom_voronoiDetachBeach(beach) {
//                 d3_geom_voronoiDetachCircle(beach);
//                 d3_geom_voronoiBeaches.remove(beach);
//                 d3_geom_voronoiBeachPool.push(beach);
//                 d3_geom_voronoiRedBlackNode(beach);
//             }
//             function d3_geom_voronoiRemoveBeach(beach) {
//                 var circle = beach.circle, x = circle.x, y = circle.cy, vertex = {
//                     x: x,
//                     y: y
//                 }, previous = beach.P, next = beach.N, disappearing = [ beach ];
//                 d3_geom_voronoiDetachBeach(beach);
//                 var lArc = previous;
//                 while (lArc.circle && abs(x - lArc.circle.x) < ε && abs(y - lArc.circle.cy) < ε) {
//                     previous = lArc.P;
//                     disappearing.unshift(lArc);
//                     d3_geom_voronoiDetachBeach(lArc);
//                     lArc = previous;
//                 }
//                 disappearing.unshift(lArc);
//                 d3_geom_voronoiDetachCircle(lArc);
//                 var rArc = next;
//                 while (rArc.circle && abs(x - rArc.circle.x) < ε && abs(y - rArc.circle.cy) < ε) {
//                     next = rArc.N;
//                     disappearing.push(rArc);
//                     d3_geom_voronoiDetachBeach(rArc);
//                     rArc = next;
//                 }
//                 disappearing.push(rArc);
//                 d3_geom_voronoiDetachCircle(rArc);
//                 var nArcs = disappearing.length, iArc;
//                 for (iArc = 1; iArc < nArcs; ++iArc) {
//                     rArc = disappearing[iArc];
//                     lArc = disappearing[iArc - 1];
//                     d3_geom_voronoiSetEdgeEnd(rArc.edge, lArc.site, rArc.site, vertex);
//                 }
//                 lArc = disappearing[0];
//                 rArc = disappearing[nArcs - 1];
//                 rArc.edge = d3_geom_voronoiCreateEdge(lArc.site, rArc.site, null, vertex);
//                 d3_geom_voronoiAttachCircle(lArc);
//                 d3_geom_voronoiAttachCircle(rArc);
//             }
//             function d3_geom_voronoiAddBeach(site) {
//                 var x = site.x, directrix = site.y, lArc, rArc, dxl, dxr, node = d3_geom_voronoiBeaches._;
//                 while (node) {
//                     dxl = d3_geom_voronoiLeftBreakPoint(node, directrix) - x;
//                     if (dxl > ε) node = node.L; else {
//                         dxr = x - d3_geom_voronoiRightBreakPoint(node, directrix);
//                         if (dxr > ε) {
//                             if (!node.R) {
//                                 lArc = node;
//                                 break;
//                             }
//                             node = node.R;
//                         } else {
//                             if (dxl > -ε) {
//                                 lArc = node.P;
//                                 rArc = node;
//                             } else if (dxr > -ε) {
//                                 lArc = node;
//                                 rArc = node.N;
//                             } else {
//                                 lArc = rArc = node;
//                             }
//                             break;
//                         }
//                     }
//                 }
//                 var newArc = d3_geom_voronoiCreateBeach(site);
//                 d3_geom_voronoiBeaches.insert(lArc, newArc);
//                 if (!lArc && !rArc) return;
//                 if (lArc === rArc) {
//                     d3_geom_voronoiDetachCircle(lArc);
//                     rArc = d3_geom_voronoiCreateBeach(lArc.site);
//                     d3_geom_voronoiBeaches.insert(newArc, rArc);
//                     newArc.edge = rArc.edge = d3_geom_voronoiCreateEdge(lArc.site, newArc.site);
//                     d3_geom_voronoiAttachCircle(lArc);
//                     d3_geom_voronoiAttachCircle(rArc);
//                     return;
//                 }
//                 if (!rArc) {
//                     newArc.edge = d3_geom_voronoiCreateEdge(lArc.site, newArc.site);
//                     return;
//                 }
//                 d3_geom_voronoiDetachCircle(lArc);
//                 d3_geom_voronoiDetachCircle(rArc);
//                 var lSite = lArc.site, ax = lSite.x, ay = lSite.y, bx = site.x - ax, by = site.y - ay, rSite = rArc.site, cx = rSite.x - ax, cy = rSite.y - ay, d = 2 * (bx * cy - by * cx), hb = bx * bx + by * by, hc = cx * cx + cy * cy, vertex = {
//                     x: (cy * hb - by * hc) / d + ax,
//                     y: (bx * hc - cx * hb) / d + ay
//                 };
//                 d3_geom_voronoiSetEdgeEnd(rArc.edge, lSite, rSite, vertex);
//                 newArc.edge = d3_geom_voronoiCreateEdge(lSite, site, null, vertex);
//                 rArc.edge = d3_geom_voronoiCreateEdge(site, rSite, null, vertex);
//                 d3_geom_voronoiAttachCircle(lArc);
//                 d3_geom_voronoiAttachCircle(rArc);
//             }
//             function d3_geom_voronoiLeftBreakPoint(arc, directrix) {
//                 var site = arc.site, rfocx = site.x, rfocy = site.y, pby2 = rfocy - directrix;
//                 if (!pby2) return rfocx;
//                 var lArc = arc.P;
//                 if (!lArc) return -Infinity;
//                 site = lArc.site;
//                 var lfocx = site.x, lfocy = site.y, plby2 = lfocy - directrix;
//                 if (!plby2) return lfocx;
//                 var hl = lfocx - rfocx, aby2 = 1 / pby2 - 1 / plby2, b = hl / plby2;
//                 if (aby2) return (-b + Math.sqrt(b * b - 2 * aby2 * (hl * hl / (-2 * plby2) - lfocy + plby2 / 2 + rfocy - pby2 / 2))) / aby2 + rfocx;
//                 return (rfocx + lfocx) / 2;
//             }
//             function d3_geom_voronoiRightBreakPoint(arc, directrix) {
//                 var rArc = arc.N;
//                 if (rArc) return d3_geom_voronoiLeftBreakPoint(rArc, directrix);
//                 var site = arc.site;
//                 return site.y === directrix ? site.x : Infinity;
//             }
//             function d3_geom_voronoiCell(site) {
//                 this.site = site;
//                 this.edges = [];
//             }
//             d3_geom_voronoiCell.prototype.prepare = function() {
//                 var halfEdges = this.edges, iHalfEdge = halfEdges.length, edge;
//                 while (iHalfEdge--) {
//                     edge = halfEdges[iHalfEdge].edge;
//                     if (!edge.b || !edge.a) halfEdges.splice(iHalfEdge, 1);
//                 }
//                 halfEdges.sort(d3_geom_voronoiHalfEdgeOrder);
//                 return halfEdges.length;
//             };
//             function d3_geom_voronoiCloseCells(extent) {
//                 var x0 = extent[0][0], x1 = extent[1][0], y0 = extent[0][1], y1 = extent[1][1], x2, y2, x3, y3, cells = d3_geom_voronoiCells, iCell = cells.length, cell, iHalfEdge, halfEdges, nHalfEdges, start, end;
//                 while (iCell--) {
//                     cell = cells[iCell];
//                     if (!cell || !cell.prepare()) continue;
//                     halfEdges = cell.edges;
//                     nHalfEdges = halfEdges.length;
//                     iHalfEdge = 0;
//                     while (iHalfEdge < nHalfEdges) {
//                         end = halfEdges[iHalfEdge].end(), x3 = end.x, y3 = end.y;
//                         start = halfEdges[++iHalfEdge % nHalfEdges].start(), x2 = start.x, y2 = start.y;
//                         if (abs(x3 - x2) > ε || abs(y3 - y2) > ε) {
//                             halfEdges.splice(iHalfEdge, 0, new d3_geom_voronoiHalfEdge(d3_geom_voronoiCreateBorderEdge(cell.site, end, abs(x3 - x0) < ε && y1 - y3 > ε ? {
//                                 x: x0,
//                                 y: abs(x2 - x0) < ε ? y2 : y1
//                             } : abs(y3 - y1) < ε && x1 - x3 > ε ? {
//                                 x: abs(y2 - y1) < ε ? x2 : x1,
//                                 y: y1
//                             } : abs(x3 - x1) < ε && y3 - y0 > ε ? {
//                                 x: x1,
//                                 y: abs(x2 - x1) < ε ? y2 : y0
//                             } : abs(y3 - y0) < ε && x3 - x0 > ε ? {
//                                 x: abs(y2 - y0) < ε ? x2 : x0,
//                                 y: y0
//                             } : null), cell.site, null));
//                             ++nHalfEdges;
//                         }
//                     }
//                 }
//             }
//             function d3_geom_voronoiHalfEdgeOrder(a, b) {
//                 return b.angle - a.angle;
//             }
//             function d3_geom_voronoiCircle() {
//                 d3_geom_voronoiRedBlackNode(this);
//                 this.x = this.y = this.arc = this.site = this.cy = null;
//             }
//             function d3_geom_voronoiAttachCircle(arc) {
//                 var lArc = arc.P, rArc = arc.N;
//                 if (!lArc || !rArc) return;
//                 var lSite = lArc.site, cSite = arc.site, rSite = rArc.site;
//                 if (lSite === rSite) return;
//                 var bx = cSite.x, by = cSite.y, ax = lSite.x - bx, ay = lSite.y - by, cx = rSite.x - bx, cy = rSite.y - by;
//                 var d = 2 * (ax * cy - ay * cx);
//                 if (d >= -ε2) return;
//                 var ha = ax * ax + ay * ay, hc = cx * cx + cy * cy, x = (cy * ha - ay * hc) / d, y = (ax * hc - cx * ha) / d, cy = y + by;
//                 var circle = d3_geom_voronoiCirclePool.pop() || new d3_geom_voronoiCircle();
//                 circle.arc = arc;
//                 circle.site = cSite;
//                 circle.x = x + bx;
//                 circle.y = cy + Math.sqrt(x * x + y * y);
//                 circle.cy = cy;
//                 arc.circle = circle;
//                 var before = null, node = d3_geom_voronoiCircles._;
//                 while (node) {
//                     if (circle.y < node.y || circle.y === node.y && circle.x <= node.x) {
//                         if (node.L) node = node.L; else {
//                             before = node.P;
//                             break;
//                         }
//                     } else {
//                         if (node.R) node = node.R; else {
//                             before = node;
//                             break;
//                         }
//                     }
//                 }
//                 d3_geom_voronoiCircles.insert(before, circle);
//                 if (!before) d3_geom_voronoiFirstCircle = circle;
//             }
//             function d3_geom_voronoiDetachCircle(arc) {
//                 var circle = arc.circle;
//                 if (circle) {
//                     if (!circle.P) d3_geom_voronoiFirstCircle = circle.N;
//                     d3_geom_voronoiCircles.remove(circle);
//                     d3_geom_voronoiCirclePool.push(circle);
//                     d3_geom_voronoiRedBlackNode(circle);
//                     arc.circle = null;
//                 }
//             }
//             function d3_geom_voronoiClipEdges(extent) {
//                 var edges = d3_geom_voronoiEdges, clip = d3_geom_clipLine(extent[0][0], extent[0][1], extent[1][0], extent[1][1]), i = edges.length, e;
//                 while (i--) {
//                     e = edges[i];
//                     if (!d3_geom_voronoiConnectEdge(e, extent) || !clip(e) || abs(e.a.x - e.b.x) < ε && abs(e.a.y - e.b.y) < ε) {
//                         e.a = e.b = null;
//                         edges.splice(i, 1);
//                     }
//                 }
//             }
//             function d3_geom_voronoiConnectEdge(edge, extent) {
//                 var vb = edge.b;
//                 if (vb) return true;
//                 var va = edge.a, x0 = extent[0][0], x1 = extent[1][0], y0 = extent[0][1], y1 = extent[1][1], lSite = edge.l, rSite = edge.r, lx = lSite.x, ly = lSite.y, rx = rSite.x, ry = rSite.y, fx = (lx + rx) / 2, fy = (ly + ry) / 2, fm, fb;
//                 if (ry === ly) {
//                     if (fx < x0 || fx >= x1) return;
//                     if (lx > rx) {
//                         if (!va) va = {
//                             x: fx,
//                                 y: y0
//                         }; else if (va.y >= y1) return;
//                         vb = {
//                             x: fx,
//                             y: y1
//                         };
//                     } else {
//                         if (!va) va = {
//                             x: fx,
//                                 y: y1
//                         }; else if (va.y < y0) return;
//                         vb = {
//                             x: fx,
//                             y: y0
//                         };
//                     }
//                 } else {
//                     fm = (lx - rx) / (ry - ly);
//                     fb = fy - fm * fx;
//                     if (fm < -1 || fm > 1) {
//                         if (lx > rx) {
//                             if (!va) va = {
//                                 x: (y0 - fb) / fm,
//                                     y: y0
//                             }; else if (va.y >= y1) return;
//                             vb = {
//                                 x: (y1 - fb) / fm,
//                                 y: y1
//                             };
//                         } else {
//                             if (!va) va = {
//                                 x: (y1 - fb) / fm,
//                                     y: y1
//                             }; else if (va.y < y0) return;
//                             vb = {
//                                 x: (y0 - fb) / fm,
//                                 y: y0
//                             };
//                         }
//                     } else {
//                         if (ly < ry) {
//                             if (!va) va = {
//                                 x: x0,
//                                     y: fm * x0 + fb
//                             }; else if (va.x >= x1) return;
//                             vb = {
//                                 x: x1,
//                                 y: fm * x1 + fb
//                             };
//                         } else {
//                             if (!va) va = {
//                                 x: x1,
//                                     y: fm * x1 + fb
//                             }; else if (va.x < x0) return;
//                             vb = {
//                                 x: x0,
//                                 y: fm * x0 + fb
//                             };
//                         }
//                     }
//                 }
//                 edge.a = va;
//                 edge.b = vb;
//                 return true;
//             }
//             function d3_geom_voronoiEdge(lSite, rSite) {
//                 this.l = lSite;
//                 this.r = rSite;
//                 this.a = this.b = null;
//             }
//             function d3_geom_voronoiCreateEdge(lSite, rSite, va, vb) {
//                 var edge = new d3_geom_voronoiEdge(lSite, rSite);
//                 d3_geom_voronoiEdges.push(edge);
//                 if (va) d3_geom_voronoiSetEdgeEnd(edge, lSite, rSite, va);
//                 if (vb) d3_geom_voronoiSetEdgeEnd(edge, rSite, lSite, vb);
//                 d3_geom_voronoiCells[lSite.i].edges.push(new d3_geom_voronoiHalfEdge(edge, lSite, rSite));
//                 d3_geom_voronoiCells[rSite.i].edges.push(new d3_geom_voronoiHalfEdge(edge, rSite, lSite));
//                 return edge;
//             }
//             function d3_geom_voronoiCreateBorderEdge(lSite, va, vb) {
//                 var edge = new d3_geom_voronoiEdge(lSite, null);
//                 edge.a = va;
//                 edge.b = vb;
//                 d3_geom_voronoiEdges.push(edge);
//                 return edge;
//             }
//             function d3_geom_voronoiSetEdgeEnd(edge, lSite, rSite, vertex) {
//                 if (!edge.a && !edge.b) {
//                     edge.a = vertex;
//                     edge.l = lSite;
//                     edge.r = rSite;
//                 } else if (edge.l === rSite) {
//                     edge.b = vertex;
//                 } else {
//                     edge.a = vertex;
//                 }
//             }
//             function d3_geom_voronoiHalfEdge(edge, lSite, rSite) {
//                 var va = edge.a, vb = edge.b;
//                 this.edge = edge;
//                 this.site = lSite;
//                 this.angle = rSite ? Math.atan2(rSite.y - lSite.y, rSite.x - lSite.x) : edge.l === lSite ? Math.atan2(vb.x - va.x, va.y - vb.y) : Math.atan2(va.x - vb.x, vb.y - va.y);
//             }
//             d3_geom_voronoiHalfEdge.prototype = {
//                 start: function() {
//                     return this.edge.l === this.site ? this.edge.a : this.edge.b;
//                 },
//                 end: function() {
//                     return this.edge.l === this.site ? this.edge.b : this.edge.a;
//                 }
//             };
//             function d3_geom_voronoiRedBlackTree() {
//                 this._ = null;
//             }
//             function d3_geom_voronoiRedBlackNode(node) {
//                 node.U = node.C = node.L = node.R = node.P = node.N = null;
//             }
//             d3_geom_voronoiRedBlackTree.prototype = {
//                 insert: function(after, node) {
//                     var parent, grandpa, uncle;
//                     if (after) {
//                         node.P = after;
//                         node.N = after.N;
//                         if (after.N) after.N.P = node;
//                         after.N = node;
//                         if (after.R) {
//                             after = after.R;
//                             while (after.L) after = after.L;
//                             after.L = node;
//                         } else {
//                             after.R = node;
//                         }
//                         parent = after;
//                     } else if (this._) {
//                         after = d3_geom_voronoiRedBlackFirst(this._);
//                         node.P = null;
//                         node.N = after;
//                         after.P = after.L = node;
//                         parent = after;
//                     } else {
//                         node.P = node.N = null;
//                         this._ = node;
//                         parent = null;
//                     }
//                     node.L = node.R = null;
//                     node.U = parent;
//                     node.C = true;
//                     after = node;
//                     while (parent && parent.C) {
//                         grandpa = parent.U;
//                         if (parent === grandpa.L) {
//                             uncle = grandpa.R;
//                             if (uncle && uncle.C) {
//                                 parent.C = uncle.C = false;
//                                 grandpa.C = true;
//                                 after = grandpa;
//                             } else {
//                                 if (after === parent.R) {
//                                     d3_geom_voronoiRedBlackRotateLeft(this, parent);
//                                     after = parent;
//                                     parent = after.U;
//                                 }
//                                 parent.C = false;
//                                 grandpa.C = true;
//                                 d3_geom_voronoiRedBlackRotateRight(this, grandpa);
//                             }
//                         } else {
//                             uncle = grandpa.L;
//                             if (uncle && uncle.C) {
//                                 parent.C = uncle.C = false;
//                                 grandpa.C = true;
//                                 after = grandpa;
//                             } else {
//                                 if (after === parent.L) {
//                                     d3_geom_voronoiRedBlackRotateRight(this, parent);
//                                     after = parent;
//                                     parent = after.U;
//                                 }
//                                 parent.C = false;
//                                 grandpa.C = true;
//                                 d3_geom_voronoiRedBlackRotateLeft(this, grandpa);
//                             }
//                         }
//                         parent = after.U;
//                     }
//                     this._.C = false;
//                 },
//                 remove: function(node) {
//                     if (node.N) node.N.P = node.P;
//                     if (node.P) node.P.N = node.N;
//                     node.N = node.P = null;
//                     var parent = node.U, sibling, left = node.L, right = node.R, next, red;
//                     if (!left) next = right; else if (!right) next = left; else next = d3_geom_voronoiRedBlackFirst(right);
//                     if (parent) {
//                         if (parent.L === node) parent.L = next; else parent.R = next;
//                     } else {
//                         this._ = next;
//                     }
//                     if (left && right) {
//                         red = next.C;
//                         next.C = node.C;
//                         next.L = left;
//                         left.U = next;
//                         if (next !== right) {
//                             parent = next.U;
//                             next.U = node.U;
//                             node = next.R;
//                             parent.L = node;
//                             next.R = right;
//                             right.U = next;
//                         } else {
//                             next.U = parent;
//                             parent = next;
//                             node = next.R;
//                         }
//                     } else {
//                         red = node.C;
//                         node = next;
//                     }
//                     if (node) node.U = parent;
//                     if (red) return;
//                     if (node && node.C) {
//                         node.C = false;
//                         return;
//                     }
//                     do {
//                         if (node === this._) break;
//                         if (node === parent.L) {
//                             sibling = parent.R;
//                             if (sibling.C) {
//                                 sibling.C = false;
//                                 parent.C = true;
//                                 d3_geom_voronoiRedBlackRotateLeft(this, parent);
//                                 sibling = parent.R;
//                             }
//                             if (sibling.L && sibling.L.C || sibling.R && sibling.R.C) {
//                                 if (!sibling.R || !sibling.R.C) {
//                                     sibling.L.C = false;
//                                     sibling.C = true;
//                                     d3_geom_voronoiRedBlackRotateRight(this, sibling);
//                                     sibling = parent.R;
//                                 }
//                                 sibling.C = parent.C;
//                                 parent.C = sibling.R.C = false;
//                                 d3_geom_voronoiRedBlackRotateLeft(this, parent);
//                                 node = this._;
//                                 break;
//                             }
//                         } else {
//                             sibling = parent.L;
//                             if (sibling.C) {
//                                 sibling.C = false;
//                                 parent.C = true;
//                                 d3_geom_voronoiRedBlackRotateRight(this, parent);
//                                 sibling = parent.L;
//                             }
//                             if (sibling.L && sibling.L.C || sibling.R && sibling.R.C) {
//                                 if (!sibling.L || !sibling.L.C) {
//                                     sibling.R.C = false;
//                                     sibling.C = true;
//                                     d3_geom_voronoiRedBlackRotateLeft(this, sibling);
//                                     sibling = parent.L;
//                                 }
//                                 sibling.C = parent.C;
//                                 parent.C = sibling.L.C = false;
//                                 d3_geom_voronoiRedBlackRotateRight(this, parent);
//                                 node = this._;
//                                 break;
//                             }
//                         }
//                         sibling.C = true;
//                         node = parent;
//                         parent = parent.U;
//                     } while (!node.C);
//                     if (node) node.C = false;
//                 }
//             };
//             function d3_geom_voronoiRedBlackRotateLeft(tree, node) {
//                 var p = node, q = node.R, parent = p.U;
//                 if (parent) {
//                     if (parent.L === p) parent.L = q; else parent.R = q;
//                 } else {
//                     tree._ = q;
//                 }
//                 q.U = parent;
//                 p.U = q;
//                 p.R = q.L;
//                 if (p.R) p.R.U = p;
//                 q.L = p;
//             }
//             function d3_geom_voronoiRedBlackRotateRight(tree, node) {
//                 var p = node, q = node.L, parent = p.U;
//                 if (parent) {
//                     if (parent.L === p) parent.L = q; else parent.R = q;
//                 } else {
//                     tree._ = q;
//                 }
//                 q.U = parent;
//                 p.U = q;
//                 p.L = q.R;
//                 if (p.L) p.L.U = p;
//                 q.R = p;
//             }
//             function d3_geom_voronoiRedBlackFirst(node) {
//                 while (node.L) node = node.L;
//                 return node;
//             }
//             function d3_geom_voronoi(sites, bbox) {
//                 var site = sites.sort(d3_geom_voronoiVertexOrder).pop(), x0, y0, circle;
//                 d3_geom_voronoiEdges = [];
//                 d3_geom_voronoiCells = new Array(sites.length);
//                 d3_geom_voronoiBeaches = new d3_geom_voronoiRedBlackTree();
//                 d3_geom_voronoiCircles = new d3_geom_voronoiRedBlackTree();
//                 while (true) {
//                     circle = d3_geom_voronoiFirstCircle;
//                     if (site && (!circle || site.y < circle.y || site.y === circle.y && site.x < circle.x)) {
//                         if (site.x !== x0 || site.y !== y0) {
//                             d3_geom_voronoiCells[site.i] = new d3_geom_voronoiCell(site);
//                             d3_geom_voronoiAddBeach(site);
//                             x0 = site.x, y0 = site.y;
//                         }
//                         site = sites.pop();
//                     } else if (circle) {
//                         d3_geom_voronoiRemoveBeach(circle.arc);
//                     } else {
//                         break;
//                     }
//                 }
//                 if (bbox) d3_geom_voronoiClipEdges(bbox), d3_geom_voronoiCloseCells(bbox);
//                 var diagram = {
//                     cells: d3_geom_voronoiCells,
//                     edges: d3_geom_voronoiEdges
//                 };
//                 d3_geom_voronoiBeaches = d3_geom_voronoiCircles = d3_geom_voronoiEdges = d3_geom_voronoiCells = null;
//                 return diagram;
//             }
//             function d3_geom_voronoiVertexOrder(a, b) {
//                 return b.y - a.y || b.x - a.x;
//             }
//             d3.geom.voronoi = function(points) {
//                 var x = d3_geom_pointX, y = d3_geom_pointY, fx = x, fy = y, clipExtent = d3_geom_voronoiClipExtent;
//                 if (points) return voronoi(points);
//                 function voronoi(data) {
//                     var polygons = new Array(data.length), x0 = clipExtent[0][0], y0 = clipExtent[0][1], x1 = clipExtent[1][0], y1 = clipExtent[1][1];
//                     d3_geom_voronoi(sites(data), clipExtent).cells.forEach(function(cell, i) {
//                         var edges = cell.edges, site = cell.site, polygon = polygons[i] = edges.length ? edges.map(function(e) {
//                             var s = e.start();
//                             return [ s.x, s.y ];
//                         }) : site.x >= x0 && site.x <= x1 && site.y >= y0 && site.y <= y1 ? [ [ x0, y1 ], [ x1, y1 ], [ x1, y0 ], [ x0, y0 ] ] : [];
//                         polygon.point = data[i];
//                     });
//                     return polygons;
//                 }
//                 function sites(data) {
//                     return data.map(function(d, i) {
//                         return {
//                             x: Math.round(fx(d, i) / ε) * ε,
//                            y: Math.round(fy(d, i) / ε) * ε,
//                            i: i
//                         };
//                     });
//                 }
//                 voronoi.links = function(data) {
//                     return d3_geom_voronoi(sites(data)).edges.filter(function(edge) {
//                         return edge.l && edge.r;
//                     }).map(function(edge) {
//                         return {
//                             source: data[edge.l.i],
//                         target: data[edge.r.i]
//                         };
//                     });
//                 };
//                 voronoi.triangles = function(data) {
//                     var triangles = [];
//                     d3_geom_voronoi(sites(data)).cells.forEach(function(cell, i) {
//                         var site = cell.site, edges = cell.edges.sort(d3_geom_voronoiHalfEdgeOrder), j = -1, m = edges.length, e0, s0, e1 = edges[m - 1].edge, s1 = e1.l === site ? e1.r : e1.l;
//                         while (++j < m) {
//                             e0 = e1;
//                             s0 = s1;
//                             e1 = edges[j].edge;
//                             s1 = e1.l === site ? e1.r : e1.l;
//                             if (i < s0.i && i < s1.i && d3_geom_voronoiTriangleArea(site, s0, s1) < 0) {
//                                 triangles.push([ data[i], data[s0.i], data[s1.i] ]);
//                             }
//                         }
//                     });
//                     return triangles;
//                 };
//                 voronoi.x = function(_) {
//                     return arguments.length ? (fx = d3_functor(x = _), voronoi) : x;
//                 };
//                 voronoi.y = function(_) {
//                     return arguments.length ? (fy = d3_functor(y = _), voronoi) : y;
//                 };
//                 voronoi.clipExtent = function(_) {
//                     if (!arguments.length) return clipExtent === d3_geom_voronoiClipExtent ? null : clipExtent;
//                     clipExtent = _ == null ? d3_geom_voronoiClipExtent : _;
//                     return voronoi;
//                 };
//                 voronoi.size = function(_) {
//                     if (!arguments.length) return clipExtent === d3_geom_voronoiClipExtent ? null : clipExtent && clipExtent[1];
//                     return voronoi.clipExtent(_ && [ [ 0, 0 ], _ ]);
//                 };
//                 return voronoi;
//             };
//             var d3_geom_voronoiClipExtent = [ [ -1e6, -1e6 ], [ 1e6, 1e6 ] ];
//             function d3_geom_voronoiTriangleArea(a, b, c) {
//                 return (a.x - c.x) * (b.y - a.y) - (a.x - b.x) * (c.y - a.y);
//             }
//             d3.geom.delaunay = function(vertices) {
//                 return d3.geom.voronoi().triangles(vertices);
//             };
//             d3.geom.quadtree = function(points, x1, y1, x2, y2) {
//                 var x = d3_geom_pointX, y = d3_geom_pointY, compat;
//                 if (compat = arguments.length) {
//                     x = d3_geom_quadtreeCompatX;
//                     y = d3_geom_quadtreeCompatY;
//                     if (compat === 3) {
//                         y2 = y1;
//                         x2 = x1;
//                         y1 = x1 = 0;
//                     }
//                     return quadtree(points);
//                 }
//                 function quadtree(data) {
//                     var d, fx = d3_functor(x), fy = d3_functor(y), xs, ys, i, n, x1_, y1_, x2_, y2_;
//                     if (x1 != null) {
//                         x1_ = x1, y1_ = y1, x2_ = x2, y2_ = y2;
//                     } else {
//                         x2_ = y2_ = -(x1_ = y1_ = Infinity);
//                         xs = [], ys = [];
//                         n = data.length;
//                         if (compat) for (i = 0; i < n; ++i) {
//                             d = data[i];
//                             if (d.x < x1_) x1_ = d.x;
//                             if (d.y < y1_) y1_ = d.y;
//                             if (d.x > x2_) x2_ = d.x;
//                             if (d.y > y2_) y2_ = d.y;
//                             xs.push(d.x);
//                             ys.push(d.y);
//                         } else for (i = 0; i < n; ++i) {
//                             var x_ = +fx(d = data[i], i), y_ = +fy(d, i);
//                             if (x_ < x1_) x1_ = x_;
//                             if (y_ < y1_) y1_ = y_;
//                             if (x_ > x2_) x2_ = x_;
//                             if (y_ > y2_) y2_ = y_;
//                             xs.push(x_);
//                             ys.push(y_);
//                         }
//                     }
//                     var dx = x2_ - x1_, dy = y2_ - y1_;
//                     if (dx > dy) y2_ = y1_ + dx; else x2_ = x1_ + dy;
//                     function insert(n, d, x, y, x1, y1, x2, y2) {
//                         if (isNaN(x) || isNaN(y)) return;
//                         if (n.leaf) {
//                             var nx = n.x, ny = n.y;
//                             if (nx != null) {
//                                 if (abs(nx - x) + abs(ny - y) < .01) {
//                                     insertChild(n, d, x, y, x1, y1, x2, y2);
//                                 } else {
//                                     var nPoint = n.point;
//                                     n.x = n.y = n.point = null;
//                                     insertChild(n, nPoint, nx, ny, x1, y1, x2, y2);
//                                     insertChild(n, d, x, y, x1, y1, x2, y2);
//                                 }
//                             } else {
//                                 n.x = x, n.y = y, n.point = d;
//                             }
//                         } else {
//                             insertChild(n, d, x, y, x1, y1, x2, y2);
//                         }
//                     }
//                     function insertChild(n, d, x, y, x1, y1, x2, y2) {
//                         var sx = (x1 + x2) * .5, sy = (y1 + y2) * .5, right = x >= sx, bottom = y >= sy, i = (bottom << 1) + right;
//                         n.leaf = false;
//                         n = n.nodes[i] || (n.nodes[i] = d3_geom_quadtreeNode());
//                         if (right) x1 = sx; else x2 = sx;
//                         if (bottom) y1 = sy; else y2 = sy;
//                         insert(n, d, x, y, x1, y1, x2, y2);
//                     }
//                     var root = d3_geom_quadtreeNode();
//                     root.add = function(d) {
//                         insert(root, d, +fx(d, ++i), +fy(d, i), x1_, y1_, x2_, y2_);
//                     };
//                     root.visit = function(f) {
//                         d3_geom_quadtreeVisit(f, root, x1_, y1_, x2_, y2_);
//                     };
//                     i = -1;
//                     if (x1 == null) {
//                         while (++i < n) {
//                             insert(root, data[i], xs[i], ys[i], x1_, y1_, x2_, y2_);
//                         }
//                         --i;
//                     } else data.forEach(root.add);
//                     xs = ys = data = d = null;
//                     return root;
//                 }
//                 quadtree.x = function(_) {
//                     return arguments.length ? (x = _, quadtree) : x;
//                 };
//                 quadtree.y = function(_) {
//                     return arguments.length ? (y = _, quadtree) : y;
//                 };
//                 quadtree.extent = function(_) {
//                     if (!arguments.length) return x1 == null ? null : [ [ x1, y1 ], [ x2, y2 ] ];
//                     if (_ == null) x1 = y1 = x2 = y2 = null; else x1 = +_[0][0], y1 = +_[0][1], x2 = +_[1][0],
//                         y2 = +_[1][1];
//                     return quadtree;
//                 };
//                 quadtree.size = function(_) {
//                     if (!arguments.length) return x1 == null ? null : [ x2 - x1, y2 - y1 ];
//                     if (_ == null) x1 = y1 = x2 = y2 = null; else x1 = y1 = 0, x2 = +_[0], y2 = +_[1];
//                     return quadtree;
//                 };
//                 return quadtree;
//             };
//             function d3_geom_quadtreeCompatX(d) {
//                 return d.x;
//             }
//             function d3_geom_quadtreeCompatY(d) {
//                 return d.y;
//             }
//             function d3_geom_quadtreeNode() {
//                 return {
//                     leaf: true,
//                         nodes: [],
//                         point: null,
//                         x: null,
//                         y: null
//                 };
//             }
//             function d3_geom_quadtreeVisit(f, node, x1, y1, x2, y2) {
//                 if (!f(node, x1, y1, x2, y2)) {
//                     var sx = (x1 + x2) * .5, sy = (y1 + y2) * .5, children = node.nodes;
//                     if (children[0]) d3_geom_quadtreeVisit(f, children[0], x1, y1, sx, sy);
//                     if (children[1]) d3_geom_quadtreeVisit(f, children[1], sx, y1, x2, sy);
//                     if (children[2]) d3_geom_quadtreeVisit(f, children[2], x1, sy, sx, y2);
//                     if (children[3]) d3_geom_quadtreeVisit(f, children[3], sx, sy, x2, y2);
//                 }
//             }
//             d3.interpolateRgb = d3_interpolateRgb;
//             function d3_interpolateRgb(a, b) {
//                 a = d3.rgb(a);
//                 b = d3.rgb(b);
//                 var ar = a.r, ag = a.g, ab = a.b, br = b.r - ar, bg = b.g - ag, bb = b.b - ab;
//                 return function(t) {
//                     return "#" + d3_rgb_hex(Math.round(ar + br * t)) + d3_rgb_hex(Math.round(ag + bg * t)) + d3_rgb_hex(Math.round(ab + bb * t));
//                 };
//             }
//             d3.interpolateObject = d3_interpolateObject;
//             function d3_interpolateObject(a, b) {
//                 var i = {}, c = {}, k;
//                 for (k in a) {
//                     if (k in b) {
//                         i[k] = d3_interpolate(a[k], b[k]);
//                     } else {
//                         c[k] = a[k];
//                     }
//                 }
//                 for (k in b) {
//                     if (!(k in a)) {
//                         c[k] = b[k];
//                     }
//                 }
//                 return function(t) {
//                     for (k in i) c[k] = i[k](t);
//                     return c;
//                 };
//             }
//             d3.interpolateNumber = d3_interpolateNumber;
//             function d3_interpolateNumber(a, b) {
//                 b -= a = +a;
//                 return function(t) {
//                     return a + b * t;
//                 };
//             }
//             d3.interpolateString = d3_interpolateString;
//             function d3_interpolateString(a, b) {
//                 var m, i, j, s0 = 0, s1 = 0, s = [], q = [], n, o;
//                 a = a + "", b = b + "";
//                 d3_interpolate_number.lastIndex = 0;
//                 for (i = 0; m = d3_interpolate_number.exec(b); ++i) {
//                     if (m.index) s.push(b.substring(s0, s1 = m.index));
//                     q.push({
//                         i: s.length,
//                         x: m[0]
//                     });
//                     s.push(null);
//                     s0 = d3_interpolate_number.lastIndex;
//                 }
//                 if (s0 < b.length) s.push(b.substring(s0));
//                 for (i = 0, n = q.length; (m = d3_interpolate_number.exec(a)) && i < n; ++i) {
//                     o = q[i];
//                     if (o.x == m[0]) {
//                         if (o.i) {
//                             if (s[o.i + 1] == null) {
//                                 s[o.i - 1] += o.x;
//                                 s.splice(o.i, 1);
//                                 for (j = i + 1; j < n; ++j) q[j].i--;
//                             } else {
//                                 s[o.i - 1] += o.x + s[o.i + 1];
//                                 s.splice(o.i, 2);
//                                 for (j = i + 1; j < n; ++j) q[j].i -= 2;
//                             }
//                         } else {
//                             if (s[o.i + 1] == null) {
//                                 s[o.i] = o.x;
//                             } else {
//                                 s[o.i] = o.x + s[o.i + 1];
//                                 s.splice(o.i + 1, 1);
//                                 for (j = i + 1; j < n; ++j) q[j].i--;
//                             }
//                         }
//                         q.splice(i, 1);
//                         n--;
//                         i--;
//                     } else {
//                         o.x = d3_interpolateNumber(parseFloat(m[0]), parseFloat(o.x));
//                     }
//                 }
//                 while (i < n) {
//                     o = q.pop();
//                     if (s[o.i + 1] == null) {
//                         s[o.i] = o.x;
//                     } else {
//                         s[o.i] = o.x + s[o.i + 1];
//                         s.splice(o.i + 1, 1);
//                     }
//                     n--;
//                 }
//                 if (s.length === 1) {
//                     return s[0] == null ? (o = q[0].x, function(t) {
//                         return o(t) + "";
//                     }) : function() {
//                         return b;
//                     };
//                 }
//                 return function(t) {
//                     for (i = 0; i < n; ++i) s[(o = q[i]).i] = o.x(t);
//                     return s.join("");
//                 };
//             }
//             var d3_interpolate_number = /[-+]?(?:\d+\.?\d*|\.?\d+)(?:[eE][-+]?\d+)?/g;
//             d3.interpolate = d3_interpolate;
//             function d3_interpolate(a, b) {
//                 var i = d3.interpolators.length, f;
//                 while (--i >= 0 && !(f = d3.interpolators[i](a, b))) ;
//                 return f;
//             }
//             d3.interpolators = [ function(a, b) {
//                 var t = typeof b;
//                 return (t === "string" ? d3_rgb_names.has(b) || /^(#|rgb\(|hsl\()/.test(b) ? d3_interpolateRgb : d3_interpolateString : b instanceof d3_Color ? d3_interpolateRgb : t === "object" ? Array.isArray(b) ? d3_interpolateArray : d3_interpolateObject : d3_interpolateNumber)(a, b);
//             } ];
//             d3.interpolateArray = d3_interpolateArray;
//             function d3_interpolateArray(a, b) {
//                 var x = [], c = [], na = a.length, nb = b.length, n0 = Math.min(a.length, b.length), i;
//                 for (i = 0; i < n0; ++i) x.push(d3_interpolate(a[i], b[i]));
//                 for (;i < na; ++i) c[i] = a[i];
//                 for (;i < nb; ++i) c[i] = b[i];
//                 return function(t) {
//                     for (i = 0; i < n0; ++i) c[i] = x[i](t);
//                     return c;
//                 };
//             }
//             var d3_ease_default = function() {
//                 return d3_identity;
//             };
//             var d3_ease = d3.map({
//                 linear: d3_ease_default,
//                 poly: d3_ease_poly,
//                 quad: function() {
//                     return d3_ease_quad;
//                 },
//                 cubic: function() {
//                     return d3_ease_cubic;
//                 },
//                 sin: function() {
//                     return d3_ease_sin;
//                 },
//                 exp: function() {
//                     return d3_ease_exp;
//                 },
//                 circle: function() {
//                     return d3_ease_circle;
//                 },
//                 elastic: d3_ease_elastic,
//                 back: d3_ease_back,
//                 bounce: function() {
//                     return d3_ease_bounce;
//                 }
//             });
//             var d3_ease_mode = d3.map({
//                 "in": d3_identity,
//                 out: d3_ease_reverse,
//                 "in-out": d3_ease_reflect,
//                 "out-in": function(f) {
//                     return d3_ease_reflect(d3_ease_reverse(f));
//                 }
//             });
//             d3.ease = function(name) {
//                 var i = name.indexOf("-"), t = i >= 0 ? name.substring(0, i) : name, m = i >= 0 ? name.substring(i + 1) : "in";
//                 t = d3_ease.get(t) || d3_ease_default;
//                 m = d3_ease_mode.get(m) || d3_identity;
//                 return d3_ease_clamp(m(t.apply(null, d3_arraySlice.call(arguments, 1))));
//             };
//             function d3_ease_clamp(f) {
//                 return function(t) {
//                     return t <= 0 ? 0 : t >= 1 ? 1 : f(t);
//                 };
//             }
//             function d3_ease_reverse(f) {
//                 return function(t) {
//                     return 1 - f(1 - t);
//                 };
//             }
//             function d3_ease_reflect(f) {
//                 return function(t) {
//                     return .5 * (t < .5 ? f(2 * t) : 2 - f(2 - 2 * t));
//                 };
//             }
//             function d3_ease_quad(t) {
//                 return t * t;
//             }
//             function d3_ease_cubic(t) {
//                 return t * t * t;
//             }
//             function d3_ease_cubicInOut(t) {
//                 if (t <= 0) return 0;
//                 if (t >= 1) return 1;
//                 var t2 = t * t, t3 = t2 * t;
//                 return 4 * (t < .5 ? t3 : 3 * (t - t2) + t3 - .75);
//             }
//             function d3_ease_poly(e) {
//                 return function(t) {
//                     return Math.pow(t, e);
//                 };
//             }
//             function d3_ease_sin(t) {
//                 return 1 - Math.cos(t * halfπ);
//             }
//             function d3_ease_exp(t) {
//                 return Math.pow(2, 10 * (t - 1));
//             }
//             function d3_ease_circle(t) {
//                 return 1 - Math.sqrt(1 - t * t);
//             }
//             function d3_ease_elastic(a, p) {
//                 var s;
//                 if (arguments.length < 2) p = .45;
//                 if (arguments.length) s = p / τ * Math.asin(1 / a); else a = 1, s = p / 4;
//                 return function(t) {
//                     return 1 + a * Math.pow(2, -10 * t) * Math.sin((t - s) * τ / p);
//                 };
//             }
//             function d3_ease_back(s) {
//                 if (!s) s = 1.70158;
//                 return function(t) {
//                     return t * t * ((s + 1) * t - s);
//                 };
//             }
//             function d3_ease_bounce(t) {
//                 return t < 1 / 2.75 ? 7.5625 * t * t : t < 2 / 2.75 ? 7.5625 * (t -= 1.5 / 2.75) * t + .75 : t < 2.5 / 2.75 ? 7.5625 * (t -= 2.25 / 2.75) * t + .9375 : 7.5625 * (t -= 2.625 / 2.75) * t + .984375;
//             }
//             d3.interpolateHcl = d3_interpolateHcl;
//             function d3_interpolateHcl(a, b) {
//                 a = d3.hcl(a);
//                 b = d3.hcl(b);
//                 var ah = a.h, ac = a.c, al = a.l, bh = b.h - ah, bc = b.c - ac, bl = b.l - al;
//                 if (isNaN(bc)) bc = 0, ac = isNaN(ac) ? b.c : ac;
//                 if (isNaN(bh)) bh = 0, ah = isNaN(ah) ? b.h : ah; else if (bh > 180) bh -= 360; else if (bh < -180) bh += 360;
//                 return function(t) {
//                     return d3_hcl_lab(ah + bh * t, ac + bc * t, al + bl * t) + "";
//                 };
//             }
//             d3.interpolateHsl = d3_interpolateHsl;
//             function d3_interpolateHsl(a, b) {
//                 a = d3.hsl(a);
//                 b = d3.hsl(b);
//                 var ah = a.h, as = a.s, al = a.l, bh = b.h - ah, bs = b.s - as, bl = b.l - al;
//                 if (isNaN(bs)) bs = 0, as = isNaN(as) ? b.s : as;
//                 if (isNaN(bh)) bh = 0, ah = isNaN(ah) ? b.h : ah; else if (bh > 180) bh -= 360; else if (bh < -180) bh += 360;
//                 return function(t) {
//                     return d3_hsl_rgb(ah + bh * t, as + bs * t, al + bl * t) + "";
//                 };
//             }
//             d3.interpolateLab = d3_interpolateLab;
//             function d3_interpolateLab(a, b) {
//                 a = d3.lab(a);
//                 b = d3.lab(b);
//                 var al = a.l, aa = a.a, ab = a.b, bl = b.l - al, ba = b.a - aa, bb = b.b - ab;
//                 return function(t) {
//                     return d3_lab_rgb(al + bl * t, aa + ba * t, ab + bb * t) + "";
//                 };
//             }
//             d3.interpolateRound = d3_interpolateRound;
//             function d3_interpolateRound(a, b) {
//                 b -= a;
//                 return function(t) {
//                     return Math.round(a + b * t);
//                 };
//             }
//             d3.transform = function(string) {
//                 var g = d3_document.createElementNS(d3.ns.prefix.svg, "g");
//                 return (d3.transform = function(string) {
//                     if (string != null) {
//                         g.setAttribute("transform", string);
//                         var t = g.transform.baseVal.consolidate();
//                     }
//                     return new d3_transform(t ? t.matrix : d3_transformIdentity);
//                 })(string);
//             };
//             function d3_transform(m) {
//                 var r0 = [ m.a, m.b ], r1 = [ m.c, m.d ], kx = d3_transformNormalize(r0), kz = d3_transformDot(r0, r1), ky = d3_transformNormalize(d3_transformCombine(r1, r0, -kz)) || 0;
//                 if (r0[0] * r1[1] < r1[0] * r0[1]) {
//                     r0[0] *= -1;
//                     r0[1] *= -1;
//                     kx *= -1;
//                     kz *= -1;
//                 }
//                 this.rotate = (kx ? Math.atan2(r0[1], r0[0]) : Math.atan2(-r1[0], r1[1])) * d3_degrees;
//                 this.translate = [ m.e, m.f ];
//                 this.scale = [ kx, ky ];
//                 this.skew = ky ? Math.atan2(kz, ky) * d3_degrees : 0;
//             }
//             d3_transform.prototype.toString = function() {
//                 return "translate(" + this.translate + ")rotate(" + this.rotate + ")skewX(" + this.skew + ")scale(" + this.scale + ")";
//             };
//             function d3_transformDot(a, b) {
//                 return a[0] * b[0] + a[1] * b[1];
//             }
//             function d3_transformNormalize(a) {
//                 var k = Math.sqrt(d3_transformDot(a, a));
//                 if (k) {
//                     a[0] /= k;
//                     a[1] /= k;
//                 }
//                 return k;
//             }
//             function d3_transformCombine(a, b, k) {
//                 a[0] += k * b[0];
//                 a[1] += k * b[1];
//                 return a;
//             }
//             var d3_transformIdentity = {
//                 a: 1,
//                 b: 0,
//                 c: 0,
//                 d: 1,
//                 e: 0,
//                 f: 0
//             };
//             d3.interpolateTransform = d3_interpolateTransform;
//             function d3_interpolateTransform(a, b) {
//                 var s = [], q = [], n, A = d3.transform(a), B = d3.transform(b), ta = A.translate, tb = B.translate, ra = A.rotate, rb = B.rotate, wa = A.skew, wb = B.skew, ka = A.scale, kb = B.scale;
//                 if (ta[0] != tb[0] || ta[1] != tb[1]) {
//                     s.push("translate(", null, ",", null, ")");
//                     q.push({
//                         i: 1,
//                         x: d3_interpolateNumber(ta[0], tb[0])
//                     }, {
//                         i: 3,
//                         x: d3_interpolateNumber(ta[1], tb[1])
//                     });
//                 } else if (tb[0] || tb[1]) {
//                     s.push("translate(" + tb + ")");
//                 } else {
//                     s.push("");
//                 }
//                 if (ra != rb) {
//                     if (ra - rb > 180) rb += 360; else if (rb - ra > 180) ra += 360;
//                     q.push({
//                         i: s.push(s.pop() + "rotate(", null, ")") - 2,
//                         x: d3_interpolateNumber(ra, rb)
//                     });
//                 } else if (rb) {
//                     s.push(s.pop() + "rotate(" + rb + ")");
//                 }
//                 if (wa != wb) {
//                     q.push({
//                         i: s.push(s.pop() + "skewX(", null, ")") - 2,
//                         x: d3_interpolateNumber(wa, wb)
//                     });
//                 } else if (wb) {
//                     s.push(s.pop() + "skewX(" + wb + ")");
//                 }
//                 if (ka[0] != kb[0] || ka[1] != kb[1]) {
//                     n = s.push(s.pop() + "scale(", null, ",", null, ")");
//                     q.push({
//                         i: n - 4,
//                         x: d3_interpolateNumber(ka[0], kb[0])
//                     }, {
//                         i: n - 2,
//                         x: d3_interpolateNumber(ka[1], kb[1])
//                     });
//                 } else if (kb[0] != 1 || kb[1] != 1) {
//                     s.push(s.pop() + "scale(" + kb + ")");
//                 }
//                 n = q.length;
//                 return function(t) {
//                     var i = -1, o;
//                     while (++i < n) s[(o = q[i]).i] = o.x(t);
//                     return s.join("");
//                 };
//             }
//             function d3_uninterpolateNumber(a, b) {
//                 b = b - (a = +a) ? 1 / (b - a) : 0;
//                 return function(x) {
//                     return (x - a) * b;
//                 };
//             }
//             function d3_uninterpolateClamp(a, b) {
//                 b = b - (a = +a) ? 1 / (b - a) : 0;
//                 return function(x) {
//                     return Math.max(0, Math.min(1, (x - a) * b));
//                 };
//             }
//             d3.layout = {};
//             d3.layout.bundle = function() {
//                 return function(links) {
//                     var paths = [], i = -1, n = links.length;
//                     while (++i < n) paths.push(d3_layout_bundlePath(links[i]));
//                     return paths;
//                 };
//             };
//             function d3_layout_bundlePath(link) {
//                 var start = link.source, end = link.target, lca = d3_layout_bundleLeastCommonAncestor(start, end), points = [ start ];
//                 while (start !== lca) {
//                     start = start.parent;
//                     points.push(start);
//                 }
//                 var k = points.length;
//                 while (end !== lca) {
//                     points.splice(k, 0, end);
//                     end = end.parent;
//                 }
//                 return points;
//             }
//             function d3_layout_bundleAncestors(node) {
//                 var ancestors = [], parent = node.parent;
//                 while (parent != null) {
//                     ancestors.push(node);
//                     node = parent;
//                     parent = parent.parent;
//                 }
//                 ancestors.push(node);
//                 return ancestors;
//             }
//             function d3_layout_bundleLeastCommonAncestor(a, b) {
//                 if (a === b) return a;
//                 var aNodes = d3_layout_bundleAncestors(a), bNodes = d3_layout_bundleAncestors(b), aNode = aNodes.pop(), bNode = bNodes.pop(), sharedNode = null;
//                 while (aNode === bNode) {
//                     sharedNode = aNode;
//                     aNode = aNodes.pop();
//                     bNode = bNodes.pop();
//                 }
//                 return sharedNode;
//             }
//             d3.layout.chord = function() {
//                 var chord = {}, chords, groups, matrix, n, padding = 0, sortGroups, sortSubgroups, sortChords;
//                 function relayout() {
//                     var subgroups = {}, groupSums = [], groupIndex = d3.range(n), subgroupIndex = [], k, x, x0, i, j;
//                     chords = [];
//                     groups = [];
//                     k = 0, i = -1;
//                     while (++i < n) {
//                         x = 0, j = -1;
//                         while (++j < n) {
//                             x += matrix[i][j];
//                         }
//                         groupSums.push(x);
//                         subgroupIndex.push(d3.range(n));
//                         k += x;
//                     }
//                     if (sortGroups) {
//                         groupIndex.sort(function(a, b) {
//                             return sortGroups(groupSums[a], groupSums[b]);
//                         });
//                     }
//                     if (sortSubgroups) {
//                         subgroupIndex.forEach(function(d, i) {
//                             d.sort(function(a, b) {
//                                 return sortSubgroups(matrix[i][a], matrix[i][b]);
//                             });
//                         });
//                     }
//                     k = (τ - padding * n) / k;
//                     x = 0, i = -1;
//                     while (++i < n) {
//                         x0 = x, j = -1;
//                         while (++j < n) {
//                             var di = groupIndex[i], dj = subgroupIndex[di][j], v = matrix[di][dj], a0 = x, a1 = x += v * k;
//                             subgroups[di + "-" + dj] = {
//                                 index: di,
//                                 subindex: dj,
//                                 startAngle: a0,
//                                 endAngle: a1,
//                                 value: v
//                             };
//                         }
//                         groups[di] = {
//                             index: di,
//                             startAngle: x0,
//                             endAngle: x,
//                             value: (x - x0) / k
//                         };
//                         x += padding;
//                     }
//                     i = -1;
//                     while (++i < n) {
//                         j = i - 1;
//                         while (++j < n) {
//                             var source = subgroups[i + "-" + j], target = subgroups[j + "-" + i];
//                             if (source.value || target.value) {
//                                 chords.push(source.value < target.value ? {
//                                     source: target,
//                                     target: source
//                                 } : {
//                                     source: source,
//                                     target: target
//                                 });
//                             }
//                         }
//                     }
//                     if (sortChords) resort();
//                 }
//                 function resort() {
//                     chords.sort(function(a, b) {
//                         return sortChords((a.source.value + a.target.value) / 2, (b.source.value + b.target.value) / 2);
//                     });
//                 }
//                 chord.matrix = function(x) {
//                     if (!arguments.length) return matrix;
//                     n = (matrix = x) && matrix.length;
//                     chords = groups = null;
//                     return chord;
//                 };
//                 chord.padding = function(x) {
//                     if (!arguments.length) return padding;
//                     padding = x;
//                     chords = groups = null;
//                     return chord;
//                 };
//                 chord.sortGroups = function(x) {
//                     if (!arguments.length) return sortGroups;
//                     sortGroups = x;
//                     chords = groups = null;
//                     return chord;
//                 };
//                 chord.sortSubgroups = function(x) {
//                     if (!arguments.length) return sortSubgroups;
//                     sortSubgroups = x;
//                     chords = null;
//                     return chord;
//                 };
//                 chord.sortChords = function(x) {
//                     if (!arguments.length) return sortChords;
//                     sortChords = x;
//                     if (chords) resort();
//                     return chord;
//                 };
//                 chord.chords = function() {
//                     if (!chords) relayout();
//                     return chords;
//                 };
//                 chord.groups = function() {
//                     if (!groups) relayout();
//                     return groups;
//                 };
//                 return chord;
//             };
//             d3.layout.force = function() {
//                 var force = {}, event = d3.dispatch("start", "tick", "end"), size = [ 1, 1 ], drag, alpha, friction = .9, linkDistance = d3_layout_forceLinkDistance, linkStrength = d3_layout_forceLinkStrength, charge = -30, gravity = .1, theta = .8, nodes = [], links = [], distances, strengths, charges;
//                 function repulse(node) {
//                     return function(quad, x1, _, x2) {
//                         if (quad.point !== node) {
//                             var dx = quad.cx - node.x, dy = quad.cy - node.y, dn = 1 / Math.sqrt(dx * dx + dy * dy);
//                             if ((x2 - x1) * dn < theta) {
//                                 var k = quad.charge * dn * dn;
//                                 node.px -= dx * k;
//                                 node.py -= dy * k;
//                                 return true;
//                             }
//                             if (quad.point && isFinite(dn)) {
//                                 var k = quad.pointCharge * dn * dn;
//                                 node.px -= dx * k;
//                                 node.py -= dy * k;
//                             }
//                         }
//                         return !quad.charge;
//                     };
//                 }
//                 force.tick = function() {
//                     if ((alpha *= .99) < .005) {
//                         event.end({
//                             type: "end",
//                             alpha: alpha = 0
//                         });
//                         return true;
//                     }
//                     var n = nodes.length, m = links.length, q, i, o, s, t, l, k, x, y;
//                     for (i = 0; i < m; ++i) {
//                         o = links[i];
//                         s = o.source;
//                         t = o.target;
//                         x = t.x - s.x;
//                         y = t.y - s.y;
//                         if (l = x * x + y * y) {
//                             l = alpha * strengths[i] * ((l = Math.sqrt(l)) - distances[i]) / l;
//                             x *= l;
//                             y *= l;
//                             t.x -= x * (k = s.weight / (t.weight + s.weight));
//                             t.y -= y * k;
//                             s.x += x * (k = 1 - k);
//                             s.y += y * k;
//                         }
//                     }
//                     if (k = alpha * gravity) {
//                         x = size[0] / 2;
//                         y = size[1] / 2;
//                         i = -1;
//                         if (k) while (++i < n) {
//                             o = nodes[i];
//                             o.x += (x - o.x) * k;
//                             o.y += (y - o.y) * k;
//                         }
//                     }
//                     if (charge) {
//                         d3_layout_forceAccumulate(q = d3.geom.quadtree(nodes), alpha, charges);
//                         i = -1;
//                         while (++i < n) {
//                             if (!(o = nodes[i]).fixed) {
//                                 q.visit(repulse(o));
//                             }
//                         }
//                     }
//                     i = -1;
//                     while (++i < n) {
//                         o = nodes[i];
//                         if (o.fixed) {
//                             o.x = o.px;
//                             o.y = o.py;
//                         } else {
//                             o.x -= (o.px - (o.px = o.x)) * friction;
//                             o.y -= (o.py - (o.py = o.y)) * friction;
//                         }
//                     }
//                     event.tick({
//                         type: "tick",
//                         alpha: alpha
//                     });
//                 };
//                 force.nodes = function(x) {
//                     if (!arguments.length) return nodes;
//                     nodes = x;
//                     return force;
//                 };
//                 force.links = function(x) {
//                     if (!arguments.length) return links;
//                     links = x;
//                     return force;
//                 };
//                 force.size = function(x) {
//                     if (!arguments.length) return size;
//                     size = x;
//                     return force;
//                 };
//                 force.linkDistance = function(x) {
//                     if (!arguments.length) return linkDistance;
//                     linkDistance = typeof x === "function" ? x : +x;
//                     return force;
//                 };
//                 force.distance = force.linkDistance;
//                 force.linkStrength = function(x) {
//                     if (!arguments.length) return linkStrength;
//                     linkStrength = typeof x === "function" ? x : +x;
//                     return force;
//                 };
//                 force.friction = function(x) {
//                     if (!arguments.length) return friction;
//                     friction = +x;
//                     return force;
//                 };
//                 force.charge = function(x) {
//                     if (!arguments.length) return charge;
//                     charge = typeof x === "function" ? x : +x;
//                     return force;
//                 };
//                 force.gravity = function(x) {
//                     if (!arguments.length) return gravity;
//                     gravity = +x;
//                     return force;
//                 };
//                 force.theta = function(x) {
//                     if (!arguments.length) return theta;
//                     theta = +x;
//                     return force;
//                 };
//                 force.alpha = function(x) {
//                     if (!arguments.length) return alpha;
//                     x = +x;
//                     if (alpha) {
//                         if (x > 0) alpha = x; else alpha = 0;
//                     } else if (x > 0) {
//                         event.start({
//                             type: "start",
//                             alpha: alpha = x
//                         });
//                         d3.timer(force.tick);
//                     }
//                     return force;
//                 };
//                 force.start = function() {
//                     var i, n = nodes.length, m = links.length, w = size[0], h = size[1], neighbors, o;
//                     for (i = 0; i < n; ++i) {
//                         (o = nodes[i]).index = i;
//                         o.weight = 0;
//                     }
//                     for (i = 0; i < m; ++i) {
//                         o = links[i];
//                         if (typeof o.source == "number") o.source = nodes[o.source];
//                         if (typeof o.target == "number") o.target = nodes[o.target];
//                         ++o.source.weight;
//                         ++o.target.weight;
//                     }
//                     for (i = 0; i < n; ++i) {
//                         o = nodes[i];
//                         if (isNaN(o.x)) o.x = position("x", w);
//                         if (isNaN(o.y)) o.y = position("y", h);
//                         if (isNaN(o.px)) o.px = o.x;
//                         if (isNaN(o.py)) o.py = o.y;
//                     }
//                     distances = [];
//                     if (typeof linkDistance === "function") for (i = 0; i < m; ++i) distances[i] = +linkDistance.call(this, links[i], i); else for (i = 0; i < m; ++i) distances[i] = linkDistance;
//                     strengths = [];
//                     if (typeof linkStrength === "function") for (i = 0; i < m; ++i) strengths[i] = +linkStrength.call(this, links[i], i); else for (i = 0; i < m; ++i) strengths[i] = linkStrength;
//                     charges = [];
//                     if (typeof charge === "function") for (i = 0; i < n; ++i) charges[i] = +charge.call(this, nodes[i], i); else for (i = 0; i < n; ++i) charges[i] = charge;
//                     function position(dimension, size) {
//                         if (!neighbors) {
//                             neighbors = new Array(n);
//                             for (j = 0; j < n; ++j) {
//                                 neighbors[j] = [];
//                             }
//                             for (j = 0; j < m; ++j) {
//                                 var o = links[j];
//                                 neighbors[o.source.index].push(o.target);
//                                 neighbors[o.target.index].push(o.source);
//                             }
//                         }
//                         var candidates = neighbors[i], j = -1, m = candidates.length, x;
//                         while (++j < m) if (!isNaN(x = candidates[j][dimension])) return x;
//                         return Math.random() * size;
//                     }
//                     return force.resume();
//                 };
//                 force.resume = function() {
//                     return force.alpha(.1);
//                 };
//                 force.stop = function() {
//                     return force.alpha(0);
//                 };
//                 force.drag = function() {
//                     if (!drag) drag = d3.behavior.drag().origin(d3_identity).on("dragstart.force", d3_layout_forceDragstart).on("drag.force", dragmove).on("dragend.force", d3_layout_forceDragend);
//                     if (!arguments.length) return drag;
//                     this.on("mouseover.force", d3_layout_forceMouseover).on("mouseout.force", d3_layout_forceMouseout).call(drag);
//                 };
//                 function dragmove(d) {
//                     d.px = d3.event.x, d.py = d3.event.y;
//                     force.resume();
//                 }
//                 return d3.rebind(force, event, "on");
//             };
//             function d3_layout_forceDragstart(d) {
//                 d.fixed |= 2;
//             }
//             function d3_layout_forceDragend(d) {
//                 d.fixed &= ~6;
//             }
//             function d3_layout_forceMouseover(d) {
//                 d.fixed |= 4;
//                 d.px = d.x, d.py = d.y;
//             }
//             function d3_layout_forceMouseout(d) {
//                 d.fixed &= ~4;
//             }
//             function d3_layout_forceAccumulate(quad, alpha, charges) {
//                 var cx = 0, cy = 0;
//                 quad.charge = 0;
//                 if (!quad.leaf) {
//                     var nodes = quad.nodes, n = nodes.length, i = -1, c;
//                     while (++i < n) {
//                         c = nodes[i];
//                         if (c == null) continue;
//                         d3_layout_forceAccumulate(c, alpha, charges);
//                         quad.charge += c.charge;
//                         cx += c.charge * c.cx;
//                         cy += c.charge * c.cy;
//                     }
//                 }
//                 if (quad.point) {
//                     if (!quad.leaf) {
//                         quad.point.x += Math.random() - .5;
//                         quad.point.y += Math.random() - .5;
//                     }
//                     var k = alpha * charges[quad.point.index];
//                     quad.charge += quad.pointCharge = k;
//                     cx += k * quad.point.x;
//                     cy += k * quad.point.y;
//                 }
//                 quad.cx = cx / quad.charge;
//                 quad.cy = cy / quad.charge;
//             }
//             var d3_layout_forceLinkDistance = 20, d3_layout_forceLinkStrength = 1;
//             d3.layout.hierarchy = function() {
//                 var sort = d3_layout_hierarchySort, children = d3_layout_hierarchyChildren, value = d3_layout_hierarchyValue;
//                 function recurse(node, depth, nodes) {
//                     var childs = children.call(hierarchy, node, depth);
//                     node.depth = depth;
//                     nodes.push(node);
//                     if (childs && (n = childs.length)) {
//                         var i = -1, n, c = node.children = new Array(n), v = 0, j = depth + 1, d;
//                         while (++i < n) {
//                             d = c[i] = recurse(childs[i], j, nodes);
//                             d.parent = node;
//                             v += d.value;
//                         }
//                         if (sort) c.sort(sort);
//                         if (value) node.value = v;
//                     } else {
//                         delete node.children;
//                         if (value) {
//                             node.value = +value.call(hierarchy, node, depth) || 0;
//                         }
//                     }
//                     return node;
//                 }
//                 function revalue(node, depth) {
//                     var children = node.children, v = 0;
//                     if (children && (n = children.length)) {
//                         var i = -1, n, j = depth + 1;
//                         while (++i < n) v += revalue(children[i], j);
//                     } else if (value) {
//                         v = +value.call(hierarchy, node, depth) || 0;
//                     }
//                     if (value) node.value = v;
//                     return v;
//                 }
//                 function hierarchy(d) {
//                     var nodes = [];
//                     recurse(d, 0, nodes);
//                     return nodes;
//                 }
//                 hierarchy.sort = function(x) {
//                     if (!arguments.length) return sort;
//                     sort = x;
//                     return hierarchy;
//                 };
//                 hierarchy.children = function(x) {
//                     if (!arguments.length) return children;
//                     children = x;
//                     return hierarchy;
//                 };
//                 hierarchy.value = function(x) {
//                     if (!arguments.length) return value;
//                     value = x;
//                     return hierarchy;
//                 };
//                 hierarchy.revalue = function(root) {
//                     revalue(root, 0);
//                     return root;
//                 };
//                 return hierarchy;
//             };
//             function d3_layout_hierarchyRebind(object, hierarchy) {
//                 d3.rebind(object, hierarchy, "sort", "children", "value");
//                 object.nodes = object;
//                 object.links = d3_layout_hierarchyLinks;
//                 return object;
//             }
//             function d3_layout_hierarchyChildren(d) {
//                 return d.children;
//             }
//             function d3_layout_hierarchyValue(d) {
//                 return d.value;
//             }
//             function d3_layout_hierarchySort(a, b) {
//                 return b.value - a.value;
//             }
//             function d3_layout_hierarchyLinks(nodes) {
//                 return d3.merge(nodes.map(function(parent) {
//                     return (parent.children || []).map(function(child) {
//                         return {
//                             source: parent,
//                            target: child
//                         };
//                     });
//                 }));
//             }
//             d3.layout.partition = function() {
//                 var hierarchy = d3.layout.hierarchy(), size = [ 1, 1 ];
//                 function position(node, x, dx, dy) {
//                     var children = node.children;
//                     node.x = x;
//                     node.y = node.depth * dy;
//                     node.dx = dx;
//                     node.dy = dy;
//                     if (children && (n = children.length)) {
//                         var i = -1, n, c, d;
//                         dx = node.value ? dx / node.value : 0;
//                         while (++i < n) {
//                             position(c = children[i], x, d = c.value * dx, dy);
//                             x += d;
//                         }
//                     }
//                 }
//                 function depth(node) {
//                     var children = node.children, d = 0;
//                     if (children && (n = children.length)) {
//                         var i = -1, n;
//                         while (++i < n) d = Math.max(d, depth(children[i]));
//                     }
//                     return 1 + d;
//                 }
//                 function partition(d, i) {
//                     var nodes = hierarchy.call(this, d, i);
//                     position(nodes[0], 0, size[0], size[1] / depth(nodes[0]));
//                     return nodes;
//                 }
//                 partition.size = function(x) {
//                     if (!arguments.length) return size;
//                     size = x;
//                     return partition;
//                 };
//                 return d3_layout_hierarchyRebind(partition, hierarchy);
//             };
//             d3.layout.pie = function() {
//                 var value = Number, sort = d3_layout_pieSortByValue, startAngle = 0, endAngle = τ;
//                 function pie(data) {
//                     var values = data.map(function(d, i) {
//                         return +value.call(pie, d, i);
//                     });
//                     var a = +(typeof startAngle === "function" ? startAngle.apply(this, arguments) : startAngle);
//                     var k = ((typeof endAngle === "function" ? endAngle.apply(this, arguments) : endAngle) - a) / d3.sum(values);
//                     var index = d3.range(data.length);
//                     if (sort != null) index.sort(sort === d3_layout_pieSortByValue ? function(i, j) {
//                         return values[j] - values[i];
//                     } : function(i, j) {
//                         return sort(data[i], data[j]);
//                     });
//                     var arcs = [];
//                     index.forEach(function(i) {
//                         var d;
//                         arcs[i] = {
//                             data: data[i],
//                         value: d = values[i],
//                         startAngle: a,
//                         endAngle: a += d * k
//                         };
//                     });
//                     return arcs;
//                 }
//                 pie.value = function(x) {
//                     if (!arguments.length) return value;
//                     value = x;
//                     return pie;
//                 };
//                 pie.sort = function(x) {
//                     if (!arguments.length) return sort;
//                     sort = x;
//                     return pie;
//                 };
//                 pie.startAngle = function(x) {
//                     if (!arguments.length) return startAngle;
//                     startAngle = x;
//                     return pie;
//                 };
//                 pie.endAngle = function(x) {
//                     if (!arguments.length) return endAngle;
//                     endAngle = x;
//                     return pie;
//                 };
//                 return pie;
//             };
//             var d3_layout_pieSortByValue = {};
//             d3.layout.stack = function() {
//                 var values = d3_identity, order = d3_layout_stackOrderDefault, offset = d3_layout_stackOffsetZero, out = d3_layout_stackOut, x = d3_layout_stackX, y = d3_layout_stackY;
//                 function stack(data, index) {
//                     var series = data.map(function(d, i) {
//                         return values.call(stack, d, i);
//                     });
//                     var points = series.map(function(d) {
//                         return d.map(function(v, i) {
//                             return [ x.call(stack, v, i), y.call(stack, v, i) ];
//                         });
//                     });
//                     var orders = order.call(stack, points, index);
//                     series = d3.permute(series, orders);
//                     points = d3.permute(points, orders);
//                     var offsets = offset.call(stack, points, index);
//                     var n = series.length, m = series[0].length, i, j, o;
//                     for (j = 0; j < m; ++j) {
//                         out.call(stack, series[0][j], o = offsets[j], points[0][j][1]);
//                         for (i = 1; i < n; ++i) {
//                             out.call(stack, series[i][j], o += points[i - 1][j][1], points[i][j][1]);
//                         }
//                     }
//                     return data;
//                 }
//                 stack.values = function(x) {
//                     if (!arguments.length) return values;
//                     values = x;
//                     return stack;
//                 };
//                 stack.order = function(x) {
//                     if (!arguments.length) return order;
//                     order = typeof x === "function" ? x : d3_layout_stackOrders.get(x) || d3_layout_stackOrderDefault;
//                     return stack;
//                 };
//                 stack.offset = function(x) {
//                     if (!arguments.length) return offset;
//                     offset = typeof x === "function" ? x : d3_layout_stackOffsets.get(x) || d3_layout_stackOffsetZero;
//                     return stack;
//                 };
//                 stack.x = function(z) {
//                     if (!arguments.length) return x;
//                     x = z;
//                     return stack;
//                 };
//                 stack.y = function(z) {
//                     if (!arguments.length) return y;
//                     y = z;
//                     return stack;
//                 };
//                 stack.out = function(z) {
//                     if (!arguments.length) return out;
//                     out = z;
//                     return stack;
//                 };
//                 return stack;
//             };
//             function d3_layout_stackX(d) {
//                 return d.x;
//             }
//             function d3_layout_stackY(d) {
//                 return d.y;
//             }
//             function d3_layout_stackOut(d, y0, y) {
//                 d.y0 = y0;
//                 d.y = y;
//             }
//             var d3_layout_stackOrders = d3.map({
//                 "inside-out": function(data) {
//                     var n = data.length, i, j, max = data.map(d3_layout_stackMaxIndex), sums = data.map(d3_layout_stackReduceSum), index = d3.range(n).sort(function(a, b) {
//                         return max[a] - max[b];
//                     }), top = 0, bottom = 0, tops = [], bottoms = [];
//                     for (i = 0; i < n; ++i) {
//                         j = index[i];
//                         if (top < bottom) {
//                             top += sums[j];
//                             tops.push(j);
//                         } else {
//                             bottom += sums[j];
//                             bottoms.push(j);
//                         }
//                     }
//                     return bottoms.reverse().concat(tops);
//                 },
//                 reverse: function(data) {
//                     return d3.range(data.length).reverse();
//                 },
//                 "default": d3_layout_stackOrderDefault
//             });
//             var d3_layout_stackOffsets = d3.map({
//                 silhouette: function(data) {
//                     var n = data.length, m = data[0].length, sums = [], max = 0, i, j, o, y0 = [];
//                     for (j = 0; j < m; ++j) {
//                         for (i = 0, o = 0; i < n; i++) o += data[i][j][1];
//                         if (o > max) max = o;
//                         sums.push(o);
//                     }
//                     for (j = 0; j < m; ++j) {
//                         y0[j] = (max - sums[j]) / 2;
//                     }
//                     return y0;
//                 },
//                 wiggle: function(data) {
//                     var n = data.length, x = data[0], m = x.length, i, j, k, s1, s2, s3, dx, o, o0, y0 = [];
//                     y0[0] = o = o0 = 0;
//                     for (j = 1; j < m; ++j) {
//                         for (i = 0, s1 = 0; i < n; ++i) s1 += data[i][j][1];
//                         for (i = 0, s2 = 0, dx = x[j][0] - x[j - 1][0]; i < n; ++i) {
//                             for (k = 0, s3 = (data[i][j][1] - data[i][j - 1][1]) / (2 * dx); k < i; ++k) {
//                                 s3 += (data[k][j][1] - data[k][j - 1][1]) / dx;
//                             }
//                             s2 += s3 * data[i][j][1];
//                         }
//                         y0[j] = o -= s1 ? s2 / s1 * dx : 0;
//                         if (o < o0) o0 = o;
//                     }
//                     for (j = 0; j < m; ++j) y0[j] -= o0;
//                     return y0;
//                 },
//                 expand: function(data) {
//                     var n = data.length, m = data[0].length, k = 1 / n, i, j, o, y0 = [];
//                     for (j = 0; j < m; ++j) {
//                         for (i = 0, o = 0; i < n; i++) o += data[i][j][1];
//                         if (o) for (i = 0; i < n; i++) data[i][j][1] /= o; else for (i = 0; i < n; i++) data[i][j][1] = k;
//                     }
//                     for (j = 0; j < m; ++j) y0[j] = 0;
//                     return y0;
//                 },
//                 zero: d3_layout_stackOffsetZero
//             });
//             function d3_layout_stackOrderDefault(data) {
//                 return d3.range(data.length);
//             }
//             function d3_layout_stackOffsetZero(data) {
//                 var j = -1, m = data[0].length, y0 = [];
//                 while (++j < m) y0[j] = 0;
//                 return y0;
//             }
//             function d3_layout_stackMaxIndex(array) {
//                 var i = 1, j = 0, v = array[0][1], k, n = array.length;
//                 for (;i < n; ++i) {
//                     if ((k = array[i][1]) > v) {
//                         j = i;
//                         v = k;
//                     }
//                 }
//                 return j;
//             }
//             function d3_layout_stackReduceSum(d) {
//                 return d.reduce(d3_layout_stackSum, 0);
//             }
//             function d3_layout_stackSum(p, d) {
//                 return p + d[1];
//             }
//             d3.layout.histogram = function() {
//                 var frequency = true, valuer = Number, ranger = d3_layout_histogramRange, binner = d3_layout_histogramBinSturges;
//                 function histogram(data, i) {
//                     var bins = [], values = data.map(valuer, this), range = ranger.call(this, values, i), thresholds = binner.call(this, range, values, i), bin, i = -1, n = values.length, m = thresholds.length - 1, k = frequency ? 1 : 1 / n, x;
//                     while (++i < m) {
//                         bin = bins[i] = [];
//                         bin.dx = thresholds[i + 1] - (bin.x = thresholds[i]);
//                         bin.y = 0;
//                     }
//                     if (m > 0) {
//                         i = -1;
//                         while (++i < n) {
//                             x = values[i];
//                             if (x >= range[0] && x <= range[1]) {
//                                 bin = bins[d3.bisect(thresholds, x, 1, m) - 1];
//                                 bin.y += k;
//                                 bin.push(data[i]);
//                             }
//                         }
//                     }
//                     return bins;
//                 }
//                 histogram.value = function(x) {
//                     if (!arguments.length) return valuer;
//                     valuer = x;
//                     return histogram;
//                 };
//                 histogram.range = function(x) {
//                     if (!arguments.length) return ranger;
//                     ranger = d3_functor(x);
//                     return histogram;
//                 };
//                 histogram.bins = function(x) {
//                     if (!arguments.length) return binner;
//                     binner = typeof x === "number" ? function(range) {
//                         return d3_layout_histogramBinFixed(range, x);
//                     } : d3_functor(x);
//                     return histogram;
//                 };
//                 histogram.frequency = function(x) {
//                     if (!arguments.length) return frequency;
//                     frequency = !!x;
//                     return histogram;
//                 };
//                 return histogram;
//             };
//             function d3_layout_histogramBinSturges(range, values) {
//                 return d3_layout_histogramBinFixed(range, Math.ceil(Math.log(values.length) / Math.LN2 + 1));
//             }
//             function d3_layout_histogramBinFixed(range, n) {
//                 var x = -1, b = +range[0], m = (range[1] - b) / n, f = [];
//                 while (++x <= n) f[x] = m * x + b;
//                 return f;
//             }
//             function d3_layout_histogramRange(values) {
//                 return [ d3.min(values), d3.max(values) ];
//             }
//             d3.layout.tree = function() {
//                 var hierarchy = d3.layout.hierarchy().sort(null).value(null), separation = d3_layout_treeSeparation, size = [ 1, 1 ], nodeSize = false;
//                 function tree(d, i) {
//                     var nodes = hierarchy.call(this, d, i), root = nodes[0];
//                     function firstWalk(node, previousSibling) {
//                         var children = node.children, layout = node._tree;
//                         if (children && (n = children.length)) {
//                             var n, firstChild = children[0], previousChild, ancestor = firstChild, child, i = -1;
//                             while (++i < n) {
//                                 child = children[i];
//                                 firstWalk(child, previousChild);
//                                 ancestor = apportion(child, previousChild, ancestor);
//                                 previousChild = child;
//                             }
//                             d3_layout_treeShift(node);
//                             var midpoint = .5 * (firstChild._tree.prelim + child._tree.prelim);
//                             if (previousSibling) {
//                                 layout.prelim = previousSibling._tree.prelim + separation(node, previousSibling);
//                                 layout.mod = layout.prelim - midpoint;
//                             } else {
//                                 layout.prelim = midpoint;
//                             }
//                         } else {
//                             if (previousSibling) {
//                                 layout.prelim = previousSibling._tree.prelim + separation(node, previousSibling);
//                             }
//                         }
//                     }
//                     function secondWalk(node, x) {
//                         node.x = node._tree.prelim + x;
//                         var children = node.children;
//                         if (children && (n = children.length)) {
//                             var i = -1, n;
//                             x += node._tree.mod;
//                             while (++i < n) {
//                                 secondWalk(children[i], x);
//                             }
//                         }
//                     }
//                     function apportion(node, previousSibling, ancestor) {
//                         if (previousSibling) {
//                             var vip = node, vop = node, vim = previousSibling, vom = node.parent.children[0], sip = vip._tree.mod, sop = vop._tree.mod, sim = vim._tree.mod, som = vom._tree.mod, shift;
//                             while (vim = d3_layout_treeRight(vim), vip = d3_layout_treeLeft(vip), vim && vip) {
//                                 vom = d3_layout_treeLeft(vom);
//                                 vop = d3_layout_treeRight(vop);
//                                 vop._tree.ancestor = node;
//                                 shift = vim._tree.prelim + sim - vip._tree.prelim - sip + separation(vim, vip);
//                                 if (shift > 0) {
//                                     d3_layout_treeMove(d3_layout_treeAncestor(vim, node, ancestor), node, shift);
//                                     sip += shift;
//                                     sop += shift;
//                                 }
//                                 sim += vim._tree.mod;
//                                 sip += vip._tree.mod;
//                                 som += vom._tree.mod;
//                                 sop += vop._tree.mod;
//                             }
//                             if (vim && !d3_layout_treeRight(vop)) {
//                                 vop._tree.thread = vim;
//                                 vop._tree.mod += sim - sop;
//                             }
//                             if (vip && !d3_layout_treeLeft(vom)) {
//                                 vom._tree.thread = vip;
//                                 vom._tree.mod += sip - som;
//                                 ancestor = node;
//                             }
//                         }
//                         return ancestor;
//                     }
//                     d3_layout_treeVisitAfter(root, function(node, previousSibling) {
//                         node._tree = {
//                             ancestor: node,
//                     prelim: 0,
//                     mod: 0,
//                     change: 0,
//                     shift: 0,
//                     number: previousSibling ? previousSibling._tree.number + 1 : 0
//                         };
//                     });
//                     firstWalk(root);
//                     secondWalk(root, -root._tree.prelim);
//                     var left = d3_layout_treeSearch(root, d3_layout_treeLeftmost), right = d3_layout_treeSearch(root, d3_layout_treeRightmost), deep = d3_layout_treeSearch(root, d3_layout_treeDeepest), x0 = left.x - separation(left, right) / 2, x1 = right.x + separation(right, left) / 2, y1 = deep.depth || 1;
//                     d3_layout_treeVisitAfter(root, nodeSize ? function(node) {
//                         node.x *= size[0];
//                         node.y = node.depth * size[1];
//                         delete node._tree;
//                     } : function(node) {
//                         node.x = (node.x - x0) / (x1 - x0) * size[0];
//                         node.y = node.depth / y1 * size[1];
//                         delete node._tree;
//                     });
//                     return nodes;
//                 }
//                 tree.separation = function(x) {
//                     if (!arguments.length) return separation;
//                     separation = x;
//                     return tree;
//                 };
//                 tree.size = function(x) {
//                     if (!arguments.length) return nodeSize ? null : size;
//                     nodeSize = (size = x) == null;
//                     return tree;
//                 };
//                 tree.nodeSize = function(x) {
//                     if (!arguments.length) return nodeSize ? size : null;
//                     nodeSize = (size = x) != null;
//                     return tree;
//                 };
//                 return d3_layout_hierarchyRebind(tree, hierarchy);
//             };
//             function d3_layout_treeSeparation(a, b) {
//                 return a.parent == b.parent ? 1 : 2;
//             }
//             function d3_layout_treeLeft(node) {
//                 var children = node.children;
//                 return children && children.length ? children[0] : node._tree.thread;
//             }
//             function d3_layout_treeRight(node) {
//                 var children = node.children, n;
//                 return children && (n = children.length) ? children[n - 1] : node._tree.thread;
//             }
//             function d3_layout_treeSearch(node, compare) {
//                 var children = node.children;
//                 if (children && (n = children.length)) {
//                     var child, n, i = -1;
//                     while (++i < n) {
//                         if (compare(child = d3_layout_treeSearch(children[i], compare), node) > 0) {
//                             node = child;
//                         }
//                     }
//                 }
//                 return node;
//             }
//             function d3_layout_treeRightmost(a, b) {
//                 return a.x - b.x;
//             }
//             function d3_layout_treeLeftmost(a, b) {
//                 return b.x - a.x;
//             }
//             function d3_layout_treeDeepest(a, b) {
//                 return a.depth - b.depth;
//             }
//             function d3_layout_treeVisitAfter(node, callback) {
//                 function visit(node, previousSibling) {
//                     var children = node.children;
//                     if (children && (n = children.length)) {
//                         var child, previousChild = null, i = -1, n;
//                         while (++i < n) {
//                             child = children[i];
//                             visit(child, previousChild);
//                             previousChild = child;
//                         }
//                     }
//                     callback(node, previousSibling);
//                 }
//                 visit(node, null);
//             }
//             function d3_layout_treeShift(node) {
//                 var shift = 0, change = 0, children = node.children, i = children.length, child;
//                 while (--i >= 0) {
//                     child = children[i]._tree;
//                     child.prelim += shift;
//                     child.mod += shift;
//                     shift += child.shift + (change += child.change);
//                 }
//             }
//             function d3_layout_treeMove(ancestor, node, shift) {
//                 ancestor = ancestor._tree;
//                 node = node._tree;
//                 var change = shift / (node.number - ancestor.number);
//                 ancestor.change += change;
//                 node.change -= change;
//                 node.shift += shift;
//                 node.prelim += shift;
//                 node.mod += shift;
//             }
//             function d3_layout_treeAncestor(vim, node, ancestor) {
//                 return vim._tree.ancestor.parent == node.parent ? vim._tree.ancestor : ancestor;
//             }
//             d3.layout.pack = function() {
//                 var hierarchy = d3.layout.hierarchy().sort(d3_layout_packSort), padding = 0, size = [ 1, 1 ], radius;
//                 function pack(d, i) {
//                     var nodes = hierarchy.call(this, d, i), root = nodes[0], w = size[0], h = size[1], r = radius == null ? Math.sqrt : typeof radius === "function" ? radius : function() {
//                         return radius;
//                     };
//                     root.x = root.y = 0;
//                     d3_layout_treeVisitAfter(root, function(d) {
//                         d.r = +r(d.value);
//                     });
//                     d3_layout_treeVisitAfter(root, d3_layout_packSiblings);
//                     if (padding) {
//                         var dr = padding * (radius ? 1 : Math.max(2 * root.r / w, 2 * root.r / h)) / 2;
//                         d3_layout_treeVisitAfter(root, function(d) {
//                             d.r += dr;
//                         });
//                         d3_layout_treeVisitAfter(root, d3_layout_packSiblings);
//                         d3_layout_treeVisitAfter(root, function(d) {
//                             d.r -= dr;
//                         });
//                     }
//                     d3_layout_packTransform(root, w / 2, h / 2, radius ? 1 : 1 / Math.max(2 * root.r / w, 2 * root.r / h));
//                     return nodes;
//                 }
//                 pack.size = function(_) {
//                     if (!arguments.length) return size;
//                     size = _;
//                     return pack;
//                 };
//                 pack.radius = function(_) {
//                     if (!arguments.length) return radius;
//                     radius = _ == null || typeof _ === "function" ? _ : +_;
//                     return pack;
//                 };
//                 pack.padding = function(_) {
//                     if (!arguments.length) return padding;
//                     padding = +_;
//                     return pack;
//                 };
//                 return d3_layout_hierarchyRebind(pack, hierarchy);
//             };
//             function d3_layout_packSort(a, b) {
//                 return a.value - b.value;
//             }
//             function d3_layout_packInsert(a, b) {
//                 var c = a._pack_next;
//                 a._pack_next = b;
//                 b._pack_prev = a;
//                 b._pack_next = c;
//                 c._pack_prev = b;
//             }
//             function d3_layout_packSplice(a, b) {
//                 a._pack_next = b;
//                 b._pack_prev = a;
//             }
//             function d3_layout_packIntersects(a, b) {
//                 var dx = b.x - a.x, dy = b.y - a.y, dr = a.r + b.r;
//                 return .999 * dr * dr > dx * dx + dy * dy;
//             }
//             function d3_layout_packSiblings(node) {
//                 if (!(nodes = node.children) || !(n = nodes.length)) return;
//                 var nodes, xMin = Infinity, xMax = -Infinity, yMin = Infinity, yMax = -Infinity, a, b, c, i, j, k, n;
//                 function bound(node) {
//                     xMin = Math.min(node.x - node.r, xMin);
//                     xMax = Math.max(node.x + node.r, xMax);
//                     yMin = Math.min(node.y - node.r, yMin);
//                     yMax = Math.max(node.y + node.r, yMax);
//                 }
//                 nodes.forEach(d3_layout_packLink);
//                 a = nodes[0];
//                 a.x = -a.r;
//                 a.y = 0;
//                 bound(a);
//                 if (n > 1) {
//                     b = nodes[1];
//                     b.x = b.r;
//                     b.y = 0;
//                     bound(b);
//                     if (n > 2) {
//                         c = nodes[2];
//                         d3_layout_packPlace(a, b, c);
//                         bound(c);
//                         d3_layout_packInsert(a, c);
//                         a._pack_prev = c;
//                         d3_layout_packInsert(c, b);
//                         b = a._pack_next;
//                         for (i = 3; i < n; i++) {
//                             d3_layout_packPlace(a, b, c = nodes[i]);
//                             var isect = 0, s1 = 1, s2 = 1;
//                             for (j = b._pack_next; j !== b; j = j._pack_next, s1++) {
//                                 if (d3_layout_packIntersects(j, c)) {
//                                     isect = 1;
//                                     break;
//                                 }
//                             }
//                             if (isect == 1) {
//                                 for (k = a._pack_prev; k !== j._pack_prev; k = k._pack_prev, s2++) {
//                                     if (d3_layout_packIntersects(k, c)) {
//                                         break;
//                                     }
//                                 }
//                             }
//                             if (isect) {
//                                 if (s1 < s2 || s1 == s2 && b.r < a.r) d3_layout_packSplice(a, b = j); else d3_layout_packSplice(a = k, b);
//                                 i--;
//                             } else {
//                                 d3_layout_packInsert(a, c);
//                                 b = c;
//                                 bound(c);
//                             }
//                         }
//                     }
//                 }
//                 var cx = (xMin + xMax) / 2, cy = (yMin + yMax) / 2, cr = 0;
//                 for (i = 0; i < n; i++) {
//                     c = nodes[i];
//                     c.x -= cx;
//                     c.y -= cy;
//                     cr = Math.max(cr, c.r + Math.sqrt(c.x * c.x + c.y * c.y));
//                 }
//                 node.r = cr;
//                 nodes.forEach(d3_layout_packUnlink);
//             }
//             function d3_layout_packLink(node) {
//                 node._pack_next = node._pack_prev = node;
//             }
//             function d3_layout_packUnlink(node) {
//                 delete node._pack_next;
//                 delete node._pack_prev;
//             }
//             function d3_layout_packTransform(node, x, y, k) {
//                 var children = node.children;
//                 node.x = x += k * node.x;
//                 node.y = y += k * node.y;
//                 node.r *= k;
//                 if (children) {
//                     var i = -1, n = children.length;
//                     while (++i < n) d3_layout_packTransform(children[i], x, y, k);
//                 }
//             }
//             function d3_layout_packPlace(a, b, c) {
//                 var db = a.r + c.r, dx = b.x - a.x, dy = b.y - a.y;
//                 if (db && (dx || dy)) {
//                     var da = b.r + c.r, dc = dx * dx + dy * dy;
//                     da *= da;
//                     db *= db;
//                     var x = .5 + (db - da) / (2 * dc), y = Math.sqrt(Math.max(0, 2 * da * (db + dc) - (db -= dc) * db - da * da)) / (2 * dc);
//                     c.x = a.x + x * dx + y * dy;
//                     c.y = a.y + x * dy - y * dx;
//                 } else {
//                     c.x = a.x + db;
//                     c.y = a.y;
//                 }
//             }
//             d3.layout.cluster = function() {
//                 var hierarchy = d3.layout.hierarchy().sort(null).value(null), separation = d3_layout_treeSeparation, size = [ 1, 1 ], nodeSize = false;
//                 function cluster(d, i) {
//                     var nodes = hierarchy.call(this, d, i), root = nodes[0], previousNode, x = 0;
//                     d3_layout_treeVisitAfter(root, function(node) {
//                         var children = node.children;
//                         if (children && children.length) {
//                             node.x = d3_layout_clusterX(children);
//                             node.y = d3_layout_clusterY(children);
//                         } else {
//                             node.x = previousNode ? x += separation(node, previousNode) : 0;
//                             node.y = 0;
//                             previousNode = node;
//                         }
//                     });
//                     var left = d3_layout_clusterLeft(root), right = d3_layout_clusterRight(root), x0 = left.x - separation(left, right) / 2, x1 = right.x + separation(right, left) / 2;
//                     d3_layout_treeVisitAfter(root, nodeSize ? function(node) {
//                         node.x = (node.x - root.x) * size[0];
//                         node.y = (root.y - node.y) * size[1];
//                     } : function(node) {
//                         node.x = (node.x - x0) / (x1 - x0) * size[0];
//                         node.y = (1 - (root.y ? node.y / root.y : 1)) * size[1];
//                     });
//                     return nodes;
//                 }
//                 cluster.separation = function(x) {
//                     if (!arguments.length) return separation;
//                     separation = x;
//                     return cluster;
//                 };
//                 cluster.size = function(x) {
//                     if (!arguments.length) return nodeSize ? null : size;
//                     nodeSize = (size = x) == null;
//                     return cluster;
//                 };
//                 cluster.nodeSize = function(x) {
//                     if (!arguments.length) return nodeSize ? size : null;
//                     nodeSize = (size = x) != null;
//                     return cluster;
//                 };
//                 return d3_layout_hierarchyRebind(cluster, hierarchy);
//             };
//             function d3_layout_clusterY(children) {
//                 return 1 + d3.max(children, function(child) {
//                     return child.y;
//                 });
//             }
//             function d3_layout_clusterX(children) {
//                 return children.reduce(function(x, child) {
//                     return x + child.x;
//                 }, 0) / children.length;
//             }
//             function d3_layout_clusterLeft(node) {
//                 var children = node.children;
//                 return children && children.length ? d3_layout_clusterLeft(children[0]) : node;
//             }
//             function d3_layout_clusterRight(node) {
//                 var children = node.children, n;
//                 return children && (n = children.length) ? d3_layout_clusterRight(children[n - 1]) : node;
//             }
//             d3.layout.treemap = function() {
//                 var hierarchy = d3.layout.hierarchy(), round = Math.round, size = [ 1, 1 ], padding = null, pad = d3_layout_treemapPadNull, sticky = false, stickies, mode = "squarify", ratio = .5 * (1 + Math.sqrt(5));
//                 function scale(children, k) {
//                     var i = -1, n = children.length, child, area;
//                     while (++i < n) {
//                         area = (child = children[i]).value * (k < 0 ? 0 : k);
//                         child.area = isNaN(area) || area <= 0 ? 0 : area;
//                     }
//                 }
//                 function squarify(node) {
//                     var children = node.children;
//                     if (children && children.length) {
//                         var rect = pad(node), row = [], remaining = children.slice(), child, best = Infinity, score, u = mode === "slice" ? rect.dx : mode === "dice" ? rect.dy : mode === "slice-dice" ? node.depth & 1 ? rect.dy : rect.dx : Math.min(rect.dx, rect.dy), n;
//                         scale(remaining, rect.dx * rect.dy / node.value);
//                         row.area = 0;
//                         while ((n = remaining.length) > 0) {
//                             row.push(child = remaining[n - 1]);
//                             row.area += child.area;
//                             if (mode !== "squarify" || (score = worst(row, u)) <= best) {
//                                 remaining.pop();
//                                 best = score;
//                             } else {
//                                 row.area -= row.pop().area;
//                                 position(row, u, rect, false);
//                                 u = Math.min(rect.dx, rect.dy);
//                                 row.length = row.area = 0;
//                                 best = Infinity;
//                             }
//                         }
//                         if (row.length) {
//                             position(row, u, rect, true);
//                             row.length = row.area = 0;
//                         }
//                         children.forEach(squarify);
//                     }
//                 }
//                 function stickify(node) {
//                     var children = node.children;
//                     if (children && children.length) {
//                         var rect = pad(node), remaining = children.slice(), child, row = [];
//                         scale(remaining, rect.dx * rect.dy / node.value);
//                         row.area = 0;
//                         while (child = remaining.pop()) {
//                             row.push(child);
//                             row.area += child.area;
//                             if (child.z != null) {
//                                 position(row, child.z ? rect.dx : rect.dy, rect, !remaining.length);
//                                 row.length = row.area = 0;
//                             }
//                         }
//                         children.forEach(stickify);
//                     }
//                 }
//                 function worst(row, u) {
//                     var s = row.area, r, rmax = 0, rmin = Infinity, i = -1, n = row.length;
//                     while (++i < n) {
//                         if (!(r = row[i].area)) continue;
//                         if (r < rmin) rmin = r;
//                         if (r > rmax) rmax = r;
//                     }
//                     s *= s;
//                     u *= u;
//                     return s ? Math.max(u * rmax * ratio / s, s / (u * rmin * ratio)) : Infinity;
//                 }
//                 function position(row, u, rect, flush) {
//                     var i = -1, n = row.length, x = rect.x, y = rect.y, v = u ? round(row.area / u) : 0, o;
//                     if (u == rect.dx) {
//                         if (flush || v > rect.dy) v = rect.dy;
//                         while (++i < n) {
//                             o = row[i];
//                             o.x = x;
//                             o.y = y;
//                             o.dy = v;
//                             x += o.dx = Math.min(rect.x + rect.dx - x, v ? round(o.area / v) : 0);
//                         }
//                         o.z = true;
//                         o.dx += rect.x + rect.dx - x;
//                         rect.y += v;
//                         rect.dy -= v;
//                     } else {
//                         if (flush || v > rect.dx) v = rect.dx;
//                         while (++i < n) {
//                             o = row[i];
//                             o.x = x;
//                             o.y = y;
//                             o.dx = v;
//                             y += o.dy = Math.min(rect.y + rect.dy - y, v ? round(o.area / v) : 0);
//                         }
//                         o.z = false;
//                         o.dy += rect.y + rect.dy - y;
//                         rect.x += v;
//                         rect.dx -= v;
//                     }
//                 }
//                 function treemap(d) {
//                     var nodes = stickies || hierarchy(d), root = nodes[0];
//                     root.x = 0;
//                     root.y = 0;
//                     root.dx = size[0];
//                     root.dy = size[1];
//                     if (stickies) hierarchy.revalue(root);
//                     scale([ root ], root.dx * root.dy / root.value);
//                     (stickies ? stickify : squarify)(root);
//                     if (sticky) stickies = nodes;
//                     return nodes;
//                 }
//                 treemap.size = function(x) {
//                     if (!arguments.length) return size;
//                     size = x;
//                     return treemap;
//                 };
//                 treemap.padding = function(x) {
//                     if (!arguments.length) return padding;
//                     function padFunction(node) {
//                         var p = x.call(treemap, node, node.depth);
//                         return p == null ? d3_layout_treemapPadNull(node) : d3_layout_treemapPad(node, typeof p === "number" ? [ p, p, p, p ] : p);
//                     }
//                     function padConstant(node) {
//                         return d3_layout_treemapPad(node, x);
//                     }
//                     var type;
//                     pad = (padding = x) == null ? d3_layout_treemapPadNull : (type = typeof x) === "function" ? padFunction : type === "number" ? (x = [ x, x, x, x ],
//                         padConstant) : padConstant;
//                     return treemap;
//                 };
//                 treemap.round = function(x) {
//                     if (!arguments.length) return round != Number;
//                     round = x ? Math.round : Number;
//                     return treemap;
//                 };
//                 treemap.sticky = function(x) {
//                     if (!arguments.length) return sticky;
//                     sticky = x;
//                     stickies = null;
//                     return treemap;
//                 };
//                 treemap.ratio = function(x) {
//                     if (!arguments.length) return ratio;
//                     ratio = x;
//                     return treemap;
//                 };
//                 treemap.mode = function(x) {
//                     if (!arguments.length) return mode;
//                     mode = x + "";
//                     return treemap;
//                 };
//                 return d3_layout_hierarchyRebind(treemap, hierarchy);
//             };
//             function d3_layout_treemapPadNull(node) {
//                 return {
//                     x: node.x,
//                         y: node.y,
//                         dx: node.dx,
//                         dy: node.dy
//                 };
//             }
//             function d3_layout_treemapPad(node, padding) {
//                 var x = node.x + padding[3], y = node.y + padding[0], dx = node.dx - padding[1] - padding[3], dy = node.dy - padding[0] - padding[2];
//                 if (dx < 0) {
//                     x += dx / 2;
//                     dx = 0;
//                 }
//                 if (dy < 0) {
//                     y += dy / 2;
//                     dy = 0;
//                 }
//                 return {
//                     x: x,
//                         y: y,
//                         dx: dx,
//                         dy: dy
//                 };
//             }
//             d3.random = {
//                 normal: function(µ, σ) {
//                     var n = arguments.length;
//                     if (n < 2) σ = 1;
//                     if (n < 1) µ = 0;
//                     return function() {
//                         var x, y, r;
//                         do {
//                             x = Math.random() * 2 - 1;
//                             y = Math.random() * 2 - 1;
//                             r = x * x + y * y;
//                         } while (!r || r > 1);
//                         return µ + σ * x * Math.sqrt(-2 * Math.log(r) / r);
//                     };
//                 },
//                 logNormal: function() {
//                     var random = d3.random.normal.apply(d3, arguments);
//                     return function() {
//                         return Math.exp(random());
//                     };
//                 },
//                 irwinHall: function(m) {
//                     return function() {
//                         for (var s = 0, j = 0; j < m; j++) s += Math.random();
//                         return s / m;
//                     };
//                 }
//             };
//             d3.scale = {};
//             function d3_scaleExtent(domain) {
//                 var start = domain[0], stop = domain[domain.length - 1];
//                 return start < stop ? [ start, stop ] : [ stop, start ];
//             }
//             function d3_scaleRange(scale) {
//                 return scale.rangeExtent ? scale.rangeExtent() : d3_scaleExtent(scale.range());
//             }
//             function d3_scale_bilinear(domain, range, uninterpolate, interpolate) {
//                 var u = uninterpolate(domain[0], domain[1]), i = interpolate(range[0], range[1]);
//                 return function(x) {
//                     return i(u(x));
//                 };
//             }
//             function d3_scale_nice(domain, nice) {
//                 var i0 = 0, i1 = domain.length - 1, x0 = domain[i0], x1 = domain[i1], dx;
//                 if (x1 < x0) {
//                     dx = i0, i0 = i1, i1 = dx;
//                     dx = x0, x0 = x1, x1 = dx;
//                 }
//                 domain[i0] = nice.floor(x0);
//                 domain[i1] = nice.ceil(x1);
//                 return domain;
//             }
//             function d3_scale_niceStep(step) {
//                 return step ? {
//                     floor: function(x) {
//                         return Math.floor(x / step) * step;
//                     },
//                     ceil: function(x) {
//                         return Math.ceil(x / step) * step;
//                     }
//                 } : d3_scale_niceIdentity;
//             }
//             var d3_scale_niceIdentity = {
//                 floor: d3_identity,
//                 ceil: d3_identity
//             };
//             function d3_scale_polylinear(domain, range, uninterpolate, interpolate) {
//                 var u = [], i = [], j = 0, k = Math.min(domain.length, range.length) - 1;
//                 if (domain[k] < domain[0]) {
//                     domain = domain.slice().reverse();
//                     range = range.slice().reverse();
//                 }
//                 while (++j <= k) {
//                     u.push(uninterpolate(domain[j - 1], domain[j]));
//                     i.push(interpolate(range[j - 1], range[j]));
//                 }
//                 return function(x) {
//                     var j = d3.bisect(domain, x, 1, k) - 1;
//                     return i[j](u[j](x));
//                 };
//             }
//             d3.scale.linear = function() {
//                 return d3_scale_linear([ 0, 1 ], [ 0, 1 ], d3_interpolate, false);
//             };
//             function d3_scale_linear(domain, range, interpolate, clamp) {
//                 var output, input;
//                 function rescale() {
//                     var linear = Math.min(domain.length, range.length) > 2 ? d3_scale_polylinear : d3_scale_bilinear, uninterpolate = clamp ? d3_uninterpolateClamp : d3_uninterpolateNumber;
//                     output = linear(domain, range, uninterpolate, interpolate);
//                     input = linear(range, domain, uninterpolate, d3_interpolate);
//                     return scale;
//                 }
//                 function scale(x) {
//                     return output(x);
//                 }
//                 scale.invert = function(y) {
//                     return input(y);
//                 };
//                 scale.domain = function(x) {
//                     if (!arguments.length) return domain;
//                     domain = x.map(Number);
//                     return rescale();
//                 };
//                 scale.range = function(x) {
//                     if (!arguments.length) return range;
//                     range = x;
//                     return rescale();
//                 };
//                 scale.rangeRound = function(x) {
//                     return scale.range(x).interpolate(d3_interpolateRound);
//                 };
//                 scale.clamp = function(x) {
//                     if (!arguments.length) return clamp;
//                     clamp = x;
//                     return rescale();
//                 };
//                 scale.interpolate = function(x) {
//                     if (!arguments.length) return interpolate;
//                     interpolate = x;
//                     return rescale();
//                 };
//                 scale.ticks = function(m) {
//                     return d3_scale_linearTicks(domain, m);
//                 };
//                 scale.tickFormat = function(m, format) {
//                     return d3_scale_linearTickFormat(domain, m, format);
//                 };
//                 scale.nice = function(m) {
//                     d3_scale_linearNice(domain, m);
//                     return rescale();
//                 };
//                 scale.copy = function() {
//                     return d3_scale_linear(domain, range, interpolate, clamp);
//                 };
//                 return rescale();
//             }
//             function d3_scale_linearRebind(scale, linear) {
//                 return d3.rebind(scale, linear, "range", "rangeRound", "interpolate", "clamp");
//             }
//             function d3_scale_linearNice(domain, m) {
//                 return d3_scale_nice(domain, d3_scale_niceStep(d3_scale_linearTickRange(domain, m)[2]));
//             }
//             function d3_scale_linearTickRange(domain, m) {
//                 if (m == null) m = 10;
//                 var extent = d3_scaleExtent(domain), span = extent[1] - extent[0], step = Math.pow(10, Math.floor(Math.log(span / m) / Math.LN10)), err = m / span * step;
//                 if (err <= .15) step *= 10; else if (err <= .35) step *= 5; else if (err <= .75) step *= 2;
//                 extent[0] = Math.ceil(extent[0] / step) * step;
//                 extent[1] = Math.floor(extent[1] / step) * step + step * .5;
//                 extent[2] = step;
//                 return extent;
//             }
//             function d3_scale_linearTicks(domain, m) {
//                 return d3.range.apply(d3, d3_scale_linearTickRange(domain, m));
//             }
//             function d3_scale_linearTickFormat(domain, m, format) {
//                 var range = d3_scale_linearTickRange(domain, m);
//                 return d3.format(format ? format.replace(d3_format_re, function(a, b, c, d, e, f, g, h, i, j) {
//                     return [ b, c, d, e, f, g, h, i || "." + d3_scale_linearFormatPrecision(j, range), j ].join("");
//                 }) : ",." + d3_scale_linearPrecision(range[2]) + "f");
//             }
//             var d3_scale_linearFormatSignificant = {
//                 s: 1,
//                 g: 1,
//                 p: 1,
//                 r: 1,
//                 e: 1
//             };
//             function d3_scale_linearPrecision(value) {
//                 return -Math.floor(Math.log(value) / Math.LN10 + .01);
//             }
//             function d3_scale_linearFormatPrecision(type, range) {
//                 var p = d3_scale_linearPrecision(range[2]);
//                 return type in d3_scale_linearFormatSignificant ? Math.abs(p - d3_scale_linearPrecision(Math.max(Math.abs(range[0]), Math.abs(range[1])))) + +(type !== "e") : p - (type === "%") * 2;
//             }
//             d3.scale.log = function() {
//                 return d3_scale_log(d3.scale.linear().domain([ 0, 1 ]), 10, true, [ 1, 10 ]);
//             };
//             function d3_scale_log(linear, base, positive, domain) {
//                 function log(x) {
//                     return (positive ? Math.log(x < 0 ? 0 : x) : -Math.log(x > 0 ? 0 : -x)) / Math.log(base);
//                 }
//                 function pow(x) {
//                     return positive ? Math.pow(base, x) : -Math.pow(base, -x);
//                 }
//                 function scale(x) {
//                     return linear(log(x));
//                 }
//                 scale.invert = function(x) {
//                     return pow(linear.invert(x));
//                 };
//                 scale.domain = function(x) {
//                     if (!arguments.length) return domain;
//                     positive = x[0] >= 0;
//                     linear.domain((domain = x.map(Number)).map(log));
//                     return scale;
//                 };
//                 scale.base = function(_) {
//                     if (!arguments.length) return base;
//                     base = +_;
//                     linear.domain(domain.map(log));
//                     return scale;
//                 };
//                 scale.nice = function() {
//                     var niced = d3_scale_nice(domain.map(log), positive ? Math : d3_scale_logNiceNegative);
//                     linear.domain(niced);
//                     domain = niced.map(pow);
//                     return scale;
//                 };
//                 scale.ticks = function() {
//                     var extent = d3_scaleExtent(domain), ticks = [], u = extent[0], v = extent[1], i = Math.floor(log(u)), j = Math.ceil(log(v)), n = base % 1 ? 2 : base;
//                     if (isFinite(j - i)) {
//                         if (positive) {
//                             for (;i < j; i++) for (var k = 1; k < n; k++) ticks.push(pow(i) * k);
//                             ticks.push(pow(i));
//                         } else {
//                             ticks.push(pow(i));
//                             for (;i++ < j; ) for (var k = n - 1; k > 0; k--) ticks.push(pow(i) * k);
//                         }
//                         for (i = 0; ticks[i] < u; i++) {}
//                         for (j = ticks.length; ticks[j - 1] > v; j--) {}
//                         ticks = ticks.slice(i, j);
//                     }
//                     return ticks;
//                 };
//                 scale.tickFormat = function(n, format) {
//                     if (!arguments.length) return d3_scale_logFormat;
//                     if (arguments.length < 2) format = d3_scale_logFormat; else if (typeof format !== "function") format = d3.format(format);
//                     var k = Math.max(.1, n / scale.ticks().length), f = positive ? (e = 1e-12, Math.ceil) : (e = -1e-12,
//                         Math.floor), e;
//                     return function(d) {
//                         return d / pow(f(log(d) + e)) <= k ? format(d) : "";
//                     };
//                 };
//                 scale.copy = function() {
//                     return d3_scale_log(linear.copy(), base, positive, domain);
//                 };
//                 return d3_scale_linearRebind(scale, linear);
//             }
//             var d3_scale_logFormat = d3.format(".0e"), d3_scale_logNiceNegative = {
//                 floor: function(x) {
//                     return -Math.ceil(-x);
//                 },
//                 ceil: function(x) {
//                     return -Math.floor(-x);
//                 }
//             };
//             d3.scale.pow = function() {
//                 return d3_scale_pow(d3.scale.linear(), 1, [ 0, 1 ]);
//             };
//             function d3_scale_pow(linear, exponent, domain) {
//                 var powp = d3_scale_powPow(exponent), powb = d3_scale_powPow(1 / exponent);
//                 function scale(x) {
//                     return linear(powp(x));
//                 }
//                 scale.invert = function(x) {
//                     return powb(linear.invert(x));
//                 };
//                 scale.domain = function(x) {
//                     if (!arguments.length) return domain;
//                     linear.domain((domain = x.map(Number)).map(powp));
//                     return scale;
//                 };
//                 scale.ticks = function(m) {
//                     return d3_scale_linearTicks(domain, m);
//                 };
//                 scale.tickFormat = function(m, format) {
//                     return d3_scale_linearTickFormat(domain, m, format);
//                 };
//                 scale.nice = function(m) {
//                     return scale.domain(d3_scale_linearNice(domain, m));
//                 };
//                 scale.exponent = function(x) {
//                     if (!arguments.length) return exponent;
//                     powp = d3_scale_powPow(exponent = x);
//                     powb = d3_scale_powPow(1 / exponent);
//                     linear.domain(domain.map(powp));
//                     return scale;
//                 };
//                 scale.copy = function() {
//                     return d3_scale_pow(linear.copy(), exponent, domain);
//                 };
//                 return d3_scale_linearRebind(scale, linear);
//             }
//             function d3_scale_powPow(e) {
//                 return function(x) {
//                     return x < 0 ? -Math.pow(-x, e) : Math.pow(x, e);
//                 };
//             }
//             d3.scale.sqrt = function() {
//                 return d3.scale.pow().exponent(.5);
//             };
//             d3.scale.ordinal = function() {
//                 return d3_scale_ordinal([], {
//                     t: "range",
//                        a: [ [] ]
//                 });
//             };
//             function d3_scale_ordinal(domain, ranger) {
//                 var index, range, rangeBand;
//                 function scale(x) {
//                     return range[((index.get(x) || ranger.t === "range" && index.set(x, domain.push(x))) - 1) % range.length];
//                 }
//                 function steps(start, step) {
//                     return d3.range(domain.length).map(function(i) {
//                         return start + step * i;
//                     });
//                 }
//                 scale.domain = function(x) {
//                     if (!arguments.length) return domain;
//                     domain = [];
//                     index = new d3_Map();
//                     var i = -1, n = x.length, xi;
//                     while (++i < n) if (!index.has(xi = x[i])) index.set(xi, domain.push(xi));
//                     return scale[ranger.t].apply(scale, ranger.a);
//                 };
//                 scale.range = function(x) {
//                     if (!arguments.length) return range;
//                     range = x;
//                     rangeBand = 0;
//                     ranger = {
//                         t: "range",
//                         a: arguments
//                     };
//                     return scale;
//                 };
//                 scale.rangePoints = function(x, padding) {
//                     if (arguments.length < 2) padding = 0;
//                     var start = x[0], stop = x[1], step = (stop - start) / (Math.max(1, domain.length - 1) + padding);
//                     range = steps(domain.length < 2 ? (start + stop) / 2 : start + step * padding / 2, step);
//                     rangeBand = 0;
//                     ranger = {
//                         t: "rangePoints",
//                         a: arguments
//                     };
//                     return scale;
//                 };
//                 scale.rangeBands = function(x, padding, outerPadding) {
//                     if (arguments.length < 2) padding = 0;
//                     if (arguments.length < 3) outerPadding = padding;
//                     var reverse = x[1] < x[0], start = x[reverse - 0], stop = x[1 - reverse], step = (stop - start) / (domain.length - padding + 2 * outerPadding);
//                     range = steps(start + step * outerPadding, step);
//                     if (reverse) range.reverse();
//                     rangeBand = step * (1 - padding);
//                     ranger = {
//                         t: "rangeBands",
//                         a: arguments
//                     };
//                     return scale;
//                 };
//                 scale.rangeRoundBands = function(x, padding, outerPadding) {
//                     if (arguments.length < 2) padding = 0;
//                     if (arguments.length < 3) outerPadding = padding;
//                     var reverse = x[1] < x[0], start = x[reverse - 0], stop = x[1 - reverse], step = Math.floor((stop - start) / (domain.length - padding + 2 * outerPadding)), error = stop - start - (domain.length - padding) * step;
//                     range = steps(start + Math.round(error / 2), step);
//                     if (reverse) range.reverse();
//                     rangeBand = Math.round(step * (1 - padding));
//                     ranger = {
//                         t: "rangeRoundBands",
//                         a: arguments
//                     };
//                     return scale;
//                 };
//                 scale.rangeBand = function() {
//                     return rangeBand;
//                 };
//                 scale.rangeExtent = function() {
//                     return d3_scaleExtent(ranger.a[0]);
//                 };
//                 scale.copy = function() {
//                     return d3_scale_ordinal(domain, ranger);
//                 };
//                 return scale.domain(domain);
//             }
//             d3.scale.category10 = function() {
//                 return d3.scale.ordinal().range(d3_category10);
//             };
//             d3.scale.category20 = function() {
//                 return d3.scale.ordinal().range(d3_category20);
//             };
//             d3.scale.category20b = function() {
//                 return d3.scale.ordinal().range(d3_category20b);
//             };
//             d3.scale.category20c = function() {
//                 return d3.scale.ordinal().range(d3_category20c);
//             };
//             var d3_category10 = [ 2062260, 16744206, 2924588, 14034728, 9725885, 9197131, 14907330, 8355711, 12369186, 1556175 ].map(d3_rgbString);
//             var d3_category20 = [ 2062260, 11454440, 16744206, 16759672, 2924588, 10018698, 14034728, 16750742, 9725885, 12955861, 9197131, 12885140, 14907330, 16234194, 8355711, 13092807, 12369186, 14408589, 1556175, 10410725 ].map(d3_rgbString);
//             var d3_category20b = [ 3750777, 5395619, 7040719, 10264286, 6519097, 9216594, 11915115, 13556636, 9202993, 12426809, 15186514, 15190932, 8666169, 11356490, 14049643, 15177372, 8077683, 10834324, 13528509, 14589654 ].map(d3_rgbString);
//             var d3_category20c = [ 3244733, 7057110, 10406625, 13032431, 15095053, 16616764, 16625259, 16634018, 3253076, 7652470, 10607003, 13101504, 7695281, 10394312, 12369372, 14342891, 6513507, 9868950, 12434877, 14277081 ].map(d3_rgbString);
//             d3.scale.quantile = function() {
//                 return d3_scale_quantile([], []);
//             };
//             function d3_scale_quantile(domain, range) {
//                 var thresholds;
//                 function rescale() {
//                     var k = 0, q = range.length;
//                     thresholds = [];
//                     while (++k < q) thresholds[k - 1] = d3.quantile(domain, k / q);
//                     return scale;
//                 }
//                 function scale(x) {
//                     if (!isNaN(x = +x)) return range[d3.bisect(thresholds, x)];
//                 }
//                 scale.domain = function(x) {
//                     if (!arguments.length) return domain;
//                     domain = x.filter(function(d) {
//                         return !isNaN(d);
//                     }).sort(d3.ascending);
//                     return rescale();
//                 };
//                 scale.range = function(x) {
//                     if (!arguments.length) return range;
//                     range = x;
//                     return rescale();
//                 };
//                 scale.quantiles = function() {
//                     return thresholds;
//                 };
//                 scale.invertExtent = function(y) {
//                     y = range.indexOf(y);
//                     return y < 0 ? [ NaN, NaN ] : [ y > 0 ? thresholds[y - 1] : domain[0], y < thresholds.length ? thresholds[y] : domain[domain.length - 1] ];
//                 };
//                 scale.copy = function() {
//                     return d3_scale_quantile(domain, range);
//                 };
//                 return rescale();
//             }
//             d3.scale.quantize = function() {
//                 return d3_scale_quantize(0, 1, [ 0, 1 ]);
//             };
//             function d3_scale_quantize(x0, x1, range) {
//                 var kx, i;
//                 function scale(x) {
//                     return range[Math.max(0, Math.min(i, Math.floor(kx * (x - x0))))];
//                 }
//                 function rescale() {
//                     kx = range.length / (x1 - x0);
//                     i = range.length - 1;
//                     return scale;
//                 }
//                 scale.domain = function(x) {
//                     if (!arguments.length) return [ x0, x1 ];
//                     x0 = +x[0];
//                     x1 = +x[x.length - 1];
//                     return rescale();
//                 };
//                 scale.range = function(x) {
//                     if (!arguments.length) return range;
//                     range = x;
//                     return rescale();
//                 };
//                 scale.invertExtent = function(y) {
//                     y = range.indexOf(y);
//                     y = y < 0 ? NaN : y / kx + x0;
//                     return [ y, y + 1 / kx ];
//                 };
//                 scale.copy = function() {
//                     return d3_scale_quantize(x0, x1, range);
//                 };
//                 return rescale();
//             }
//             d3.scale.threshold = function() {
//                 return d3_scale_threshold([ .5 ], [ 0, 1 ]);
//             };
//             function d3_scale_threshold(domain, range) {
//                 function scale(x) {
//                     if (x <= x) return range[d3.bisect(domain, x)];
//                 }
//                 scale.domain = function(_) {
//                     if (!arguments.length) return domain;
//                     domain = _;
//                     return scale;
//                 };
//                 scale.range = function(_) {
//                     if (!arguments.length) return range;
//                     range = _;
//                     return scale;
//                 };
//                 scale.invertExtent = function(y) {
//                     y = range.indexOf(y);
//                     return [ domain[y - 1], domain[y] ];
//                 };
//                 scale.copy = function() {
//                     return d3_scale_threshold(domain, range);
//                 };
//                 return scale;
//             }
//             d3.scale.identity = function() {
//                 return d3_scale_identity([ 0, 1 ]);
//             };
//             function d3_scale_identity(domain) {
//                 function identity(x) {
//                     return +x;
//                 }
//                 identity.invert = identity;
//                 identity.domain = identity.range = function(x) {
//                     if (!arguments.length) return domain;
//                     domain = x.map(identity);
//                     return identity;
//                 };
//                 identity.ticks = function(m) {
//                     return d3_scale_linearTicks(domain, m);
//                 };
//                 identity.tickFormat = function(m, format) {
//                     return d3_scale_linearTickFormat(domain, m, format);
//                 };
//                 identity.copy = function() {
//                     return d3_scale_identity(domain);
//                 };
//                 return identity;
//             }
//             d3.svg = {};
//             d3.svg.arc = function() {
//                 var innerRadius = d3_svg_arcInnerRadius, outerRadius = d3_svg_arcOuterRadius, startAngle = d3_svg_arcStartAngle, endAngle = d3_svg_arcEndAngle;
//                 function arc() {
//                     var r0 = innerRadius.apply(this, arguments), r1 = outerRadius.apply(this, arguments), a0 = startAngle.apply(this, arguments) + d3_svg_arcOffset, a1 = endAngle.apply(this, arguments) + d3_svg_arcOffset, da = (a1 < a0 && (da = a0,
//                         a0 = a1, a1 = da), a1 - a0), df = da < π ? "0" : "1", c0 = Math.cos(a0), s0 = Math.sin(a0), c1 = Math.cos(a1), s1 = Math.sin(a1);
//                     return da >= d3_svg_arcMax ? r0 ? "M0," + r1 + "A" + r1 + "," + r1 + " 0 1,1 0," + -r1 + "A" + r1 + "," + r1 + " 0 1,1 0," + r1 + "M0," + r0 + "A" + r0 + "," + r0 + " 0 1,0 0," + -r0 + "A" + r0 + "," + r0 + " 0 1,0 0," + r0 + "Z" : "M0," + r1 + "A" + r1 + "," + r1 + " 0 1,1 0," + -r1 + "A" + r1 + "," + r1 + " 0 1,1 0," + r1 + "Z" : r0 ? "M" + r1 * c0 + "," + r1 * s0 + "A" + r1 + "," + r1 + " 0 " + df + ",1 " + r1 * c1 + "," + r1 * s1 + "L" + r0 * c1 + "," + r0 * s1 + "A" + r0 + "," + r0 + " 0 " + df + ",0 " + r0 * c0 + "," + r0 * s0 + "Z" : "M" + r1 * c0 + "," + r1 * s0 + "A" + r1 + "," + r1 + " 0 " + df + ",1 " + r1 * c1 + "," + r1 * s1 + "L0,0" + "Z";
//                 }
//                 arc.innerRadius = function(v) {
//                     if (!arguments.length) return innerRadius;
//                     innerRadius = d3_functor(v);
//                     return arc;
//                 };
//                 arc.outerRadius = function(v) {
//                     if (!arguments.length) return outerRadius;
//                     outerRadius = d3_functor(v);
//                     return arc;
//                 };
//                 arc.startAngle = function(v) {
//                     if (!arguments.length) return startAngle;
//                     startAngle = d3_functor(v);
//                     return arc;
//                 };
//                 arc.endAngle = function(v) {
//                     if (!arguments.length) return endAngle;
//                     endAngle = d3_functor(v);
//                     return arc;
//                 };
//                 arc.centroid = function() {
//                     var r = (innerRadius.apply(this, arguments) + outerRadius.apply(this, arguments)) / 2, a = (startAngle.apply(this, arguments) + endAngle.apply(this, arguments)) / 2 + d3_svg_arcOffset;
//                     return [ Math.cos(a) * r, Math.sin(a) * r ];
//                 };
//                 return arc;
//             };
//             var d3_svg_arcOffset = -halfπ, d3_svg_arcMax = τ - ε;
//             function d3_svg_arcInnerRadius(d) {
//                 return d.innerRadius;
//             }
//             function d3_svg_arcOuterRadius(d) {
//                 return d.outerRadius;
//             }
//             function d3_svg_arcStartAngle(d) {
//                 return d.startAngle;
//             }
//             function d3_svg_arcEndAngle(d) {
//                 return d.endAngle;
//             }
//             function d3_svg_line(projection) {
//                 var x = d3_geom_pointX, y = d3_geom_pointY, defined = d3_true, interpolate = d3_svg_lineLinear, interpolateKey = interpolate.key, tension = .7;
//                 function line(data) {
//                     var segments = [], points = [], i = -1, n = data.length, d, fx = d3_functor(x), fy = d3_functor(y);
//                     function segment() {
//                         segments.push("M", interpolate(projection(points), tension));
//                     }
//                     while (++i < n) {
//                         if (defined.call(this, d = data[i], i)) {
//                             points.push([ +fx.call(this, d, i), +fy.call(this, d, i) ]);
//                         } else if (points.length) {
//                             segment();
//                             points = [];
//                         }
//                     }
//                     if (points.length) segment();
//                     return segments.length ? segments.join("") : null;
//                 }
//                 line.x = function(_) {
//                     if (!arguments.length) return x;
//                     x = _;
//                     return line;
//                 };
//                 line.y = function(_) {
//                     if (!arguments.length) return y;
//                     y = _;
//                     return line;
//                 };
//                 line.defined = function(_) {
//                     if (!arguments.length) return defined;
//                     defined = _;
//                     return line;
//                 };
//                 line.interpolate = function(_) {
//                     if (!arguments.length) return interpolateKey;
//                     if (typeof _ === "function") interpolateKey = interpolate = _; else interpolateKey = (interpolate = d3_svg_lineInterpolators.get(_) || d3_svg_lineLinear).key;
//                     return line;
//                 };
//                 line.tension = function(_) {
//                     if (!arguments.length) return tension;
//                     tension = _;
//                     return line;
//                 };
//                 return line;
//             }
//             d3.svg.line = function() {
//                 return d3_svg_line(d3_identity);
//             };
//             var d3_svg_lineInterpolators = d3.map({
//                 linear: d3_svg_lineLinear,
//                 "linear-closed": d3_svg_lineLinearClosed,
//                 step: d3_svg_lineStep,
//                 "step-before": d3_svg_lineStepBefore,
//                 "step-after": d3_svg_lineStepAfter,
//                 basis: d3_svg_lineBasis,
//                 "basis-open": d3_svg_lineBasisOpen,
//                 "basis-closed": d3_svg_lineBasisClosed,
//                 bundle: d3_svg_lineBundle,
//                 cardinal: d3_svg_lineCardinal,
//                 "cardinal-open": d3_svg_lineCardinalOpen,
//                 "cardinal-closed": d3_svg_lineCardinalClosed,
//                 monotone: d3_svg_lineMonotone
//             });
//             d3_svg_lineInterpolators.forEach(function(key, value) {
//                 value.key = key;
//                 value.closed = /-closed$/.test(key);
//             });
//             function d3_svg_lineLinear(points) {
//                 return points.join("L");
//             }
//             function d3_svg_lineLinearClosed(points) {
//                 return d3_svg_lineLinear(points) + "Z";
//             }
//             function d3_svg_lineStep(points) {
//                 var i = 0, n = points.length, p = points[0], path = [ p[0], ",", p[1] ];
//                 while (++i < n) path.push("H", (p[0] + (p = points[i])[0]) / 2, "V", p[1]);
//                 if (n > 1) path.push("H", p[0]);
//                 return path.join("");
//             }
//             function d3_svg_lineStepBefore(points) {
//                 var i = 0, n = points.length, p = points[0], path = [ p[0], ",", p[1] ];
//                 while (++i < n) path.push("V", (p = points[i])[1], "H", p[0]);
//                 return path.join("");
//             }
//             function d3_svg_lineStepAfter(points) {
//                 var i = 0, n = points.length, p = points[0], path = [ p[0], ",", p[1] ];
//                 while (++i < n) path.push("H", (p = points[i])[0], "V", p[1]);
//                 return path.join("");
//             }
//             function d3_svg_lineCardinalOpen(points, tension) {
//                 return points.length < 4 ? d3_svg_lineLinear(points) : points[1] + d3_svg_lineHermite(points.slice(1, points.length - 1), d3_svg_lineCardinalTangents(points, tension));
//             }
//             function d3_svg_lineCardinalClosed(points, tension) {
//                 return points.length < 3 ? d3_svg_lineLinear(points) : points[0] + d3_svg_lineHermite((points.push(points[0]),
//                     points), d3_svg_lineCardinalTangents([ points[points.length - 2] ].concat(points, [ points[1] ]), tension));
//             }
//             function d3_svg_lineCardinal(points, tension) {
//                 return points.length < 3 ? d3_svg_lineLinear(points) : points[0] + d3_svg_lineHermite(points, d3_svg_lineCardinalTangents(points, tension));
//             }
//             function d3_svg_lineHermite(points, tangents) {
//                 if (tangents.length < 1 || points.length != tangents.length && points.length != tangents.length + 2) {
//                     return d3_svg_lineLinear(points);
//                 }
//                 var quad = points.length != tangents.length, path = "", p0 = points[0], p = points[1], t0 = tangents[0], t = t0, pi = 1;
//                 if (quad) {
//                     path += "Q" + (p[0] - t0[0] * 2 / 3) + "," + (p[1] - t0[1] * 2 / 3) + "," + p[0] + "," + p[1];
//                     p0 = points[1];
//                     pi = 2;
//                 }
//                 if (tangents.length > 1) {
//                     t = tangents[1];
//                     p = points[pi];
//                     pi++;
//                     path += "C" + (p0[0] + t0[0]) + "," + (p0[1] + t0[1]) + "," + (p[0] - t[0]) + "," + (p[1] - t[1]) + "," + p[0] + "," + p[1];
//                     for (var i = 2; i < tangents.length; i++, pi++) {
//                         p = points[pi];
//                         t = tangents[i];
//                         path += "S" + (p[0] - t[0]) + "," + (p[1] - t[1]) + "," + p[0] + "," + p[1];
//                     }
//                 }
//                 if (quad) {
//                     var lp = points[pi];
//                     path += "Q" + (p[0] + t[0] * 2 / 3) + "," + (p[1] + t[1] * 2 / 3) + "," + lp[0] + "," + lp[1];
//                 }
//                 return path;
//             }
//             function d3_svg_lineCardinalTangents(points, tension) {
//                 var tangents = [], a = (1 - tension) / 2, p0, p1 = points[0], p2 = points[1], i = 1, n = points.length;
//                 while (++i < n) {
//                     p0 = p1;
//                     p1 = p2;
//                     p2 = points[i];
//                     tangents.push([ a * (p2[0] - p0[0]), a * (p2[1] - p0[1]) ]);
//                 }
//                 return tangents;
//             }
//             function d3_svg_lineBasis(points) {
//                 if (points.length < 3) return d3_svg_lineLinear(points);
//                 var i = 1, n = points.length, pi = points[0], x0 = pi[0], y0 = pi[1], px = [ x0, x0, x0, (pi = points[1])[0] ], py = [ y0, y0, y0, pi[1] ], path = [ x0, ",", y0, "L", d3_svg_lineDot4(d3_svg_lineBasisBezier3, px), ",", d3_svg_lineDot4(d3_svg_lineBasisBezier3, py) ];
//                 points.push(points[n - 1]);
//                 while (++i <= n) {
//                     pi = points[i];
//                     px.shift();
//                     px.push(pi[0]);
//                     py.shift();
//                     py.push(pi[1]);
//                     d3_svg_lineBasisBezier(path, px, py);
//                 }
//                 points.pop();
//                 path.push("L", pi);
//                 return path.join("");
//             }
//             function d3_svg_lineBasisOpen(points) {
//                 if (points.length < 4) return d3_svg_lineLinear(points);
//                 var path = [], i = -1, n = points.length, pi, px = [ 0 ], py = [ 0 ];
//                 while (++i < 3) {
//                     pi = points[i];
//                     px.push(pi[0]);
//                     py.push(pi[1]);
//                 }
//                 path.push(d3_svg_lineDot4(d3_svg_lineBasisBezier3, px) + "," + d3_svg_lineDot4(d3_svg_lineBasisBezier3, py));
//                 --i;
//                 while (++i < n) {
//                     pi = points[i];
//                     px.shift();
//                     px.push(pi[0]);
//                     py.shift();
//                     py.push(pi[1]);
//                     d3_svg_lineBasisBezier(path, px, py);
//                 }
//                 return path.join("");
//             }
//             function d3_svg_lineBasisClosed(points) {
//                 var path, i = -1, n = points.length, m = n + 4, pi, px = [], py = [];
//                 while (++i < 4) {
//                     pi = points[i % n];
//                     px.push(pi[0]);
//                     py.push(pi[1]);
//                 }
//                 path = [ d3_svg_lineDot4(d3_svg_lineBasisBezier3, px), ",", d3_svg_lineDot4(d3_svg_lineBasisBezier3, py) ];
//                 --i;
//                 while (++i < m) {
//                     pi = points[i % n];
//                     px.shift();
//                     px.push(pi[0]);
//                     py.shift();
//                     py.push(pi[1]);
//                     d3_svg_lineBasisBezier(path, px, py);
//                 }
//                 return path.join("");
//             }
//             function d3_svg_lineBundle(points, tension) {
//                 var n = points.length - 1;
//                 if (n) {
//                     var x0 = points[0][0], y0 = points[0][1], dx = points[n][0] - x0, dy = points[n][1] - y0, i = -1, p, t;
//                     while (++i <= n) {
//                         p = points[i];
//                         t = i / n;
//                         p[0] = tension * p[0] + (1 - tension) * (x0 + t * dx);
//                         p[1] = tension * p[1] + (1 - tension) * (y0 + t * dy);
//                     }
//                 }
//                 return d3_svg_lineBasis(points);
//             }
//             function d3_svg_lineDot4(a, b) {
//                 return a[0] * b[0] + a[1] * b[1] + a[2] * b[2] + a[3] * b[3];
//             }
//             var d3_svg_lineBasisBezier1 = [ 0, 2 / 3, 1 / 3, 0 ], d3_svg_lineBasisBezier2 = [ 0, 1 / 3, 2 / 3, 0 ], d3_svg_lineBasisBezier3 = [ 0, 1 / 6, 2 / 3, 1 / 6 ];
//             function d3_svg_lineBasisBezier(path, x, y) {
//                 path.push("C", d3_svg_lineDot4(d3_svg_lineBasisBezier1, x), ",", d3_svg_lineDot4(d3_svg_lineBasisBezier1, y), ",", d3_svg_lineDot4(d3_svg_lineBasisBezier2, x), ",", d3_svg_lineDot4(d3_svg_lineBasisBezier2, y), ",", d3_svg_lineDot4(d3_svg_lineBasisBezier3, x), ",", d3_svg_lineDot4(d3_svg_lineBasisBezier3, y));
//             }
//             function d3_svg_lineSlope(p0, p1) {
//                 return (p1[1] - p0[1]) / (p1[0] - p0[0]);
//             }
//             function d3_svg_lineFiniteDifferences(points) {
//                 var i = 0, j = points.length - 1, m = [], p0 = points[0], p1 = points[1], d = m[0] = d3_svg_lineSlope(p0, p1);
//                 while (++i < j) {
//                     m[i] = (d + (d = d3_svg_lineSlope(p0 = p1, p1 = points[i + 1]))) / 2;
//                 }
//                 m[i] = d;
//                 return m;
//             }
//             function d3_svg_lineMonotoneTangents(points) {
//                 var tangents = [], d, a, b, s, m = d3_svg_lineFiniteDifferences(points), i = -1, j = points.length - 1;
//                 while (++i < j) {
//                     d = d3_svg_lineSlope(points[i], points[i + 1]);
//                     if (abs(d) < ε) {
//                         m[i] = m[i + 1] = 0;
//                     } else {
//                         a = m[i] / d;
//                         b = m[i + 1] / d;
//                         s = a * a + b * b;
//                         if (s > 9) {
//                             s = d * 3 / Math.sqrt(s);
//                             m[i] = s * a;
//                             m[i + 1] = s * b;
//                         }
//                     }
//                 }
//                 i = -1;
//                 while (++i <= j) {
//                     s = (points[Math.min(j, i + 1)][0] - points[Math.max(0, i - 1)][0]) / (6 * (1 + m[i] * m[i]));
//                     tangents.push([ s || 0, m[i] * s || 0 ]);
//                 }
//                 return tangents;
//             }
//             function d3_svg_lineMonotone(points) {
//                 return points.length < 3 ? d3_svg_lineLinear(points) : points[0] + d3_svg_lineHermite(points, d3_svg_lineMonotoneTangents(points));
//             }
//             d3.svg.line.radial = function() {
//                 var line = d3_svg_line(d3_svg_lineRadial);
//                 line.radius = line.x, delete line.x;
//                 line.angle = line.y, delete line.y;
//                 return line;
//             };
//             function d3_svg_lineRadial(points) {
//                 var point, i = -1, n = points.length, r, a;
//                 while (++i < n) {
//                     point = points[i];
//                     r = point[0];
//                     a = point[1] + d3_svg_arcOffset;
//                     point[0] = r * Math.cos(a);
//                     point[1] = r * Math.sin(a);
//                 }
//                 return points;
//             }
//             function d3_svg_area(projection) {
//                 var x0 = d3_geom_pointX, x1 = d3_geom_pointX, y0 = 0, y1 = d3_geom_pointY, defined = d3_true, interpolate = d3_svg_lineLinear, interpolateKey = interpolate.key, interpolateReverse = interpolate, L = "L", tension = .7;
//                 function area(data) {
//                     var segments = [], points0 = [], points1 = [], i = -1, n = data.length, d, fx0 = d3_functor(x0), fy0 = d3_functor(y0), fx1 = x0 === x1 ? function() {
//                         return x;
//                     } : d3_functor(x1), fy1 = y0 === y1 ? function() {
//                         return y;
//                     } : d3_functor(y1), x, y;
//                     function segment() {
//                         segments.push("M", interpolate(projection(points1), tension), L, interpolateReverse(projection(points0.reverse()), tension), "Z");
//                     }
//                     while (++i < n) {
//                         if (defined.call(this, d = data[i], i)) {
//                             points0.push([ x = +fx0.call(this, d, i), y = +fy0.call(this, d, i) ]);
//                             points1.push([ +fx1.call(this, d, i), +fy1.call(this, d, i) ]);
//                         } else if (points0.length) {
//                             segment();
//                             points0 = [];
//                             points1 = [];
//                         }
//                     }
//                     if (points0.length) segment();
//                     return segments.length ? segments.join("") : null;
//                 }
//                 area.x = function(_) {
//                     if (!arguments.length) return x1;
//                     x0 = x1 = _;
//                     return area;
//                 };
//                 area.x0 = function(_) {
//                     if (!arguments.length) return x0;
//                     x0 = _;
//                     return area;
//                 };
//                 area.x1 = function(_) {
//                     if (!arguments.length) return x1;
//                     x1 = _;
//                     return area;
//                 };
//                 area.y = function(_) {
//                     if (!arguments.length) return y1;
//                     y0 = y1 = _;
//                     return area;
//                 };
//                 area.y0 = function(_) {
//                     if (!arguments.length) return y0;
//                     y0 = _;
//                     return area;
//                 };
//                 area.y1 = function(_) {
//                     if (!arguments.length) return y1;
//                     y1 = _;
//                     return area;
//                 };
//                 area.defined = function(_) {
//                     if (!arguments.length) return defined;
//                     defined = _;
//                     return area;
//                 };
//                 area.interpolate = function(_) {
//                     if (!arguments.length) return interpolateKey;
//                     if (typeof _ === "function") interpolateKey = interpolate = _; else interpolateKey = (interpolate = d3_svg_lineInterpolators.get(_) || d3_svg_lineLinear).key;
//                     interpolateReverse = interpolate.reverse || interpolate;
//                     L = interpolate.closed ? "M" : "L";
//                     return area;
//                 };
//                 area.tension = function(_) {
//                     if (!arguments.length) return tension;
//                     tension = _;
//                     return area;
//                 };
//                 return area;
//             }
//             d3_svg_lineStepBefore.reverse = d3_svg_lineStepAfter;
//             d3_svg_lineStepAfter.reverse = d3_svg_lineStepBefore;
//             d3.svg.area = function() {
//                 return d3_svg_area(d3_identity);
//             };
//             d3.svg.area.radial = function() {
//                 var area = d3_svg_area(d3_svg_lineRadial);
//                 area.radius = area.x, delete area.x;
//                 area.innerRadius = area.x0, delete area.x0;
//                 area.outerRadius = area.x1, delete area.x1;
//                 area.angle = area.y, delete area.y;
//                 area.startAngle = area.y0, delete area.y0;
//                 area.endAngle = area.y1, delete area.y1;
//                 return area;
//             };
//             d3.svg.chord = function() {
//                 var source = d3_source, target = d3_target, radius = d3_svg_chordRadius, startAngle = d3_svg_arcStartAngle, endAngle = d3_svg_arcEndAngle;
//                 function chord(d, i) {
//                     var s = subgroup(this, source, d, i), t = subgroup(this, target, d, i);
//                     return "M" + s.p0 + arc(s.r, s.p1, s.a1 - s.a0) + (equals(s, t) ? curve(s.r, s.p1, s.r, s.p0) : curve(s.r, s.p1, t.r, t.p0) + arc(t.r, t.p1, t.a1 - t.a0) + curve(t.r, t.p1, s.r, s.p0)) + "Z";
//                 }
//                 function subgroup(self, f, d, i) {
//                     var subgroup = f.call(self, d, i), r = radius.call(self, subgroup, i), a0 = startAngle.call(self, subgroup, i) + d3_svg_arcOffset, a1 = endAngle.call(self, subgroup, i) + d3_svg_arcOffset;
//                     return {
//                         r: r,
//                             a0: a0,
//                             a1: a1,
//                             p0: [ r * Math.cos(a0), r * Math.sin(a0) ],
//                             p1: [ r * Math.cos(a1), r * Math.sin(a1) ]
//                     };
//                 }
//                 function equals(a, b) {
//                     return a.a0 == b.a0 && a.a1 == b.a1;
//                 }
//                 function arc(r, p, a) {
//                     return "A" + r + "," + r + " 0 " + +(a > π) + ",1 " + p;
//                 }
//                 function curve(r0, p0, r1, p1) {
//                     return "Q 0,0 " + p1;
//                 }
//                 chord.radius = function(v) {
//                     if (!arguments.length) return radius;
//                     radius = d3_functor(v);
//                     return chord;
//                 };
//                 chord.source = function(v) {
//                     if (!arguments.length) return source;
//                     source = d3_functor(v);
//                     return chord;
//                 };
//                 chord.target = function(v) {
//                     if (!arguments.length) return target;
//                     target = d3_functor(v);
//                     return chord;
//                 };
//                 chord.startAngle = function(v) {
//                     if (!arguments.length) return startAngle;
//                     startAngle = d3_functor(v);
//                     return chord;
//                 };
//                 chord.endAngle = function(v) {
//                     if (!arguments.length) return endAngle;
//                     endAngle = d3_functor(v);
//                     return chord;
//                 };
//                 return chord;
//             };
//             function d3_svg_chordRadius(d) {
//                 return d.radius;
//             }
//             d3.svg.diagonal = function() {
//                 var source = d3_source, target = d3_target, projection = d3_svg_diagonalProjection;
//                 function diagonal(d, i) {
//                     var p0 = source.call(this, d, i), p3 = target.call(this, d, i), m = (p0.y + p3.y) / 2, p = [ p0, {
//                         x: p0.x,
//                             y: m
//                     }, {
//                         x: p3.x,
//                             y: m
//                     }, p3 ];
//                     p = p.map(projection);
//                     return "M" + p[0] + "C" + p[1] + " " + p[2] + " " + p[3];
//                 }
//                 diagonal.source = function(x) {
//                     if (!arguments.length) return source;
//                     source = d3_functor(x);
//                     return diagonal;
//                 };
//                 diagonal.target = function(x) {
//                     if (!arguments.length) return target;
//                     target = d3_functor(x);
//                     return diagonal;
//                 };
//                 diagonal.projection = function(x) {
//                     if (!arguments.length) return projection;
//                     projection = x;
//                     return diagonal;
//                 };
//                 return diagonal;
//             };
//             function d3_svg_diagonalProjection(d) {
//                 return [ d.x, d.y ];
//             }
//             d3.svg.diagonal.radial = function() {
//                 var diagonal = d3.svg.diagonal(), projection = d3_svg_diagonalProjection, projection_ = diagonal.projection;
//                 diagonal.projection = function(x) {
//                     return arguments.length ? projection_(d3_svg_diagonalRadialProjection(projection = x)) : projection;
//                 };
//                 return diagonal;
//             };
//             function d3_svg_diagonalRadialProjection(projection) {
//                 return function() {
//                     var d = projection.apply(this, arguments), r = d[0], a = d[1] + d3_svg_arcOffset;
//                     return [ r * Math.cos(a), r * Math.sin(a) ];
//                 };
//             }
//             d3.svg.symbol = function() {
//                 var type = d3_svg_symbolType, size = d3_svg_symbolSize;
//                 function symbol(d, i) {
//                     return (d3_svg_symbols.get(type.call(this, d, i)) || d3_svg_symbolCircle)(size.call(this, d, i));
//                 }
//                 symbol.type = function(x) {
//                     if (!arguments.length) return type;
//                     type = d3_functor(x);
//                     return symbol;
//                 };
//                 symbol.size = function(x) {
//                     if (!arguments.length) return size;
//                     size = d3_functor(x);
//                     return symbol;
//                 };
//                 return symbol;
//             };
//             function d3_svg_symbolSize() {
//                 return 64;
//             }
//             function d3_svg_symbolType() {
//                 return "circle";
//             }
//             function d3_svg_symbolCircle(size) {
//                 var r = Math.sqrt(size / π);
//                 return "M0," + r + "A" + r + "," + r + " 0 1,1 0," + -r + "A" + r + "," + r + " 0 1,1 0," + r + "Z";
//             }
//             var d3_svg_symbols = d3.map({
//                 circle: d3_svg_symbolCircle,
//                 cross: function(size) {
//                     var r = Math.sqrt(size / 5) / 2;
//                     return "M" + -3 * r + "," + -r + "H" + -r + "V" + -3 * r + "H" + r + "V" + -r + "H" + 3 * r + "V" + r + "H" + r + "V" + 3 * r + "H" + -r + "V" + r + "H" + -3 * r + "Z";
//                 },
//                 diamond: function(size) {
//                     var ry = Math.sqrt(size / (2 * d3_svg_symbolTan30)), rx = ry * d3_svg_symbolTan30;
//                     return "M0," + -ry + "L" + rx + ",0" + " 0," + ry + " " + -rx + ",0" + "Z";
//                 },
//                 square: function(size) {
//                     var r = Math.sqrt(size) / 2;
//                     return "M" + -r + "," + -r + "L" + r + "," + -r + " " + r + "," + r + " " + -r + "," + r + "Z";
//                 },
//                 "triangle-down": function(size) {
//                     var rx = Math.sqrt(size / d3_svg_symbolSqrt3), ry = rx * d3_svg_symbolSqrt3 / 2;
//                     return "M0," + ry + "L" + rx + "," + -ry + " " + -rx + "," + -ry + "Z";
//                 },
//                 "triangle-up": function(size) {
//                     var rx = Math.sqrt(size / d3_svg_symbolSqrt3), ry = rx * d3_svg_symbolSqrt3 / 2;
//                     return "M0," + -ry + "L" + rx + "," + ry + " " + -rx + "," + ry + "Z";
//                 }
//             });
//             d3.svg.symbolTypes = d3_svg_symbols.keys();
//             var d3_svg_symbolSqrt3 = Math.sqrt(3), d3_svg_symbolTan30 = Math.tan(30 * d3_radians);
//             function d3_transition(groups, id) {
//                 d3_subclass(groups, d3_transitionPrototype);
//                 groups.id = id;
//                 return groups;
//             }
//             var d3_transitionPrototype = [], d3_transitionId = 0, d3_transitionInheritId, d3_transitionInherit;
//             d3_transitionPrototype.call = d3_selectionPrototype.call;
//             d3_transitionPrototype.empty = d3_selectionPrototype.empty;
//             d3_transitionPrototype.node = d3_selectionPrototype.node;
//             d3_transitionPrototype.size = d3_selectionPrototype.size;
//             d3.transition = function(selection) {
//                 return arguments.length ? d3_transitionInheritId ? selection.transition() : selection : d3_selectionRoot.transition();
//             };
//             d3.transition.prototype = d3_transitionPrototype;
//             d3_transitionPrototype.select = function(selector) {
//                 var id = this.id, subgroups = [], subgroup, subnode, node;
//                 selector = d3_selection_selector(selector);
//                 for (var j = -1, m = this.length; ++j < m; ) {
//                     subgroups.push(subgroup = []);
//                     for (var group = this[j], i = -1, n = group.length; ++i < n; ) {
//                         if ((node = group[i]) && (subnode = selector.call(node, node.__data__, i, j))) {
//                             if ("__data__" in node) subnode.__data__ = node.__data__;
//                             d3_transitionNode(subnode, i, id, node.__transition__[id]);
//                             subgroup.push(subnode);
//                         } else {
//                             subgroup.push(null);
//                         }
//                     }
//                 }
//                 return d3_transition(subgroups, id);
//             };
//             d3_transitionPrototype.selectAll = function(selector) {
//                 var id = this.id, subgroups = [], subgroup, subnodes, node, subnode, transition;
//                 selector = d3_selection_selectorAll(selector);
//                 for (var j = -1, m = this.length; ++j < m; ) {
//                     for (var group = this[j], i = -1, n = group.length; ++i < n; ) {
//                         if (node = group[i]) {
//                             transition = node.__transition__[id];
//                             subnodes = selector.call(node, node.__data__, i, j);
//                             subgroups.push(subgroup = []);
//                             for (var k = -1, o = subnodes.length; ++k < o; ) {
//                                 if (subnode = subnodes[k]) d3_transitionNode(subnode, k, id, transition);
//                                 subgroup.push(subnode);
//                             }
//                         }
//                     }
//                 }
//                 return d3_transition(subgroups, id);
//             };
//             d3_transitionPrototype.filter = function(filter) {
//                 var subgroups = [], subgroup, group, node;
//                 if (typeof filter !== "function") filter = d3_selection_filter(filter);
//                 for (var j = 0, m = this.length; j < m; j++) {
//                     subgroups.push(subgroup = []);
//                     for (var group = this[j], i = 0, n = group.length; i < n; i++) {
//                         if ((node = group[i]) && filter.call(node, node.__data__, i)) {
//                             subgroup.push(node);
//                         }
//                     }
//                 }
//                 return d3_transition(subgroups, this.id);
//             };
//             d3_transitionPrototype.tween = function(name, tween) {
//                 var id = this.id;
//                 if (arguments.length < 2) return this.node().__transition__[id].tween.get(name);
//                 return d3_selection_each(this, tween == null ? function(node) {
//                     node.__transition__[id].tween.remove(name);
//                 } : function(node) {
//                     node.__transition__[id].tween.set(name, tween);
//                 });
//             };
//             function d3_transition_tween(groups, name, value, tween) {
//                 var id = groups.id;
//                 return d3_selection_each(groups, typeof value === "function" ? function(node, i, j) {
//                     node.__transition__[id].tween.set(name, tween(value.call(node, node.__data__, i, j)));
//                 } : (value = tween(value), function(node) {
//                     node.__transition__[id].tween.set(name, value);
//                 }));
//             }
//             d3_transitionPrototype.attr = function(nameNS, value) {
//                 if (arguments.length < 2) {
//                     for (value in nameNS) this.attr(value, nameNS[value]);
//                     return this;
//                 }
//                 var interpolate = nameNS == "transform" ? d3_interpolateTransform : d3_interpolate, name = d3.ns.qualify(nameNS);
//                 function attrNull() {
//                     this.removeAttribute(name);
//                 }
//                 function attrNullNS() {
//                     this.removeAttributeNS(name.space, name.local);
//                 }
//                 function attrTween(b) {
//                     return b == null ? attrNull : (b += "", function() {
//                         var a = this.getAttribute(name), i;
//                         return a !== b && (i = interpolate(a, b), function(t) {
//                             this.setAttribute(name, i(t));
//                         });
//                     });
//                 }
//                 function attrTweenNS(b) {
//                     return b == null ? attrNullNS : (b += "", function() {
//                         var a = this.getAttributeNS(name.space, name.local), i;
//                         return a !== b && (i = interpolate(a, b), function(t) {
//                             this.setAttributeNS(name.space, name.local, i(t));
//                         });
//                     });
//                 }
//                 return d3_transition_tween(this, "attr." + nameNS, value, name.local ? attrTweenNS : attrTween);
//             };
//             d3_transitionPrototype.attrTween = function(nameNS, tween) {
//                 var name = d3.ns.qualify(nameNS);
//                 function attrTween(d, i) {
//                     var f = tween.call(this, d, i, this.getAttribute(name));
//                     return f && function(t) {
//                         this.setAttribute(name, f(t));
//                     };
//                 }
//                 function attrTweenNS(d, i) {
//                     var f = tween.call(this, d, i, this.getAttributeNS(name.space, name.local));
//                     return f && function(t) {
//                         this.setAttributeNS(name.space, name.local, f(t));
//                     };
//                 }
//                 return this.tween("attr." + nameNS, name.local ? attrTweenNS : attrTween);
//             };
//             d3_transitionPrototype.style = function(name, value, priority) {
//                 var n = arguments.length;
//                 if (n < 3) {
//                     if (typeof name !== "string") {
//                         if (n < 2) value = "";
//                         for (priority in name) this.style(priority, name[priority], value);
//                         return this;
//                     }
//                     priority = "";
//                 }
//                 function styleNull() {
//                     this.style.removeProperty(name);
//                 }
//                 function styleString(b) {
//                     return b == null ? styleNull : (b += "", function() {
//                         var a = d3_window.getComputedStyle(this, null).getPropertyValue(name), i;
//                         return a !== b && (i = d3_interpolate(a, b), function(t) {
//                             this.style.setProperty(name, i(t), priority);
//                         });
//                     });
//                 }
//                 return d3_transition_tween(this, "style." + name, value, styleString);
//             };
//             d3_transitionPrototype.styleTween = function(name, tween, priority) {
//                 if (arguments.length < 3) priority = "";
//                 function styleTween(d, i) {
//                     var f = tween.call(this, d, i, d3_window.getComputedStyle(this, null).getPropertyValue(name));
//                     return f && function(t) {
//                         this.style.setProperty(name, f(t), priority);
//                     };
//                 }
//                 return this.tween("style." + name, styleTween);
//             };
//             d3_transitionPrototype.text = function(value) {
//                 return d3_transition_tween(this, "text", value, d3_transition_text);
//             };
//             function d3_transition_text(b) {
//                 if (b == null) b = "";
//                 return function() {
//                     this.textContent = b;
//                 };
//             }
//             d3_transitionPrototype.remove = function() {
//                 return this.each("end.transition", function() {
//                     var p;
//                     if (this.__transition__.count < 2 && (p = this.parentNode)) p.removeChild(this);
//                 });
//             };
//             d3_transitionPrototype.ease = function(value) {
//                 var id = this.id;
//                 if (arguments.length < 1) return this.node().__transition__[id].ease;
//                 if (typeof value !== "function") value = d3.ease.apply(d3, arguments);
//                 return d3_selection_each(this, function(node) {
//                     node.__transition__[id].ease = value;
//                 });
//             };
//             d3_transitionPrototype.delay = function(value) {
//                 var id = this.id;
//                 return d3_selection_each(this, typeof value === "function" ? function(node, i, j) {
//                     node.__transition__[id].delay = +value.call(node, node.__data__, i, j);
//                 } : (value = +value, function(node) {
//                     node.__transition__[id].delay = value;
//                 }));
//             };
//             d3_transitionPrototype.duration = function(value) {
//                 var id = this.id;
//                 return d3_selection_each(this, typeof value === "function" ? function(node, i, j) {
//                     node.__transition__[id].duration = Math.max(1, value.call(node, node.__data__, i, j));
//                 } : (value = Math.max(1, value), function(node) {
//                     node.__transition__[id].duration = value;
//                 }));
//             };
//             d3_transitionPrototype.each = function(type, listener) {
//                 var id = this.id;
//                 if (arguments.length < 2) {
//                     var inherit = d3_transitionInherit, inheritId = d3_transitionInheritId;
//                     d3_transitionInheritId = id;
//                     d3_selection_each(this, function(node, i, j) {
//                         d3_transitionInherit = node.__transition__[id];
//                         type.call(node, node.__data__, i, j);
//                     });
//                     d3_transitionInherit = inherit;
//                     d3_transitionInheritId = inheritId;
//                 } else {
//                     d3_selection_each(this, function(node) {
//                         var transition = node.__transition__[id];
//                         (transition.event || (transition.event = d3.dispatch("start", "end"))).on(type, listener);
//                     });
//                 }
//                 return this;
//             };
//             d3_transitionPrototype.transition = function() {
//                 var id0 = this.id, id1 = ++d3_transitionId, subgroups = [], subgroup, group, node, transition;
//                 for (var j = 0, m = this.length; j < m; j++) {
//                     subgroups.push(subgroup = []);
//                     for (var group = this[j], i = 0, n = group.length; i < n; i++) {
//                         if (node = group[i]) {
//                             transition = Object.create(node.__transition__[id0]);
//                             transition.delay += transition.duration;
//                             d3_transitionNode(node, i, id1, transition);
//                         }
//                         subgroup.push(node);
//                     }
//                 }
//                 return d3_transition(subgroups, id1);
//             };
//             function d3_transitionNode(node, i, id, inherit) {
//                 var lock = node.__transition__ || (node.__transition__ = {
//                     active: 0,
//                     count: 0
//                 }), transition = lock[id];
//                 if (!transition) {
//                     var time = inherit.time;
//                     transition = lock[id] = {
//                         tween: new d3_Map(),
//                         time: time,
//                         ease: inherit.ease,
//                         delay: inherit.delay,
//                         duration: inherit.duration
//                     };
//                     ++lock.count;
//                     d3.timer(function(elapsed) {
//                         var d = node.__data__, ease = transition.ease, delay = transition.delay, duration = transition.duration, timer = d3_timer_active, tweened = [];
//                         timer.t = delay + time;
//                         if (delay <= elapsed) return start(elapsed - delay);
//                         timer.c = start;
//                         function start(elapsed) {
//                             if (lock.active > id) return stop();
//                             lock.active = id;
//                             transition.event && transition.event.start.call(node, d, i);
//                             transition.tween.forEach(function(key, value) {
//                                 if (value = value.call(node, d, i)) {
//                                     tweened.push(value);
//                                 }
//                             });
//                             d3.timer(function() {
//                                 timer.c = tick(elapsed || 1) ? d3_true : tick;
//                                 return 1;
//                             }, 0, time);
//                         }
//                         function tick(elapsed) {
//                             if (lock.active !== id) return stop();
//                             var t = elapsed / duration, e = ease(t), n = tweened.length;
//                             while (n > 0) {
//                                 tweened[--n].call(node, e);
//                             }
//                             if (t >= 1) {
//                                 transition.event && transition.event.end.call(node, d, i);
//                                 return stop();
//                             }
//                         }
//                         function stop() {
//                             if (--lock.count) delete lock[id]; else delete node.__transition__;
//                             return 1;
//                         }
//                     }, 0, time);
//                 }
//             }
//             d3.svg.axis = function() {
//                 var scale = d3.scale.linear(), orient = d3_svg_axisDefaultOrient, innerTickSize = 6, outerTickSize = 6, tickPadding = 3, tickArguments_ = [ 10 ], tickValues = null, tickFormat_;
//                 function axis(g) {
//                     g.each(function() {
//                         var g = d3.select(this);
//                         var scale0 = this.__chart__ || scale, scale1 = this.__chart__ = scale.copy();
//                         var ticks = tickValues == null ? scale1.ticks ? scale1.ticks.apply(scale1, tickArguments_) : scale1.domain() : tickValues, tickFormat = tickFormat_ == null ? scale1.tickFormat ? scale1.tickFormat.apply(scale1, tickArguments_) : d3_identity : tickFormat_, tick = g.selectAll(".tick").data(ticks, scale1), tickEnter = tick.enter().insert("g", ".domain").attr("class", "tick").style("opacity", ε), tickExit = d3.transition(tick.exit()).style("opacity", ε).remove(), tickUpdate = d3.transition(tick).style("opacity", 1), tickTransform;
//                         var range = d3_scaleRange(scale1), path = g.selectAll(".domain").data([ 0 ]), pathUpdate = (path.enter().append("path").attr("class", "domain"),
//                         d3.transition(path));
//                         tickEnter.append("line");
//                         tickEnter.append("text");
//                         var lineEnter = tickEnter.select("line"), lineUpdate = tickUpdate.select("line"), text = tick.select("text").text(tickFormat), textEnter = tickEnter.select("text"), textUpdate = tickUpdate.select("text");
//                         switch (orient) {
//                         case "bottom":
//                             {
//                                 tickTransform = d3_svg_axisX;
//                                 lineEnter.attr("y2", innerTickSize);
//                                 textEnter.attr("y", Math.max(innerTickSize, 0) + tickPadding);
//                                 lineUpdate.attr("x2", 0).attr("y2", innerTickSize);
//                                 textUpdate.attr("x", 0).attr("y", Math.max(innerTickSize, 0) + tickPadding);
//                                 text.attr("dy", ".71em").style("text-anchor", "middle");
//                                 pathUpdate.attr("d", "M" + range[0] + "," + outerTickSize + "V0H" + range[1] + "V" + outerTickSize);
//                                 break;
//                             }
//
//                         case "top":
//                             {
//                                 tickTransform = d3_svg_axisX;
//                                 lineEnter.attr("y2", -innerTickSize);
//                                 textEnter.attr("y", -(Math.max(innerTickSize, 0) + tickPadding));
//                                 lineUpdate.attr("x2", 0).attr("y2", -innerTickSize);
//                                 textUpdate.attr("x", 0).attr("y", -(Math.max(innerTickSize, 0) + tickPadding));
//                                 text.attr("dy", "0em").style("text-anchor", "middle");
//                                 pathUpdate.attr("d", "M" + range[0] + "," + -outerTickSize + "V0H" + range[1] + "V" + -outerTickSize);
//                                 break;
//                             }
//
//                         case "left":
//                             {
//                                 tickTransform = d3_svg_axisY;
//                                 lineEnter.attr("x2", -innerTickSize);
//                                 textEnter.attr("x", -(Math.max(innerTickSize, 0) + tickPadding));
//                                 lineUpdate.attr("x2", -innerTickSize).attr("y2", 0);
//                                 textUpdate.attr("x", -(Math.max(innerTickSize, 0) + tickPadding)).attr("y", 0);
//                                 text.attr("dy", ".32em").style("text-anchor", "end");
//                                 pathUpdate.attr("d", "M" + -outerTickSize + "," + range[0] + "H0V" + range[1] + "H" + -outerTickSize);
//                                 break;
//                             }
//
//                         case "right":
//                             {
//                                 tickTransform = d3_svg_axisY;
//                                 lineEnter.attr("x2", innerTickSize);
//                                 textEnter.attr("x", Math.max(innerTickSize, 0) + tickPadding);
//                                 lineUpdate.attr("x2", innerTickSize).attr("y2", 0);
//                                 textUpdate.attr("x", Math.max(innerTickSize, 0) + tickPadding).attr("y", 0);
//                                 text.attr("dy", ".32em").style("text-anchor", "start");
//                                 pathUpdate.attr("d", "M" + outerTickSize + "," + range[0] + "H0V" + range[1] + "H" + outerTickSize);
//                                 break;
//                             }
//                         }
//                         if (scale1.rangeBand) {
//                             var dx = scale1.rangeBand() / 2, x = function(d) {
//                                 return scale1(d) + dx;
//                             };
//                             tickEnter.call(tickTransform, x);
//                             tickUpdate.call(tickTransform, x);
//                         } else {
//                             tickEnter.call(tickTransform, scale0);
//                             tickUpdate.call(tickTransform, scale1);
//                             tickExit.call(tickTransform, scale1);
//                         }
//                     });
//                 }
//                 axis.scale = function(x) {
//                     if (!arguments.length) return scale;
//                     scale = x;
//                     return axis;
//                 };
//                 axis.orient = function(x) {
//                     if (!arguments.length) return orient;
//                     orient = x in d3_svg_axisOrients ? x + "" : d3_svg_axisDefaultOrient;
//                     return axis;
//                 };
//                 axis.ticks = function() {
//                     if (!arguments.length) return tickArguments_;
//                     tickArguments_ = arguments;
//                     return axis;
//                 };
//                 axis.tickValues = function(x) {
//                     if (!arguments.length) return tickValues;
//                     tickValues = x;
//                     return axis;
//                 };
//                 axis.tickFormat = function(x) {
//                     if (!arguments.length) return tickFormat_;
//                     tickFormat_ = x;
//                     return axis;
//                 };
//                 axis.tickSize = function(x) {
//                     var n = arguments.length;
//                     if (!n) return innerTickSize;
//                     innerTickSize = +x;
//                     outerTickSize = +arguments[n - 1];
//                     return axis;
//                 };
//                 axis.innerTickSize = function(x) {
//                     if (!arguments.length) return innerTickSize;
//                     innerTickSize = +x;
//                     return axis;
//                 };
//                 axis.outerTickSize = function(x) {
//                     if (!arguments.length) return outerTickSize;
//                     outerTickSize = +x;
//                     return axis;
//                 };
//                 axis.tickPadding = function(x) {
//                     if (!arguments.length) return tickPadding;
//                     tickPadding = +x;
//                     return axis;
//                 };
//                 axis.tickSubdivide = function() {
//                     return arguments.length && axis;
//                 };
//                 return axis;
//             };
//             var d3_svg_axisDefaultOrient = "bottom", d3_svg_axisOrients = {
//                 top: 1,
//                 right: 1,
//                 bottom: 1,
//                 left: 1
//             };
//             function d3_svg_axisX(selection, x) {
//                 selection.attr("transform", function(d) {
//                     return "translate(" + x(d) + ",0)";
//                 });
//             }
//             function d3_svg_axisY(selection, y) {
//                 selection.attr("transform", function(d) {
//                     return "translate(0," + y(d) + ")";
//                 });
//             }
//             d3.svg.brush = function() {
//                 var event = d3_eventDispatch(brush, "brushstart", "brush", "brushend"), x = null, y = null, xExtent = [ 0, 0 ], yExtent = [ 0, 0 ], xExtentDomain, yExtentDomain, xClamp = true, yClamp = true, resizes = d3_svg_brushResizes[0];
//                 function brush(g) {
//                     g.each(function() {
//                         var g = d3.select(this).style("pointer-events", "all").style("-webkit-tap-highlight-color", "rgba(0,0,0,0)").on("mousedown.brush", brushstart).on("touchstart.brush", brushstart);
//                         var background = g.selectAll(".background").data([ 0 ]);
//                         background.enter().append("rect").attr("class", "background").style("visibility", "hidden").style("cursor", "crosshair");
//                         g.selectAll(".extent").data([ 0 ]).enter().append("rect").attr("class", "extent").style("cursor", "move");
//                         var resize = g.selectAll(".resize").data(resizes, d3_identity);
//                         resize.exit().remove();
//                         resize.enter().append("g").attr("class", function(d) {
//                             return "resize " + d;
//                         }).style("cursor", function(d) {
//                             return d3_svg_brushCursor[d];
//                         }).append("rect").attr("x", function(d) {
//                             return /[ew]$/.test(d) ? -3 : null;
//                         }).attr("y", function(d) {
//                             return /^[ns]/.test(d) ? -3 : null;
//                         }).attr("width", 6).attr("height", 6).style("visibility", "hidden");
//                         resize.style("display", brush.empty() ? "none" : null);
//                         var gUpdate = d3.transition(g), backgroundUpdate = d3.transition(background), range;
//                         if (x) {
//                             range = d3_scaleRange(x);
//                             backgroundUpdate.attr("x", range[0]).attr("width", range[1] - range[0]);
//                             redrawX(gUpdate);
//                         }
//                         if (y) {
//                             range = d3_scaleRange(y);
//                             backgroundUpdate.attr("y", range[0]).attr("height", range[1] - range[0]);
//                             redrawY(gUpdate);
//                         }
//                         redraw(gUpdate);
//                     });
//                 }
//                 brush.event = function(g) {
//                     g.each(function() {
//                         var event_ = event.of(this, arguments), extent1 = {
//                             x: xExtent,
//                     y: yExtent,
//                     i: xExtentDomain,
//                     j: yExtentDomain
//                         }, extent0 = this.__chart__ || extent1;
//                         this.__chart__ = extent1;
//                         if (d3_transitionInheritId) {
//                             d3.select(this).transition().each("start.brush", function() {
//                                 xExtentDomain = extent0.i;
//                                 yExtentDomain = extent0.j;
//                                 xExtent = extent0.x;
//                                 yExtent = extent0.y;
//                                 event_({
//                                     type: "brushstart"
//                                 });
//                             }).tween("brush:brush", function() {
//                                 var xi = d3_interpolateArray(xExtent, extent1.x), yi = d3_interpolateArray(yExtent, extent1.y);
//                                 xExtentDomain = yExtentDomain = null;
//                                 return function(t) {
//                                     xExtent = extent1.x = xi(t);
//                                     yExtent = extent1.y = yi(t);
//                                     event_({
//                                         type: "brush",
//                                         mode: "resize"
//                                     });
//                                 };
//                             }).each("end.brush", function() {
//                                 xExtentDomain = extent1.i;
//                                 yExtentDomain = extent1.j;
//                                 event_({
//                                     type: "brush",
//                                     mode: "resize"
//                                 });
//                                 event_({
//                                     type: "brushend"
//                                 });
//                             });
//                         } else {
//                             event_({
//                                 type: "brushstart"
//                             });
//                             event_({
//                                 type: "brush",
//                                 mode: "resize"
//                             });
//                             event_({
//                                 type: "brushend"
//                             });
//                         }
//                     });
//                 };
//                 function redraw(g) {
//                     g.selectAll(".resize").attr("transform", function(d) {
//                         return "translate(" + xExtent[+/e$/.test(d)] + "," + yExtent[+/^s/.test(d)] + ")";
//                     });
//                 }
//                 function redrawX(g) {
//                     g.select(".extent").attr("x", xExtent[0]);
//                     g.selectAll(".extent,.n>rect,.s>rect").attr("width", xExtent[1] - xExtent[0]);
//                 }
//                 function redrawY(g) {
//                     g.select(".extent").attr("y", yExtent[0]);
//                     g.selectAll(".extent,.e>rect,.w>rect").attr("height", yExtent[1] - yExtent[0]);
//                 }
//                 function brushstart() {
//                     var target = this, eventTarget = d3.select(d3.event.target), event_ = event.of(target, arguments), g = d3.select(target), resizing = eventTarget.datum(), resizingX = !/^(n|s)$/.test(resizing) && x, resizingY = !/^(e|w)$/.test(resizing) && y, dragging = eventTarget.classed("extent"), dragRestore = d3_event_dragSuppress(), center, origin = d3.mouse(target), offset;
//                     var w = d3.select(d3_window).on("keydown.brush", keydown).on("keyup.brush", keyup);
//                     if (d3.event.changedTouches) {
//                         w.on("touchmove.brush", brushmove).on("touchend.brush", brushend);
//                     } else {
//                         w.on("mousemove.brush", brushmove).on("mouseup.brush", brushend);
//                     }
//                     g.interrupt().selectAll("*").interrupt();
//                     if (dragging) {
//                         origin[0] = xExtent[0] - origin[0];
//                         origin[1] = yExtent[0] - origin[1];
//                     } else if (resizing) {
//                         var ex = +/w$/.test(resizing), ey = +/^n/.test(resizing);
//                         offset = [ xExtent[1 - ex] - origin[0], yExtent[1 - ey] - origin[1] ];
//                         origin[0] = xExtent[ex];
//                         origin[1] = yExtent[ey];
//                     } else if (d3.event.altKey) center = origin.slice();
//                     g.style("pointer-events", "none").selectAll(".resize").style("display", null);
//                     d3.select("body").style("cursor", eventTarget.style("cursor"));
//                     event_({
//                         type: "brushstart"
//                     });
//                     brushmove();
//                     function keydown() {
//                         if (d3.event.keyCode == 32) {
//                             if (!dragging) {
//                                 center = null;
//                                 origin[0] -= xExtent[1];
//                                 origin[1] -= yExtent[1];
//                                 dragging = 2;
//                             }
//                             d3_eventPreventDefault();
//                         }
//                     }
//                     function keyup() {
//                         if (d3.event.keyCode == 32 && dragging == 2) {
//                             origin[0] += xExtent[1];
//                             origin[1] += yExtent[1];
//                             dragging = 0;
//                             d3_eventPreventDefault();
//                         }
//                     }
//                     function brushmove() {
//                         var point = d3.mouse(target), moved = false;
//                         if (offset) {
//                             point[0] += offset[0];
//                             point[1] += offset[1];
//                         }
//                         if (!dragging) {
//                             if (d3.event.altKey) {
//                                 if (!center) center = [ (xExtent[0] + xExtent[1]) / 2, (yExtent[0] + yExtent[1]) / 2 ];
//                                 origin[0] = xExtent[+(point[0] < center[0])];
//                                 origin[1] = yExtent[+(point[1] < center[1])];
//                             } else center = null;
//                         }
//                         if (resizingX && move1(point, x, 0)) {
//                             redrawX(g);
//                             moved = true;
//                         }
//                         if (resizingY && move1(point, y, 1)) {
//                             redrawY(g);
//                             moved = true;
//                         }
//                         if (moved) {
//                             redraw(g);
//                             event_({
//                                 type: "brush",
//                                 mode: dragging ? "move" : "resize"
//                             });
//                         }
//                     }
//                     function move1(point, scale, i) {
//                         var range = d3_scaleRange(scale), r0 = range[0], r1 = range[1], position = origin[i], extent = i ? yExtent : xExtent, size = extent[1] - extent[0], min, max;
//                         if (dragging) {
//                             r0 -= position;
//                             r1 -= size + position;
//                         }
//                         min = (i ? yClamp : xClamp) ? Math.max(r0, Math.min(r1, point[i])) : point[i];
//                         if (dragging) {
//                             max = (min += position) + size;
//                         } else {
//                             if (center) position = Math.max(r0, Math.min(r1, 2 * center[i] - min));
//                             if (position < min) {
//                                 max = min;
//                                 min = position;
//                             } else {
//                                 max = position;
//                             }
//                         }
//                         if (extent[0] != min || extent[1] != max) {
//                             if (i) yExtentDomain = null; else xExtentDomain = null;
//                             extent[0] = min;
//                             extent[1] = max;
//                             return true;
//                         }
//                     }
//                     function brushend() {
//                         brushmove();
//                         g.style("pointer-events", "all").selectAll(".resize").style("display", brush.empty() ? "none" : null);
//                         d3.select("body").style("cursor", null);
//                         w.on("mousemove.brush", null).on("mouseup.brush", null).on("touchmove.brush", null).on("touchend.brush", null).on("keydown.brush", null).on("keyup.brush", null);
//                         dragRestore();
//                         event_({
//                             type: "brushend"
//                         });
//                     }
//                 }
//                 brush.x = function(z) {
//                     if (!arguments.length) return x;
//                     x = z;
//                     resizes = d3_svg_brushResizes[!x << 1 | !y];
//                     return brush;
//                 };
//                 brush.y = function(z) {
//                     if (!arguments.length) return y;
//                     y = z;
//                     resizes = d3_svg_brushResizes[!x << 1 | !y];
//                     return brush;
//                 };
//                 brush.clamp = function(z) {
//                     if (!arguments.length) return x && y ? [ xClamp, yClamp ] : x ? xClamp : y ? yClamp : null;
//                     if (x && y) xClamp = !!z[0], yClamp = !!z[1]; else if (x) xClamp = !!z; else if (y) yClamp = !!z;
//                     return brush;
//                 };
//                 brush.extent = function(z) {
//                     var x0, x1, y0, y1, t;
//                     if (!arguments.length) {
//                         if (x) {
//                             if (xExtentDomain) {
//                                 x0 = xExtentDomain[0], x1 = xExtentDomain[1];
//                             } else {
//                                 x0 = xExtent[0], x1 = xExtent[1];
//                                 if (x.invert) x0 = x.invert(x0), x1 = x.invert(x1);
//                                 if (x1 < x0) t = x0, x0 = x1, x1 = t;
//                             }
//                         }
//                         if (y) {
//                             if (yExtentDomain) {
//                                 y0 = yExtentDomain[0], y1 = yExtentDomain[1];
//                             } else {
//                                 y0 = yExtent[0], y1 = yExtent[1];
//                                 if (y.invert) y0 = y.invert(y0), y1 = y.invert(y1);
//                                 if (y1 < y0) t = y0, y0 = y1, y1 = t;
//                             }
//                         }
//                         return x && y ? [ [ x0, y0 ], [ x1, y1 ] ] : x ? [ x0, x1 ] : y && [ y0, y1 ];
//                     }
//                     if (x) {
//                         x0 = z[0], x1 = z[1];
//                         if (y) x0 = x0[0], x1 = x1[0];
//                         xExtentDomain = [ x0, x1 ];
//                         if (x.invert) x0 = x(x0), x1 = x(x1);
//                         if (x1 < x0) t = x0, x0 = x1, x1 = t;
//                         if (x0 != xExtent[0] || x1 != xExtent[1]) xExtent = [ x0, x1 ];
//                     }
//                     if (y) {
//                         y0 = z[0], y1 = z[1];
//                         if (x) y0 = y0[1], y1 = y1[1];
//                         yExtentDomain = [ y0, y1 ];
//                         if (y.invert) y0 = y(y0), y1 = y(y1);
//                         if (y1 < y0) t = y0, y0 = y1, y1 = t;
//                         if (y0 != yExtent[0] || y1 != yExtent[1]) yExtent = [ y0, y1 ];
//                     }
//                     return brush;
//                 };
//                 brush.clear = function() {
//                     if (!brush.empty()) {
//                         xExtent = [ 0, 0 ], yExtent = [ 0, 0 ];
//                         xExtentDomain = yExtentDomain = null;
//                     }
//                     return brush;
//                 };
//                 brush.empty = function() {
//                     return !!x && xExtent[0] == xExtent[1] || !!y && yExtent[0] == yExtent[1];
//                 };
//                 return d3.rebind(brush, event, "on");
//             };
//             var d3_svg_brushCursor = {
//                 n: "ns-resize",
//                 e: "ew-resize",
//                 s: "ns-resize",
//                 w: "ew-resize",
//                 nw: "nwse-resize",
//                 ne: "nesw-resize",
//                 se: "nwse-resize",
//                 sw: "nesw-resize"
//             };
//             var d3_svg_brushResizes = [ [ "n", "e", "s", "w", "nw", "ne", "se", "sw" ], [ "e", "w" ], [ "n", "s" ], [] ];
//             var d3_time = d3.time = {}, d3_date = Date, d3_time_daySymbols = [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" ];
//             function d3_date_utc() {
//                 this._ = new Date(arguments.length > 1 ? Date.UTC.apply(this, arguments) : arguments[0]);
//             }
//             d3_date_utc.prototype = {
//                 getDate: function() {
//                     return this._.getUTCDate();
//                 },
//                 getDay: function() {
//                     return this._.getUTCDay();
//                 },
//                 getFullYear: function() {
//                     return this._.getUTCFullYear();
//                 },
//                 getHours: function() {
//                     return this._.getUTCHours();
//                 },
//                 getMilliseconds: function() {
//                     return this._.getUTCMilliseconds();
//                 },
//                 getMinutes: function() {
//                     return this._.getUTCMinutes();
//                 },
//                 getMonth: function() {
//                     return this._.getUTCMonth();
//                 },
//                 getSeconds: function() {
//                     return this._.getUTCSeconds();
//                 },
//                 getTime: function() {
//                     return this._.getTime();
//                 },
//                 getTimezoneOffset: function() {
//                     return 0;
//                 },
//                 valueOf: function() {
//                     return this._.valueOf();
//                 },
//                 setDate: function() {
//                     d3_time_prototype.setUTCDate.apply(this._, arguments);
//                 },
//                 setDay: function() {
//                     d3_time_prototype.setUTCDay.apply(this._, arguments);
//                 },
//                 setFullYear: function() {
//                     d3_time_prototype.setUTCFullYear.apply(this._, arguments);
//                 },
//                 setHours: function() {
//                     d3_time_prototype.setUTCHours.apply(this._, arguments);
//                 },
//                 setMilliseconds: function() {
//                     d3_time_prototype.setUTCMilliseconds.apply(this._, arguments);
//                 },
//                 setMinutes: function() {
//                     d3_time_prototype.setUTCMinutes.apply(this._, arguments);
//                 },
//                 setMonth: function() {
//                     d3_time_prototype.setUTCMonth.apply(this._, arguments);
//                 },
//                 setSeconds: function() {
//                     d3_time_prototype.setUTCSeconds.apply(this._, arguments);
//                 },
//                 setTime: function() {
//                     d3_time_prototype.setTime.apply(this._, arguments);
//                 }
//             };
//             var d3_time_prototype = Date.prototype;
//             var d3_time_formatDateTime = "%a %b %e %X %Y", d3_time_formatDate = "%m/%d/%Y", d3_time_formatTime = "%H:%M:%S";
//             var d3_time_days = [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" ], d3_time_dayAbbreviations = [ "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" ], d3_time_months = [ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" ], d3_time_monthAbbreviations = [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ];
//             function d3_time_interval(local, step, number) {
//                 function round(date) {
//                     var d0 = local(date), d1 = offset(d0, 1);
//                     return date - d0 < d1 - date ? d0 : d1;
//                 }
//                 function ceil(date) {
//                     step(date = local(new d3_date(date - 1)), 1);
//                     return date;
//                 }
//                 function offset(date, k) {
//                     step(date = new d3_date(+date), k);
//                     return date;
//                 }
//                 function range(t0, t1, dt) {
//                     var time = ceil(t0), times = [];
//                     if (dt > 1) {
//                         while (time < t1) {
//                             if (!(number(time) % dt)) times.push(new Date(+time));
//                             step(time, 1);
//                         }
//                     } else {
//                         while (time < t1) times.push(new Date(+time)), step(time, 1);
//                     }
//                     return times;
//                 }
//                 function range_utc(t0, t1, dt) {
//                     try {
//                         d3_date = d3_date_utc;
//                         var utc = new d3_date_utc();
//                         utc._ = t0;
//                         return range(utc, t1, dt);
//                     } finally {
//                         d3_date = Date;
//                     }
//                 }
//                 local.floor = local;
//                 local.round = round;
//                 local.ceil = ceil;
//                 local.offset = offset;
//                 local.range = range;
//                 var utc = local.utc = d3_time_interval_utc(local);
//                 utc.floor = utc;
//                 utc.round = d3_time_interval_utc(round);
//                 utc.ceil = d3_time_interval_utc(ceil);
//                 utc.offset = d3_time_interval_utc(offset);
//                 utc.range = range_utc;
//                 return local;
//             }
//             function d3_time_interval_utc(method) {
//                 return function(date, k) {
//                     try {
//                         d3_date = d3_date_utc;
//                         var utc = new d3_date_utc();
//                         utc._ = date;
//                         return method(utc, k)._;
//                     } finally {
//                         d3_date = Date;
//                     }
//                 };
//             }
//             d3_time.year = d3_time_interval(function(date) {
//                 date = d3_time.day(date);
//                 date.setMonth(0, 1);
//                 return date;
//             }, function(date, offset) {
//                 date.setFullYear(date.getFullYear() + offset);
//             }, function(date) {
//                 return date.getFullYear();
//             });
//             d3_time.years = d3_time.year.range;
//             d3_time.years.utc = d3_time.year.utc.range;
//             d3_time.day = d3_time_interval(function(date) {
//                 var day = new d3_date(2e3, 0);
//                 day.setFullYear(date.getFullYear(), date.getMonth(), date.getDate());
//                 return day;
//             }, function(date, offset) {
//                 date.setDate(date.getDate() + offset);
//             }, function(date) {
//                 return date.getDate() - 1;
//             });
//             d3_time.days = d3_time.day.range;
//             d3_time.days.utc = d3_time.day.utc.range;
//             d3_time.dayOfYear = function(date) {
//                 var year = d3_time.year(date);
//                 return Math.floor((date - year - (date.getTimezoneOffset() - year.getTimezoneOffset()) * 6e4) / 864e5);
//             };
//             d3_time_daySymbols.forEach(function(day, i) {
//                 day = day.toLowerCase();
//                 i = 7 - i;
//                 var interval = d3_time[day] = d3_time_interval(function(date) {
//                     (date = d3_time.day(date)).setDate(date.getDate() - (date.getDay() + i) % 7);
//                     return date;
//                 }, function(date, offset) {
//                     date.setDate(date.getDate() + Math.floor(offset) * 7);
//                 }, function(date) {
//                     var day = d3_time.year(date).getDay();
//                     return Math.floor((d3_time.dayOfYear(date) + (day + i) % 7) / 7) - (day !== i);
//                 });
//                 d3_time[day + "s"] = interval.range;
//                 d3_time[day + "s"].utc = interval.utc.range;
//                 d3_time[day + "OfYear"] = function(date) {
//                     var day = d3_time.year(date).getDay();
//                     return Math.floor((d3_time.dayOfYear(date) + (day + i) % 7) / 7);
//                 };
//             });
//             d3_time.week = d3_time.sunday;
//             d3_time.weeks = d3_time.sunday.range;
//             d3_time.weeks.utc = d3_time.sunday.utc.range;
//             d3_time.weekOfYear = d3_time.sundayOfYear;
//             d3_time.format = d3_time_format;
//             function d3_time_format(template) {
//                 var n = template.length;
//                 function format(date) {
//                     var string = [], i = -1, j = 0, c, p, f;
//                     while (++i < n) {
//                         if (template.charCodeAt(i) === 37) {
//                             string.push(template.substring(j, i));
//                             if ((p = d3_time_formatPads[c = template.charAt(++i)]) != null) c = template.charAt(++i);
//                             if (f = d3_time_formats[c]) c = f(date, p == null ? c === "e" ? " " : "0" : p);
//                             string.push(c);
//                             j = i + 1;
//                         }
//                     }
//                     string.push(template.substring(j, i));
//                     return string.join("");
//                 }
//                 format.parse = function(string) {
//                     var d = {
//                         y: 1900,
//                         m: 0,
//                         d: 1,
//                         H: 0,
//                         M: 0,
//                         S: 0,
//                         L: 0,
//                         Z: null
//                     }, i = d3_time_parse(d, template, string, 0);
//                     if (i != string.length) return null;
//                     if ("p" in d) d.H = d.H % 12 + d.p * 12;
//                     var localZ = d.Z != null && d3_date !== d3_date_utc, date = new (localZ ? d3_date_utc : d3_date)();
//                     if ("j" in d) date.setFullYear(d.y, 0, d.j); else if ("w" in d && ("W" in d || "U" in d)) {
//                         date.setFullYear(d.y, 0, 1);
//                         date.setFullYear(d.y, 0, "W" in d ? (d.w + 6) % 7 + d.W * 7 - (date.getDay() + 5) % 7 : d.w + d.U * 7 - (date.getDay() + 6) % 7);
//                     } else date.setFullYear(d.y, d.m, d.d);
//                     date.setHours(d.H + Math.floor(d.Z / 100), d.M + d.Z % 100, d.S, d.L);
//                     return localZ ? date._ : date;
//                 };
//                 format.toString = function() {
//                     return template;
//                 };
//                 return format;
//             }
//             function d3_time_parse(date, template, string, j) {
//                 var c, p, t, i = 0, n = template.length, m = string.length;
//                 while (i < n) {
//                     if (j >= m) return -1;
//                     c = template.charCodeAt(i++);
//                     if (c === 37) {
//                         t = template.charAt(i++);
//                         p = d3_time_parsers[t in d3_time_formatPads ? template.charAt(i++) : t];
//                         if (!p || (j = p(date, string, j)) < 0) return -1;
//                     } else if (c != string.charCodeAt(j++)) {
//                         return -1;
//                     }
//                 }
//                 return j;
//             }
//             function d3_time_formatRe(names) {
//                 return new RegExp("^(?:" + names.map(d3.requote).join("|") + ")", "i");
//             }
//             function d3_time_formatLookup(names) {
//                 var map = new d3_Map(), i = -1, n = names.length;
//                 while (++i < n) map.set(names[i].toLowerCase(), i);
//                 return map;
//             }
//             function d3_time_formatPad(value, fill, width) {
//                 var sign = value < 0 ? "-" : "", string = (sign ? -value : value) + "", length = string.length;
//                 return sign + (length < width ? new Array(width - length + 1).join(fill) + string : string);
//             }
//             var d3_time_dayRe = d3_time_formatRe(d3_time_days), d3_time_dayLookup = d3_time_formatLookup(d3_time_days), d3_time_dayAbbrevRe = d3_time_formatRe(d3_time_dayAbbreviations), d3_time_dayAbbrevLookup = d3_time_formatLookup(d3_time_dayAbbreviations), d3_time_monthRe = d3_time_formatRe(d3_time_months), d3_time_monthLookup = d3_time_formatLookup(d3_time_months), d3_time_monthAbbrevRe = d3_time_formatRe(d3_time_monthAbbreviations), d3_time_monthAbbrevLookup = d3_time_formatLookup(d3_time_monthAbbreviations), d3_time_percentRe = /^%/;
//             var d3_time_formatPads = {
//                 "-": "",
//                 _: " ",
//                 "0": "0"
//             };
//             var d3_time_formats = {
//                 a: function(d) {
//                     return d3_time_dayAbbreviations[d.getDay()];
//                 },
//                 A: function(d) {
//                     return d3_time_days[d.getDay()];
//                 },
//                 b: function(d) {
//                     return d3_time_monthAbbreviations[d.getMonth()];
//                 },
//                 B: function(d) {
//                     return d3_time_months[d.getMonth()];
//                 },
//                 c: d3_time_format(d3_time_formatDateTime),
//                 d: function(d, p) {
//                     return d3_time_formatPad(d.getDate(), p, 2);
//                 },
//                 e: function(d, p) {
//                     return d3_time_formatPad(d.getDate(), p, 2);
//                 },
//                 H: function(d, p) {
//                     return d3_time_formatPad(d.getHours(), p, 2);
//                 },
//                 I: function(d, p) {
//                     return d3_time_formatPad(d.getHours() % 12 || 12, p, 2);
//                 },
//                 j: function(d, p) {
//                     return d3_time_formatPad(1 + d3_time.dayOfYear(d), p, 3);
//                 },
//                 L: function(d, p) {
//                     return d3_time_formatPad(d.getMilliseconds(), p, 3);
//                 },
//                 m: function(d, p) {
//                     return d3_time_formatPad(d.getMonth() + 1, p, 2);
//                 },
//                 M: function(d, p) {
//                     return d3_time_formatPad(d.getMinutes(), p, 2);
//                 },
//                 p: function(d) {
//                     return d.getHours() >= 12 ? "PM" : "AM";
//                 },
//                 S: function(d, p) {
//                     return d3_time_formatPad(d.getSeconds(), p, 2);
//                 },
//                 U: function(d, p) {
//                     return d3_time_formatPad(d3_time.sundayOfYear(d), p, 2);
//                 },
//                 w: function(d) {
//                     return d.getDay();
//                 },
//                 W: function(d, p) {
//                     return d3_time_formatPad(d3_time.mondayOfYear(d), p, 2);
//                 },
//                 x: d3_time_format(d3_time_formatDate),
//                 X: d3_time_format(d3_time_formatTime),
//                 y: function(d, p) {
//                     return d3_time_formatPad(d.getFullYear() % 100, p, 2);
//                 },
//                 Y: function(d, p) {
//                     return d3_time_formatPad(d.getFullYear() % 1e4, p, 4);
//                 },
//                 Z: d3_time_zone,
//                 "%": function() {
//                     return "%";
//                 }
//             };
//             var d3_time_parsers = {
//                 a: d3_time_parseWeekdayAbbrev,
//                 A: d3_time_parseWeekday,
//                 b: d3_time_parseMonthAbbrev,
//                 B: d3_time_parseMonth,
//                 c: d3_time_parseLocaleFull,
//                 d: d3_time_parseDay,
//                 e: d3_time_parseDay,
//                 H: d3_time_parseHour24,
//                 I: d3_time_parseHour24,
//                 j: d3_time_parseDayOfYear,
//                 L: d3_time_parseMilliseconds,
//                 m: d3_time_parseMonthNumber,
//                 M: d3_time_parseMinutes,
//                 p: d3_time_parseAmPm,
//                 S: d3_time_parseSeconds,
//                 U: d3_time_parseWeekNumberSunday,
//                 w: d3_time_parseWeekdayNumber,
//                 W: d3_time_parseWeekNumberMonday,
//                 x: d3_time_parseLocaleDate,
//                 X: d3_time_parseLocaleTime,
//                 y: d3_time_parseYear,
//                 Y: d3_time_parseFullYear,
//                 Z: d3_time_parseZone,
//                 "%": d3_time_parseLiteralPercent
//             };
//             function d3_time_parseWeekdayAbbrev(date, string, i) {
//                 d3_time_dayAbbrevRe.lastIndex = 0;
//                 var n = d3_time_dayAbbrevRe.exec(string.substring(i));
//                 return n ? (date.w = d3_time_dayAbbrevLookup.get(n[0].toLowerCase()), i + n[0].length) : -1;
//             }
//             function d3_time_parseWeekday(date, string, i) {
//                 d3_time_dayRe.lastIndex = 0;
//                 var n = d3_time_dayRe.exec(string.substring(i));
//                 return n ? (date.w = d3_time_dayLookup.get(n[0].toLowerCase()), i + n[0].length) : -1;
//             }
//             function d3_time_parseWeekdayNumber(date, string, i) {
//                 d3_time_numberRe.lastIndex = 0;
//                 var n = d3_time_numberRe.exec(string.substring(i, i + 1));
//                 return n ? (date.w = +n[0], i + n[0].length) : -1;
//             }
//             function d3_time_parseWeekNumberSunday(date, string, i) {
//                 d3_time_numberRe.lastIndex = 0;
//                 var n = d3_time_numberRe.exec(string.substring(i));
//                 return n ? (date.U = +n[0], i + n[0].length) : -1;
//             }
//             function d3_time_parseWeekNumberMonday(date, string, i) {
//                 d3_time_numberRe.lastIndex = 0;
//                 var n = d3_time_numberRe.exec(string.substring(i));
//                 return n ? (date.W = +n[0], i + n[0].length) : -1;
//             }
//             function d3_time_parseMonthAbbrev(date, string, i) {
//                 d3_time_monthAbbrevRe.lastIndex = 0;
//                 var n = d3_time_monthAbbrevRe.exec(string.substring(i));
//                 return n ? (date.m = d3_time_monthAbbrevLookup.get(n[0].toLowerCase()), i + n[0].length) : -1;
//             }
//             function d3_time_parseMonth(date, string, i) {
//                 d3_time_monthRe.lastIndex = 0;
//                 var n = d3_time_monthRe.exec(string.substring(i));
//                 return n ? (date.m = d3_time_monthLookup.get(n[0].toLowerCase()), i + n[0].length) : -1;
//             }
//             function d3_time_parseLocaleFull(date, string, i) {
//                 return d3_time_parse(date, d3_time_formats.c.toString(), string, i);
//             }
//             function d3_time_parseLocaleDate(date, string, i) {
//                 return d3_time_parse(date, d3_time_formats.x.toString(), string, i);
//             }
//             function d3_time_parseLocaleTime(date, string, i) {
//                 return d3_time_parse(date, d3_time_formats.X.toString(), string, i);
//             }
//             function d3_time_parseFullYear(date, string, i) {
//                 d3_time_numberRe.lastIndex = 0;
//                 var n = d3_time_numberRe.exec(string.substring(i, i + 4));
//                 return n ? (date.y = +n[0], i + n[0].length) : -1;
//             }
//             function d3_time_parseYear(date, string, i) {
//                 d3_time_numberRe.lastIndex = 0;
//                 var n = d3_time_numberRe.exec(string.substring(i, i + 2));
//                 return n ? (date.y = d3_time_expandYear(+n[0]), i + n[0].length) : -1;
//             }
//             function d3_time_parseZone(date, string, i) {
//                 return /^[+-]\d{4}$/.test(string = string.substring(i, i + 5)) ? (date.Z = +string,
//                     i + 5) : -1;
//             }
//             function d3_time_expandYear(d) {
//                 return d + (d > 68 ? 1900 : 2e3);
//             }
//             function d3_time_parseMonthNumber(date, string, i) {
//                 d3_time_numberRe.lastIndex = 0;
//                 var n = d3_time_numberRe.exec(string.substring(i, i + 2));
//                 return n ? (date.m = n[0] - 1, i + n[0].length) : -1;
//             }
//             function d3_time_parseDay(date, string, i) {
//                 d3_time_numberRe.lastIndex = 0;
//                 var n = d3_time_numberRe.exec(string.substring(i, i + 2));
//                 return n ? (date.d = +n[0], i + n[0].length) : -1;
//             }
//             function d3_time_parseDayOfYear(date, string, i) {
//                 d3_time_numberRe.lastIndex = 0;
//                 var n = d3_time_numberRe.exec(string.substring(i, i + 3));
//                 return n ? (date.j = +n[0], i + n[0].length) : -1;
//             }
//             function d3_time_parseHour24(date, string, i) {
//                 d3_time_numberRe.lastIndex = 0;
//                 var n = d3_time_numberRe.exec(string.substring(i, i + 2));
//                 return n ? (date.H = +n[0], i + n[0].length) : -1;
//             }
//             function d3_time_parseMinutes(date, string, i) {
//                 d3_time_numberRe.lastIndex = 0;
//                 var n = d3_time_numberRe.exec(string.substring(i, i + 2));
//                 return n ? (date.M = +n[0], i + n[0].length) : -1;
//             }
//             function d3_time_parseSeconds(date, string, i) {
//                 d3_time_numberRe.lastIndex = 0;
//                 var n = d3_time_numberRe.exec(string.substring(i, i + 2));
//                 return n ? (date.S = +n[0], i + n[0].length) : -1;
//             }
//             function d3_time_parseMilliseconds(date, string, i) {
//                 d3_time_numberRe.lastIndex = 0;
//                 var n = d3_time_numberRe.exec(string.substring(i, i + 3));
//                 return n ? (date.L = +n[0], i + n[0].length) : -1;
//             }
//             var d3_time_numberRe = /^\s*\d+/;
//             function d3_time_parseAmPm(date, string, i) {
//                 var n = d3_time_amPmLookup.get(string.substring(i, i += 2).toLowerCase());
//                 return n == null ? -1 : (date.p = n, i);
//             }
//             var d3_time_amPmLookup = d3.map({
//                 am: 0,
//                 pm: 1
//             });
//             function d3_time_zone(d) {
//                 var z = d.getTimezoneOffset(), zs = z > 0 ? "-" : "+", zh = ~~(abs(z) / 60), zm = abs(z) % 60;
//                 return zs + d3_time_formatPad(zh, "0", 2) + d3_time_formatPad(zm, "0", 2);
//             }
//             function d3_time_parseLiteralPercent(date, string, i) {
//                 d3_time_percentRe.lastIndex = 0;
//                 var n = d3_time_percentRe.exec(string.substring(i, i + 1));
//                 return n ? i + n[0].length : -1;
//             }
//             d3_time_format.utc = d3_time_formatUtc;
//             function d3_time_formatUtc(template) {
//                 var local = d3_time_format(template);
//                 function format(date) {
//                     try {
//                         d3_date = d3_date_utc;
//                         var utc = new d3_date();
//                         utc._ = date;
//                         return local(utc);
//                     } finally {
//                         d3_date = Date;
//                     }
//                 }
//                 format.parse = function(string) {
//                     try {
//                         d3_date = d3_date_utc;
//                         var date = local.parse(string);
//                         return date && date._;
//                     } finally {
//                         d3_date = Date;
//                     }
//                 };
//                 format.toString = local.toString;
//                 return format;
//             }
//             var d3_time_formatIso = d3_time_formatUtc("%Y-%m-%dT%H:%M:%S.%LZ");
//             d3_time_format.iso = Date.prototype.toISOString && +new Date("2000-01-01T00:00:00.000Z") ? d3_time_formatIsoNative : d3_time_formatIso;
//             function d3_time_formatIsoNative(date) {
//                 return date.toISOString();
//             }
//             d3_time_formatIsoNative.parse = function(string) {
//                 var date = new Date(string);
//                 return isNaN(date) ? null : date;
//             };
//             d3_time_formatIsoNative.toString = d3_time_formatIso.toString;
//             d3_time.second = d3_time_interval(function(date) {
//                 return new d3_date(Math.floor(date / 1e3) * 1e3);
//             }, function(date, offset) {
//                 date.setTime(date.getTime() + Math.floor(offset) * 1e3);
//             }, function(date) {
//                 return date.getSeconds();
//             });
//             d3_time.seconds = d3_time.second.range;
//             d3_time.seconds.utc = d3_time.second.utc.range;
//             d3_time.minute = d3_time_interval(function(date) {
//                 return new d3_date(Math.floor(date / 6e4) * 6e4);
//             }, function(date, offset) {
//                 date.setTime(date.getTime() + Math.floor(offset) * 6e4);
//             }, function(date) {
//                 return date.getMinutes();
//             });
//             d3_time.minutes = d3_time.minute.range;
//             d3_time.minutes.utc = d3_time.minute.utc.range;
//             d3_time.hour = d3_time_interval(function(date) {
//                 var timezone = date.getTimezoneOffset() / 60;
//                 return new d3_date((Math.floor(date / 36e5 - timezone) + timezone) * 36e5);
//             }, function(date, offset) {
//                 date.setTime(date.getTime() + Math.floor(offset) * 36e5);
//             }, function(date) {
//                 return date.getHours();
//             });
//             d3_time.hours = d3_time.hour.range;
//             d3_time.hours.utc = d3_time.hour.utc.range;
//             d3_time.month = d3_time_interval(function(date) {
//                 date = d3_time.day(date);
//                 date.setDate(1);
//                 return date;
//             }, function(date, offset) {
//                 date.setMonth(date.getMonth() + offset);
//             }, function(date) {
//                 return date.getMonth();
//             });
//             d3_time.months = d3_time.month.range;
//             d3_time.months.utc = d3_time.month.utc.range;
//             function d3_time_scale(linear, methods, format) {
//                 function scale(x) {
//                     return linear(x);
//                 }
//                 scale.invert = function(x) {
//                     return d3_time_scaleDate(linear.invert(x));
//                 };
//                 scale.domain = function(x) {
//                     if (!arguments.length) return linear.domain().map(d3_time_scaleDate);
//                     linear.domain(x);
//                     return scale;
//                 };
//                 function tickMethod(extent, count) {
//                     var span = extent[1] - extent[0], target = span / count, i = d3.bisect(d3_time_scaleSteps, target);
//                     return i == d3_time_scaleSteps.length ? [ methods.year, d3_scale_linearTickRange(extent.map(function(d) {
//                         return d / 31536e6;
//                     }), count)[2] ] : !i ? [ d3_time_scaleMilliseconds, d3_scale_linearTickRange(extent, count)[2] ] : methods[target / d3_time_scaleSteps[i - 1] < d3_time_scaleSteps[i] / target ? i - 1 : i];
//                 }
//                 scale.nice = function(interval, skip) {
//                     var domain = scale.domain(), extent = d3_scaleExtent(domain), method = interval == null ? tickMethod(extent, 10) : typeof interval === "number" && tickMethod(extent, interval);
//                     if (method) interval = method[0], skip = method[1];
//                     function skipped(date) {
//                         return !isNaN(date) && !interval.range(date, d3_time_scaleDate(+date + 1), skip).length;
//                     }
//                     return scale.domain(d3_scale_nice(domain, skip > 1 ? {
//                         floor: function(date) {
//                             while (skipped(date = interval.floor(date))) date = d3_time_scaleDate(date - 1);
//                             return date;
//                         },
//                            ceil: function(date) {
//                                while (skipped(date = interval.ceil(date))) date = d3_time_scaleDate(+date + 1);
//                                return date;
//                            }
//                     } : interval));
//                 };
//                 scale.ticks = function(interval, skip) {
//                     var extent = d3_scaleExtent(scale.domain()), method = interval == null ? tickMethod(extent, 10) : typeof interval === "number" ? tickMethod(extent, interval) : !interval.range && [ {
//                         range: interval
//                     }, skip ];
//                     if (method) interval = method[0], skip = method[1];
//                     return interval.range(extent[0], d3_time_scaleDate(+extent[1] + 1), skip < 1 ? 1 : skip);
//                 };
//                 scale.tickFormat = function() {
//                     return format;
//                 };
//                 scale.copy = function() {
//                     return d3_time_scale(linear.copy(), methods, format);
//                 };
//                 return d3_scale_linearRebind(scale, linear);
//             }
//             function d3_time_scaleDate(t) {
//                 return new Date(t);
//             }
//             function d3_time_scaleFormat(formats) {
//                 return function(date) {
//                     var i = formats.length - 1, f = formats[i];
//                     while (!f[1](date)) f = formats[--i];
//                     return f[0](date);
//                 };
//             }
//             var d3_time_scaleSteps = [ 1e3, 5e3, 15e3, 3e4, 6e4, 3e5, 9e5, 18e5, 36e5, 108e5, 216e5, 432e5, 864e5, 1728e5, 6048e5, 2592e6, 7776e6, 31536e6 ];
//             var d3_time_scaleLocalMethods = [ [ d3_time.second, 1 ], [ d3_time.second, 5 ], [ d3_time.second, 15 ], [ d3_time.second, 30 ], [ d3_time.minute, 1 ], [ d3_time.minute, 5 ], [ d3_time.minute, 15 ], [ d3_time.minute, 30 ], [ d3_time.hour, 1 ], [ d3_time.hour, 3 ], [ d3_time.hour, 6 ], [ d3_time.hour, 12 ], [ d3_time.day, 1 ], [ d3_time.day, 2 ], [ d3_time.week, 1 ], [ d3_time.month, 1 ], [ d3_time.month, 3 ], [ d3_time.year, 1 ] ];
//             var d3_time_scaleLocalFormats = [ [ d3_time_format("%Y"), d3_true ], [ d3_time_format("%B"), function(d) {
//                 return d.getMonth();
//             } ], [ d3_time_format("%b %d"), function(d) {
//                 return d.getDate() != 1;
//             } ], [ d3_time_format("%a %d"), function(d) {
//                 return d.getDay() && d.getDate() != 1;
//             } ], [ d3_time_format("%I %p"), function(d) {
//                 return d.getHours();
//             } ], [ d3_time_format("%I:%M"), function(d) {
//                 return d.getMinutes();
//             } ], [ d3_time_format(":%S"), function(d) {
//                 return d.getSeconds();
//             } ], [ d3_time_format(".%L"), function(d) {
//                 return d.getMilliseconds();
//             } ] ];
//             var d3_time_scaleLocalFormat = d3_time_scaleFormat(d3_time_scaleLocalFormats);
//             d3_time_scaleLocalMethods.year = d3_time.year;
//             d3_time.scale = function() {
//                 return d3_time_scale(d3.scale.linear(), d3_time_scaleLocalMethods, d3_time_scaleLocalFormat);
//             };
//             var d3_time_scaleMilliseconds = {
//                 range: function(start, stop, step) {
//                     return d3.range(+start, +stop, step).map(d3_time_scaleDate);
//                 }
//             };
//             var d3_time_scaleUTCMethods = d3_time_scaleLocalMethods.map(function(m) {
//                 return [ m[0].utc, m[1] ];
//             });
//             var d3_time_scaleUTCFormats = [ [ d3_time_formatUtc("%Y"), d3_true ], [ d3_time_formatUtc("%B"), function(d) {
//                 return d.getUTCMonth();
//             } ], [ d3_time_formatUtc("%b %d"), function(d) {
//                 return d.getUTCDate() != 1;
//             } ], [ d3_time_formatUtc("%a %d"), function(d) {
//                 return d.getUTCDay() && d.getUTCDate() != 1;
//             } ], [ d3_time_formatUtc("%I %p"), function(d) {
//                 return d.getUTCHours();
//             } ], [ d3_time_formatUtc("%I:%M"), function(d) {
//                 return d.getUTCMinutes();
//             } ], [ d3_time_formatUtc(":%S"), function(d) {
//                 return d.getUTCSeconds();
//             } ], [ d3_time_formatUtc(".%L"), function(d) {
//                 return d.getUTCMilliseconds();
//             } ] ];
//             var d3_time_scaleUTCFormat = d3_time_scaleFormat(d3_time_scaleUTCFormats);
//             d3_time_scaleUTCMethods.year = d3_time.year.utc;
//             d3_time.scale.utc = function() {
//                 return d3_time_scale(d3.scale.linear(), d3_time_scaleUTCMethods, d3_time_scaleUTCFormat);
//             };
//             d3.text = d3_xhrType(function(request) {
//                 return request.responseText;
//             });
//             d3.json = function(url, callback) {
//                 return d3_xhr(url, "application/json", d3_json, callback);
//             };
//             function d3_json(request) {
//                 return JSON.parse(request.responseText);
//             }
//             d3.html = function(url, callback) {
//                 return d3_xhr(url, "text/html", d3_html, callback);
//             };
//             function d3_html(request) {
//                 var range = d3_document.createRange();
//                 range.selectNode(d3_document.body);
//                 return range.createContextualFragment(request.responseText);
//             }
//             d3.xml = d3_xhrType(function(request) {
//                 return request.responseXML;
//             });
//             return d3;
//         }();
//
//         return d3;
//     }]);
