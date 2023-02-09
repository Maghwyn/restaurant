<html>
    <head>
        <style>
            body {
                font-family: Arial, sans-serif;
                font-size: 12pt;
            }
            h1 {
                font-size: 30pt;
                width: 100%;
                text-align: center;
                color: #ffa861;
            }
            h2 {
                font-size: 16pt;
            }
            h3 {
                font-size: 10pt;
                font-weight: lighter;
            }

            table {
                font-family: Arial, Helvetica, sans-serif;
                border-collapse: collapse;
                width: 100%;
            }

            table td, table th {
                border: 1px solid #ddd;
                padding: 8px;
                font-size: 10pt;
            }

            table tr:nth-child(even){
                background-color: #f2f2f2;
            }

            table th {
                padding-top: 12px;
                padding-bottom: 12px;
                text-align: left;
                background-color: #ffa861;
                color: black;
            }
        </style>
    </head>
    <body>
    <h1>BILAN PRÉVISIONEL</h1>
    <br/>
    <br/>
    <h2>Bilan recettes</h2>
    <hr/>
    <table>
        <tr>
            <th></th>
            <th>${report0_title}</th>
            <th>${report1_title}</th>
            <th>${report2_title}</th>
        </tr>
        <tr>
            <td>Capital</td>
            <td>${report0_cap}</td>
            <td>${report1_cap}</td>
            <td>${report2_cap}</td>
        </tr>
    </table>
    <br/>
    <br/>
    <h2>Bilan dépenses</h2>
    <hr/>
    <table>
        <tr>
            <th></th>
            <th>${report0_title}</th>
            <th>${report1_title}</th>
            <th>${report2_title}</th>
        </tr>
        <tr>
            <td>Dettes</td>
            <td>${report0_exp}</td>
            <td>${report1_exp}</td>
            <td>${report2_exp}</td>
        </tr>
    </table>
    <br/>
    <footer>
        <h3>Fais le ${date} à ${time}</h3>
    </footer>
    </body>
</html>