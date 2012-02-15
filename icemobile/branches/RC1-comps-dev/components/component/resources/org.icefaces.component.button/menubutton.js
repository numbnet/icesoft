if (!window['mobi']) {
    window.mobi = {};
}

mobi.menubutton = {
    cfg: {},
    init: function(clientId, cfg){
        this.cfg[clientId] = cfg;
    },
    select: function(clientId){
         var myselect = document.getElementById(clientId+'_sel');
         var index = 0;
         for (var i=0; i<myselect.options.length; i++){
             if (myselect.options[i].selected==true){
                index = i;
                var txt = myselect.options[index].text;
                break
             }
         }
         var textA = myselect.options[index].text;
         var optId = myselect.options[index].id;
         var singleSubmit = false;
         var disabled = false;
         if (this.cfg[clientId]){
             singleSubmit = this.cfg[clientId].singleSubmit;
             disabled = this.cfg[clientId].disabled;
         }
         if (index ==0)return;
         if (singleSubmit){
             ice.se(null, optId);
         } else {
             ice.s(null, optId);
         }
        myselect.options.index =0;

    }
  /*  around: function(clientId){
        this.options[this.selectedIndex].onclick();
    }  */
};
