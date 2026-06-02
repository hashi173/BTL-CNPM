import sys
import PyPDF2

sys.stdout.reconfigure(encoding='utf-8')

pdf_path = 'd:/Project/BTL-CNPM/BG CNPM 2020.pdf'

reader = PyPDF2.PdfReader(pdf_path)
print(f"Total pages: {len(reader.pages)}")

# Search for chapters in the first few pages or outline
for i in range(15): # check first 15 pages for table of contents
    text = reader.pages[i].extract_text()
    if 'kiểm thử' in text.lower() or 'cài đặt' in text.lower() or 'mục lục' in text.lower():
        print(f"--- Page {i+1} ---")
        for line in text.split('\n'):
            if any(w in line.lower() for w in ['kiểm thử', 'cài đặt', 'chương', 'pha']):
                print(line)
