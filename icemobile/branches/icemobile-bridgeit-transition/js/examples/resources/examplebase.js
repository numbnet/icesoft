var stringTemplate = '<td>{#auto}<input value="{value}" />{/auto}</td>';
var dateTemplate = '<td>{#auto}<input type="date" value="{value}" />{/auto}</td>';
var booleanTemplate = '<td>{#auto}<input type="checkbox" {?.value}checked{/value} />{/auto}</td>';
var floatTemplate = '<td>{#auto}<input value="{value}" />{/auto}</td>';
var integerTemplate = '<td>{#auto}<input value="{value}" />{/auto}</td>';
var alertCellTemplate = '<td><button onclick="alert(JSON.stringify(this.parentNode.parentNode.data));">JSON</button></td>';
var activateCellTemplate = '<td><button onclick="dv.activateDetail(this.parentNode.parentNode.data)">Activate</button></td>';
var detailContentsTemplate = '<h5>Details:</h5><p>Known Alias: <em>{data.fullname}</em><button style="display: inline-block; margin-left: 5em;" onclick="dv.deactivateDetail(this.parentNode.parentNode.parentNode.data)">Deactivate</button></p>';
var filterHeader = '<th class="{.name}">{.headerText}<br/>{#filter}<input/>{/filter}</th>';
var numFilterHeader = '<th class="{.name}">{.headerText}<br/>{#filter}<input type="number"/>{/filter}</th>';


var defaultExampleArguments = ['tableId',
    testData,
    {
        target: '#tableHolder',
        detailPosition: 'global',
        templates: {
            detail : detailContentsTemplate
            // bodycell: stringTemplate
        },
        columns : [
            {name:'index', headerText:'ID', footerText:'ID'},
            {name:'name', headerText:'Name', footerText:'Name',
                template:{ bodycell: stringTemplate, headcell: filterHeader}},
            {name:'surname' , headerText:'Surname', footerText:'Surname',
                template:{ bodycell: stringTemplate}},
            {name:'integer', headerText:'Age', footerText:'Age',
                template:{ bodycell: integerTemplate, headcell: numFilterHeader}},
            {name:'index_start_at', headerText:'Ordered Integer', footerText:'Org. Int',
                template:{ bodycell: integerTemplate}},
            {name:'time', headerText:'Date', footerText:'Date',
                template:{ bodycell: dateTemplate}},
            {name:'float' , headerText:'Float', footerText:'Float',
                template:{ bodycell: floatTemplate}},
            {name:'email' , headerText:'EMail', footerText:'Email',
                template:{ bodycell: stringTemplate}},
            {name:'bool' , headerText:'Boolean', footerText:'Bool',
                template:{ bodycell: booleanTemplate }},
            {name:'alert', headerText:'JSON Alert', footerText:'Alert',
                template:{ bodycell: alertCellTemplate }},
            {name:'activate', headerText:'Activate', footerText:'Activate',
                template:{ bodycell: activateCellTemplate }}
        ]
    }
];