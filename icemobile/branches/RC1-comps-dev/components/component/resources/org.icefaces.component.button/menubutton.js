if (!window['mobi']) {
    window.mobi = {};
}

mobi.menubutton = {
    cfg: {},
    initmenu: function(clientId){
         var myselect = document.getElementById(clientId+'_sel');
         var anoption = document.createElement("option");
         anoption.label="Select";
         try{
            myselect.add(new Option("Select", "0"), myselect.options[0]);
         } catch(e) {
             myselect.add(new Option("Select", "0"), 0);
         }
        myselect.render;
    },
    select: function(clientId){
         var myselect = document.getElementById(clientId+'_sel');
         var index = 0;
         for (var i=1; i<myselect.options.length; i++){
             if (myselect.options[i].selected==true){
                index = i;
                break
             }
         }
    //     var textA = myselect.options[index].text;
         var optId = myselect.options[index].id;
         var singleSubmit = false;
         var disabled = false;
         if (this.cfg[optId]){
             singleSubmit = this.cfg[optId].singleSubmit;
             disabled = this.cfg[optId].disabled;
         }
         if (index ==0)return;
         if (this.cfg[optId].pcId){
            var pcId= this.cfg[optId].pcId;
            mobi.panelConf.init(pcId, optId, true, this.cfg[optId] ) ;
            reset(myselect,index);
            return;
         }
         if (singleSubmit){
             ice.se(null, optId);
         } else {
             ice.s(null, optId);
         }
        this.reset(myselect, index);
    },
    reset: function reset(myselect, index) {
            myselect.options[index].selected = false;
            myselect.options.index = 0;

    },
    initCfg: function(clientId, optionId, cfg){
        this.cfg[optionId] = cfg;
    }
  /*  around: function(clientId){
        this.options[this.selectedIndex].onclick();
    }  */
};
