# xml-parser
XMLをパースしていろいろするツール

## 使い方

1. 添付ファイルのプロジェクトをEclipseにインポート
2. 実行構成でWidgetCounterの引数に解析したいディレクトリを指定(複数指定可能)し、実行
3. 指定されたディレクトリ以下のui.xmlで使われている部品名とその出現回数が出力されます

## 拡張方法

部品の出現回数カウント以外の処理を行いたい場合は、以下の実装を行う事で実現できます。
1. NodeVisitorを実装した新しいクラスを作成し、visitメソッドを実装  
引数に深さ優先探索で訪問されたXMLのノードが渡されてきます
2. WidgetCounter#mainと同様にXmlParser#addParserを呼んで上記で作成したVisitorクラスを追加

## License
[MIT](https://github.com/tsyki/xml-parser/blob/master/LICENSE)
