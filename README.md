# xml-parser
XMLをパースしていろいろするツール

## 
## 使い方

1. Eclipseで`Check out as Maven project from SCM`を利用してインポートします。
2. 実行構成から動かしたいクラスの引数にファイルパスを指定して実行させます。

## 実装されている機能

* XMLのタグの出現数計測(WidgetCounter)
* Object Browser ERで作成したedmファイルから不正に重複しているENTITYタグを検出する(EdmDuplicateEntityDetector)

## 拡張方法

以下の実装を行う事で任意の解析処理を実現できます。
1. NodeVisitorを実装した新しいクラスを作成し、visit,afterChildVisitメソッドを実装  
引数に深さ優先探索で訪問されたXMLのノードが渡されてきます
2. WidgetCounter#mainと同様にXmlParser#addParserを呼んで上記で作成したVisitorクラスを追加

## License
[MIT](https://github.com/tsyki/xml-parser/blob/master/LICENSE)
