/**
 * ICEMobile DataView
 * User: Nils Lundquist
 * Date: 2013-08-02 * Time: 9:59 AM
 */

if (!window['mobi']) window['mobi'] = {};

/*
 renderConf spec
 {
 target
 - selector defining the container for the table

 templates
 - defines templates if there are any

 columns
 - defines sets of rendering instructions for a particular named property of an object
 }

 */

(function(mStr) {
    var m = window[mStr];
    /**
     * A tabular interface component. TODO
     *
     * Constructor arguments can take the form of : TODO
     */
    m.DataView = function(id) {
        this.id = id;
        var data = arguments[1] ? arguments[1] : [];
        this.renderConf = arguments[2] ? arguments[2] : {};
        //this.featureConf = arguments[3] ? arguments[3] : {};

        // Check for dependencies
        if (!this.id)
            throw new Error('Required unique identifier was not supplied');

        if (!dustCheck())
            throw new Error('Required Dust.js dependency is not loaded.');

        if (!this.renderConf || !this.renderConf.target)
            throw new Error('Required \'target\' config parameter is not given. This selector defines where the table is rendered.');

        // Initialize DOM
        construct.call(this, data);
    }





    // Private dataView methods - call using privateMethod.call(dvInstance, argArray);
    function reconstruct() {
        construct.call(this, this.getRowDataList());
    }

    function construct(data) {
        var conf = this.renderConf;
        if (!conf.template) conf.template = {};

        // Row type definitions TODO
        conf.defaultRow = data[0];

        var templates = {
            bodycell    : conf.template.bodycell ? conf.template.bodycell : defaultTemplates.bodycell,
            headcell    : conf.template.headcell ? conf.template.headcell : defaultTemplates.headcell,
            footcell    : conf.template.footcell ? conf.template.footcell : defaultTemplates.footcell,
            row         : conf.template.row ? conf.template.row : defaultTemplates.row,
            body        : conf.template.body ? conf.template.body : defaultTemplates.body,
            head        : conf.template.head ? conf.template.head : defaultTemplates.head,
            foot        : conf.template.foot ? conf.template.foot : defaultTemplates.foot,
            table       : conf.template.table ? conf.template.table : defaultTemplates.table
        }

        var base = getDustBase(), id = this.id;

        // Register Dust templates
        var columns = conf.columns ? conf.columns : [], self = this;
        for (var elem in templates) {
            // Register per-column templates if defined
            if (elem.indexOf('cell') > -1) {
                columns.forEach(function(col) {
                    if (col.template && col.template[elem]) dust.loadSource(dust.compile(col.template[elem], self.id+'_'+col.name+'_'+elem));
                    else dust.loadSource(dust.compile(templates[elem], self.id+'_'+col.name+'_'+elem));
                })
            } else {
                dust.loadSource(dust.compile(templates[elem], this.id+'_'+elem));
            }
        }

        /* Attach the row object to every generated row */
        function attachData() {
            Array.prototype.forEach.call(
                document.querySelectorAll('#'+id+' > tbody > tr'),
                function(row, idx) {
                    row.data = data[idx];
                    return true;
                });
        }

        function attachAutoListener(tbody) {
            tbody.addEventListener('change', function(event) {
                var autoTag = (function(parent){
                    while (parent != tbody) {
                        if (parent.classList.contains('dv-auto'))
                            break;
                        parent = parent.parentNode;
                    }
                    return parent == tbody ? undefined : parent;
                })(event.target.parentNode);

                if (autoTag)
                    window[mStr].DataView.autoUpdate(autoTag, event);
            });
        }

        function applyFixedHeaderSizing(target) {
            var table = target.querySelector('#'+id),
                head = table.querySelector('thead'),
                headCells = head.querySelectorAll('tr > th'),
                foot = table.querySelector('tfoot'),
                footCells = foot.querySelectorAll('tr > td'),
                tbody = table.querySelector('tbody'),
                firstRowCells = tbody.querySelector('tr:first-child').childNodes,
                dupeHead = head.cloneNode(true),
                dupeHeadCells = dupeHead.querySelectorAll('tr > th'),
                dupeFoot = foot.cloneNode(true),
                dupeFootCells = dupeFoot.querySelectorAll('tr > td');

            var dupeHeadTable = document.createElement('table'),
                dupeFootTable = document.createElement('table'),
                headWrapper = document.createElement('div'),
                footWrapper = document.createElement('div');

            dupeHeadTable.appendChild(dupeHead);
            dupeFootTable.appendChild(dupeFoot);

            headWrapper.style.overflow = 'hidden';
            footWrapper.style.overflow = 'hidden';

            headWrapper.appendChild(dupeHeadTable);
            footWrapper.appendChild(dupeFootTable);

            target.insertBefore(headWrapper, target.firstChild);
            target.appendChild(footWrapper);

            table.style.tableLayout = 'fixed';

            if( window.getComputedStyle ){
                var frcWidths = Array.prototype.map.call(
                    firstRowCells,
                    function(n) {
                        var compd = document.defaultView.getComputedStyle(n, null);
                        return n.clientWidth;
                    });

                /* fix body column widths */
                for (var i = 0; i < frcWidths.length; i++) {
                    //Array.prototype.forEach.call(tbody.childNodes, function(x) {
                        firstRowCells[i].style.minWidth = frcWidths[i] + 'px';
                    //});
                }

                var headCellWidths = Array.prototype.map.call(
                    headCells,
                    function(n) {
                        return n.clientWidth;
                    });

                var footCellWidths = Array.prototype.map.call(
                    footCells,
                    function(n) {
                        return n.clientWidth;
                    });

                /* copy head col widths from duplicate header */
                for (var i = 0; i < headCellWidths.length; i++) {
                    dupeHeadCells[i].style.minWidth = headCellWidths[i] + 'px';
                }

                /* copy foot col widths from duplicate footer */
                if (footCells.length > 0)
                    for (var i = 0; i < footCellWidths.length; i++) {
                        dupeFootCells[i].style.minWidth = footCellWidths[i] + 'px';
                    }
            }

            function attachScrollHandler(wrapper) {
                wrapper.addEventListener('scroll', function() {
                    dupeHeadTable.parentNode.scrollLeft = wrapper.scrollLeft;
                    dupeFootTable.parentNode.scrollLeft = wrapper.scrollLeft;
                })
            }

            /* hide duplicate header */
            setTimeout(function() {
                if (dupeHead) {
                    head.style.display = 'none';
                    table.style.marginTop = '-1px';
                    dupeHeadTable.style.marginBottom = '0';
                }
                if (dupeFoot) {
                    foot.style.display = 'none';
                    table.style.marginBottom = '0';
                    table.style.borderBottom = '0px';
                    dupeFootTable.style.marginBottom = '0';
                }
                recalcScrollHeight.call(self, table, dupeHeadTable, dupeFootTable, conf.fixedHeaderSizing.sizeFromWindow);
            }, 0) /* hiding instantly broke scrolling when init'ing the first time on landscape ipad */

            attachScrollHandler(table.parentNode);
        };

        // Exec table renderer
        var startTime = new Date().getTime();
        var dv = this;
        dust.render(id+'_table',
            base.push({ id : id, data : data, columns : conf.columns }),
            function(err, out) {
                console.log('HtmlGenTime: ' + (new Date().getTime() - startTime));

                if (out) {
                    if (conf.fixedHeaderSizing)
                        out = '<div>'+out+'</div>';

                    startTime = new Date().getTime();
                    // Attach rendered output
                    var target = getDOMTarget.call(dv);
                    target.insertAdjacentHTML('afterbegin',out);
                    console.log('HtmlInsertTime: ' + (new Date().getTime() - startTime));
                    console.log('* Note complex styling to the table elements is most responsible for long insert times - except in Firefox, FF is fast as hell at HTML string inserts');


                    startTime = new Date().getTime();
                    attachAutoListener(target.querySelector('#'+id+' > tbody'));
                    attachData();
                    console.log('AttachTime: ' + (new Date().getTime() - startTime));

                    startTime = new Date().getTime();
                    if (conf.fixedHeaderSizing)
                        applyFixedHeaderSizing(target);
                    console.log('SizingTime: ' + (new Date().getTime() - startTime));
                }
                else throw Error(err);
            }
        )
    }

    function getDOMTarget() {
        return document.querySelector(this.renderConf.target);
    }

    function recalcScrollHeight(table, dupeHeadTable, dupeFootTable, sizeFromWindow) {
        if (table === undefined) table = document.getElementById(this.id);
        if (dupeHeadTable === undefined) dupeHeadTable = getDOMTarget.call(this).firstChild;
        if (dupeFootTable === undefined) dupeFootTable = getDOMTarget.call(this).lastChild;
        if (sizeFromWindow === undefined) sizeFromWindow = this.renderConf.fixedHeaderSizing.sizeFromWindow;

        var bodyDivWrapper = table.parentNode,
            headHeight = dupeHeadTable.offsetHeight,
            footHeight = dupeFootTable.offsetHeight,
            maxHeight = sizeFromWindow ? window.innerHeight : bodyDivWrapper.parentNode.clientHeight,
            fullHeight = maxHeight - headHeight - footHeight;

        /* set height to full visible size of parent */
        if( isNumber(fullHeight) ) {
            bodyDivWrapper.style.height = fullHeight + 'px';
            bodyDivWrapper.style.overflow = 'auto';
        }

        /* set height to full visible size of parent minus
         height of all following elements */
//        var bottomResize = function() {
//            fullHeight -= (tbody.scrollHeight - tbody.clientHeight);
//            if( isNumber(fullHeight)){
//                if (navigator.userAgent.match(/iPhone/i) || navigator.userAgent.match(/iPod/i))
//                    fullHeight += 60;
//                bodyDivWrapper.style.height = fullHeight + 'px';
//            }
//        };
//
//        bottomResize();
    }




    // Private global methods - could be moved to other files
    function dustCheck() { return window['dust'] ? true : false; }

    function isNumber(n) {
        return !isNaN(parseFloat(n)) && isFinite(n);
    }

    function getDustBase() {
        return dust.makeBase({
            auto: function(chunk, ctx, bodies, params) {
                if (!params) params = {};
                // if no explicit property name, see if a prop name variable exists, as in a col context
                var propName = params.propName ? params.propName : ctx.get('name');

                // written as onmouseover to bind onchange to avoid Chrome error with onchange
                // https://code.google.com/p/chromium/issues/detail?id=131574
                chunk.write('<span class="dv-auto" data-prop="'+propName+'">');
                chunk = chunk.render( bodies.block, ctx );
                chunk.write('</span>')
            },

            filter: function(chunk, ctx, bodies, params) {
                if (!params) params = {};
                // if no explicit property name, see if a prop name variable exists, as in a col context
                var propName = params.propName ? params.propName : ctx.get('name');

                chunk.write('<span class="dv-filter" data-prop="'+propName+'">');
                chunk = chunk.render( bodies.block, ctx );
                chunk.write('</span>')
            },

            addVar : function(chunk, context, bodies, params) {
                var val = params.obj[params.prop];

                context.stack.head[params.var] = val;
                var out = chunk.render( bodies.block, context );
                delete context.stack.head[params.var];
                return out;
            }
        })
    }

    function matches(elem, selector) {
        var impl = elem.webkitMatchesSelector || elem.msMatchesSelector || elem.mozMatchesSelector;
        if( impl && impl.bind ){
            return (impl.bind(elem))(selector);
        }
        else{
            return Array.prototype.indexOf.call(document.querySelectorAll(selector), elem) > -1;
        }
    }

    function getValue(elem) {
        // getValue is only for abstracting the extraction of a js object out of our component instances

        // converting or validating that object should be done in autoUpdate

        if (elem.type === 'checkbox') return elem.checked;
        else return elem.value;
    }






    // Private constant values
    var defaultTemplates = {
        head        : '<thead><tr>{#columns}{>"{id}_{.name}_headcell" /}{/columns}</tr></thead>',
        foot        : '<tfoot><tr>{#columns}{>"{id}_{.name}_footcell" /}{/columns}</tr></tfoot>',

        headcell    : '<th class="{.name}">{.headerText}</th>',

        footcell    : '<td class="{.name}">{.footerText}</td>',

        bodycell    : '<td class="{.name}">{.value}</td>',

        row         : '<tr>' +
            '{#columns}' +
            '{#addVar var="value" obj=rowData prop=.name}' +
            '{>"{id}_{.name}_bodycell" /}' +
            '{/addVar}' +
            '{/columns}' +
            '</tr>',

        body        : '<tbody>' +
            '{#data}' +
                '{>"{id}_row" rowData=./}' +
            '{:else}' +
                'No Data!' +
            '{/data}' +
            '</tbody>',

        table       : '<table id="{id}">' +
            '{>"{id}_head"/}' +
            '{>"{id}_body"/}' +
            '{>"{id}_foot"/}' +
            '</table>',

        detail : '{data}',
        rowdetail : '<tr class="dv-detail"><td>{>"{id}_detail"/}</td></tr>',
        globaldetail : '<div class="dv-detail">{>"{id}_detail"/}</div>'
    };





    // Exposed 'static' dataView interface
    m.DataView.autoUpdate = function(elem, event) {
        var src = event.target,
            propName = elem.getAttribute('data-prop'),
            val = getValue(src),
            row = (function(){
                var parent = elem;
                while (parent = parent.parentNode)
                    if (matches(parent, 'tr'))
                        return parent;

            })();

        // Basic type conversion
        if (typeof row.data[propName] === 'boolean')
            val = val === true || val === 'true' ? true : val === false || val === 'false' ? false : undefined;
        else if (isNumber(row.data[propName]))
            val = parseFloat(val);

        if (val != undefined) row.data[propName] = val;

        console.log(row.data[propName]);
    };

    // Exposed instanced dataView interface
    (function(dvProto) {
        // Create ---------------- //

        /**
         * Add a row to the data model.
         * @param data
         */
        dvProto.addRow = function() {
            var data = arguments[0], target;

            // if insert before target
            if (arguments.length == 2) {
                target = arguments[0];
                data = arguments[1];
            }

            var dv = this;
            dust.render(this.id+'_row',
                getDustBase().push({ id : this.id, rowData : data, columns : this.renderConf.columns }),
                function(err, out) {
                    // Create row
                    var rowHtml = out,
                        row,
                        curRow = dv.getRowElement(target);

                    // Insert row
                    if (curRow) {
                        curRow.insertAdjacentHTML('beforebegin', rowHtml);
                        row = curRow.previousSibling;
                    }
                    else {
                        var tbody = dv.getTBodyElement();
                        tbody.insertAdjacentHTML('beforeend', rowHtml);
                        row = tbody.lastChild;
                    }

                    // Attach data
                    row.data = data;

                    // TODO Add callback
                }
            )
        };

        /**
         * Attach a data model, replacing any current model.
         * @param data
         */
        dvProto.attachData = function(data) {
            // TODO: just rebuild tbody...?
            construct.call(this, data);
        };

        /**
         * Attach a rendering configuration, replacing any current config.
         * @param config
         */
        dvProto.attachRenderConfig = function(config) {
            this.renderConf = config;
            reconstruct.call(this);
        };

        // Read ---------------- //

        // TODO: Documentation
        dvProto.getRowDataList = function() {
            return Array.prototype.map.call(getRowElementList(), function(x) {
                return x.data;
            });
        }

        dvProto.getRowElementList = function() {
            return this.getTBodyElement().childNodes;
        }

        dvProto.getRowData = function(target) {
            return this.getRowElement.call(this, target).data;
        };

        dvProto.getRowElement = function(target) {
            if (isNumber(target)) {
                /* Get row at index */
                return this.getTBodyElement().childNodes[target];
            } else if ({}.toString.call(target) === '[object Function]') {
                /* Get row by predicate */
                var rows = this.getTBodyElement().childNodes;
                for (var idx in rows) {
                    var row = rows[idx];
                    if (target(row.data)) {
                        return row;
                    }
                }
            } else {
                /* Get tr if object equals rowData*/
                var rows = this.getTBodyElement().childNodes,
                    targetJSON = JSON.stringify(target);

                for (var idx in rows) {
                    var row = rows[idx];
                    /* not the most robust equality, but considering we took JSON as input it should work */
                    if (targetJSON===JSON.stringify(row.data)) {
                        return row;
                    }
                }

            }
        };

        dvProto.containsRowData = function(rowData) {
            /* Get row by equality */
            var rows = this.getTBodyElement().childNodes,
                targetJSON = JSON.stringify(target);

            for (var idx in rows) {
                var row = rows[idx];
                /* not the most robust equality, but considering we took JSON as input it should work */
                if (targetJSON===JSON.stringify(row.data)) {
                    return true;
                }
            }

            return false;        }

        dvProto.getTBodyElement = function() {
            return document.querySelector('#'+ this.id +' > tbody');
        };


        // Update ---------------- //
        dvProto.updateRowData = function(target, input) {
            var data = this.getRowData(target);

            if (!data) throw Error('Update failed: Row cannot be found or no row data attached to row.');

            for (var prop in input) {
                data[prop] = input[prop];
            }
        }

        // Delete ---------------- //
        dvProto.removeRow = function(target) {
            var elem = target instanceof HTMLElement ? target : this.getRowElement(target);
            elem.parent.removeChild(elem);
        }

        dvProto.detachData = function() {
            this.getTBodyElement().innerHTML = '';
        }

        dvProto.destroy = function() {
            // Unbind events TODO

            // Remove DOM elem
            var table = this.getTBodyElement().parent;
            table.parent.removeChild(table);
        }

        // Activation ---------------- //
        dvProto.activateDetail = function(target) {
            var row, data, detailTemplate, conf = this.renderConf,
                position = (conf.detailPosition) ? conf.detailPosition : 'row',
                table = this;

            if (target instanceof HTMLElement) {
                row = target;
                data = row.data;
            } else {
                data = target;
                row = this.getRowElement(data);
                if (!row) {
                    throw Error('No row with given data to activate.');
                }
            }

            if (row.classList.contains('dv-active')) {
                console.log('Row to activate was already active.');
                return;
            }

            // If using a global region deactivate any current active rows
            if (position === 'global') this.deactivateDetail();

            function writeDetail(contents) {
                // Insert detail region at config'd position
                if (position === 'row') {
                    row.insertAdjacentHTML('afterend', contents);
                    var detail = row.nextSibling;
                    detail.firstChild.setAttribute('colspan', table.renderConf.columns.length);
                    detail.data = data;
                    detail.className = 'dv-detail';
                    row.classList.add('dv-active')
                    setTimeout(function() {
                        detail.classList.add('dv-opaque');
                    }, 50);
                } else if (position === 'global') {
                    document.querySelector(conf.target).insertAdjacentHTML('afterbegin', contents);
                    var wrapper = row.parentNode.parentNode.parentNode,
                        detail = document.querySelector(conf.target).querySelector('.dv-detail'),
                        cstyle = window.getComputedStyle(detail);

                    wrapper.style.height = (parseFloat(wrapper.style.height) - detail.offsetHeight - parseFloat(cstyle.marginTop) - parseFloat(cstyle.marginBottom)) + 'px';
                    setTimeout(function() {
                        detail.classList.add('dv-opaque');
                    }, 50)
                }
            }

            // Build template for detail type
            var templates = {
                detail      : conf.template.detail ? conf.template.detail : defaultTemplates.detail,
                rowdetail   : conf.template.rowdetail ? conf.template.rowdetail : defaultTemplates.rowdetail,
                globaldetail: conf.template.globaldetail ? conf.template.globaldetail : defaultTemplates.globaldetail
            }

            dust.loadSource(dust.compile(templates.detail, this.id+'_detail'));

            switch (position) {
                case 'row':
                    detailTemplate = this.id+'_rowdetail';
                    dust.loadSource(dust.compile(templates.rowdetail, detailTemplate));
                    break;
                case 'global':
                    detailTemplate = this.id+'_globaldetail';
                    dust.loadSource(dust.compile(templates.globaldetail, detailTemplate));
                    break;
                default:
                    throw Error('Invalid detail position defined.');
            }

            // Render detail region contents
            dust.render(detailTemplate, getDustBase().push({ id : this.id, data : data, columns : conf.columns }),
                function(err, out) {
                    console.log(out);
                    if (err) throw Error(err);
                    else writeDetail(out);
                });
        }

        dvProto.deactivateDetail = function(target) {
            var table = this,
                position = (this.renderConf.detailPosition) ? this.renderConf.detailPosition : 'row';

            if (position === 'global') {
                target = document.querySelector(this.renderConf.target).querySelector('.dv-detail');
                if (target) {
                    target.classList.remove('dv-opaque');
                    setTimeout(function() {
                        target.parentNode.removeChild(target);
                        if (table.renderConf.fixedHeaderSizing) recalcScrollHeight.call(table);
                    }, 1005);
                }
            } else if (position === 'row') {
                if (target != undefined) {
                    if (!(target instanceof HTMLElement)) {
                        target = this.getRowElement(target);
                    }
                    if (target.classList.contains('dv-active')) {
                        var detail = target.nextSibling;
                        detail.classList.remove('dv-opaque');
                        target.classList.remove('dv-active');
                        setTimeout(function() {
                            target.parentNode.removeChild(detail);
                        }, 1005);
                    } else {
                        console.log('Row to deactivate was not active.');
                    }
                } else {
                    Array.prototype.filter.call(this.getRowElementList(), function(row) {
                        return row.classList.contains('dv-active');
                    }).forEach(function(row) {
                        table.deactivateDetail(row);
                    });
                }
            }
        }

        // Column Priority (? column cfg changes may be handled uniformly)
        dvProto.updateColumnConfig = function(name, partialConfig) {
            // rerender column
        }

        // Sort ---------------- //
        dvProto.sortColumn = function(name) {
            // get direction, explicit arg or toggle direction
            // get sort function, defined for column in feature config, explicitly as arg, or default to natural ordering

        }

        // Filter ---------------- //
        dvProto.filterColumn = function(name, value) {
            // get filter type, defined for column in feature config, explicitly as arg, or default to startsWith

        }


    })(m.DataView.prototype);

})('mobi');
